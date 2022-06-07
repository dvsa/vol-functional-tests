package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.NotFoundException;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EBSRUpload extends BasePage implements En {
    private final World world;

    public EBSRUpload(World world) {
        this.world = world;
    }

    @Then("A short notice flag should be displayed in selfserve")
    public void aShortNoticeFlagShouldBeDisplayedInSelfserve() {
        world.busRegistrationJourney.viewEBSRInExternal();
        assertTrue(isElementPresent("//span[@class='status green' and contains(text(),'successful')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//span[@class='status orange' and contains(text(),'New')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//span[@class='status orange' and contains(text(),'short notice')]", SelectorType.XPATH));
    }

    @Then("A short notice flag should not be displayed in selfserve")
    public void aShortNoticeFlagShouldNotBeDisplayedInSelfserve() {
        world.busRegistrationJourney.viewEBSRInExternal();
        waitForTextToBePresent("successful");
        assertTrue(isElementPresent("//span[@class='status green' and contains(text(),'successful')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//span[@class='status orange' and contains(text(),'New')]", SelectorType.XPATH));
        assertFalse(isElementPresent("//span[@class='status orange' and contains(text(),'short notice')]", SelectorType.XPATH));

    }

    @And("i add a new bus registration")
    public void iAddANewBusRegistration() {
        world.internalNavigation.getLicence();
        world.busRegistrationJourney.internalSiteAddBusNewReg(5);
        clickByLinkText("Register");
        findSelectAllRadioButtonsByValue("Y");
        clickByName("form-actions[submit]");
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        clickByName("form-actions[submit]");
    }

    @When("it has been paid and granted")
    public void itHasBeenPaidAndGranted() {
        clickByLinkText("Fees");
        world.feeAndPaymentJourney.selectFee();
        world.feeAndPaymentJourney.payFee("60", "cash");
        waitAndClick("//*[contains(text(),'Grant')]", SelectorType.XPATH);
    }

    @Then("the bus registration should be granted")
    public void theBusRegistrationShouldBeGranted() {
        Assert.assertTrue(isTextPresent("Registered"));
    }

    @And("the traffic areas should be displayed on the service details page")
    public void theTrafficAreasShouldBeDisplayedOnTheServiceDetailsPage() {
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        String trafficArea = findElement("//*[@id=\"bus-reg-ta\"]/ul/li[1]/dd", SelectorType.XPATH, 10).getText();
        Assert.assertNotNull(trafficArea);
    }

    @And("Documents are generated")
    public void documentsAreGenerated() {
        waitAndClick(String.format("//*[contains(text(),'%s')]", world.applicationDetails.getLicenceNumber()), SelectorType.XPATH);
        long kickOutTime = System.currentTimeMillis() + 120000;
        do {
            // Refresh page
            refreshPageWithJavascript();
            if (isElementPresent("//*[contains(text(),'View bus')]", SelectorType.XPATH)) {
                waitAndClick("//*[contains(text(),'View bus')]", SelectorType.XPATH);
            }
        } while ((long) findElements("//*[@class='files']", SelectorType.XPATH).size() <= 3 && System.currentTimeMillis() < kickOutTime);
        try {
            assertTrue(findElements("//*[@class='files']", SelectorType.XPATH).stream().anyMatch(
                    webElement -> webElement.getText().contains("Route Track Map PDF (Auto Scale)")));
        } catch (Exception e) {
            throw new NotFoundException("Files not generated.");
        }
    }
}