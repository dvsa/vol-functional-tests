package org.dvsa.testing.framework.Journeys.licence;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.system.Properties;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class InternalNavigation extends BasePage {

    private final World world;
    private String url = URL.build(ApplicationType.INTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();
    public String adminDropdown = "//li[@class='admin__title']";
    public String taskTitle = "//h2[text()='Edit task']";

    public InternalNavigation(World world) {
        this.world = world;
    }

    public void navigateToLogin(String username, String emailAddress) {
        world.globalMethods.navigateToLoginWithoutCookies(username, emailAddress, ApplicationType.INTERNAL);
    }

    public void logInAsAdmin() throws HttpException {
        if (world.updateLicence.getInternalUserId() == null) {
            world.APIJourney.createAdminUser();
        }
        navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
    }

    public void logInAndNavigateToApplicationDocsTable(boolean variation) throws HttpException {
        loginAndGetApplication(variation);
        clickByLinkText("Docs");
    }

    public void logInAndNavigateToApplicationProcessingPage(boolean variation) throws HttpException {
        loginAndGetApplication(variation);
        waitForTextToBePresent("Processing");
        clickByLinkText("Processing");
    }

    public void adminNavigation(@NotNull AdminOption option) {
        waitAndClick(adminDropdown, SelectorType.XPATH);
        clickByLinkText(option.toString());
        switch (option) {
            case CONTINUATIONS:
            case PUBLICATIONS:
            case REPORTS:
            case PRINTING:
            case DATA_RETENTION:
            case USER_MANAGEMENT:
                waitForElementToBePresent(String.format("//h4[contains(text(),'%s')]", option));
                break;
            case BUS_REGISTRATIONS:
                waitForElementToBePresent("//h4[contains(text(),'Bus Registrations')]");
                break;
            case FEATURE_TOGGLE:
                waitForElementToBePresent("//h4[contains(text(),'Feature toggles')]");
                break;
            case FEE_RATES:
                waitForElementToBePresent("//h4[contains(text(),'Fee Rates')]");
                break;
            case CONTENT_MANAGEMENT:
                waitForElementToBePresent("//h4[contains(text(),'Templates')]");
                break;
            default:
                waitForTitleToBePresent(option.toString());
        }
    }
    public void loginIntoInternal() throws HttpException {
        String intSystemAdmin = world.configuration.config.getString("intSystemAdmin");
        String intEnvPassword = world.configuration.config.getString("intEnvPassword");
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env, "auth/login").toString();
            DriverUtils.get(myURL);
            if (isElementPresent("declarationRead", SelectorType.ID)) {
                waitAndClick("declarationRead", SelectorType.ID);
            }
            world.globalMethods.signIn(intSystemAdmin, intEnvPassword);
        } else {
            world.internalNavigation.logInAsAdmin();
        }
    }

    public void navigateToAuthorisationPage() {
        if (world.licenceCreation.isLGVOnlyLicence())
            clickByLinkText("Licence authorisation");
        else
            clickByLinkText("Operating centres and authorisation");
    }

    public void loginAndGetApplication(boolean variation) throws HttpException {
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
        String myURL = URL.build(ApplicationType.INTERNAL, world.configuration.env, "/search/user/search/").toString();
    }

    public void getCase() {
        get(this.url.concat(String.format("case/details/%s", world.updateLicence.getCaseId())));
    }

    public void getCaseNote() {
        get(this.url.concat(String.format("case/%s/processing/notes", world.updateLicence.getCaseId())));
    }

    public void getApplication() {
        get(this.url.concat(String.format("application/%s", world.createApplication.getApplicationId())));
    }

    public void getLicence() {
        get(this.url.concat(String.format("licence/%s", world.createApplication.getLicenceId())));
    }

    public void getVariationApplication() {
        get(this.url.concat(String.format("variation/%s", world.updateLicence.getVariationApplicationId())));
    }

    public void getAdminEditFee(String feeNumber) {
        get(this.url.concat(String.format("admin/payment-processing/fees/edit-fee/%s", feeNumber)));
    }

    public void getEditUserAccount(String adminUserId) {
        get(this.url.concat(String.format("admin/user-management/users/edit/%s", adminUserId)));
    }

    public void getVariationFinancialEvidencePage() {
        get(this.url.concat(String.format("variation/%s/financial-evidence", world.updateLicence.getVariationApplicationId())));
    }

    public void logIntoInternalAndClickOnTask(String taskLinkText) throws HttpException {
        logInAndNavigateToApplicationProcessingPage(false);
        clickByXPath(taskLinkText);
        waitForElementToBePresent(taskTitle);
    }

    public void navigateToPage(String type, SelfServeSection page) throws HttpException {
        if (isElementNotPresent(world.internalNavigation.adminDropdown, SelectorType.XPATH)) {
            if (world.updateLicence.getInternalUserId() == null) {
                world.APIJourney.createAdminUser();
                world.internalNavigation.logInAsAdmin();
            }
        }
        switch (type) {
            case "application":
                getApplication();
                break;
            case "licence":
                getLicence();
                break;
            case "variation":
                getVariationApplication();
                break;
        }
        switch (page.toString()) {
            case "View":
                break;
            case "Vehicles":
                clickByLinkText("Vehicles");
                //Once DVLA integration has been switched on, this needs updating
                waitForTextToBePresent("Vehicle details");
                break;
            case "Convictions and penalties":
                clickByLinkText("Convictions and penalties");
                waitForTextToBePresent("Convictions and Penalties");
                break;
            default:
                clickByLinkText(page.toString());
                waitForTextToBePresent(page.toString());
                break;
        }
    }
}
