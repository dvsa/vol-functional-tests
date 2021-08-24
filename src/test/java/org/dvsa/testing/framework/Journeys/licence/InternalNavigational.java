package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.system.Properties;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import static activesupport.driver.Browser.navigate;
import static org.junit.Assert.assertTrue;

public class InternalNavigational extends BasePage {

    private World world;
    private String myURL = URL.build(ApplicationType.INTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();

    String adminDropdown = "//li[@class='admin__title']";
    String financialStandingAdminLink = "//a[@id='menu-admin-dashboard/admin-financial-standing']";
    String financialStandingTitle = "Financial standing rates";
    public String taskTitle = "//h2[text()='Edit task']";

    public InternalNavigational(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) {
        world.globalMethods.navigateToLogin(username, emailAddress, ApplicationType.INTERNAL);
    }

    public void navigateToFinancialStandingRates() {
        click(adminDropdown, SelectorType.XPATH);
        click(financialStandingAdminLink, SelectorType.XPATH);
        waitForTitleToBePresent(financialStandingTitle);
    }

    public void logInAndNavigateToDocsTable()  {
        world.APIJourney.createAdminUser();
        navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        urlSearchAndViewApplication();
        clickByLinkText("Docs");
    } // refactor to use global navigate to task method or something on the end after the login steps.

    public void logInAndNavigateToTask()  {
        world.APIJourney.createAdminUser();
        navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        urlSearchAndViewApplication();
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
        assertTrue(isElementEnabled("//body", SelectorType.XPATH));
    } // refactor to use global navigate to task method or something on the end after the login steps.

    public void urlSearchAndViewApplication()  {
        navigate().get(this.myURL.concat(String.format("application/%s", world.createApplication.getApplicationId())));
    }

    public void urlSearchAndViewLicence()  {
        navigate().get(this.myURL.concat(String.format("licence/%s", world.createApplication.getLicenceId())));
    }

    public void urlSearchAndViewVariational()  {
        navigate().get(this.myURL.concat(String.format("variation/%s", world.updateLicence.getVariationApplicationId())));
    }

    public void urlSearchAndViewEditFee(String feeNumber)  {
        navigate().get(this.myURL.concat(String.format("admin/payment-processing/fees/edit-fee/%s", feeNumber)));
    }

    public void urlSearchAndViewInternalUserAccount(String adminUserId)  {
        navigate().get(this.myURL.concat(String.format("admin/user-management/users/edit/%s", adminUserId)));
    }

    public void logIntoInternalAndClickOnTask(String taskLinkText) {
        logInAndNavigateToTask();
        clickByXPath(taskLinkText);
        waitForElementToBePresent(taskTitle);
    }
}
