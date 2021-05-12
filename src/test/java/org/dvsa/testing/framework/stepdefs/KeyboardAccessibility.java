package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;


import java.net.URL;

import static junit.framework.TestCase.assertTrue;

public class KeyboardAccessibility extends BasePage implements En {
    private static final URL scriptUrl = KeyboardAccessibility.class.getResource("/axe/axe.min.js");


    public KeyboardAccessibility(World world) {
        When("^i navigate to self serve application main pages i can skip to main content$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Addresses");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Financial evidence");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Vehicle declarations");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Financial history");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Licence history");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Convictions and penalties");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Review and declarations");
            world.UIJourneySteps.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence main pages i can skip to main content$", () -> {
            world.UIJourneySteps.CheckSkipToMainContentOnExternalUserLogin();
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "View");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Type of licence");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Business type");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Business details");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Address");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Directors");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence","Operating centres and authorisation");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Vehicles");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Trailers");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Safety and compliance");
            world.UIJourneySteps.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence nav bar pages i can skip to main content$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToNavBarPage("Home");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToNavBarPage("Manage users");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToNavBarPage("Your account");
            world.UIJourneySteps.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence surrender pages i can skip to main content$", () -> {
            world.surrenderJourneySteps.navigateToSurrendersStartPage();
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.surrenderJourneySteps.startSurrender();
            world.UIJourneySteps.skipToMainContentAndCheck();
            waitAndClick("form-actions[submit]",SelectorType.ID);
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.surrenderJourneySteps.addDiscInformation();
            waitForTextToBePresent("In your possession");
            world.UIJourneySteps.skipToMainContentAndCheck();
            world.surrenderJourneySteps.addOperatorLicenceDetails();
            if (world.createApplication.getLicenceType().equals("standard_international")) {
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
                world.UIJourneySteps.skipToMainContentAndCheck();
                world.surrenderJourneySteps.addCommunityLicenceDetails();
            }
            world.UIJourneySteps.skipToMainContentAndCheck();
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Securely destroy");
            world.UIJourneySteps.skipToMainContentAndCheck();
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTitleToBePresent("Declaration");
        });
    }
}