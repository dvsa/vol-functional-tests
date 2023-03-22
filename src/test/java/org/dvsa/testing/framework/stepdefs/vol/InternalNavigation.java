package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.Objects;

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
        world.UIJourney.clickSubmit();
    }

    @And("i have logged in to internal")
    public void iHaveLoggedIntoInternal() throws HttpException {
        String intSystemAdmin = world.configuration.config.getString("intSystemAdmin");
        String intEnvPassword = world.configuration.config.getString("intEnvPassword");

        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env, "auth/login").toString();
            DriverUtils.get(myURL);
            if (isElementPresent("declarationRead", SelectorType.ID)) {
                waitAndClick("declarationRead", SelectorType.ID);
            }
            world.globalMethods.signIn(intSystemAdmin, intEnvPassword);
        } else {
            world.internalNavigation.logInAsAdmin();
        }
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
}