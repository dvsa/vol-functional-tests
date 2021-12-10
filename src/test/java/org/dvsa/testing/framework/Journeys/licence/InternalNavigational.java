package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.system.Properties;
import com.amazonaws.services.dynamodbv2.xspec.AddAction;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.jetbrains.annotations.NotNull;


public class InternalNavigational extends BasePage {

    private World world;
    private String url = URL.build(ApplicationType.INTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();
    String adminDropdown = "//li[@class='admin__title']";
    public String taskTitle = "//h2[text()='Edit task']";

    public InternalNavigational(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) {
        world.globalMethods.navigateToLogin(username, emailAddress, ApplicationType.INTERNAL);
    }

    public void logInAsAdmin() {
        if (world.updateLicence.getInternalUserId() == null)
            world.APIJourney.createAdminUser();
        navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
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

    public void AdminNavigation(@NotNull AdminOption option) {
        click(adminDropdown, SelectorType.XPATH);
        clickByLinkText(option.toString());
        waitForTitleToBePresent(option.toString());
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
