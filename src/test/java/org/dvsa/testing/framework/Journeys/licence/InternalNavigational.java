package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.system.Properties;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

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

    public void logInAndNavigateToApplicationDocsTable(boolean variation)  {
        loginAndGetApplication(variation);
        clickByLinkText("Docs");
    }

    public void logInAndNavigateToApplicationProcessingPage(boolean variation)  {
        loginAndGetApplication(variation);
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
    }

    public void adminPageUiNavigation(String page) {
        if (world.updateLicence.getInternalUserId() == null)
            world.APIJourney.createAdminUser();
        logInAsAdmin();
        click(adminDropdown, SelectorType.XPATH);
        switch (page) {
            case "Scanning":
                clickByLinkText("Scanning");
                break;
            case "Printing":
                clickByLinkText("Printing");
                break;
            case "Task allocation rules":
                clickByLinkText("Task allocation rules");
                break;
            case "Public holidays":
                clickByLinkText("Public holidays");
                break;
            case "Bus registrations":
                clickByLinkText("Bus registrations");
                break;
            case "Continuations" :
                clickByLinkText("Continuations");
                break;
            case "Your account":
                clickByLinkText("Your account");
                break;
            case "System parameters":
                clickByLinkText("System parameters");
                break;
            case "Permits":
                clickByLinkText("Permits");
                break;
            case "Data rentention":
                clickByLinkText("Data retention");
                break;
            case "User management":
                clickByLinkText("User management");
                break;
            case "Publications":
                clickByLinkText("Publications ");
                break;
            case "Payment processing":
                clickByLinkText("Payment processing");
                break;
            case "Reports":
                clickByLinkText("Reports");
                break;
            case "Feature toggle":
                clickByLinkText("Feature toggle");
                break;
            case "System messages":
                clickByLinkText("System messages");
                break;
            case "Content Management":
                clickByLinkText("Content management");
                break;
            case "Fee rates":
                clickByLinkText("Fee rates");
                break;
            case "Financial standing rates":
                clickByLinkText("Finanical standing rates");
                break;
        }
    }

    public void loginAndGetApplication(boolean variation) {
        if (world.updateLicence.getInternalUserId() == null)
            world.APIJourney.createAdminUser();
        logInAsAdmin();
        if (variation) {
            getVariationApplication();
        } else {
            getApplication();
        }
    }

    public void urlViewUsers() {
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env,"/search/user/search/").toString();
    }

    public void getApplication()  {
        get(this.url.concat(String.format("application/%s", world.createApplication.getApplicationId())));
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
        logInAndNavigateToApplicationProcessingPage(false);
        clickByXPath(taskLinkText);
        waitForElementToBePresent(taskTitle);
    }
}
