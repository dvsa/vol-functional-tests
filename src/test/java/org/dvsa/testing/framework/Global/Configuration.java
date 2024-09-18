package org.dvsa.testing.framework.Global;

import activesupport.aws.s3.SecretsManager;
import activesupport.mailhog.Mailhog;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

public class Configuration {
    public EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    public Config config = new activesupport.config.Configuration(env.toString()).getConfig();
    public Mailhog mailhog = new Mailhog();
    private final World world;

    public Configuration(World world) {
        this.world = world;
    }

    public String getBucketName() {
        return SecretsManager.getSecretValue("bucketName");
    }

    public String getTempPassword(String emailAddress) {
        return S3.getTempPassword(emailAddress, getBucketName());
    }

  /*  public String getTempPasswordFromMailhog(String emailSubject){
        return mailhog.retrieveTempPassword(emailSubject);
    }*/

    public String getGovCode() throws InterruptedException {
        return String.valueOf(S3.getSignInCode());
    }


    public String getPasswordResetLink() {
        return String.valueOf(S3.getPasswordResetLink(world.registerUser.getEmailAddress()));
    }

    public String getTmAppLink() {
        return S3.getTmAppLink(world.registerUser.getEmailAddress());
    }

    public String getUsernameResetLink() {
        return String.valueOf(S3.getUsernameInfoLink(world.registerUser.getEmailAddress()));
    }

}