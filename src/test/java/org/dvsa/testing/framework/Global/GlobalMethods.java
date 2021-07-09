package org.dvsa.testing.framework.Global;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.driver.Browser;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.concurrent.TimeUnit;

import static activesupport.driver.Browser.navigate;

public class GlobalMethods extends BasePage {

    private World world;
    private String loginPassword;
    public Dates date = new Dates(new LocalDateCalendar());
    private String emailField = nameAttribute("input", "username");
    private String passwordField = nameAttribute("input", "password");
    private String submitButton = nameAttribute("input", "submit") + "[value=\"Sign in\"]";


    public GlobalMethods(World world){
        this.world = world;
    }


    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String password) { this.loginPassword = password; }

    public void navigateToLoginWithoutCookies(String username, String emailAddress, ApplicationType applicationType) {
        String newPassword = world.configuration.config.getString("internalNewPassword");
        String myURL = URL.build(applicationType, world.configuration.env, "auth/login").toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }

        get(myURL);
        String password = world.configuration.getTempPassword(emailAddress);

        // TODO: Setup way to store new passwords after they are set and once they are set default to them?
        // Also look at calls in SS and Internal Navigational steps cause there is a lot of replication.

        try {
            signIn(username, password);
        } catch (Exception e) {
            //User is already registered
            signIn(username, getLoginPassword());
        } finally {
            if (isTextPresent("Current password")) {
                waitForTextToBePresent("Re-enter new password");
                enterText(nameAttribute("input", "oldPassword"), SelectorType.CSS, password);
                enterText(nameAttribute("input", "newPassword"), SelectorType.CSS, newPassword);
                enterText(nameAttribute("input", "confirmPassword"), SelectorType.CSS, newPassword);
                click(nameAttribute("input", "submit"), SelectorType.CSS);
                setLoginPassword(newPassword);
            }
        }
    }

    public void navigateToLogin(String username, String emailAddress, ApplicationType applicationType) {
        navigateToLoginWithoutCookies(username, emailAddress, applicationType);

        if (isElementPresent("//*[contains(text(),'Accept')]", SelectorType.XPATH)) {
            waitAndClick("//*[contains(text(),'Accept')]", SelectorType.XPATH);}
    }

    private void signIn(String userName, String password) {
        replaceText(emailField, SelectorType.CSS, userName);
        replaceText(passwordField, SelectorType.CSS, password);
        click(submitButton, SelectorType.CSS);
        untilNotInDOM(submitButton, 5);
    }

    public void submit() {
        click(submitButton, SelectorType.CSS);
    }
}
