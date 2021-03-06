package io.forestframework.extensions.jdbc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.forestframework.core.config.Config;
import io.forestframework.ext.api.Extension;
import io.forestframework.ext.api.StartupContext;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

public class JDBCClientExtension implements Extension {
    @Override
    public void beforeInjector(StartupContext startupContext) {
        startupContext.getComponentClasses().add(JDBCModule.class);
    }

    public static class JDBCModule extends AbstractModule {
        @Provides
        public JDBCClient createClient(@Config("forest.jdbc") JsonObject config, Vertx vertx) {
            return JDBCClient.create(vertx, config);
        }
    }
}
