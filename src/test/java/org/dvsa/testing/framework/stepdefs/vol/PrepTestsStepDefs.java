package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

public class PrepTestsStepDefs extends BasePage {

    private final World world;

    Initialisation initialisation;

    public PrepTestsStepDefs(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Before
    public void setEnvironmentToInt() {
        world.configuration.env = EnvironmentType.INTEGRATION;
    }

    @Given("I have a prep self serve account")
    public void iHaveAPrepSelfServeAccount() {
        world.selfServeNavigation.navigateToLoginPage();
        world.globalMethods.signIn(SecretsManager.getSecretValue("prepUser"),
                SecretsManager.getSecretValue("intEnvPassword"));
    }
}
