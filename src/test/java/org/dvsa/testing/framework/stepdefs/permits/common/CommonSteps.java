package org.dvsa.testing.framework.stepdefs.permits.common;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.system.Properties;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonSteps extends BasePermitJourney {
    private final World world;

    public CommonSteps(World world) {
        this.world = world;
    }

    @Given("I am on the VOL self-serve site")
    public void iAmOnTheVOLSelfServeSite() {
        deleteCookies();
        refreshPage();
        get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());
    }

    @And("I am on the Annual ECMT licence selection page")
    public void iAmOnTheAnnualECMTLicence() {
        EcmtApplicationJourney.beginApplicationToLicenceSelectionPage(world);
    }

    @Then("I will get an error message on the licence page")
    public void iWillGetAnErrorMessage() {
        assertTrue(SelectALicencePage.isErrorMessagePresent());
    }

    @Then("I should get an error message")
    public void iShouldGetAnErrorMessage() {
        boolean hasError = BasePage.isErrorMessagePresent();
        assertTrue(hasError,"Error message was not displayed on the page");
    }

    @When("I save and return to overview")
    public void iSaveAndReturnToOverview() {
        BasePermitPage.clickReturnToOverview();
    }

    @When("I sign on as an external user")
    public void iSignOnAsAnExternalUser() {
        world.APIJourney.createAdminUser();
    }

    @And("all fees have been waived")
    public void allFeesHaveBeenWaived() {
        LicenceDetailsPageJourney.clickFeesTab();
        FeeDetailsPageJourney.whileFeesPresentWaveFee();
    }

    @And("I am on the permits dashboard on external")
    public void iAmOnThePermitsDashboardOnExternal() {
        HomePageJourney.selectPermitTab();
    }

    public static void clickToPermitTypePage(@NotNull World world) {
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