package org.dvsa.testing.framework.stepdefs.permits.common;

import Injectors.World;
import activesupport.system.Properties;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.FeeDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SelectALicencePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class CommonSteps extends BasePermitJourney implements En {

    public CommonSteps(World world) {
        Given("^I am on the VOL self-serve site$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());
        });
        And("^I am on the Annual ECMT licence selection page$", () -> {
            EcmtApplicationJourney.beginApplicationToLicenceSelectionPage(world);
        });
        Then("^I will get an error message on the licence page$", () -> {
            assertTrue(SelectALicencePage.isErrorMessagePresent());
        });
        Then("^I should get an error message$", () -> {
             boolean hasError = BasePage.isErrorMessagePresent();
             assertTrue("Error message was not displayed on the page", hasError);
        });
        When("^I save and return to overview$", BasePermitPage::clickReturnToOverview);
        When("^I sign on as an external user$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());        });
        And("^all fees have been waived$", () -> {
            LicenceDetailsPageJourney.clickFeesTab();
            FeeDetailsPageJourney.whileFeesPresentWaveFee();
        });
        And("^I am on the permits dashboard on external$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
        });
    }

    public static void clickToPermitTypePage(@NotNull World world) {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        HomePageJourney.beginPermitApplication();
    }


    public static void waitUntilPermitHasStatus(World world) {
        HomePage.PermitsTab.untilPermitHasStatus(
                world.applicationDetails.getLicenceNumber(),
                PermitStatus.VALID,
                Duration.LONG,
                TimeUnit.MINUTES);
    }
}
