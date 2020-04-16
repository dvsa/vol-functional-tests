package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.net.MalformedURLException;

import static activesupport.driver.Browser.navigate;

public class InternalNavigationalJourneySteps extends BasePage {

    private World world;

    public InternalNavigationalJourneySteps(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) throws MalformedURLException, IllegalBrowserException {
        world.globalMethods.navigateToLogin(username, emailAddress, ApplicationType.INTERNAL);
    }

    public void logInAndNavigateToDocsTable() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        urlSearchAndViewApplication();
        clickByLinkText("Docs");
    } // refactor to use global navigate to task method or something on the end after the login steps.

    public void logInAndNavigateToTask() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        urlSearchAndViewApplication();
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
        isElementEnabled("//body", SelectorType.XPATH);
    } // refactor to use global navigate to task method or something on the end after the login steps.

    public void urlSearchAndViewApplication() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("application/%s", world.createLicence.getApplicationNumber())));
    }

    public void urlSearchAndViewLicence() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("licence/%s", world.createLicence.getLicenceId())));
    }

    public void urlSearchAndViewVariational() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("variation/%s", world.updateLicence.getVariationApplicationNumber())));
    }

    public void urlSearchAndViewEditFee(String feeNumber) throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("admin/payment-processing/fees/edit-fee/%s", feeNumber)));
    }

    public void urlSearchAndViewInternalUserAccount(String adminUserId) throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("admin/user-management/users/edit/%s", adminUserId)));
    }
}
