package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import apiCalls.enums.LicenceType;
import io.cucumber.datatable.DataTable;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.openqa.selenium.InvalidArgumentException;

import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SurrenderLogic extends BasePage implements En {
    private String contactNumber = "07123465976";
    private FakerUtils faker = new FakerUtils();
    private HashMap<String, String> address = faker.generateAddress();


    public SurrenderLogic(World world) {
        And("^i have started a surrender$", () -> {
            world.surrenderJourneySteps.navigateToSurrendersStartPage();
            world.surrenderJourneySteps.startSurrender();
        });
        Given("^i update my address details on my licence$", () -> {
            waitAndClick("form-actions[submit]", SelectorType.ID);
            clickByLinkText("Home");
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            clickByLinkText("Addresses");
            world.UIJourneySteps.addNewAddressDetails(address, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");
            replaceText("phone_primary", SelectorType.ID, contactNumber);
            waitAndClick("form-actions[save]", SelectorType.ID);
        });
        Then("^continue with application link is displayed$", () -> {
            assertFalse(isLinkPresent("Apply to surrender licence", 30));
            assertTrue(isLinkPresent("Continue with application to surrender licence", 30));
        });
        And("^user is taken to information change page on clicking continue application$", () -> {
            clickByLinkText("Continue with");
            assertTrue(Browser.navigate().getCurrentUrl().contains("information-changed"));
            String expectedChangedText = "Warning\n" +
                    "Since starting your application to surrender your licence, you have made changes to your licence information.";
            String actualChangeText = getText("//*[@class='govuk-warning-text__text']", SelectorType.XPATH);
            assertEquals(expectedChangedText, actualChangeText);
        });
        Given("^i remove a disc to my licence$", () -> {
            world.surrenderJourneySteps.removeDisc();
        });
        And("^the new correspondence details are displayed on correspondence page$", () -> {
            click("//*[contains(text(),'Review')]", SelectorType.XPATH);
            assertEquals(world.surrenderJourneySteps.getSurrenderAddressLine1(), String.format("%s\n%s\n%s\n%s",
                    address.get("addressLine1"), address.get("addressLine2"), address.get("addressLine3"), address.get("addressLine4")));
        });
        Given("^i add a disc to my licence$", () -> {
            world.UIJourneySteps.addDisc();
        });
        Given("^i am on the surrenders review contact details page$", () -> {
            assertTrue(Browser.navigate().getCurrentUrl().contains("review-contact-details"));
        });
        And("^i leave the surrenders journey$", () -> {
            clickByLinkText("Home");
            clickByLinkText(world.applicationDetails.getLicenceNumber());
        });
        And("^user is taken to review contact page on clicking continue application$", () -> {
            clickByLinkText("Continue");
            assertTrue(Browser.navigate().getCurrentUrl().contains("review-contact-details"));
            assertEquals(world.surrenderJourneySteps.getSurrenderAddressLine1(), String.format("%s\n%s\n%s\n%s", world.createApplication.getCorrespondenceAddressLine1(), world.createApplication.getCorrespondenceAddressLine2(), world.createApplication.getCorrespondenceAddressLine3(), world.createApplication.getCorrespondenceAddressLine4()));
        });
        Given("^i am on the surrenders current discs page$", () -> {
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            assertTrue(Browser.navigate().getCurrentUrl().contains("current-discs"));
        });
        And("^user is taken to the surrenders current discs on clicking continue application$", () -> {
            clickByLinkText("Continue");
            assertTrue(Browser.navigate().getCurrentUrl().contains("current-discs"));
        });
        And("^i am on the operator licence page$", () -> {
            waitAndClick("form-actions[submit]", SelectorType.ID);
            world.surrenderJourneySteps.addDiscInformation();
            waitForTextToBePresent("In your possession");
            assertTrue(Browser.navigate().getCurrentUrl().contains("operator-licence"));
        });
        And("^user is taken to the operator licence page on clicking continue application$", () -> {
            clickByLinkText("Continue");
            assertTrue(Browser.navigate().getCurrentUrl().contains("operator-licence"));
        });
        And("^i am on the community licence page$", () -> {
            if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
                waitAndClick("form-actions[submit]", SelectorType.ID);
                world.surrenderJourneySteps.addDiscInformation();
                waitForTextToBePresent("In your possession");
                world.surrenderJourneySteps.addOperatorLicenceDetails();
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
            } else {
                throw new InvalidArgumentException("Only a goods standard international licence has community pages");
            }
        });
        And("^user is taken to the community licence page on clicking continue application$", () -> {
            clickByLinkText("Continue");
            assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
        });
        And("^i am on the disc and doc review page$", () -> {
            waitAndClick("form-actions[submit]", SelectorType.ID);
            world.surrenderJourneySteps.addDiscInformation();
            waitForTextToBePresent("In your possession");
            world.surrenderJourneySteps.addOperatorLicenceDetails();
            if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
                world.surrenderJourneySteps.addCommunityLicenceDetails();
            }
            assertTrue(Browser.navigate().getCurrentUrl().contains("review"));
        });
        And("^user is taken to the disc and doc review page on clicking continue application$", () -> {
            clickByLinkText("Continue");
            assertTrue(Browser.navigate().getCurrentUrl().contains("review"));
        });
        And("^i am on the destroy disc page$", () -> {
            waitAndClick("form-actions[submit]", SelectorType.ID);
            world.surrenderJourneySteps.addDiscInformation();
            waitForTextToBePresent("In your possession");
            world.surrenderJourneySteps.addOperatorLicenceDetails();
            if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
                world.surrenderJourneySteps.addCommunityLicenceDetails();
            }
            waitAndClick("form-actions[submit]", SelectorType.NAME);
            assertTrue(Browser.navigate().getCurrentUrl().contains("destroy"));
        });
        And("^i am on the declaration page$", () -> {
            waitAndClick("form-actions[submit]", SelectorType.ID);
            world.surrenderJourneySteps.addDiscInformation();
            waitForTextToBePresent("In your possession");
            world.surrenderJourneySteps.addOperatorLicenceDetails();
            if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
                world.surrenderJourneySteps.addCommunityLicenceDetails();
            }
            waitAndClick("form-actions[submit]", SelectorType.NAME);
            waitAndClick("form-actions[submit]", SelectorType.NAME);
            assertTrue(Browser.navigate().getCurrentUrl().contains("declaration"));
        });
        And("^my application to surrender is under consideration$", () -> {
            world.updateLicence.printLicenceDiscs();
            world.surrenderJourneySteps.submitSurrender();
        });
        When("^the caseworker approves the surrender$", () -> {
            world.surrenderJourneySteps.caseworkManageSurrender();
            // Refresh page
            javaScriptExecutor("location.reload(true)");
            waitAndClick("actions[surrender]", SelectorType.ID);
        });
        Then("^the licence status should be \"([^\"]*)\"$", (String status) -> {
            world.UIJourneySteps.checkLicenceStatus(status);
        });
        And("^the surrender menu should be hidden in internal$", () -> {
            assertFalse(isElementPresent("//*[contains(@id,'menu-licence_surrender"));
        });
        And("^the \"([^\"]*)\" page should display$", (String page) -> {
            assertTrue(isTextPresent(page));
        });
        When("^the caseworker attempts to withdraw the surrender$", () -> {
            world.surrenderJourneySteps.caseworkManageSurrender();
            waitForElementToBeClickable("actions[surrender]", SelectorType.ID);
            waitAndClick("//*[contains(text(),'Withdraw')]", SelectorType.XPATH);
        });
        Then("^a modal box is displayed$", () -> {
            assertTrue(isElementPresent("//*[contains(text(),'Continue')]", SelectorType.XPATH));
        });
        And("^the caseworker confirms the withdraw$", () -> {
            waitAndClick("continue", SelectorType.ID);
        });
        Then("^the modal box is hidden$", () -> {
            assertFalse(isElementPresent("//*[@class='modal']", SelectorType.XPATH));
        });
        And("^the caseworker cancels the withdraw$", () -> {
            waitAndClick("cancel", SelectorType.ID);
        });
        And("^the surrender menu should be displayed$", () -> {
            assertTrue(isElementPresent("//*[contains(text(),'Surrender')]", SelectorType.XPATH));
        });
        Then("^the user should remain on the surrender details page$", () -> {
            assertTrue(Browser.navigate().getCurrentUrl().contains("surrender-details"));
            assertTrue(isLinkPresent("Surrender", 30));
        });
        And("^the licence should not displayed in selfserve$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            assertFalse(isLinkPresent(world.applicationDetails.getLicenceNumber(), 30));
        });
        And("^the user should be able to re apply for a surrender in internal$", () -> {
            world.surrenderJourneySteps.submitSurrender();
        });
        Then("^the quick actions and decision buttons are not displayed for the menu items listed$", (DataTable buttons) -> {
            assertFalse(isTextPresent("Quick actions"));
            List<String> section_button = buttons.asList(String.class);
            for (String button : section_button) {
                clickByLinkText(button);
                assertTrue(isElementNotPresent("//*[contains(@id,'menu-licence-quick-actions')]", SelectorType.XPATH));
                assertTrue(isElementNotPresent("//*[contains(@id,'menu-licence-decisions')]", SelectorType.XPATH));
            }
        });
        And("^the case worker undoes the surrender$", () -> {
            waitAndClick("//*[contains(@id,'menu-licence-decisions-undo-surrender')]", SelectorType.XPATH);
            waitForTextToBePresent("Are you sure you want to undo the surrender of this licence?");
            waitAndClick("form-actions[submit]", SelectorType.ID);
            waitForTextToBePresent("The licence surrender has been undone");
        });
        Then("^the change history has the surrender under consideration$", () -> {
            world.UIJourneySteps.navigateToChangeHistory();
            checkForPartialMatch("Surrender Under Consideration");
        });
        Then("^the change history shows the surrender and its withdrawal$", () -> {
            world.UIJourneySteps.navigateToChangeHistory();
            checkForPartialMatch("Surrender Application Withdrawn");
            checkForPartialMatch("Surrender Under Consideration");
        });
    }
}