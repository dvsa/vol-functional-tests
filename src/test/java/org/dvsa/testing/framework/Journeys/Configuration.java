package org.dvsa.testing.framework.Journeys;

import activesupport.system.Properties;
import com.typesafe.config.Config;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

public class Configuration {
    public EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    public Config config = new activesupport.config.Configuration(env.toString()).getConfig();
}
