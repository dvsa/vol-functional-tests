package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import apiCalls.enums.UserType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static activesupport.database.DBUnit.executeUpdateSQL;
import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.Journeys.licence.APIJourney.tmCount;
import static org.junit.jupiter.api.Assertions.*;

public class RemoveTM extends BasePage {

    private static final String oldAlertValue = "You are removing your last Transport Manager. If you haven't yet made an application to appoint a replacement, " +
            "you must contact us on 0300 123 9000 or at notifications@vehicle-operator-licensing.service.gov.uk";
    private static final String newAlertValue = "You are about to remove the last transport manager for this licence. Do you want to send a letter about this to the operator?\n" +
            "If yes, this will be automatically issued tomorrow.";
    public static String alertHeaderValue = "Are you sure you want to remove this Transport Manager?";
    private static final String applicationVariationTMAlertContent = "This action is permanent and cannot be undone.";
    private final World world;

    public RemoveTM(World world) {
        this.world = world;
    }

    @Given("the licence has been granted")
    public void theLicenceHasBeenGranted() throws HttpException {
        world.grantApplication.grantLicence();
        world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
    }

    @When("the internal user goes to remove the last transport manager")
    public void theInternalUserGoesToRemoveTheLastTransportManager() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        world.TMJourney.promptRemovalOfInternalTransportManager();
    }

    @Then("a pop up message should be displayed")
    public void aPopUpMessageShouldBeDisplayed() {
        waitForTextToBePresent(alertHeaderValue);
        String alertContent = getElementValueByText("//*[@id=\"pg:lva-licence/transport_managers:index\"]/div[2]/div/div[2]/div/p", SelectorType.XPATH);
        assertEquals(alertContent, newAlertValue);
    }

    @When("a self-serve user removes the last TM")
    public void aSelfServeUserRemovesTheLastTM() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.TRANSPORT_MANAGERS);
        click("//*[@class='right-aligned govuk-button govuk-button--secondary trigger-modal']", SelectorType.XPATH);
    }

    @Then("the remove TM popup should not be displaying new TM remove text")
    public void theRemoveTMPopupShouldNotBeDisplayingNewTMRemoveText() {
        waitForTextToBePresent(alertHeaderValue);
        if (Browser.navigate().getCurrentUrl().contains("variation") || Browser.navigate().getCurrentUrl().contains("application")) {
            assertFalse(isTextPresent(newAlertValue));
            assertTrue(isTextPresent(applicationVariationTMAlertContent));
        }
        if (Browser.navigate().getCurrentUrl().contains("ssap1")) {
            String alertContent = getElementValueByText("//div[@class='modal__content']/p", SelectorType.XPATH);
            assertEquals(alertContent, oldAlertValue);
        }
        if (tmCount > 1) {
            assertFalse(isTextPresent(newAlertValue));
            assertTrue(isTextPresent(applicationVariationTMAlertContent));
        }
    }

    @And("user attempts to remove the last TM without selecting an option")
    public void userAttemptsToRemoveTheLastTMWithoutSelectingAnOption() {
        waitForTextToBePresent(alertHeaderValue);
        world.UIJourney.clickSubmit();
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        waitForTextToBePresent(alertHeaderValue);
        do {
            // do nothing
        } while (!isTextPresent("You must select an option"));
        isLinkPresent("You must select an option", 60);
    }

    @When("i create a variation")
    public void iCreateAVariation() throws HttpException {
        world.updateLicence.createVariation();
    }

    @And("i update the licence type")
    public void iUpdateTheLicenceType() throws HttpException {
        world.updateLicence.updateLicenceType();
    }

    @When("the transport manager has been removed by an internal user")
    public void theTransportManagerHasBeenRemovedByAnInternalUser() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        world.TMJourney.removeInternalTransportManager();
    }

    @Then("the TM email should be generated and letter attached")
    public void theTMEmailShouldBeGeneratedAndLetterAttached() throws InterruptedException {
        String email = world.createApplication.getOrganisationEmailAddress();
        String licenceNo = world.applicationDetails.getLicenceNumber();
        sleep(10000);
        boolean letterExists = S3.checkLastTMLetterAttachment(email, licenceNo);
        assertTrue(letterExists);
    }
}