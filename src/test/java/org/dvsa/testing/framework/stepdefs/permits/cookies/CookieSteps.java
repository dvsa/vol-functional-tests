package org.dvsa.testing.framework.stepdefs.permits.cookies;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CookiesPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

import static org.dvsa.testing.framework.pageObjects.external.pages.CookiesPage.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookieSteps extends BasePage implements En {

    public CookieSteps(World world) {

        WebDriver driver;
        And("^I logged into Self Serve site before accepting cookies$", () -> {
            world.globalMethods.navigateToLoginWithoutCookies(world.registerUser.getUserName(), world.registerUser.getEmailAddress(), ApplicationType.EXTERNAL, "no");
        });
        And("^I am on the permit type page before accepting cookies$", () -> {
            world.globalMethods.navigateToLoginWithoutCookies(world.registerUser.getUserName(), world.registerUser.getEmailAddress(), ApplicationType.EXTERNAL, "no");
            HomePageJourney.beginPermitApplication();
        });
        And("^I should see the cookies list$", () -> {
            Set<Cookie> cookies = Browser.navigate().manage().getCookies();
            Cookie cookiePHP = Browser.navigate().manage().getCookieNamed("PHPSESSID");
            Cookie secureToken = Browser.navigate().manage().getCookieNamed("secureToken");
            Cookie gid = Browser.navigate().manage().getCookieNamed("_gid");
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