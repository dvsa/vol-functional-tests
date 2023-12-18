package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import scanner.AXEScanner;

import java.io.IOException;

public class SubmitSelfServeApplication extends BasePage {

    World world;

   public static final AXEScanner scanner = new AXEScanner();

    public SubmitSelfServeApplication(World world) {
        this.world = world;
    }

    @Given("I submit and pay for a {string} licence application with axe scanner {}")
    public void iSubmitAndPayForLicenceApplicationWithAxeScanner(String licenceType, boolean scanOrNot) throws IllegalBrowserException, IOException {
        world.submitApplicationJourney.startANewLicenceApplication(licenceType, scanOrNot);
        if (world.configuration.env.toString().equals("int")) {
            world.govSignInJourney.navigateToGovUkSignIn();
            world.govSignInJourney.signInGovAccount();
            world.govSignIn.iCompleteThePaymentProcess();
        } else {
            world.submitApplicationJourney.submitAndPayForApplication();
        }
        if (scanOrNot) {
            scanner.scan(true);
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

    @When("i scan for accessibility violations across the create application journey")
    public void iScanForAccessibilityViolationsAcrossTheCreateApplicationJourney() throws IllegalBrowserException, IOException {
        scanner.scan(false);
    }

}