package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.driver.Browser;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static activesupport.driver.Browser.navigate;

public class SelfServeNavigationalJourneySteps extends BasePage {

    public World world;
    private String password;

    public SelfServeNavigationalJourneySteps(World world) {
        this.world = world;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void navigateToExternalUserLogin(String username, String emailAddress) throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        String newPassword = world.configuration.config.getString("internalNewPassword");
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env).toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            if (isElementPresent("//*[contains(text(),'Accept')]", SelectorType.XPATH)) {
                waitAndClick("//*[contains(text(),'Accept')]", SelectorType.XPATH);}
        }

        get(myURL);
        String password = world.configuration.getTempPassword(emailAddress);

        if (isElementPresent("//*[contains(text(),'Accept')]", SelectorType.XPATH)) {
            waitAndClick("//*[contains(text(),'Accept')]", SelectorType.XPATH);}

        try {
            signIn(username, password);
        } catch (Exception e) {
            //User is already registered
            signIn(username, getPassword());
        } finally {
            if (isTextPresent("Current password", 60)) {
                enterField(nameAttribute("input", "oldPassword"), password);
                enterField(nameAttribute("input", "newPassword"), newPassword);
                enterField(nameAttribute("input", "confirmPassword"), newPassword);
                click(nameAttribute("input", "submit"));
                setPassword(newPassword);
            }
        }
    }

    public void navigateToExternalSearch() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "search/find-lorry-bus-operators/").toString();
        navigate().get(myURL);
    }


    public void navigateToSelfServePage(String type, String page) throws IllegalBrowserException, MalformedURLException {
        clickByLinkText("GOV.UK");
        waitForTextToBePresent("You must keep your records up to date");
        String applicationStatus = null;
        String variationApplicationStatus = null;
        String overviewStatus;
        switch (type.toLowerCase()) {
            case "licence":
                clickByLinkText(world.createLicence.getLicenceNumber());
                waitForTextToBePresent("View and amend your licence");
                break;
            case "application":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.createLicence.getApplicationNumber());
                applicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.createLicence.getApplicationNumber());
                if (applicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply for a new licence");
                } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTextToBePresent("Application overview");
                }
                break;
            case "variation":
                overviewStatus = String.format("//table//tr[td//*[contains(text(),'%s')]]//span[contains(@class,'overview__status')]", world.updateLicence.getVariationApplicationNumber());
                variationApplicationStatus = getText(overviewStatus, SelectorType.XPATH);
                clickByLinkText(world.updateLicence.getVariationApplicationNumber());
                if (variationApplicationStatus.equals("NOT YET SUBMITTED")) {
                    waitForTextToBePresent("Apply to change a licence");
                } else if (variationApplicationStatus.equals("UNDER CONSIDERATION")) {
                    waitForTextToBePresent("Application overview");
                }
                break;
        }
        switch (page.toLowerCase()) {
            case "view":
                switch (type.toLowerCase()) {
                    case "licence":
                        waitForTextToBePresent("View and amend your licence");
                        break;
                    case "application":
                        if (applicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTextToBePresent("Apply for a new licence");
                        } else if (applicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What you need to do next");
                        }
                        break;
                    case "variation":
                        if (variationApplicationStatus.equals("NOT YET SUBMITTED")) {
                            waitForTextToBePresent("Apply to change a licence");
                        } else if (variationApplicationStatus.equals("UNDER CONSIDERATION")) {
                            waitForTextToBePresent("What happens next?");
                        }
                        break;
                }
                break;
            case "type of licence":
                clickByLinkText("Type of licence");
                waitForTextToBePresent("Operator location");
                break;
            case "business type":
                clickByLinkText("Business type");
                waitForTextToBePresent("Business type");
                break;
            case "business details":
                clickByLinkText("Business details");
                waitForTextToBePresent("Business details");
                break;
            case "address":
                clickByLinkText("Address");
                waitForTextToBePresent("Address");
                break;
            case "addresses":
                clickByLinkText("Addresses");
                waitForTextToBePresent("Addresses");
                break;
            case "directors":
                clickByLinkText("Directors");
                waitForTextToBePresent("Directors");
                break;
            case "operating centres":
                clickByLinkText("Operating centres and authorisation");
                waitForTextToBePresent("Operating centres and authorisation");
                break;
            case "transport managers":
                clickByLinkText("Transport Managers");
                waitForTextToBePresent("Transport Managers");
                break;
            case "vehicles":
                clickByLinkText("Vehicles");
                waitForTextToBePresent("Vehicle details");
                break;
            case "vehicle declarations":
                clickByLinkText("Vehicle declarations");
                waitForTextToBePresent("Vehicle declarations");
                break;
            case "trailers":
                clickByLinkText("Trailers");
                waitForTextToBePresent("Trailers");
                break;
            case "licence discs":
                clickByLinkText("Licence discs");
                waitForTextToBePresent("Licence discs");
                break;
            case "safety and compliance":
                clickByLinkText("Safety and compliance");
                waitForTextToBePresent("Safety and compliance");
                break;
            case "conditions and undertakings":
                clickByLinkText("Conditions and undertakings");
                waitForTextToBePresent("Conditions and undertakings");
                break;
            case "financial history":
                clickByLinkText("Financial history");
                waitForTextToBePresent("Financial history");
                break;
            case "financial evidence":
                clickByLinkText("Financial evidence");
                waitForTextToBePresent("Financial evidence");
                break;
            case "licence history":
                clickByLinkText("Licence history");
                waitForTextToBePresent("Licence history");
                break;
            case "convictions and penalties":
                clickByLinkText("Convictions and penalties");
                waitForTextToBePresent("Convictions and Penalties");
                break;
            case "review and declarations":
                clickByLinkText("Review and declarations");
                waitForTextToBePresent("Review and declarations");
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

    private static void signIn(@NotNull String emailAddress, @NotNull String password) throws IllegalBrowserException, MalformedURLException {
        LoginPage.email(emailAddress);
        LoginPage.password(password);
        LoginPage.submit();
        LoginPage.untilNotOnPage(5);
    }
}
