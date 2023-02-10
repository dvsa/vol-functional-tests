package org.dvsa.testing.framework.Journeys.licence;

import activesupport.aws.s3.SecretsManager;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegistrationJourney extends BasePage {
    private World world;

    public UserRegistrationJourney(World world){
        this.world = world;
    }

    public void registerUserWithNoLicence() {
        world.selfServeNavigation.navigateToLoginPage();
        world.selfServeNavigation.navigateToCreateAnAccount();
        world.UIJourney.addNewOperator(null, false);
        assertTrue(isTextPresent(world.DataGenerator.getOperatorUserEmail()));
    }

    public void navigateAndLogIntoSelfServiceWithExistingUser() {
        String intEnvUsername = world.configuration.config.getString("intEnvUsername");
        String intEnvPassword = world.configuration.config.getString("intEnvPassword");

        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
            DriverUtils.get(myURL);
            world.globalMethods.signIn(intEnvUsername, intEnvPassword);
        } else {
            world.userRegistrationJourney.registerUserWithNoLicence();
            world.globalMethods.navigateToLoginWithoutCookies(world.DataGenerator.getOperatorUser(), world.DataGenerator.getOperatorUserEmail(), ApplicationType.EXTERNAL, "yes");
        }
    }
}