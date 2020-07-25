package io.forestframework.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import io.forestframework.core.http.DefaultHttpVerticle;
import io.forestframework.core.http.routing.DefaultRoutings;
import io.forestframework.core.http.routing.Routings;
import io.forestframework.ext.api.StartupContext;
import io.forestframework.utils.completablefuture.VertxCompletableFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static io.forestframework.utils.StartupUtils.instantiateWithDefaultConstructor;

/**
 * For internal use only. This is not public API.
 */
public class Application implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final StartupContext startupContext;
    private Injector injector;
    private String deploymentId;

    public Application(StartupContext startupContext) {
        this.startupContext = startupContext;
//        this.vertx = Vertx.vertx(configProvider.getInstance("forest.vertx", VertxOptions.class));
//        this.startupContext = new StartupContext(vertx, applicationClass, configProvider, instantiateExtensions(extensionClasses));
    }

    public void start() {
        configureComponents();
        createInjector();
        configureApplication();
        startHttpServer();
    }

    public Injector getInjector() {
        return injector;
    }

    protected void configureComponents() {
        // 1. Start. Instantiate all extensions and configure components.
        startupContext.getExtensions().forEach(extension -> {
            LOGGER.debug("Apply extension beforeInjector: {}", extension.getClass());
            extension.beforeInjector(startupContext);
        });
    }

    protected void createInjector() {
        // 2. Filter out all Module classes, instantiate them and create the injector
        List<Module> modules = startupContext.getComponentClasses().stream().filter(Module.class::isAssignableFrom)
                .map(componentClass -> (Module) instantiateWithDefaultConstructor(componentClass))
                .collect(Collectors.toList());

        injector = createInjector(startupContext, modules);

        LOGGER.debug("Injector created successfully with {}", modules);

        // 3. Inject members to modules because they're created by us, not Guice.
        modules.forEach(injector::injectMembers);
    }

    protected void configureApplication() {
        // 4. Configure the application
        startupContext.getExtensions().forEach(extension -> extension.afterInjector(injector));
    }

    protected void startHttpServer() {
        // 5. Start the HTTP server
        DeploymentOptions deploymentOptions = startupContext.getConfigProvider().getInstance("forest.deploy", DeploymentOptions.class);
        ((DefaultRoutings) injector.getInstance(Routings.class)).finalizeRoutings();

        Future<String> vertxFuture = startupContext.getVertx().deployVerticle(() -> injector.getInstance(DefaultHttpVerticle.class), deploymentOptions);
        CompletableFuture<String> future = VertxCompletableFuture.from(startupContext.getVertx().getOrCreateContext(), vertxFuture);
        try {
            deploymentId = future.get();
            LOGGER.info("Http server successful started on {} with {} instances", startupContext.getConfigProvider().getInstance("forest.http.port", String.class), deploymentOptions.getInstances());
        } catch (Throwable e) {
            LOGGER.error("", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        VertxCompletableFuture.from(startupContext.getVertx().getOrCreateContext(), startupContext.getVertx().undeploy(deploymentId)).get();
    }

    private static Injector createInjector(StartupContext startupContext, List<Module> modules) {
        Module current = new CoreModule(startupContext);
        for (Module module : modules) {
            current = Modules.override(current).with(module);
        }
        return Guice.createInjector(current);
    }
}
