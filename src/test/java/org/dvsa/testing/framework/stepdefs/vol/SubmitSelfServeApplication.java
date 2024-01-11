package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import scanner.AXEScanner;

import java.io.IOException;

public class SubmitSelfServeApplication extends BasePage {

    World world;

    AXEScanner axeScanner = AccessibilitySteps.scanner;

    public SubmitSelfServeApplication(World world) {
        this.world = world;
    }

    @Given("I submit and pay for a {string} licence application with axe scanner {}")
    public void iSubmitAndPayForLicenceApplicationWithAxeScanner(String licenceType, boolean scanOrNot) throws IllegalBrowserException, IOException, IllegalBrowserException, IOException {
        world.submitApplicationJourney.startANewLicenceApplication(licenceType, scanOrNot);
        if (world.configuration.env.toString().equals("int")) {
            world.govSignInJourney.navigateToGovUkSignIn();
            world.govSignInJourney.signInGovAccount();
            world.govSignIn.iCompleteThePaymentProcess();
        } else {
            world.submitApplicationJourney.submitAndPayForApplication();
        }
        if (scanOrNot) {
            axeScanner.scan(true);
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