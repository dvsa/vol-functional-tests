package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import apiCalls.enums.UserType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static activesupport.database.DBUnit.executeUpdateSQL;
import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.APIJourney.tmCount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RemoveTM extends BasePage implements En {

    private static String oldAlertValue = "You are removing your last Transport Manager. If you haven't yet made an application to appoint a replacement, " +
            "you must contact us on 0300 123 9000 or at notifications@vehicle-operator-licensing.service.gov.uk";
    private static String newAlertValue = "You are about to remove the last transport manager for this licence. Do you want to send a letter about this to the operator to all known addresses?\n" +
            "If yes, this will be automatically issued tomorrow.";
    public static String alertHeaderValue = "Are you sure you want to remove this Transport Manager?";
    private static String applicationVariationTMAlertContent = "This action is permanent and cannot be undone.";
    private World world;

    public RemoveTM (World world) {this.world = world;}

    @Given("i have an application with a transport manager")
    public void iHaveAnApplicationWithATransportManager() {
        if (world.createApplication.getOperatorType() == null) {
            world.createApplication.setOperatorType("public");
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.APIJourney.submitApplication();
    }

    @Given("the licence has been granted")
    public void theLicenceHasBeenGranted() {
        world.grantApplication.grantLicence();
        world.grantApplication.payGrantFees();
    }

    @When("the internal user goes to remove the last transport manager")
    public void theInternalUserGoesToRemoveTheLastTransportManager() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getLicence();
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
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.TRANSPORT_MANAGERS);
        click("//*[@value='Remove']", SelectorType.XPATH);
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
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
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
    public void iCreateAVariation() {
        world.updateLicence.createVariation();
    }

    @And("i update the licence type")
    public void iUpdateTheLicenceType() {
        world.updateLicence.updateLicenceType();
    }

    @When("the transport manager has been removed by an internal user")
    public void theTransportManagerHasBeenRemovedByAnInternalUser() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getLicence();
        world.TMJourney.removeInternalTransportManager();
    }

    @And("the removal date is changed to {int} hours into the future")
    public void theRemovalDateIsChangedToHoursIntoTheFuture(int arg0) throws SQLException, UnsupportedDatabaseDriverException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime futureDate = LocalDateTime.now().minusDays(2);
        String dateAndTime = dtf.format(futureDate);
        String sqlStatement = String.format(
                "UPDATE `OLCS_RDS_OLCSDB`.`transport_manager_licence` SET `deleted_date` = '%s' WHERE (`licence_id` = '%s')",
                dateAndTime,
                world.createApplication.getLicenceId()
        );
        Properties.set("dbUsername", world.configuration.config.getString("dbUsername"));
        Properties.set("dbPassword", world.configuration.config.getString("dbPassword"));
        executeUpdateSQL(sqlStatement);
    }

    @Then("the TM email should be generated and letter attached")
    public void theTMEmailShouldBeGeneratedAndLetterAttached() throws InterruptedException {
        String email = world.createApplication.getOrganisationEmailAddress();
        String licenceNo = world.applicationDetails.getLicenceNumber();
        sleep(10000);
        boolean letterExists = S3.checkLastTMLetterAttachment(email, licenceNo);
        Assert.assertTrue(letterExists);
    }
}
