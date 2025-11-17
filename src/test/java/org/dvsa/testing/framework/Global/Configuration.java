package org.dvsa.testing.framework.Global;

import activesupport.mailPit.MailPit;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

public class Configuration {
    public EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    public Config config = new activesupport.config.Configuration(env.toString()).getConfig();
    public MailPit mailPit = new MailPit(env);
    private final World world;

    public Configuration(World world) {
        this.world = world;
    }

    public String getTempPassword(String emailAddress) {
        return mailPit.retrieveTempPassword(emailAddress);
    }

    public String getGovCode() throws InterruptedException {
        return mailPit.retrieveSignInCode(world.registerUser.getEmailAddress());
    }

    public String getPasswordResetLink() throws activesupport.MissingRequiredArgument {
        return mailPit.retrievePasswordResetLink(world.registerUser.getEmailAddress(), 2);
    }

    public String getTmAppLink() throws activesupport.MissingRequiredArgument {
        return mailPit.retrieveTmAppLink(world.registerUser.getEmailAddress());
    }

    public String getUsernameResetLink() throws activesupport.MissingRequiredArgument {
        return mailPit.retrieveUsernameInfo(world.registerUser.getEmailAddress());
    }
}