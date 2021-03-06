package io.forestframework.ext.api;

import com.github.blindpirate.annotationmagic.AnnotationMagic;
import io.forestframework.core.config.ConfigProvider;
import io.vertx.core.Vertx;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultStartupContext implements StartupContext {
    private final Vertx vertx;
    private final Class<?> appClass;
    private final ConfigProvider configProvider;
    private final List<Class<?>> componentClasses = new ArrayList<>();
    private final List<Extension> extensions;

    public DefaultStartupContext(Vertx vertx, Class<?> applicationClass, ConfigProvider configProvider, List<Extension> extensions) {
        this.vertx = vertx;
        this.appClass = applicationClass;
        this.configProvider = configProvider;
        this.extensions = Collections.unmodifiableList(extensions);
    }

    public Vertx getVertx() {
        return vertx;
    }

    public Class<?> getAppClass() {
        return appClass;
    }

    public ConfigProvider getConfigProvider() {
        return configProvider;
    }

    public List<Class<?>> getComponentClasses() {
        return componentClasses;
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    @Override
    public <T extends Annotation> List<T> getEnableExtensionsAnnotation(Class<T> annotationClass) {
        return AnnotationMagic.getAnnotationsOnClass(getAppClass(), annotationClass);
    }
}
