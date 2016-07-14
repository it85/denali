package app;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

class ConfigurationManager {

    static Config getConfig(String appName) {
        Config parentConf = ConfigFactory.load("denali-application.conf");
        return parentConf.getConfig(appName);
    }
}
