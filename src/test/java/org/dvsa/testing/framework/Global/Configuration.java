package org.dvsa.testing.framework.Global;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

public class Configuration{
    public EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    public Config config = new activesupport.config.Configuration(env.toString()).getConfig();
    public String localDefaultPassword;
    private World world;

    public Configuration(World world) {
        this.world = world;
    }

    public String getBucketName() {
        return config.getString("bucketName");
    }

    public String getTempPassword(String emailAddress) {
        if (env == EnvironmentType.LOCAL) {
            return localDefaultPassword;
        }
        return S3.getTempPassword(emailAddress, getBucketName());
    }
}
