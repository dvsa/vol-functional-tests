package org.dvsa.testing.framework.Journeys.licence;

import activesupport.aws.s3.SecretsManager;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import com.sun.istack.NotNull;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeNavBar;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.webAppURL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import java.net.URL;
import java.time.Duration;
import java.util.Set;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.xpath;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelfServeNavigation extends BasePage {

    private final World world;
    private final String url = webAppURL.build(ApplicationType.EXTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();

    public SelfServeNavigation(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) {
        world.globalMethods.navigateToLoginWithoutCookies(username, emailAddress, ApplicationType.EXTERNAL);
    }

    public void navigateToExternalSearch() {
        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        }
        get(url.concat("search/"));
    }

    public void navigateToFindLorryAndBusOperatorsSearch() {
        navigateToExternalSearch();
        waitAndClickByLinkText("Lorry and bus operators");
    }

    public void navigateToVehicleOperatorDecisionsAndApplications() {
        navigateToExternalSearch();
        waitAndClickByLinkText("Vehicle operator decisions and applications");
    }

    public void navigateToCheckerPage() {
        var myURL = webAppURL.build(ApplicationType.EXTERNAL, world.configuration.env, "are-you-ready/").toString();
        navigate().get(myURL);
    }

    public void navigateToLoginPage() {
        var myURL = webAppURL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login/").toString();
        navigate().get(myURL);
    }

    public void navigateToCreateAnAccount() {
        if (isLinkPresent("Sign out", 3)) {
            waitAndClickByLinkText("Sign out");
        }
        getDriver().manage().deleteAllCookies();
        refreshPage();
        waitForTitleToBePresent("Sign in to your Vehicle Operator Licensing account");
        waitAndClickByLinkText("create an account");
    }

    public void navigateToPage(String type, SelfServeSection page) {
        refreshPage();
        String applicationStatus;
        String overviewStatus;
        switch (type.toLowerCase()) {
            case "licence" -> {
                if (world.configuration.env.toString().equals("int")) {
                    waitAndClickByLinkText(existingLicenceNumber);
                } else {
                    waitAndClickByLinkText(world.applicationDetails.getLicenceNumber());
                }
                waitForTitleToBePresent("View and amend your licence");
            }
            case "application" -> {
                overviewStatus = String.format("//table//tbody[tr//*[contains(text(),'%s')]]//strong[contains(@class,'govuk-tag')]", world.createApplication.getApplicationId());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                waitAndClickByLinkText(world.createApplication.getApplicationId());
                switch (applicationStatus) {
                    case "NOT YET SUBMITTED" -> waitForTitleToBePresent("Apply for a new licence");
                    case "UNDER CONSIDERATION" -> waitForTitleToBePresent("Application overview");
                }
            }
            case "variation" -> {
                UniversalActions.clickHome();
                overviewStatus = String.format("//table//tbody[tr//*[contains(text(),'%s')]]//strong[contains(@class,'govuk-tag')]", world.updateLicence.getVariationApplicationId());
                applicationStatus = waitAndGetText(overviewStatus, SelectorType.XPATH);
                waitAndClickByLinkText(world.updateLicence.getVariationApplicationId());
                switch (applicationStatus) {
                    case "NOT YET SUBMITTED" -> waitForTitleToBePresent("Apply to change a licence");
                    case "UNDER CONSIDERATION" -> waitForTitleToBePresent("Application overview");
                }
            }
        }
        switch (page.toString()) {
            case "View" -> {
            }
            case "Vehicles" -> waitAndClickByLinkText("Vehicles");
            //Once DVLA integration has been switched on, this needs updating
//                waitForTitleToBePresent("Vehicle details");
            case "Convictions and penalties" -> {
                waitAndClickByLinkText("Convictions and penalties");
                waitForTitleToBePresent("Convictions and Penalties");
            }
            default -> {
                waitAndClickByLinkText(page.toString());
                waitForTitleToBePresent(page.toString());
            }
        }
    }

    public void navigateToNavBarPage(SelfServeNavBar page) {
        switch (page.toString()) {
            case "Home" -> {
                UniversalActions.clickHome();
                waitForTextToBePresent("Licences");
            }
            case "Sign out" -> {
                waitAndClickByLinkText("Sign out");
                waitForTextToBePresent("Thank you");
            }
            default -> {
                waitAndClickByLinkText(page.toString());
                waitForTitleToBePresent(page.toString());
            }
        }
    }

    /***
     @exceptionMessage an example of this should be: "KickOut reached. Operator name external search failed."
     This method is used for the self service search when trying to search for 'address', 'business', 'licence', or 'person'.
     */
    public void clickSearchWhileCheckingTextPresent(@NotNull String text, @NotNull int seconds, @NotNull String exceptionMessage) {
        long kickOut = System.currentTimeMillis() + Duration.ofSeconds(seconds).toMillis();
        ;
        do {
            click("submit", SelectorType.ID);
            waitForPageLoad();
        } while (!isTextPresent(text) && System.currentTimeMillis() < kickOut);
        if (System.currentTimeMillis() > kickOut) {
            throw new TimeoutException(exceptionMessage);
        }
    }

    public void enterAndSearchUntilTextIsPresent(@NotNull String selector, @NotNull SelectorType selectorType, @NotNull String searchString) {
        findElement(selector, selectorType).sendKeys(searchString);
        long kickOut = System.currentTimeMillis() + 5000;
        do {
            waitAndClick("submit", SelectorType.ID);
        } while (!isTextPresent(searchString) && System.currentTimeMillis() < kickOut);
        if (System.currentTimeMillis() > kickOut) {
            throw new TimeoutException("Text not found within " + kickOut + " ms");
        }
    }

    public void navigateThroughApplication() {
        var workingDir = System.getProperty("user.dir");
        var financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";

        UniversalActions.clickSaveAndContinue();
        waitAndContinuePage("Business type");
        waitAndContinuePage("Business details");
        waitAndContinuePage("Addresses");
        waitAndContinuePage("Directors");
        waitAndContinuePage("Operating centres and authorisation");
        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Upload documents now')]", SelectorType.XPATH);

        String jScript = "document.getElementById('evidence[files][file]').style.left = 0";
        javaScriptExecutor(jScript);

        if (System.getProperty("platform") == null) {
            waitAndEnterText("//*[@id='evidence[files][file]']", SelectorType.XPATH, workingDir.concat(financialEvidenceFile));
        } else {
            WebElement addFile = getDriver().findElement(By.xpath("//*[@id='evidence[files][file]']"));
            ((RemoteWebElement) addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(workingDir.concat(financialEvidenceFile));
        }
        waitForTextToBePresent("File name");
        UniversalActions.clickSaveAndContinue();
        waitAndContinuePage("Transport Managers");
        waitAndContinuePage("Vehicle details");
        if (isTitlePresent("Vehicles size", 30)) {
            world.psvJourney.selectVehicleSize("nine_and_above");
            world.psvJourney.completeVehiclesWith9SeatsOrMorePage();
            world.psvJourney.completeLimousinesVehicles("Yes");
            waitAndClick("overview-item__safety", SelectorType.ID);
        }
        waitAndContinuePage("Safety and compliance");
        waitAndContinuePage("Financial history");
        waitAndContinuePage("Licence history");
        waitAndContinuePage("Convictions and Penalties");
    }

    private void waitAndContinuePage(String pageTitle) {
        waitForTitleToBePresent(pageTitle);
        UniversalActions.clickSaveAndContinue();
    }

    public void getVariationFinancialEvidencePage() {
        get(this.url.concat(String.format("variation/%s/financial-evidence", world.updateLicence.getVariationApplicationId())));
    }

    public void navigateToBusRegExternal() {
        java.net.URL url = getURL();
        String[] urlParts = url.getPath().split("/");
        String id = urlParts[urlParts.length - 3];
        String myURL = webAppURL.build(ApplicationType.EXTERNAL, world.configuration.env, "search/find-registered-local-bus-services/details/" + id).toString();
        navigate().get(myURL);
    }

    public void loginIntoExternal(String userName) {
        navigateToLoginPage();
        String user;
        String password;
        if (!isTextPresent("Current licences")) {
            switch (userName) {
                case "prepUser" -> user = SecretsManager.getSecretValue("prepUser");
                case "prepUser2" -> user = SecretsManager.getSecretValue("prepUser2");
                case "prodUser" -> user = SecretsManager.getSecretValue("prodUser");
                default -> user = userName;
            }
            if (user != null && (user.equals(userName))) {
                password = SecretsManager.getSecretValue("internalNewPassword");
            } else {
                password = SecretsManager.getSecretValue("prepEnvPassword");
            }
            world.globalMethods.signIn(user, password);
        }
    }

    public void navigateToOperatorReports() {
        refreshPage();
        String originalWindowHandle = getDriver().getWindowHandle();

        waitAndClick("//a[@href=\"/dashboard/topsreport\" and contains(@class, \"govuk-link\") and text()=\"Your DVSA Operator Reports\"]", SelectorType.XPATH);

        Set<String> windowHandles = getDriver().getWindowHandles();
        String secondTabHandle = windowHandles.toArray(new String[0])[1];
        getDriver().switchTo().window(secondTabHandle);

        String currentUrl = waitForRedirectToOperatorReports();
        System.out.println("URL after redirect: " + currentUrl.replaceAll("id_token=[^&]*", "id_token=***"));

        String userName = SecretsManager.getSecretValue("topsUsername");
        String passWord = SecretsManager.getSecretValue("topsPassword");

        if (currentUrl.contains("operator-reports")) {
            String modifiedUrl = currentUrl.replaceFirst("https://", "https://" + userName + ":" + passWord + "@");
            System.out.println("Navigating to authenticated URL...");

            Browser.navigate().get(modifiedUrl);

            String finalUrl = safeGetCurrentUrl();
            System.out.println("Final URL: " + finalUrl.replaceAll(":[^:/@]+@", ":***@").replaceAll("id_token=[^&]*", "id_token=***"));
        } else {
            System.out.println("Redirect to operator-reports never happened: " + currentUrl);
        }

        getDriver().switchTo().window(originalWindowHandle);
    }

    private String waitForRedirectToOperatorReports() {
        for (int i = 0; i < 60; i++) {
            try {
                Thread.sleep(1000);
                String url = safeGetCurrentUrl();

                if (i % 5 == 0) {
                    System.out.println("Waiting for redirect (attempt " + i + "): " + url.replaceAll("id_token=[^&]*", "id_token=***"));
                }

                if (url.contains("operator-reports")) {
                    System.out.println("Redirect completed after " + i + " seconds!");
                    return url;
                }

                if (url.contains("topsreport")) {
                    if (i % 10 == 0) {
                        System.out.println("Still on topsreport page, waiting for token service...");
                    }
                    continue;
                }

                if (url.equals("about:blank")) {
                    continue;
                }

            } catch (Exception e) {
                System.out.println("Error during redirect wait: " + e.getMessage());
            }
        }

        String finalUrl = safeGetCurrentUrl();
        System.out.println("Redirect timeout - final URL: " + finalUrl);
        return finalUrl;
    }

    private String safeGetCurrentUrl() {
        try {
            return getDriver().getCurrentUrl();
        } catch (UnhandledAlertException e) {
            try {
                Thread.sleep(2000);
                return getDriver().getCurrentUrl();
            } catch (Exception e2) {
                return "about:blank";
            }
        }
    }
}