package org.dvsa.testing.framework.Global;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.driver.Browser;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static activesupport.driver.Browser.navigate;

public class GlobalMethods extends BasePage{

    private World world;
    private String loginPassword;
    public Dates date = new Dates(new LocalDateCalendar());


    public GlobalMethods(World world){
        this.world = world;
    }


    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String password) { this.loginPassword = password; }

    public void navigateToLogin(String username, String emailAddress, ApplicationType applicationType) throws MalformedURLException{
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
            LoginPage.signIn(username, password);
        } catch (Exception e) {
            //User is already registered
            LoginPage.signIn(username, getLoginPassword());
        } finally {
            if (isTextPresent("Current password", 60)) {
                waitForTextToBePresent("Re-enter new password");
                enterField(nameAttribute("input", "oldPassword"), password);
                enterField(nameAttribute("input", "newPassword"), newPassword);
                enterField(nameAttribute("input", "confirmPassword"), newPassword);
                click(nameAttribute("input", "submit"));
                setLoginPassword(newPassword);
            }
        }

        if (isElementPresent("//*[contains(text(),'Accept')]", SelectorType.XPATH)) {
            waitAndClick("//*[contains(text(),'Accept')]", SelectorType.XPATH);}
    }
}
