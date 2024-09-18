package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.NotFoundException;
import scanner.AXEScanner;

import java.io.IOException;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;
import static org.junit.jupiter.api.Assertions.*;

public class EBSRUpload extends BasePage {
    private final World world;

    public EBSRUpload(World world) {
        this.world = world;
    }

    @Then("A short notice flag should be displayed in selfserve")
    public void aShortNoticeFlagShouldBeDisplayedInSelfserve() throws IllegalBrowserException, IOException {
        world.busRegistrationJourney.viewEBSRInExternal();
        world.selfServeNavigation.refreshPageWhileCheckingTextPresent("Successful", 900, "Kickout reached");
        waitForTextToBePresent("Successful");
        refreshPage();
        assertTrue(isTextPresent("Successful"));
        assertTrue(isTextPresent("New"));
        assertTrue(isTextPresent("Short notice"));
    }

    @Then("A short notice flag should not be displayed in selfserve")
    public void aShortNoticeFlagShouldNotBeDisplayedInSelfserve() throws IllegalBrowserException, IOException {
        world.busRegistrationJourney.viewEBSRInExternal();
        world.selfServeNavigation.refreshPageWhileCheckingTextPresent("Successful", 900, "Kickout reached");
        waitForTextToBePresent("Successful");
        refreshPage();
        assertTrue(isTextPresent("Successful"));
        assertTrue(isTextPresent("New"));
        assertFalse(isTextPresent("Short notice"));
    }

    @And("i add a new bus registration")
    public void iAddANewBusRegistration() {
        world.internalUIJourney.manualBusRegistration(0,5,0);
    }

    @When("it has been paid and granted")
    public void itHasBeenPaidAndGranted() {
        world.internalUIJourney.payFee();
    }

    @Then("the bus registration should be granted")
    public void theBusRegistrationShouldBeGranted() {
        assertTrue(isTextPresent("Registered"));
    }

    @And("the traffic areas should be displayed on the service details page")
    public void theTrafficAreasShouldBeDisplayedOnTheServiceDetailsPage() {
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        String trafficArea = findElement("//*[@id='bus-reg-ta']", SelectorType.XPATH, 10).getText();
        assertNotNull(trafficArea);
    }

    @And("Documents are generated")
    public void documentsAreGenerated() throws IllegalBrowserException, IOException {
        AXEScanner axeScanner = AccessibilitySteps.scanner;
        String licenceNumber;
        if(world.configuration.env.toString().equals("int")){
            licenceNumber = existingLicenceNumber;
        }else{
            licenceNumber = world.applicationDetails.getLicenceNumber();
            axeScanner.scan(true);
        }
        waitAndClick(String.format("//*[contains(text(),'%s')]", licenceNumber), SelectorType.XPATH);
        if (isElementPresent("//*[contains(text(),'View bus')]", SelectorType.XPATH)) {
            waitAndClick("//*[contains(text(),'View bus')]", SelectorType.XPATH);
            axeScanner.scan(true);
        }
        long kickOutTime = System.currentTimeMillis() + 30000;
        if(!world.configuration.env.toString().equals("local")) {
            do {
                // Refresh page
                refreshPageWithJavascript();

            } while ((long) findElements("//*[@class='field file-upload']", SelectorType.XPATH).size() < 2 && System.currentTimeMillis() < kickOutTime);
            try {
                assertTrue(findElements("//*[@class='field file-upload']", SelectorType.XPATH).stream().anyMatch(
                        webElement -> webElement.getText().contains("Route Track Map PDF (Auto Scale)")));
            } catch (Exception e) {
                throw new NotFoundException("Files not generated.");
            }
        }
    }

    @Then("all Service Details fields should be editable")
    public void allServiceDetailsFieldsShouldBeEditable() {
        clickByLinkText("Service details");
        world.busRegistrationJourney.internalSiteEditBusReg();
    }

    @And("the edited Bus Registration details should be saved")
    public void theEditedBusRegistrationDetailsShouldBeSaved() {
        world.selfServeNavigation.navigateToBusRegExternal();
        assertTrue(isTextPresent("1234"));
    }
}