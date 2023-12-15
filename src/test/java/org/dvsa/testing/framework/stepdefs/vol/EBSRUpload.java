package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.NotFoundException;

import java.io.IOException;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;
import static org.junit.jupiter.api.Assertions.*;

public class EBSRUpload extends BasePage {
    private final World world;

    public EBSRUpload(World world) {
        this.world = world;
    }

    @Then("A short notice flag should be displayed in selfserve")
    public void aShortNoticeFlagShouldBeDisplayedInSelfserve() throws IllegalBrowserException, IOException {
        world.busRegistrationJourney.viewEBSRInExternal(false);
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--green' and contains(text(),'successful')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'New')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'short notice')]", SelectorType.XPATH));
    }

    @Then("A short notice flag should not be displayed in selfserve")
    public void aShortNoticeFlagShouldNotBeDisplayedInSelfserve() throws IllegalBrowserException, IOException {
        world.busRegistrationJourney.viewEBSRInExternal(false);
        waitForTextToBePresent("successful");
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--green' and contains(text(),'successful')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'New')]", SelectorType.XPATH));
        assertFalse(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'short notice')]", SelectorType.XPATH));
    }

    @And("i add a new bus registration")
    public void iAddANewBusRegistration() {
        world.internalNavigation.getLicence();
        world.busRegistrationJourney.internalSiteAddBusNewReg(5);
        clickByLinkText("Register");
        findSelectAllRadioButtonsByValue("Y");
        world.UIJourney.clickSubmit();
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        click("//*[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result\"]");
        click("//*[@id='localAuthoritys_chosen']/ul[@class='chosen-choices']", SelectorType.XPATH);
        selectFirstValueInList("//*[@class=\"active-result group-option\"]");
        world.UIJourney.clickSubmit();
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
        assertTrue(isTextPresent("Registered"));
    }

    @And("the traffic areas should be displayed on the service details page")
    public void theTrafficAreasShouldBeDisplayedOnTheServiceDetailsPage() {
        clickByLinkText("Service details");
        clickByLinkText("TA's");
        String trafficArea = findElement("//*[@id='bus-reg-ta']", SelectorType.XPATH, 10).getText();
        assertNotNull(trafficArea);
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


    @And("Documents are generated with axeScanner {}")
    public void documentsAreGeneratedWithAxeScanner(boolean scanOrNot) throws IllegalBrowserException, IOException {
        String licenceNumber;
        if(world.configuration.env.toString().equals("int")){
            licenceNumber = existingLicenceNumber;
        }else{
            licenceNumber = world.applicationDetails.getLicenceNumber();
            if (scanOrNot) {
                world.submitApplicationJourney.axeScanner.scan(true);
            }
        }
        waitAndClick(String.format("//*[contains(text(),'%s')]", licenceNumber), SelectorType.XPATH);
        waitForTextToBePresent("Your file was processed successfully");
        if (isElementPresent("//*[contains(text(),'View bus')]", SelectorType.XPATH)) {
            waitAndClick("//*[contains(text(),'View bus')]", SelectorType.XPATH);
            if (scanOrNot) {
                world.submitApplicationJourney.axeScanner.scan(true);
            }
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
    }