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
    private String url = URL.build(ApplicationType.INTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();

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

    public void logInAsAdmin() {
        navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
    }

    public void navigateToFinancialStandingRates() {
        click(adminDropdown, SelectorType.XPATH);
        click(financialStandingAdminLink, SelectorType.XPATH);
        waitForTitleToBePresent(financialStandingTitle);
    }

    public void logInAndNavigateToDocsTable()  {
        world.APIJourney.createAdminUser();
        logInAsAdmin();
        urlSearchAndViewApplication();
        clickByLinkText("Docs");
    } // refactor to use global navigate to task method or something on the end after the login steps.

    public void logInAndNavigateToApplicationProcessing()  {
        world.APIJourney.createAdminUser();
        logInAsAdmin();
        urlSearchAndViewApplication();
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
    } // refactor to use global navigate to task method or something on the end after the login steps.

    public void logInAndNavigateToAdminProcessing()  {
        world.APIJourney.createAdminUser();
        logInAsAdmin();
        click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
        click("//*[@id='menu-admin-dashboard/admin-publication']", SelectorType.XPATH);
    }

    public void urlSearchAndViewApplication()  {
        navigate().get(this.url.concat(String.format("application/%s", world.createApplication.getApplicationId())));
    }

    public void getLicence()  {
        get(this.url.concat(String.format("licence/%s", world.createApplication.getLicenceId())));
    }

    public void getVariationApplication()  {
        get(this.url.concat(String.format("variation/%s", world.updateLicence.getVariationApplicationId())));
    }

    public void getAdminEditFee(String feeNumber)  {
        get(this.url.concat(String.format("admin/payment-processing/fees/edit-fee/%s", feeNumber)));
    }

    public void getEditUserAccount(String adminUserId)  {
        get(this.url.concat(String.format("admin/user-management/users/edit/%s", adminUserId)));
    }

    public void getVariationFinancialEvidencePage() {
        get(this.url.concat(String.format("variation/%s/financial-evidence", world.updateLicence.getVariationApplicationId())));
    }

    public void logIntoInternalAndClickOnTask(String taskLinkText) {
        logInAndNavigateToApplicationProcessing();
        clickByXPath(taskLinkText);
        waitForElementToBePresent(taskTitle);
    }
}
