package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import com.sun.istack.NotNull;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

import static activesupport.driver.Browser.navigate;

public class SelfServeNavigationalJourneySteps extends BasePage {

    public World world;

    public SelfServeNavigationalJourneySteps(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) {
        world.globalMethods.navigateToLogin(username, emailAddress, ApplicationType.EXTERNAL);
    }

    public void navigateToSearch()  {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "search/find-lorry-bus-operators/").toString();
        navigate().get(myURL);
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
                waitForTitleToBePresent("Vehicle details");
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
            conditionNotTrue = !isTextPresent(text, 10);
            click("submit", SelectorType.ID);
            waitForPageLoad();
            if (System.currentTimeMillis() > kickOut) {
                throw new TimeoutException(exceptionMessage);
            }
        }
    }

    public void navigateThroughApplication()  {
        waitForTitleToBePresent("Apply for a new licence");
        clickByLinkText("Type of licence");
        waitForTitleToBePresent("Type of licence");
        String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Business type");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Business details");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Addresses");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Directors");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Operating centres and authorisation");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Financial evidence");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Transport Managers");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Vehicle details");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        if (isTitlePresent("Vehicle declarations", 30)) {
            waitAndClick(saveAndContinue, SelectorType.XPATH);
        }
        waitForTitleToBePresent("Safety and compliance");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Financial history");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Licence history");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Convictions and Penalties");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
    }
}
