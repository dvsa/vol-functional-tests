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
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import java.time.Duration;


import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;

public class SelfServeNavigation extends BasePage {

    private final World world;
    private final String url = URL.build(ApplicationType.EXTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();

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
        clickByLinkText("Lorry and bus operators");
    }

    public void navigateToVehicleOperatorDecisionsAndApplications() {
        navigateToExternalSearch();
        clickByLinkText("Vehicle operator decisions and applications");
    }

    public void navigateToCheckerPage() {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "are-you-ready/").toString();
        navigate().get(myURL);
    }

    public void navigateToLoginPage() {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login/").toString();
        navigate().get(myURL);
    }

    public void navigateToCreateAnAccount() {
        if (isLinkPresent("Sign out", 3)) {
            clickByLinkText("Sign out");
        }
        getDriver().manage().deleteAllCookies();
        refreshPage();
        waitForTitleToBePresent("Sign in to your Vehicle Operator Licensing account");
        clickByLinkText("create an account");
    }

    public void navigateToPage(String type, SelfServeSection page) {
        refreshPage();
        String applicationStatus;
        String overviewStatus;
        switch (type.toLowerCase()) {
            case "licence":
                if (world.configuration.env.toString().equals("int")) {
                    clickByLinkText(existingLicenceNumber);
                } else {
                    clickByLinkText(world.applicationDetails.getLicenceNumber());
                }
                waitForTitleToBePresent("View and amend your licence");
                break;
            case "application":
                overviewStatus = String.format("//table//tbody[tr//*[contains(text(),'%s')]]//strong[contains(@class,'govuk-tag')]", world.createApplication.getApplicationId());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.createApplication.getApplicationId());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTitleToBePresent("Apply for a new licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTitleToBePresent("Application overview");
                }
                break;
            case "variation":
                clickByLinkText("Home");
                overviewStatus = String.format("//table//tbody[tr//*[contains(text(),'%s')]]//strong[contains(@class,'govuk-tag')]", world.updateLicence.getVariationApplicationId());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.updateLicence.getVariationApplicationId());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTitleToBePresent("Apply to change a licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTitleToBePresent("Application overview");
                }
                break;
        }
        switch (page.toString()) {
            case "View":
                break;
            case "Vehicles":
                clickByLinkText("Vehicles");
                //Once DVLA integration has been switched on, this needs updating
//                waitForTitleToBePresent("Vehicle details");
                break;
            case "Convictions and penalties":
                clickByLinkText("Convictions and penalties");
                waitForTitleToBePresent("Convictions and Penalties");
                break;
            default:
                clickByLinkText(page.toString());
                waitForTitleToBePresent(page.toString());
                break;
        }
    }

    public void navigateToNavBarPage(SelfServeNavBar page) {
        switch (page.toString()) {
            case "Home":
                clickByLinkText("Home");
                waitForTextToBePresent("Licences");
                break;
            case "Sign out":
                clickByLinkText("Sign out");
                waitForTextToBePresent("Thank you");
                break;
            default:
                clickByLinkText(page.toString());
                waitForTitleToBePresent(page.toString());
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
        String workingDir = System.getProperty("user.dir");
        String financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";

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
            enterText("//*[@id='evidence[files][file]']", SelectorType.XPATH, workingDir.concat(financialEvidenceFile));
        } else {
            WebElement addFile = getDriver().findElement(By.xpath("//*[@id='evidence[files][file]']"));
            ((RemoteWebElement) addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(workingDir.concat(financialEvidenceFile));
        }
        waitForTextToBePresent("File name");
        UniversalActions.clickSaveAndContinue();
        waitAndContinuePage("Transport Managers");
        waitAndContinuePage("Vehicle details");

        if (isTitlePresent("Vehicle declarations", 30)) {
            UniversalActions.clickSaveAndContinue();
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
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "search/find-registered-local-bus-services/details/" + id).toString();
        navigate().get(myURL);
    }

    public void loginIntoExternal(String userName) {
        navigateToLoginPage();
        if (!isTextPresent("Current licences")) {
            switch (userName) {
                case "prepUser":
                    world.globalMethods.signIn(SecretsManager.getSecretValue("prepUser"),
                            SecretsManager.getSecretValue("intEnvPassword"));
                    break;
                case "prepUser2":
                    world.globalMethods.signIn(SecretsManager.getSecretValue("prepUser2"),
                            SecretsManager.getSecretValue("intEnvPassword"));
                    break;
                case "prodUser":
                    world.globalMethods.signIn(SecretsManager.getSecretValue("prodUser"),
                            SecretsManager.getSecretValue("intEnvPassword"));
                default:
                    world.globalMethods.signIn(userName, SecretsManager.getSecretValue("internalNewPassword"));
            }
        }
    }
}