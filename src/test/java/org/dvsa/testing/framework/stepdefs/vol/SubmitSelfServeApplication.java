package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import scanner.AXEScanner;

import java.io.IOException;

import java.net.MalformedURLException;

public class SubmitSelfServeApplication extends BasePage {

    World world;

    AXEScanner axeScanner = AccessibilitySteps.scanner;

    public SubmitSelfServeApplication(World world) {
        this.world = world;
    }

    @And("I submit and pay for a {string} licence application")
    public void iSubmitAndPayForALicenceApplication(String licenceType) throws IllegalBrowserException, IOException {
        world.submitApplicationJourney.startANewLicenceApplication(licenceType);
        if (world.configuration.env.toString().equals("int")) {
            world.govSignInJourney.navigateToGovUkSignIn();
            world.govSignInJourney.signInGovAccount();
            world.govSignIn.iCompleteThePaymentProcess();
        } else {
            world.submitApplicationJourney.submitAndPayForApplication();
        }
        axeScanner.scan(true);
    }

    @Given("I submit and pay for a {string} licence application")
    public void iSubmitAndPayForLicenceApplicationWithAxeScanner(String licenceType) throws IllegalBrowserException, IOException {
        world.submitApplicationJourney.startANewLicenceApplication(licenceType);
        world.submitApplicationJourney.submitAndPayForApplication();
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