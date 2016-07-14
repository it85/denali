package app;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

public abstract class DenaliModule extends AbstractModule {

    private final Config config;

    public DenaliModule(String appName) {
        this.config = ConfigurationManager.getConfig(appName);
    }

    protected abstract void configure();

    public Config getConfig() {
        return config;
    }
}
