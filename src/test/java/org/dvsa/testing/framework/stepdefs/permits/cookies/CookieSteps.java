package org.dvsa.testing.framework.stepdefs.permits.cookies;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CookiesPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

import static org.dvsa.testing.framework.pageObjects.external.pages.CookiesPage.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CookieSteps extends BasePage {
    private final World world;
    public CookieSteps(World world) {
        this.world = world;
    }

    @And("I logged into Self Serve site before accepting cookies")
    public void iLoggedIntoSelfServeSiteBeforeAcceptingCookies() {
        world.globalMethods.navigateToLoginWithoutCookies(world.registerUser.getUserName(), world.registerUser.getEmailAddress(), ApplicationType.EXTERNAL);
    }
    @And("I am on the permit type page before accepting cookies")
    public void iAmOnThePermitTypePageBeforeAcceptingCookies() {
        world.globalMethods.navigateToLoginWithoutCookies(world.registerUser.getUserName(), world.registerUser.getEmailAddress(), ApplicationType.EXTERNAL);
        HomePageJourney.beginPermitApplication();
    }
    @And("I should see the cookies list")
    public void iShouldSeeTheCookiesList() {
        Set<Cookie> cookies = getDriver().manage().getCookies();
        Cookie cookiePHP = getDriver().manage().getCookieNamed("PHPSESSID");
        Cookie secureToken = getDriver().manage().getCookieNamed("secureToken");
        Cookie gid = getDriver().manage().getCookieNamed("_gid");
        System.out.println("All Available cookie count is  :  " + cookies.size());
        System.out.println("All Available cookies are  :  " + cookies);
        System.out.println("PHP cookie is  :  " + cookiePHP);
        System.out.println("secure Token cookie is  :  " + secureToken);
        System.out.println("_gid cookie is  :  " + gid);
    }
    @And("I accept all cookies from banner")
    public void iAcceptAllCookiesFromBanner() {
        cookiesBannerIsPresent();
        selectAllCookies();
    }
    @And("I should see no banner in the page")
    public void iShouldSeeNoBannerInThePage() {
        assertTrue(CookiesPage.cookiesBannerIsNotPresent());
    }

    @And("I should see banner in the page")
    public void iShouldSeeBannerInThePage() {
        CookiesPage.cookiesBannerIsPresent();
    }

    @And("I navigate to cookies details Page")
    public void iNavigateToCookiesDetailsPage() {
        goToCookiesDetailsPage();
        cookiesBannerIsNotPresent();
        cookiePageTextPresent();
    }
    @And("I navigate to cookies details Page from collect info link")
    public void iNavigateToCookiesDetailsPageFromCollectInfoLink() {
        goToCookiesDetailsPageFromLink();
        cookiesBannerIsNotPresent();
        cookieSaveTextPresent();
    }
}