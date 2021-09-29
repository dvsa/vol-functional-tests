package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
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
    private final World world;

    public Surrenders(World world) {this.world = world;}

    @When("i surrender my licence to the review discs and documentation page")
    public void iSurrenderMyLicenceToTheReviewDiscsAndDocumentationPage() {
        world.surrenderJourney.submitSurrenderUntilReviewPage();
    }

    @And("the correct destroyed disc details should be displayed")
    public void theCorrectDestroyedDiscDetailsShouldBeDisplayed() {
        String destroyedDiscs = getText("//dt[contains(text(),'Number to be destroyed')]//..//dd", SelectorType.XPATH);
        assertEquals(world.surrenderJourney.getDiscsToDestroy(), destroyedDiscs);
    }

    @And("the correct lost disc details should be displayed")
    public void theCorrectLostDiscDetailsShouldBeDisplayed() {
        String lostDiscs = getText("//dt[contains(text(),'Number lost')]//..//dd", SelectorType.XPATH);
        assertEquals(world.surrenderJourney.getDiscsLost(), lostDiscs);
    }

    @Then("the correct stolen disc details should be displayed")
    public void theCorrectStolenDiscDetailsShouldBeDisplayed() {
        String stolenDiscs = getText("//dt[contains(text(),'Number stolen')]//..//dd", SelectorType.XPATH);
        assertEquals(world.surrenderJourney.getDiscsStolen(), stolenDiscs);
    }

    @And("the correct operator details should be displayed")
    public void theCorrectOperatorDetailsShouldBeDisplayed() {
        String operatorLicenceDocumentStatus = getText("//dt[contains(text(),'Licence document')]//..//dd", SelectorType.XPATH);
        assertEquals("lost", operatorLicenceDocumentStatus);
    }

    @And("the correct community licence details should be displayed")
    public void theCorrectCommunityLicenceDetailsShouldBeDisplayed() {
        String communityLicenceDocumentStatus = getText("//dt[contains(text(),'Licence document and all certified copies')]//..//dd", SelectorType.XPATH);
        assertEquals("stolen", communityLicenceDocumentStatus);
    }

    @Given("i have a valid {string} {string} licence with an open case and bus reg")
    public void iHaveAValidLicenceWithAnOpenCaseAndBusReg(String operatorType, String licenceType) {
        world.busRegistrationJourney.createLicenceWithOpenCaseAndBusReg(operatorType, licenceType);
    }


    @And("i choose to surrender my licence with {string}")
    public void iChooseToSurrenderMyLicenceWith(String surrenderMethod) {
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
    }

    @Given("a caseworker views the surrender details")
    public void aCaseworkerViewsTheSurrenderDetails() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewLicence();
        waitAndClick("menu-licence_surrender", SelectorType.ID);
    }

    @Then("any open cases should be displayed")
    public void anyOpenCasesShouldBeDisplayed() {
        Assert.assertTrue(isTextPresent("open cases associated with this licence"));
        Assert.assertTrue(isLinkPresent(String.valueOf(world.updateLicence.getCaseId()),10));
    }

    @And("any open bus registrations should be displayed")
    public void anyOpenBusRegistrationsShouldBeDisplayed() {
        Assert.assertTrue(isTextPresent("active bus registrations associated with this licence."));
        Assert.assertTrue(isLinkPresent(String.valueOf(world.applicationDetails.getLicenceNumber()),10));
    }

    @And("tick boxes should be displayed")
    public void tickBoxesShouldBeDisplayed() {
        isTextPresent("Digital signature has been checked");
        isTextPresent("ECMS has been checked");
    }

    @Then("the Surrender button should not be clickable")
    public void theSurrenderButtonShouldNotBeClickable() {
        if (isElementPresent("//*[contains(@name,'actions[surrender]')]", SelectorType.XPATH))
            assertTrue(isElementEnabled("//*[@id='actions[surrender]']", SelectorType.XPATH));
    }

    @When("the caseworker checks the case and bus reg is visible in surrenders")
    public void theCaseworkerChecksTheCaseAndBusRegIsVisibleInSurrenders() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewLicence();
        waitForTextToBePresent("Overview");
        if (isTextPresent("Surrender")) {
            clickByLinkText("Surrender");
            waitForTextToBePresent("Summary: Application to surrender an operator licence");
            Assert.assertTrue(isTextPresent("open cases associated with this licence"));
            Assert.assertTrue(isLinkPresent(String.valueOf(world.updateLicence.getCaseId()), 10));
            Assert.assertTrue(isTextPresent("active bus registrations associated with this licence."));
            Assert.assertTrue(isLinkPresent(String.valueOf(world.applicationDetails.getLicenceNumber()), 10));
            WebElement surrenderButton = findElement("//*[@id='actions[surrender]']", SelectorType.XPATH);
            Assert.assertTrue(surrenderButton.getAttribute("class").contains("disabled"));
        }
    }

    @When("the open case and bus reg is closed")
    public void theOpenCaseAndBusRegIsClosed() {
        world.internalNavigation.urlSearchAndViewLicence();
        clickByLinkText("Cases");
        world.UIJourney.closeCase();
        waitForTextToBePresent("Case closed");
        world.internalNavigation.urlSearchAndViewLicence();
        clickByLinkText("Bus registrations");
        world.busRegistrationJourney.closeBusReg();
    }

    @And("the tick boxes are checked")
    public void theTickBoxesAreChecked() {
        boolean isDigital = isElementPresent("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);
        if (isDigital) {
            waitAndClick("//*[contains(text(),'Digital signature has been checked')]", SelectorType.XPATH);
        } else {
            waitAndClick("//*[contains(text(),'Physical signature has been checked')]", SelectorType.XPATH);
        }
        waitForTextToBePresent("Your changes have been successfully saved");
        waitAndClick("//*[contains(text(),'ECMS has been checked')]", SelectorType.XPATH);
        waitForTextToBePresent("Your changes have been successfully saved");
    }

    @When("the Surrender button is clicked")
    public void theSurrenderButtonIsClicked() {
        click("actions[surrender]", SelectorType.ID);
    }

    @Then("the licence should be surrendered")
    public void theLicenceShouldBeSurrendered() {
        assertTrue(isElementPresent("//*[contains(text(),'Surrendered')]", SelectorType.XPATH));

    }
}
