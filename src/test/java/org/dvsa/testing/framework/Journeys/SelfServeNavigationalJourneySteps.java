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
        String variationApplicationStatus = null;
        String overviewStatus;
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                waitForTextToBePresent("View and amend your licence                ");
                break;
            case "application":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.createLicence.getApplicationNumber());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.createLicence.getApplicationNumber());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply for a new licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTextToBePresent("Application overview                ");
                }
                break;
            case "variation":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.updateLicence.getVariationApplicationNumber());
                variationApplicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                if (variationApplicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply to change a licence");
                } else if (variationApplicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTextToBePresent("Application overview                ");
                }
                break;
        }
        switch (page.toLowerCase()) {
            case "view":
                switch (type.toLowerCase()) {
                    case "licence":
                        waitForTextToBePresent("View and amend your licence                ");
                        break;
                    case "application":
                        if (applicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTextToBePresent("Apply for a new licence                ");
                        } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What you need to do next");
                        }
                        break;
                    case "variation":
                        if (variationApplicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTextToBePresent("Apply to change a licence                ");
                        } else if (variationApplicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What happens next?");
                        }
                        break;
                }
                break;
            case "type of licence":
                clickByLinkText("Type of licence");
                waitForTextToBePresent("Operator location                ");
                break;
            case "business type":
                clickByLinkText("Business type");
                waitForTextToBePresent("Business type                ");
                break;
            case "business details":
                clickByLinkText("Business details");
                waitForTextToBePresent("Business details                ");
                break;
            case "address":
                clickByLinkText("Address");
                waitForTextToBePresent("Address                ");
                break;
            case "addresses":
                clickByLinkText("Addresses");
                waitForTextToBePresent("Addresses                ");
                break;
            case "directors":
                clickByLinkText("Directors");
                waitForTextToBePresent("Directors                ");
                break;
            case "operating centres":
                clickByLinkText("Operating centres and authorisation");
                waitForTextToBePresent("Operating centres and authorisation                ");
                break;
            case "transport managers":
                clickByLinkText("Transport Managers");
                waitForTextToBePresent("Transport Managers                ");
                break;
            case "vehicles":
                clickByLinkText("Vehicles");
                waitForTextToBePresent("Vehicle details                ");
                break;
            case "vehicle declarations":
                clickByLinkText("Vehicle declarations");
                waitForTextToBePresent("Vehicle declarations                ");
                break;
            case "trailers":
                clickByLinkText("Trailers");
                waitForTextToBePresent("Trailers                ");
                break;
            case "licence discs":
                clickByLinkText("Licence discs");
                waitForTextToBePresent("Licence discs                ");
                break;
            case "safety and compliance":
                clickByLinkText("Safety and compliance");
                waitForTextToBePresent("Safety and compliance                ");
                break;
            case "conditions and undertakings":
                clickByLinkText("Conditions and undertakings");
                waitForTextToBePresent("Conditions and undertakings                ");
                break;
            case "financial history":
                clickByLinkText("Financial history");
                waitForTextToBePresent("Financial history                ");
                break;
            case "financial evidence":
                clickByLinkText("Financial evidence");
                waitForTextToBePresent("Financial evidence                ");
                break;
            case "licence history":
                clickByLinkText("Licence history");
                waitForTextToBePresent("Licence history                ");
                break;
            case "convictions and penalties":
                clickByLinkText("Convictions and penalties");
                waitForTextToBePresent("Convictions and Penalties                ");
                break;
            case "review and declarations":
                clickByLinkText("Review and declarations");
                waitForTextToBePresent("Review and declarations                ");
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
