package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static junit.framework.TestCase.assertTrue;
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
            world.UIJourneySteps.generateLetter();
        });

        And("^i save the letter$", () -> {
            click("//*[@id='form-actions[submit]']",SelectorType.XPATH);
            waitForTextToBePresent("Send letter");
            click("//*[@id='close']",SelectorType.XPATH);
            waitForTextToBePresent("The document has been saved");
        });

        When("^I generate Licence Document$", () -> {
            world.UIJourneySteps.printLicence();
        });

        When("^I delete a licence document from table$", () -> {
            world.UIJourneySteps.deleteLicenceDocument();
        });

        When("^I delete generated letter above from the table$", () -> {
            world.UIJourneySteps.deleteLetterDocument();
        });

        When("^the document should be deleted$", () -> {
            waitForTextToBePresent("Deleted successfully");
        });

        When("^a caseworker adds a new operating centre out of the traffic area$", () -> {
            world.UIJourneySteps.addNewOperatingCentre();
        });

        Then("^the postcode warning message should be displayed on internal$", () -> {
            assertTrue(isTextPresent("This operating centre is in a different traffic area from the other centres.",30));
            click("form-actions[confirm-add]", SelectorType.ID);
            click("form-actions[submit]", SelectorType.ID);
            waitForTextToBePresent("Operating centres and authorisation");
            assertTrue(isElementPresent("//input[@value='2 MAR PLACE, ALLOA, FK10 1AA']", SelectorType.XPATH));
        });

        Given("^I have partially applied for a \"([^\"]*)\" \"([^\"]*)\" licence$", (String operator, String licenceType) -> {
            world.createLicence.setOperatorType(operator);
            world.createLicence.setLicenceType(licenceType);
            if (licenceType.equals("special_restricted") && (world.createLicence.getApplicationNumber() == null)) {
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createSpecialRestrictedLicence();
            } else if (world.createLicence.getApplicationNumber() == null) {
                world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
                world.APIJourneySteps.createApplication();

            }
        });

        When("^the caseworker completes and submits the application$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalNavigation.urlSearchAndViewApplication();
            click("//*[@id='menu-application-decisions-submit']", SelectorType.XPATH);
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);

            waitForTextToBePresent("has been submitted");

            world.UIJourneySteps.caseWorkerCompleteConditionsAndUndertakings();

            world.UIJourneySteps.caseWorkerCompleteReviewAndDeclarations();

            world.UIJourneySteps.caseWorkerCompleteOverview();
        });

        And("^grants the application$", () -> {
            int tableColumns;
            waitAndClick("//*[@id='menu-application_fee']", SelectorType.XPATH);
            world.feeAndPaymentJourneySteps.selectFee();
            String fee = getAttribute("details[maxAmountForValidator]", SelectorType.ID, "value").toString();
            world.feeAndPaymentJourneySteps.payFee(fee, "cash");
            waitForTextToBePresent("The payment was made successfully");
            long kickoutTime = System.currentTimeMillis() + 15000;

            do {
                tableColumns = returnTableRows("//tbody/tr/*",SelectorType.XPATH);
                javaScriptExecutor("location.reload(true)");
            } while (tableColumns > 1 && System.currentTimeMillis() < kickoutTime);

            if (System.currentTimeMillis() > kickoutTime) {
                throw new TimeoutException("Kickout time for expecting no fee is present when granting a licence exceeded.");
            }
            waitAndClick("//*[@id='menu-application-decisions-grant']", SelectorType.XPATH);
            waitAndClick("//*[@id='inspection-request-confirm[createInspectionRequest]']", SelectorType.XPATH);
            click("//*[@id='form-actions[grant]']", SelectorType.XPATH);
        });

        Then("^the licence is granted in Internal$", () -> {
            waitForTextToBePresent("Overview");
            world.UIJourneySteps.checkLicenceStatus("Granted");
        });
    }
}