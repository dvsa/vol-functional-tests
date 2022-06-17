package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import com.sun.istack.NotNull;
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

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication.accessibilityScanner;

public class SelfServeNavigation extends BasePage {

    public World world;
    private String url = URL.build(ApplicationType.EXTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();

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
        get(this.url.concat("search/"));
    }

    public void navigateToFindLorryAndBusOperatorsSearch()  {
        navigateToExternalSearch();
        clickByLinkText("Lorry and bus operators");
    }

    public void navigateToVehicleOperatorDecisionsAndApplications() {
        navigateToExternalSearch();
        clickByLinkText("Vehicle operator decisions and applications");
    }

    public void navigateToCheckerPage()  {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "are-you-ready/").toString();
        navigate().get(myURL);
    }

    public void navigateToLoginPage() {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env,"auth/login/").toString();
        navigate().get(myURL);
    }

    public void navigateToCreateAnAccount() {
        clickByLinkText("create an account");
    }

    public void navigateToPage(String type, SelfServeSection page)  {
        clickByLinkText("GOV.UK");
        waitForTextToBePresent("You must keep your records up to date");
        String applicationStatus;
        String overviewStatus;
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.applicationDetails.getLicenceNumber());
                waitForTitleToBePresent("View and amend your licence");
                break;
            case "application":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.createApplication.getApplicationId());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.createApplication.getApplicationId());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTitleToBePresent("Apply for a new licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTitleToBePresent("Application overview");
                }
                break;
            case "variation":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.updateLicence.getVariationApplicationId());
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

    public void navigateToNavBarPage(SelfServeNavBar page)  {
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
    public void clickSearchWhileCheckingTextPresent(@NotNull String text, @NotNull int seconds, @NotNull String exceptionMessage)  {
        long kickOut = System.currentTimeMillis() + Duration.ofSeconds(seconds).toMillis();;
        do {
            click("submit", SelectorType.ID);
            waitForPageLoad();
        } while (!isTextPresent(text) && System.currentTimeMillis() < kickOut);
        if (System.currentTimeMillis() > kickOut) {
            throw new TimeoutException(exceptionMessage);
        }
    }

    public void navigateThroughApplication()  {
        String workingDir = System.getProperty("user.dir");
        String financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";

        UIJourney.clickSaveAndContinue();
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
        UIJourney.clickSaveAndContinue();
        waitAndContinuePage("Transport Managers");
        waitAndContinuePage("Vehicle details");

        if (isTitlePresent("Vehicle declarations", 30)) {
            UIJourney.clickSaveAndContinue();
        }
        waitAndContinuePage("Safety and compliance");
        waitAndContinuePage("Financial history");
        waitAndContinuePage("Licence history");
        waitAndContinuePage("Convictions and Penalties");
    }

    private void waitAndContinuePage(String pageTitle) {
        waitForTitleToBePresent(pageTitle);
        UIJourney.clickSaveAndContinue();
    }

    public void getVariationFinancialEvidencePage() {
        get(this.url.concat(String.format("variation/%s/financial-evidence", world.updateLicence.getVariationApplicationId())));
    }
}