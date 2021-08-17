package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserType;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.Assert.assertNotNull;

public class InternalApplication extends BasePage implements En {
    public InternalApplication(World world) {
        Then("^The pop up should contain letter details$", () -> {

            waitForTextToBePresent("Amend letter");

            String categoryValue = getText("//*[@id='generate-document']/div[2]", SelectorType.XPATH);
            assertNotNull(categoryValue);

            String subCategoryValue = getText("//*[@id='generate-document']/div[3]", SelectorType.XPATH);
            assertNotNull(subCategoryValue);

            String templateValue = getText("//*[@id='generate-document']/div[4]", SelectorType.XPATH);
            assertNotNull(templateValue);

            String docStoreLink = getAttribute("//a[contains(@href,'file:////')]", SelectorType.XPATH, "href");
            assertNotNull(docStoreLink);
            assertTrue(docStoreLink.contains(".rtf"));
        });

        When("^i generate a letter$", () -> {
            world.UIJourney.generateLetter();
        });

        And("^i save the letter$", () -> {
            click("//*[@id='form-actions[submit]']",SelectorType.XPATH);
            waitForTextToBePresent("Send letter");
            click("//*[@id='close']",SelectorType.XPATH);
            waitForTextToBePresent("The document has been saved");
        });

        When("^I generate Licence Document$", () -> {
            world.UIJourney.printLicence();
        });

        When("^I delete a licence document from table$", () -> {
            world.UIJourney.deleteLicenceDocument();
        });

        When("^I delete generated letter above from the table$", () -> {
            world.UIJourney.deleteLetterDocument();
        });

        When("^the document should be deleted$", () -> {
            waitForTextToBePresent("Deleted successfully");
        });

        When("^a caseworker adds a new operating centre out of the traffic area$", () -> {
            world.UIJourney.addNewOperatingCentre();
        });

        Then("^the postcode warning message should be displayed on internal$", () -> {
            assertTrue(isTextPresent("This operating centre is in a different traffic area from the other centres."));
            click("form-actions[confirm-add]", SelectorType.ID);
            click("form-actions[submit]", SelectorType.ID);
            waitForTextToBePresent("Operating centres and authorisation");
            assertTrue(isElementPresent("//input[@value='2 MAR PLACE, ALLOA, FK10 1AA']", SelectorType.XPATH));
        });

        Given("^I have partially applied for a \"([^\"]*)\" \"([^\"]*)\" licence$", (String operator, String licenceType) -> {
            world.createApplication.setOperatorType(operator);
            world.createApplication.setLicenceType(licenceType);
            if (licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)) {
                world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourney.createSpecialRestrictedLicence();
            } else if (world.createApplication.getApplicationId() == null) {
                world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
                world.APIJourney.createApplication();

            }
        });

        When("^the caseworker completes and submits the application$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.urlSearchAndViewApplication();
            click("//*[@id='menu-application-decisions-submit']", SelectorType.XPATH);
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);

            waitForTextToBePresent("has been submitted");

            world.UIJourney.caseWorkerCompleteConditionsAndUndertakings();

            world.UIJourney.caseWorkerCompleteReviewAndDeclarations();

            world.UIJourney.caseWorkerCompleteOverview();
        });

        And("^grants the application$", () -> {
            int tableColumns;
            waitAndClick("//*[@id='menu-application_fee']", SelectorType.XPATH);
            world.feeAndPaymentJourney.selectFee();
            String fee = getAttribute("details[maxAmountForValidator]", SelectorType.ID, "value").toString();
            world.feeAndPaymentJourney.payFee(fee, "cash");
            waitForTextToBePresent("The payment was made successfully");
            long kickoutTime = System.currentTimeMillis() + 15000;

            do {
                tableColumns = returnTableRows("//tbody/tr/*",SelectorType.XPATH);
                refreshPageWithJavascript();
            } while (tableColumns > 1 && System.currentTimeMillis() < kickoutTime);

            if (System.currentTimeMillis() > kickoutTime) {
                throw new TimeoutException("Kickout time for expecting no fee is present when granting a licence exceeded.");
            }
            waitAndClick("//*[@id='menu-application-decisions-grant']", SelectorType.XPATH);
            waitAndClick("//input[@id='grant-authority']", SelectorType.XPATH);
            waitAndClick("//button[@id='form-actions[continue-to-grant]']", SelectorType.XPATH);
            waitAndClick("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH);
            click("//*[@id='form-actions[grant]']", SelectorType.XPATH);
        });

        Then("^the licence is granted in Internal$", () -> {
            waitForTextToBePresent("Overview");
            world.UIJourney.checkLicenceStatus("Granted");
        });
    }
}