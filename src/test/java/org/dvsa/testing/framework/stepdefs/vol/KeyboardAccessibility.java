package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;


import static junit.framework.TestCase.assertTrue;

public class KeyboardAccessibility extends BasePage implements En {

    public KeyboardAccessibility(World world) {
        When("^i navigate to self serve application main pages i can skip to main content$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Addresses");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Financial evidence");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Vehicle declarations");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Financial history");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Licence history");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Convictions and penalties");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("application", "Review and declarations");
            world.UIJourney.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence main pages i can skip to main content$", () -> {
            world.UIJourney.CheckSkipToMainContentOnExternalUserLogin();
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "View");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Type of licence");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Business type");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Business details");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Address");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Directors");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence","Operating centres and authorisation");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Vehicles");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Trailers");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToPage("licence", "Safety and compliance");
            world.UIJourney.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence nav bar pages i can skip to main content$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToNavBarPage("Home");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToNavBarPage("Manage users");
            world.UIJourney.skipToMainContentAndCheck();
            world.selfServeNavigation.navigateToNavBarPage("Your account");
            world.UIJourney.skipToMainContentAndCheck();
        });
        When("^i navigate to self serve licence surrender pages i can skip to main content$", () -> {
            world.surrenderJourney.navigateToSurrendersStartPage();
            world.UIJourney.skipToMainContentAndCheck();
            world.surrenderJourney.startSurrender();
            world.UIJourney.skipToMainContentAndCheck();
            waitAndClick("form-actions[submit]",SelectorType.ID);
            world.UIJourney.skipToMainContentAndCheck();
            world.surrenderJourney.addDiscInformation();
            waitForTextToBePresent("In your possession");
            world.UIJourney.skipToMainContentAndCheck();
            world.surrenderJourney.addOperatorLicenceDetails();
            if (world.createApplication.getLicenceType().equals("standard_international")) {
                assertTrue(Browser.navigate().getCurrentUrl().contains("community-licence"));
                world.UIJourney.skipToMainContentAndCheck();
                world.surrenderJourney.addCommunityLicenceDetails();
            }
            world.UIJourney.skipToMainContentAndCheck();
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Securely destroy");
            world.UIJourney.skipToMainContentAndCheck();
            waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTitleToBePresent("Declaration");
        });
    }
}