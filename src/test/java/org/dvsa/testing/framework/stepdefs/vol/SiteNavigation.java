package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.packages.Browser;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

public class SiteNavigation extends BasePage {

    World world;
    Initialisation initialisation;

    public SiteNavigation(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("I am on Selfserve homepage")
    public void iAmOnSelfServeHomepage() {
        String externalLoginURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
        Browser.navigate().get(externalLoginURL);
    }

    @Given("i am on the internal admin login page")
    public void iAmOnTheInternalAdminLoginPage(){
        String internalLoginURL = URL.build(ApplicationType.INTERNAL, world.configuration.env, "auth/login").toString();
        Browser.navigate().get(internalLoginURL);
    }
}