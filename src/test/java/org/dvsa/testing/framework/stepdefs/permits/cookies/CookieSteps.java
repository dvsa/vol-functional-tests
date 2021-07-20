package org.dvsa.testing.framework.stepdefs.permits.cookies;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CookiesPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

import static org.dvsa.testing.framework.pageObjects.external.pages.CookiesPage.*;
import static org.junit.Assert.assertTrue;


public class CookieSteps extends BasePage implements En {

    public CookieSteps(World world, OperatorStore operatorStore) {

        WebDriver driver;
        And("^I logged into Self Serve site before accepting cookies$", () -> {
            world.globalMethods.navigateToLoginWithoutCookies(world.registerUser.getUserName(), world.registerUser.getEmailAddress(), ApplicationType.EXTERNAL);
        });
        And("^I am on the permit type page before accepting cookies$", () -> {
            world.globalMethods.navigateToLoginWithoutCookies(world.registerUser.getUserName(), world.registerUser.getEmailAddress(), ApplicationType.EXTERNAL);
            HomePageJourney.beginPermitApplication();
        });
        And("^I should see the cookies list$", () -> {
            Set<Cookie> cookies = getDriver().manage().getCookies();
            Cookie cookiePHP = getDriver().manage().getCookieNamed("PHPSESSID");
            Cookie secureToken = getDriver().manage().getCookieNamed("secureToken");
            Cookie gid = getDriver().manage().getCookieNamed("_gid");
            System.out.println("All Available cookie count is  :  " + cookies.size());
            System.out.println("All Available cookies are  :  " + cookies);
            System.out.println("PHP cookie is  :  " + cookiePHP);
            System.out.println("secure Token cookie is  :  " + secureToken);
            System.out.println("_gid cookie is  :  " + gid);
        });

        And("^I accept all cookies from banner$", () -> {
            cookiesBannerIsPresent();
            selectAllCookies();
        });

        And("^I should see no banner in the page$", () -> {
            assertTrue(CookiesPage.cookiesBannerIsNotPresent());
        });

        And("^I should see banner in the page$", CookiesPage::cookiesBannerIsPresent);

        And("^I navigate to cookies details Page$", () -> {
            goToCookiesDetailsPage();
            cookiesBannerIsNotPresent();
            cookiePageTextPresent();
        });

        And("^I navigate to cookies details Page from collect info link$", () -> {
            goToCookiesDetailsPageFromLink();
            cookiesBannerIsNotPresent();
            cookieSaveTextPresent();
        });

    }
}
