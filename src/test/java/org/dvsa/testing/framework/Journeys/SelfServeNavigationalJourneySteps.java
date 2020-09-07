package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import com.sun.istack.NotNull;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.TimeoutException;

import java.net.MalformedURLException;
import java.time.Duration;

import static activesupport.driver.Browser.navigate;

public class SelfServeNavigationalJourneySteps extends BasePage {

    public World world;

    public SelfServeNavigationalJourneySteps(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) throws MalformedURLException, IllegalBrowserException {
        world.globalMethods.navigateToLogin(username, emailAddress, ApplicationType.EXTERNAL);
    }

    public void navigateToSearch() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "search/find-lorry-bus-operators/").toString();
        navigate().get(myURL);
    }

    public void navigateToPage(String type, String page) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        waitForTextToBePresent("You must keep your records up to date");
        String applicationStatus = null;
        String overviewStatus;
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                waitForTitleToBePresent("View and amend your licence");
                break;
            case "application":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.createLicence.getApplicationNumber());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.createLicence.getApplicationNumber());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply for a new licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTitleToBePresent("Application overview");
                }
                break;
            case "variation":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.updateLicence.getVariationApplicationNumber());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply to change a licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTitleToBePresent("Application overview");
                }
                break;
        }
        switch (page) {
            case "View":
                switch (type.toLowerCase()) {
                    case "licence":
                        waitForTitleToBePresent("View and amend your licence");
                        break;
                    case "application":
                        if (applicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTitleToBePresent("Apply for a new licence");
                        } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What you need to do next");
                        }
                        break;
                    case "variation":
                        if (applicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTitleToBePresent("Apply to change a licence");
                        } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What happens next?");
                        }
                        break;
                }
                break;
            default:
                clickByLinkText(page);
                waitForTitleToBePresent(page);
                break;
        }
    }

    public void navigateToNavBarPage(String page) throws IllegalBrowserException, MalformedURLException {
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
    public void clickSearchWhileCheckingTextPresent(@NotNull String text, @NotNull int seconds, @NotNull String exceptionMessage) throws IllegalBrowserException, MalformedURLException {
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
}
