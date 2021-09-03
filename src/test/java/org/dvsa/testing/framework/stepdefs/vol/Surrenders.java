package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.Assert.assertEquals;

public class Surrenders extends BasePage implements En {
    ValidatableResponse apiResponse;
    private Integer surrenderId;
    private String operatorLicence;
    private String communityLicence;

    public Surrenders(World world) {
        When("^i surrender my licence to the review discs and documentation page$", () -> {
            world.surrenderJourney.submitSurrenderUntilReviewPage();
        });
        Then("^the correct destroyed disc details should be displayed$", () -> {
            String destroyedDiscs = getText("//dt[contains(text(),'Number to be destroyed')]//..//dd", SelectorType.XPATH);
            assertEquals(world.surrenderJourney.getDiscsToDestroy(), destroyedDiscs);
        });
        And("^the correct lost disc details should be displayed$", () -> {
            String lostDiscs = getText("//dt[contains(text(),'Number lost')]//..//dd", SelectorType.XPATH);
            assertEquals(world.surrenderJourney.getDiscsLost(), lostDiscs);
        });
        And("^the correct stolen disc details should be displayed$", () -> {
            String stolenDiscs = getText("//dt[contains(text(),'Number stolen')]//..//dd", SelectorType.XPATH);
            assertEquals(world.surrenderJourney.getDiscsStolen(), stolenDiscs);
        });
        And("^the correct operator details should be displayed$", () -> {
            String operatorLicenceDocumentStatus = getText("//dt[contains(text(),'Licence document')]//..//dd", SelectorType.XPATH);
            assertEquals("lost", operatorLicenceDocumentStatus);
        });
        And("^the correct community licence details should be displayed$", () -> {
            String communityLicenceDocumentStatus = getText("//dt[contains(text(),'Licence document and all certified copies')]//..//dd", SelectorType.XPATH);
            assertEquals("stolen", communityLicenceDocumentStatus);
        });
        Then("^the internal surrender menu should be displayed$", () -> {
            waitForTextToBePresent(world.applicationDetails.getLicenceNumber());
        });
        Then("^any open cases should be displayed$", () -> {
            Assert.assertTrue(isTextPresent("open cases associated with this licence"));
            Assert.assertTrue(isLinkPresent(String.valueOf(world.updateLicence.getCaseId()),10));
        });
        And("^any open bus registrations should be displayed$", () -> {
            Assert.assertTrue(isTextPresent("active bus registrations associated with this licence."));
            Assert.assertTrue(isLinkPresent(String.valueOf(world.applicationDetails.getLicenceNumber()),10));
        });
        And("^tick boxes should be displayed$", () -> {
            isTextPresent("Digital signature has been checked");
            isTextPresent("ECMS has been checked");
        });
        Then("^the surrender print and sign page is displayed$", () -> {
            world.UIJourney.signManually();
        });
        When("^a caseworker views the surrender$", () -> {
            world.surrenderJourney.caseworkManageSurrender();
        });
        And("^an open case and bus reg are created$", () -> {
            world.busRegistrationJourney.internalSiteAddBusNewReg(5);
            world.updateLicence.createCase();
        });
        Given("^i have a valid \"([^\"]*)\" \"([^\"]*)\" licence with an open case and bus reg$", (String operatorType, String licenceType) -> {
            world.busRegistrationJourney.createLicenceWithOpenCaseAndBusReg(operatorType, licenceType);
        });
        When("^a caseworker views the surrender details$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.getLicence();
            waitAndClick("menu-licence_surrender", SelectorType.ID);
        });

        And("^i choose to surrender my licence with \"([^\"]*)\"$", (String surrenderMethod) -> {
            world.surrenderJourney.submitSurrenderUntilChoiceOfVerification();
            EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
            if (surrenderMethod.equalsIgnoreCase("verify")) {
                if (GenericUtils.isVerifySupportedPlatform(env.name())) {
                    waitAndClick("//*[@id='sign']", SelectorType.XPATH);
                    world.UIJourney.signWithVerify();
                    world.surrenderJourney.checkVerifyConfirmation();
                   } else {
                    fail("Verify not supported on this platform");
                }
            } else {
                waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
                world.UIJourney.signManually();
                refreshPageWithJavascript();
            }
            assertEquals(getText("//*[@class='overview__status green']", SelectorType.XPATH), "SURRENDER UNDER CONSIDERATION");
        });

        Then("^the Surrender button should not be clickable$", () -> {
            if (isElementPresent("//*[contains(@name,'actions[surrender]')]", SelectorType.XPATH)) {
                assertTrue(isElementEnabled("//*[@id='actions[surrender]']", SelectorType.XPATH));
            }
        });
        And("^the open case and bus reg is closed$", () -> {
            world.internalNavigation.getLicence();
            clickByLinkText("Cases");
            world.UIJourney.closeCase();
            waitForTextToBePresent("Case closed");
            world.internalNavigation.getLicence();
            clickByLinkText("Bus registrations");
            world.busRegistrationJourney.closeBusReg();
        });
        And("^the tick boxes are checked$", () -> {
            boolean isDigital = isElementPresent("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);

            if (isDigital) {
                waitAndClick("//*[contains(text(),'Digital signature has been checked')]", SelectorType.XPATH);
            } else {
                waitAndClick("//*[contains(text(),'Physical signature has been checked')]", SelectorType.XPATH);
            }
            waitForTextToBePresent("Your changes have been successfully saved");
            waitAndClick("//*[contains(text(),'ECMS has been checked')]", SelectorType.XPATH);
            waitForTextToBePresent("Your changes have been successfully saved");
        });
        When("^the Surrender button is clicked$", () -> {
            click("actions[surrender]", SelectorType.ID);
        });
        Then("^the licence should be surrendered$", () -> {
            assertTrue(isElementPresent("//*[contains(text(),'Surrendered')]", SelectorType.XPATH));
        });
        When("^the caseworker checks the case and bus reg is visible in surrenders$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.getLicence();
            waitForTextToBePresent("Overview");
            if (isTextPresent("Surrender")){
                clickByLinkText("Surrender");
                waitForTextToBePresent("Summary: Application to surrender an operator licence");
                Assert.assertTrue(isTextPresent("open cases associated with this licence"));
                Assert.assertTrue(isLinkPresent(String.valueOf(world.updateLicence.getCaseId()),10));
                Assert.assertTrue(isTextPresent("active bus registrations associated with this licence."));
                Assert.assertTrue(isLinkPresent(String.valueOf(world.applicationDetails.getLicenceNumber()),10));
                WebElement surrenderButton = findElement("//*[@id='actions[surrender]']", SelectorType.XPATH);
                Assert.assertTrue(surrenderButton.getAttribute("class").contains("disabled"));
            }
        });
    }
}