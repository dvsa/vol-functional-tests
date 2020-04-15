package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.S3;
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

public class InternalNavigationalJourneySteps extends BasePage {

    private World world;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InternalNavigationalJourneySteps(World world) {
        this.world = world;
    }

    public void navigateToInternalAdminUserLogin(String username, String emailAddress) throws MissingRequiredArgument, IllegalBrowserException, MalformedURLException {
        String newPassword = world.configuration.config.getString("internalNewPassword");
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();

        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
            navigate().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
        navigate().get(myURL);
        String password = S3.getTempPassword(emailAddress, world.configuration.getBucketName());

        try {
            signIn(username, password);
        } catch (Exception e) {
            //User is already registered
            signIn(username, getPassword());
        } finally {
            if (isTextPresent("Current password", 2000)) {
                enterField(nameAttribute("input", "oldPassword"), password);
                enterField(nameAttribute("input", "newPassword"), newPassword);
                enterField(nameAttribute("input", "confirmPassword"), newPassword);
                click(nameAttribute("input", "submit"));
                setPassword(newPassword);
            }
        }
    }

    public void internalUserNavigateToDocsTable() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        urlSearchAndViewApplication();
        clickByLinkText("Docs");
    }

    public void navigateToInternalTask() throws IllegalBrowserException, MalformedURLException {
        world.APIJourneySteps.createAdminUser();
        navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
        urlSearchAndViewApplication();
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
        isElementEnabled("//body", SelectorType.XPATH);
    }

    private static void signIn(@NotNull String emailAddress, @NotNull String password) throws IllegalBrowserException, MalformedURLException {
        LoginPage.email(emailAddress);
        LoginPage.password(password);
        LoginPage.submit();
        LoginPage.untilNotOnPage(5);
    }

    public void urlSearchAndViewApplication() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("application/%s", world.createLicence.getApplicationNumber())));
    }

    public void urlSearchAndViewLicence() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("licence/%s", world.createLicence.getLicenceId())));
    }

    public void urlSearchAndViewVariational() throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("variation/%s", world.updateLicence.getVariationApplicationNumber())));
    }

    public void urlSearchAndViewEditFee(String feeNumber) throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("admin/payment-processing/fees/edit-fee/%s", feeNumber)));
    }

    public void urlSearchAndViewInternalUserAccount(String adminUserId) throws IllegalBrowserException, MalformedURLException {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env).toString();
        navigate().get(myURL.concat(String.format("admin/user-management/users/edit/%s", adminUserId)));
    }
}
