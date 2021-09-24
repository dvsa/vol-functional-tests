package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.system.Properties;
import com.sun.istack.NotNull;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

public class SelfServeNavigational extends BasePage {

    public World world;
    private String url = URL.build(ApplicationType.EXTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();
    public String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";

    public SelfServeNavigational(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) {
        world.globalMethods.navigateToLogin(username, emailAddress, ApplicationType.EXTERNAL);
    }

    public void navigateToSearch()  {
        get(this.url.concat("search/find-lorry-bus-operators/"));
    }

    public void navigateToPage(String type, String page)  {
        clickByLinkText("GOV.UK");
        waitForTextToBePresent("You must keep your records up to date");
        String applicationStatus = null;
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
        switch (page) {
            case "View":
                break;
            case "Vehicles":
                clickByLinkText("Vehicles");
                waitForTitleToBePresent("Manage your vehicles");
                break;
            case "Convictions and penalties":
                clickByLinkText("Convictions and penalties");
                waitForTitleToBePresent("Convictions and Penalties");
                break;
            default:
                clickByLinkText(page);
                waitForTitleToBePresent(page);
                break;
        }
    }

    public void navigateToNavBarPage(String page)  {
        switch (page.toLowerCase()) {
            case "home":
                clickByLinkText("Home");
                waitForTextToBePresent("You must keep your records up to date");
                break;
            case "manage users":
                clickByLinkText("Manage users");
                waitForTextToBePresent("Permission");
                break;
            case "your account":
                clickByLinkText("Your account");
                waitForTextToBePresent("Username");
                break;
            case "sign out":
                clickByLinkText("Sign out");
                waitForTextToBePresent("Thank you");
                break;
        }
    }
/***
    @exceptionMessage an example of this should be: "KickOut reached. Operator name external search failed."
    This method is used for the self service search when trying to search for 'address', 'business', 'licence', or 'person'.
 */
    public void clickSearchWhileCheckingTextPresent(@NotNull String text, @NotNull int seconds, @NotNull String exceptionMessage)  {
        boolean conditionNotTrue = true;
        long kickOut = System.currentTimeMillis() + Duration.ofSeconds(seconds).toMillis();
        while (conditionNotTrue) {
            conditionNotTrue = !isTextPresent(text);
            click("submit", SelectorType.ID);
            waitForPageLoad();
            if (System.currentTimeMillis() > kickOut) {
                throw new TimeoutException(exceptionMessage);
            }
        }
    }

    public void navigateThroughApplication()  {
        String workingDir = System.getProperty("user.dir");
        String financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";

        waitAndClick("//*[@id='form-actions[saveAndContinue]']", SelectorType.XPATH);
        waitAndContinuePage("Business type");
        waitAndContinuePage("Business details");
        waitAndContinuePage("Addresses");
        waitAndContinuePage("Directors");
        waitAndContinuePage("Operating centres and authorisation");
        waitAndContinuePage("Financial evidence");
        waitAndClick("//*[contains(text(),'Upload documents now')]",SelectorType.XPATH);
        uploadFile("//*[@id='evidence[files][file]']", workingDir + financialEvidenceFile, "document.getElementById('evidence[files][file]').style.left = 0", SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitAndContinuePage("Transport Managers");
        waitAndContinuePage("Vehicle details");

        if (isTitlePresent("Vehicle declarations", 30)) {
            waitAndClick(saveAndContinue, SelectorType.XPATH);
        }
        waitAndContinuePage("Safety and compliance");
        waitAndContinuePage("Financial history");
        waitAndContinuePage("Licence history");
        waitAndContinuePage("Convictions and Penalties");
    }

    private void waitAndContinuePage(String pageTitle) {
        waitForTitleToBePresent(pageTitle);
        waitAndClick(saveAndContinue, SelectorType.XPATH);
    }

    public void getVariationFinancialEvidencePage() {
        get(this.url.concat(String.format("variation/%s/financial-evidence", world.updateLicence.getVariationApplicationId())));
    }
}
