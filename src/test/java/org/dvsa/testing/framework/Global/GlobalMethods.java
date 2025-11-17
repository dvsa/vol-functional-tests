package org.dvsa.testing.framework.Global;

import activesupport.aws.s3.SecretsManager;
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
import org.dvsa.testing.lib.url.webapp.webAppURL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.time.Duration;

import static activesupport.driver.Browser.navigate;

public class GlobalMethods extends BasePage {

    private final World world;
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
        var newPassword = SecretsManager.getSecretValue("internalNewPassword");
        var domainURL = webAppURL.build(applicationType, world.configuration.env, "auth/login").toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        }

        DriverUtils.get(domainURL);

        try {
            enterCredentialsAndLogin(username, emailAddress, newPassword);
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        if (isTextPresent("Welcome to your account")) {
            click("termsAgreed", SelectorType.ID);
            waitAndClick("submit", SelectorType.ID);
        }
    }

    public void enterCredentialsAndLogin(String username, String emailAddress, String newPassword) throws DecoderException {
        // TODO: Setup way to store new passwords after they are set and once they are set default to them?
        // Also look at calls in SS and Internal Navigational steps cause there is a lot of replication.
        var quotedPrintableCodec = new QuotedPrintableCodec();
        var password = switch (world.configuration.env.toString()) {
           // case "local" -> throw new IllegalStateException("getTempPasswordFromMailhog method is missing");
            default -> quotedPrintableCodec.decode(world.configuration.getTempPassword(emailAddress));
        };

        if (password == null) {
            throw new IllegalArgumentException("Retrieved password is null");
        }

        try {
            signIn(username, password);
            if (isTextPresent("Please check your username and password")) {
                signIn(username, getLoginPassword());
            }
        } finally {
            if (isTextPresent("Your password must:")) {
                waitAndEnterText(newPasswordField, SelectorType.CSS, newPassword);
                waitAndEnterText(confirmPasswordField, SelectorType.CSS, newPassword);
                click(nameAttribute("input", "submit"), SelectorType.CSS);
                setLoginPassword(newPassword);
                untilNotInDOM(submitButton, 1);
            }
        }
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
        if (isTextPresent("Welcome to your account")) {
            waitAndClick("termsAgreed", SelectorType.ID);
            waitAndClick("submit", SelectorType.ID);
        }
    }
    public void submit() {
        click(submitButton, SelectorType.CSS);
    }
}