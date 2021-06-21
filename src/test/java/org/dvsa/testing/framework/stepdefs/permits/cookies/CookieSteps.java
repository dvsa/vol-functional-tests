package org.dvsa.testing.framework.stepdefs.permits.cookies;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.Driver.DriverUtils;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.CookiesPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage;
import org.junit.Assert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.signIn;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.external.CookiesPage.*;


public class CookieSteps extends DriverUtils implements En {

    public CookieSteps(World world, OperatorStore operatorStore) {

        WebDriver driver;
        And("^I logged into Self Serve site before accepting cookies$", () -> {
            signIn(world);
        });
        And("^I am on the permit type page before accepting cookies$", () -> {
            signIn(world);
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
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

        And("^I should see no banner in the page$", CookiesPage::cookiesBannerIsNotPresent);

        And("^I should see banner in the page$", CookiesPage::cookiesBannerIsPresent);

        And("^I navigate to cookies details Page$", () -> {
            goToCookiesDetailsPage();
            cookiesBannerIsNotPresent();
            cookiePageTextPresent();
        });

        And("^I Opt out google analytics cookies$", () -> {
            optOutAnalyticsCookies();
            cookiesBannerIsNotPresent();
            cookiePageTextPresent();
        });

        And("^I Opt in google analytics cookies$", () -> {
            optInAnalyticsCookies();
            optInAnalyticsCookies();
            cookiesBannerIsNotPresent();
            cookiePageTextPresent();
        });

        And("^I Opt out choice cookies$", () -> {
            optOutChoiceCookies();
            cookiesBannerIsNotPresent();
            cookiePageTextPresent();
        });

        And("^I Opt In choice cookies$", () -> {
            optInChoiceCookies();
            cookiesBannerIsNotPresent();
            cookiePageTextPresent();
        });

        And("^I navigate to cookies details Page from collect info link$", () -> {
            goToCookiesDetailsPageFromLink();
            cookiesBannerIsNotPresent();
            cookieSaveTextPresent();
        });

        And("^I Should see google analytics cookies$", () -> {

            String gatCookie = getDriver().manage().getCookieNamed("_gat").getValue().trim();
            System.out.println(gatCookie);
            Assert.assertEquals(gatCookie, "1");
            String gidCookie = getDriver().manage().getCookieNamed("_gid").getValue().substring(0, 2);
            System.out.println(gidCookie);
            Assert.assertEquals(gidCookie, "GA");
            And("^I Should see one google analytics cookies$", () -> {
                String gaCookie = getDriver().manage().getCookieNamed("_ga").getValue().substring(0, 2);
                System.out.println(gaCookie);
                Assert.assertEquals(gaCookie, "GA");
            });

            And("^I Should see All google analytics cookies$", () -> {
                String gaCookie = getDriver().manage().getCookieNamed("_ga").getValue().substring(0, 2);
                System.out.println(gaCookie);
                Assert.assertEquals(gaCookie, "GA");
                String gatUA1 = getDriver().manage().getCookieNamed("_gat_UA-145652997-1").getValue().trim();
                System.out.println(gatUA1);
                Assert.assertEquals(gatUA1, "1");
                String gatUA2 = getDriver().manage().getCookieNamed("_gat_UA-56588861-6").getValue().trim();
                System.out.println(gatUA2);
                Assert.assertEquals(gatUA2, "1");
            });

            And("^I Should not see google analytics cookies$", () -> {
                goToCookiesDetailsPageFromLink();
                cookiesBannerIsNotPresent();
                cookieSaveTextPresent();
            });

            And("^I Should see settings cookies$", () -> {
           /* String langPrefCookie = Browser.getDriver().manage().getCookieNamed("langPref").getValue().trim();
            System.out.println(langPrefCookie);
            Assert.assertEquals(langPrefCookie,"en-GB");*/
                Assert.assertTrue(getElementValueByText("//a[contains(text(),'Set cookie preferences')]", SelectorType.XPATH), true);
            });

            And("^I Should not see settings cookies$", () -> {
                Set<Cookie> cookies = getDriver().manage().getCookies();
            });


            And("^I delete all cookies$", () -> {
                getDriver().manage().deleteAllCookies();
            });

            And("^I delete token cookies$", () -> {
                getDriver().manage().getCookieNamed("secureToken");
            });

            And("^I should be on login page$", () -> {
                isPath("/auth/login/");
            });

            And("^I should see the same cookies list in Permit Page$", () -> {
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

            And("^I should see the same cookies list in ECMT Removal Check Answers Page$", () -> {
                HomePage.selectTab(Tab.PERMITS);
                HomePage.applyForLicenceButton();
                EcmtInternationalRemovalJourney.getInstance()
                        .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                        .licencePage(operatorStore, world);
                EcmtInternationalRemovalJourney.getInstance()
                        .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                        .removalsEligibility(true)
                        .cabotagePage()
                        .certificatesRequiredPage()
                        .permitStartDatePage()
                        .numberOfPermits();
                Set<Cookie> cookies = getDriver().manage().getCookies();
                Cookie cookiePHP = getDriver().manage().getCookieNamed("PHPSESSID");
                String secureToken = getDriver().manage().getCookieNamed("secureToken").getValue();
                Cookie gid = getDriver().manage().getCookieNamed("_gid");
                System.out.println("All Available cookie count is  :  " + cookies.size());
                System.out.println("All Available cookies are  :  " + cookies);
                System.out.println("PHP cookie is  :  " + cookiePHP);
                System.out.println("secure Token cookie is  :  " + secureToken);
                System.out.println("_gid cookie is  :  " + gid);
            });


            And("^I should see the same cookies list in ECMT Removal valid Page$", () -> {
                EcmtInternationalRemovalJourney.getInstance()
                        .checkYourAnswers()
                        .declaration();
                EcmtApplicationJourney.getInstance()
                        .feeOverviewPage()
                        .cardDetailsPage()
                        .cardHolderDetailsPage()
                        .confirmAndPay();
                Set<Cookie> cookies = getDriver().manage().getCookies();
                Cookie cookiePHP = getDriver().manage().getCookieNamed("PHPSESSID");
                String secureToken = getDriver().manage().getCookieNamed("secureToken").getValue();
                Cookie gid = getDriver().manage().getCookieNamed("_gid");
                System.out.println("All Available cookie count is  :  " + cookies.size());
                System.out.println("All Available cookies are  :  " + cookies);
                System.out.println("PHP cookie is  :  " + cookiePHP);
                System.out.println("secure Token cookie is  :  " + secureToken);
                System.out.println("_gid cookie is  :  " + gid);
            });

        });
    }
}
