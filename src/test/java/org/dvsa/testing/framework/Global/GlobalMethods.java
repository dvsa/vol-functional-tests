package org.dvsa.testing.framework.Global;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.driver.Browser;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static activesupport.driver.Browser.navigate;

public class GlobalMethods extends BasePage {

    private World world;
    private String loginPassword;
    public Dates date = new Dates(new LocalDateCalendar());
    private final String emailField = nameAttribute("input", "username");
    private final String passwordField = nameAttribute("input", "password");
    private final String oldPasswordField = nameAttribute("input", "oldPassword");
    private final String newPasswordField = nameAttribute("input", "newPassword");
    private final String confirmPasswordField = nameAttribute("input", "confirmPassword");
    private final String submitButton = nameAttribute("input", "submit") + "[value=\"Sign in\"]";


    public GlobalMethods(World world) {
        this.world = world;
    }


    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String password) {
        this.loginPassword = password;
    }

    public void navigateToLoginWithoutCookies(String username, String emailAddress, ApplicationType applicationType) {
        String newPassword = world.configuration.config.getString("internalNewPassword");
        String myURL = URL.build(applicationType, world.configuration.env, "auth/login").toString();
        DriverUtils.get(myURL);
        enterCredentialsAndLogin(username, emailAddress, newPassword);
    }

    public void enterCredentialsAndLogin(String username, String emailAddress, String newPassword) {
        // TODO: Setup way to store new passwords after they are set and once they are set default to them?
        // Also look at calls in SS and Internal Navigational steps cause there is a lot of replication.
        String password = world.configuration.getTempPassword(emailAddress);
        navigate().manage().deleteAllCookies();
        if (isElementPresent("//*[contains(text(),'Accept')]", SelectorType.XPATH)) {
            waitAndClick("//*[contains(text(),'Accept')]", SelectorType.XPATH);
        }
        try {
            if (isTitlePresent("Sign in to your Vehicle Operator Licensing account", 1)) {
                signIn(username, password);
            }
        } catch (Exception e) {
            //User is already registered
            if (isTitlePresent("Sign in to your Vehicle Operator Licensing account", 1)) {
                signIn(username, getLoginPassword());
            }
        } finally {
            if (isTitlePresent("Change your password", 1)) {
                waitAndEnterText(oldPasswordField, SelectorType.CSS, password);
                waitAndEnterText(newPasswordField, SelectorType.CSS, newPassword);
                waitAndEnterText(confirmPasswordField, SelectorType.CSS, newPassword);
                click(nameAttribute("input", "submit"), SelectorType.CSS);
                setLoginPassword(newPassword);
            }
        }
    }

    public void navigateToLogin(String username, String emailAddress, ApplicationType applicationType) {
        navigateToLoginWithoutCookies(username, emailAddress, applicationType);
    }

    public void signIn(String userName, String password) {
        waitAndEnterText(emailField, SelectorType.CSS, userName);
        waitAndEnterText(passwordField, SelectorType.CSS, password);
        waitAndClick(submitButton, SelectorType.CSS);
        untilNotInDOM(submitButton, 5);
    }

    public void submit() {
        click(submitButton, SelectorType.CSS);
    }
}