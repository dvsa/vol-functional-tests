package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class SubmitSelfServeApplication extends BasePage {

    World world;
    Initialisation initialisation;

    public SubmitSelfServeApplication(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @And("i submit and pay for a {string} licence application")
    public void iStartANewLicenceApplication(String licenceType){
        world.submitApplicationJourney.startANewLicenceApplication(licenceType);
        world.submitApplicationJourney.submitAndPayForApplication();
    }

    @Given("i have a self serve account")
    public void iHaveASelfServeAccount() {
        world.userRegistrationJourney.navigateAndLogIntoSelfServiceWithExistingUser();
    }

    @And("i have no existing applications")
    public void iHaveNoExistingApplications() {
        world.submitApplicationJourney.cancelAndWithdrawExistingApplications();
    }

    private void chooseLicenceType(String licenceType) {
        waitForTitleToBePresent("Type of licence");
        waitAndClick("//*[contains(text(),'Great Britain')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'" + licenceType + "')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Standard National')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Save')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Business type')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Limited Company')]", SelectorType.XPATH);
    }
}