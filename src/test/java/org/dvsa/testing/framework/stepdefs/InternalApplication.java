package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;


import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

public class InternalApplication extends BasePage implements En {
    public InternalApplication(World world) {
        Then("^The pop up should contain letter details$", () -> {

            waitForTextToBePresent("Amend letter");

            String categoryValue = getElementValueByText("//*[@id='generate-document']/div[2]", SelectorType.XPATH);
            assertNotNull(categoryValue);

            String subCategoryValue = getElementValueByText("//*[@id='generate-document']/div[3]", SelectorType.XPATH);
            assertNotNull(subCategoryValue);

            String templateValue = getElementValueByText("//*[@id='generate-document']/div[4]", SelectorType.XPATH);
            assertNotNull(templateValue);

            String docStoreLink = getElementValueByText("//*[@id='generate-document']/div[4]/div/strong", SelectorType.XPATH);
            assertNotNull(docStoreLink);
            assertTrue(docStoreLink.contains(".rtf"));
        });
        When("^I generate a letter$", () -> {
            world.UIJourneySteps.generateLetter();
        });
        When("^a caseworker adds a new operating centre out of the traffic area$", () -> {
            world.UIJourneySteps.addNewOperatingCentre();
        });
        Then("^the postcode warning message should be displayed on internal$", () -> {
            assertTrue(isTextPresent("This operating centre is in a different traffic area from the other centres.", 10));
            click("form-actions[confirm-add]", SelectorType.ID);
            click("form-actions[submit]", SelectorType.ID);
            waitForTextToBePresent("Operating centres and authorisation");
            assertTrue(isElementPresent("//*[@value='2 MAR PLACE, ALLOA, FK10 1AA']", SelectorType.XPATH));
        });
        Given("^I have partially applied for a \"([^\"]*)\" \"([^\"]*)\" licence$", (String operator, String licenceType) -> {
            world.createLicence.setOperatorType(operator);
            world.createLicence.setLicenceType(licenceType);
            if (licenceType.equals("special_restricted") && (world.createLicence.getApplicationNumber() == null)) {
                world.APIJourneySteps.registerAndGetUserDetails();
                world.APIJourneySteps.createSpecialRestrictedLicence();
            } else if (world.createLicence.getApplicationNumber() == null) {
                world.APIJourneySteps.registerAndGetUserDetails();
                world.APIJourneySteps.createApplication();

            }
        });
        When("^the caseworker completes and submits the application$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            Browser.navigate().get("https://iuap1.olcs.qa.nonprod.dvsa.aws/application/" + world.createLicence.getApplicationNumber());
            click("//*[@id='menu-application-decisions-submit']", SelectorType.XPATH);
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            javaScriptExecutor("location.reload(true)");
            waitForTextToBePresent("Application details");

            world.UIJourneySteps.caseWorkerCompleteConditionsAndUndertakings();

            world.UIJourneySteps.caseWorkerCompleteReviewAndDeclarations();

            world.UIJourneySteps.caseWorkerCompleteOverview();
        });

        And("^grants the application$", () -> {
            int tableColumns;
            waitAndClick("//*[@id='menu-application_fee']", SelectorType.XPATH);
            world.UIJourneySteps.selectFee();
            world.UIJourneySteps.payFee("209", "cash", null, null, null);
            do {
                tableColumns = returnTableRows("//tbody/tr/*",SelectorType.XPATH);
                javaScriptExecutor("location.reload(true)");
            }while (tableColumns>1);
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