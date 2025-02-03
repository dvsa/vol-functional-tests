package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

public class InternalNavigation extends BasePage {

    public World world;

    public InternalNavigation(World world) {
        this.world = world;
    }

    @When("i navigate to the {string} safety and compliance page on internal")
    public void iNavigateToTheSafetyAndCompliancePageOnInternal(String type) throws HttpException {
        world.internalNavigation.navigateToPage(type, SelfServeSection.SAFETY_AND_COMPLIANCE);
    }

    @When("i click submit")
    public void iClickSubmit() {
        UniversalActions.clickSubmit();
    }

    @And("i have logged in to internal as {string}")
    public void iHaveLoggedIntoInternalAs(String role) throws HttpException {
        world.internalNavigation.loginIntoInternal(role);
    }

    @When("i am on the internal application overview page")
    public void iAmOnTheApplicationOverviewPage() throws HttpException {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
    }

    @When("i am on the internal variation overview page")
    public void iAmOnTheVariationOverviewPage() throws HttpException {
        world.internalNavigation.navigateToPage("variation", SelfServeSection.VIEW);
    }

    @When("I navigate to the undertakings page on internal")
    public void iNavigateToTheUndertakingsPageOnInternal() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.CONDITIONS_AND_UNDERTAKINGS);
    }

    @When("I am on a licence Overview page")
    public void iAmOnALicenceOverviewPage() {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env, "licence/318365/").toString();
        DriverUtils.get(myURL);
    }

    @And("i url search for my licence {string}")
    public void iUrlSearchForMyLicence(String applicationId) {
        world.internalNavigation.getLicence(applicationId);
    }
}