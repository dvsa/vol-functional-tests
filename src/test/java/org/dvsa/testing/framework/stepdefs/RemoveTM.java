package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.jenkins.Jenkins;
import activesupport.system.Properties;
import activesupport.aws.s3.S3;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static activesupport.database.DBUnit.*;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.APIJourneySteps.*;
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

    public RemoveTM(World world) throws Exception {
        this.world = world;

        Given("^i have an application with a transport manager$", () -> {
            if (world.createLicence.getOperatorType() == null) {
                world.createLicence.setOperatorType("public");
            }
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createApplication();
            world.APIJourneySteps.submitApplication();
        });
        When("^the internal user goes to remove the last transport manager$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalNavigation.urlSearchAndViewLicence();
            world.transportManagerJourneySteps.promptRemovalOfInternalTransportManager();
        });
        When("^the transport manager has been removed by an internal user$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalNavigation.urlSearchAndViewLicence();
            world.transportManagerJourneySteps.removeInternalTransportManager();
        });
        Then("^a pop up message should be displayed$", () -> {
            waitForTextToBePresent(alertHeaderValue);
            String alertContent = getElementValueByText("//*[@id=\"pg:lva-licence/transport_managers:index\"]/div[2]/div/div[2]/div/p", SelectorType.XPATH);
            assertEquals(alertContent, newAlertValue);
        });
        Given("^i add a transport manager to an existing licence$", () -> {
            world.createLicence.setUsername("newTmApi");
            world.createLicence.addTransportManager();
            world.createLicence.submitTransport();
            world.createLicence.addTmResponsibilities();
            world.createLicence.submitTmResponsibilities();
        });
        Then("^the remove TM popup should not be displaying new TM remove text$", () -> {
            waitForTextToBePresent(alertHeaderValue);
            if (Browser.navigate().getCurrentUrl().contains("variation") || Browser.navigate().getCurrentUrl().contains("application")) {
                assertFalse(isTextPresent(newAlertValue, 60));
                assertTrue(isTextPresent(applicationVariationTMAlertContent, 60));
            }
            if (Browser.navigate().getCurrentUrl().contains("ssap1")) {
                String alertContent = getElementValueByText("//div[@class='modal__content']/p", SelectorType.XPATH);
                assertEquals(alertContent, oldAlertValue);
            }
            if (tmCount > 1) {
                assertFalse(isTextPresent(newAlertValue, 60));
                assertTrue(isTextPresent(applicationVariationTMAlertContent, 60));
            }
        });
        Given("^a self-serve user removes the last TM$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
            click("//*[@value='Remove']", SelectorType.XPATH);
        });
        Given("^the licence has been granted$", () -> {
            world.grantLicence.grantLicence();
            world.grantLicence.payGrantFees();
        });
        When("^i create a variation$", () -> {
            world.updateLicence.createVariation(null);
        });
        And("^user attempts to remove the last TM without selecting an option$", () -> {
            waitForTextToBePresent(alertHeaderValue);
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        });
        Then("^an error message should be displayed$", () -> {
            waitForTextToBePresent(alertHeaderValue);
            do {
                // do nothing
            } while (!isTextPresent("You must select an option", 60));
            isLinkPresent("You must select an option", 60);
        });

        And("^i update the licence type$", () -> {
            world.updateLicence.updateLicenceType(world.createLicence.getLicenceId());
        });
        And("^the removal date is changed to (\\d+) hours into the future$", (Integer arg0) -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime futureDate = LocalDateTime.now().plusDays(2);
            String dateAndTime = dtf.format(futureDate);
            String sqlStatement = String.format(
                    "UPDATE `OLCS_RDS_OLCSDB`.`transport_manager_licence` SET `deleted_date` = '%s' WHERE (`licence_id` = '%s')",
                    dateAndTime,
                    world.createLicence.getLicenceId()
            );
            Properties.set("dbUsername", world.configuration.config.getString("dbUsername"));
            Properties.set("dbPassword", world.configuration.config.getString("dbPassword"));
            executeUpdateSQL(sqlStatement);
        });
        Then("^the TM email should be generated and letter attached$", () -> {
            String email = world.createLicence.getBusinessEmailAddress();
            String licenceNo = world.createLicence.getLicenceNumber();
            boolean letterExists = S3.checkLastTMLetterAttachment(email, licenceNo);
            Assert.assertTrue(letterExists);

        });
    }
}