package org.dvsa.testing.framework.Global;

import activesupport.aws.s3.SecretsManager;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.driver.Browser;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.time.Duration;

import static activesupport.driver.Browser.navigate;

public class GlobalMethods extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(GlobalMethods.class);
    private World world;
    private String loginPassword;
    public Dates date = new Dates(new LocalDateCalendar());
    private final String emailField = nameAttribute("input", "username");
    private final String passwordField = nameAttribute("input", "password");
    private final String newPasswordField = nameAttribute("input", "newPassword");
    private final String confirmPasswordField = nameAttribute("input", "confirmPassword");
    private final String submitButton = "//*[@id='auth.login.button']";


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
        String newPassword = SecretsManager.getSecretValue("internalNewPassword");
        String domainURL = URL.build(applicationType, world.configuration.env, "auth/login").toString();
        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        }
        DriverUtils.get(domainURL);
        enterCredentialsAndLogin(username, emailAddress, newPassword);
        if (isTextPresent("Welcome to your account")){
            click("termsAgreed",SelectorType.ID);
            UniversalActions.clickSubmit();}
    }

    public void enterCredentialsAndLogin(String username, String emailAddress, String newPassword) {
        LOGGER.info("Entering username: " + username);
        replaceText(emailField, SelectorType.CSS, username);
        click(submitButton, SelectorType.XPATH);
        untilNotInDOM(submitButton, 5);
    }

    public void signIn(String userName, String password) {
        if (isElementPresent("declarationRead", SelectorType.ID)
                && (!isElementSelected("declarationRead", SelectorType.ID))) {
            waitAndClick("declarationRead", SelectorType.ID);
        }
        replaceText(emailField, SelectorType.CSS, userName);
        replaceText(passwordField, SelectorType.CSS, password);
        click(submitButton, SelectorType.XPATH);
        untilNotInDOM(submitButton, 5);
    }
    public void submit() {
        click(submitButton, SelectorType.CSS);
    }
}