package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class SubmitSelfServeApplication extends BasePage {

    World world;

    public SubmitSelfServeApplication(World world) {
        this.world = world;
    }

    @And("i submit and pay for a {string} licence application")
    public void iStartANewLicenceApplication(String licenceType) {
        world.submitApplicationJourney.startANewLicenceApplication(licenceType);
        if (world.configuration.env.toString().equals("int")) {
            world.govSignInJourney.navigateToGovUkSignIn();
            world.govSignInJourney.signInGovAccount();
            world.govSignIn.iCompleteThePaymentProcess();
        } else {
            world.submitApplicationJourney.submitAndPayForApplication();
        }
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