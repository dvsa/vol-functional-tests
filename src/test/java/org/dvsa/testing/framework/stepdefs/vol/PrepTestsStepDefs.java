package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class PrepTestsStepDefs extends BasePage {

    private final World world;

    Initialisation initialisation;

    public PrepTestsStepDefs(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("I have logged into Self Serve on PREP")
    public void iHaveLoggedIntoSelfServeOnPREP() {
        world.globalMethods.signIn(SecretsManager.getSecretValue("prepUser"),
                SecretsManager.getSecretValue("intEnvPassword"));
    }
}
