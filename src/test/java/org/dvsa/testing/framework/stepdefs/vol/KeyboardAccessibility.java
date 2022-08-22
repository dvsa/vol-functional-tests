package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.enums.SelfServeNavBar;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.net.MalformedURLException;

import static junit.framework.TestCase.assertTrue;

public class KeyboardAccessibility extends BasePage implements En {
    private final World world;

    public KeyboardAccessibility (World world) {this.world = world;}

    @When("i navigate to self serve application main pages i can skip to main content")
    public void iNavigateToSelfServeApplicationMainPagesICanSkipToMainContent() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.ADDRESSES);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.FINANCIAL_EVIDENCE);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.VEHICLE_DECLARATIONS);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.FINANCIAL_HISTORY);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.LICENCE_HISTORY);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.CONVICTIONS_AND_PENALTIES);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
        world.UIJourney.skipToMainContentAndCheck();
    }

    @When("i navigate to self serve licence main pages i can skip to main content")
    public void iNavigateToSelfServeLicenceMainPagesICanSkipToMainContent() {
        world.UIJourney.CheckSkipToMainContentOnExternalUserLogin();
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.TYPE_OF_LICENCE);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.BUSINESS_TYPE);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.BUSINESS_DETAILS);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.ADDRESSES);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.DIRECTORS);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence",SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.TRANSPORT_MANAGERS);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VEHICLES);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.TRAILERS);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.SAFETY_AND_COMPLIANCE);
        world.UIJourney.skipToMainContentAndCheck();
    }

    @When("i navigate to self serve licence nav bar pages i can skip to main content")
    public void iNavigateToSelfServeLicenceNavBarPagesICanSkipToMainContent() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.HOME);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.MANAGE_USERS);
        world.UIJourney.skipToMainContentAndCheck();
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.YOUR_ACCOUNT);
        world.UIJourney.skipToMainContentAndCheck();
    }

    @When("i navigate to self serve licence surrender pages i can skip to main content")
    public void iNavigateToSelfServeLicenceSurrenderPagesICanSkipToMainContent() {
        world.surrenderJourney.navigateToSurrendersStartPage();
        world.UIJourney.skipToMainContentAndCheck();
        world.surrenderJourney.startSurrender();
        world.UIJourney.skipToMainContentAndCheck();
        world.UIJourney.clickSubmit();
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
        world.UIJourney.clickSubmit();
        waitForTextToBePresent("Securely destroy");
        world.UIJourney.skipToMainContentAndCheck();
        world.UIJourney.clickSubmit();
        waitForTitleToBePresent("Declaration");
    }
}
