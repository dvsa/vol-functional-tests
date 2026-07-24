package org.dvsa.testing.framework.Journeys.licence;

import activesupport.aws.s3.SecretsManager;
import activesupport.driver.Browser;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.system.Properties;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.dvsa.testing.lib.url.webapp.webAppURL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static activesupport.driver.Browser.navigate;

public class InternalNavigation extends BasePage {

    World world;
    private final String url = webAppURL.build(ApplicationType.INTERNAL, EnvironmentType.getEnum(Properties.get("env", true))).toString();
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
            navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        } else {
            navigateToLoginPage();
            if (isElementNotPresent(world.internalNavigation.adminDropdown, SelectorType.XPATH)) {
                world.globalMethods.signIn(world.registerUser.getUserName(), SecretsManager.getSecretValue("internalNewPassword"));
            }
        }
    }

    public void logInAndNavigateToApplicationDocsTable(boolean variation) throws HttpException {
        loginAndGetApplication(variation);
        waitAndClickByLinkText("Docs");
    }

    public void logInAndNavigateToApplicationProcessingPage(boolean variation) throws HttpException {
        loginAndGetApplication(variation);
        waitForTextToBePresent("Processing");
        waitAndClickByLinkText("Processing");
    }

    public void adminNavigation(@NotNull AdminOption option) {
        waitAndClick(adminDropdown, SelectorType.XPATH);
        waitAndClickByLinkText(option.toString());
        switch (option) {
            case CONTINUATIONS, PRESIDING_TCS -> {}
            case PUBLICATIONS, REPORTS, PRINTING, DATA_RETENTION, USER_MANAGEMENT -> waitForElementToBePresent(String.format("//h4[contains(text(),'%s')]", option));
            case BUS_REGISTRATIONS -> waitForElementToBePresent("//h4[contains(text(),'Bus Registrations')]");
            case FEATURE_TOGGLE -> waitForElementToBePresent("//h4[contains(text(),'Feature toggles')]");
            case FEE_RATES -> waitForElementToBePresent("//h4[contains(text(),'Fee Rates')]");
            case CONTENT_MANAGEMENT -> waitForElementToBePresent("//h4[contains(text(),'Templates')]");
            default -> waitForTitleToBePresent(option.toString());
        }
    }

    public void loginIntoInternal(String userRole) throws HttpException {
        navigateToLoginPage();
        if (isElementNotPresent(world.internalNavigation.adminDropdown, SelectorType.XPATH)) {
            switch (userRole) {
                case "limitedReadOnlyUser" -> world.globalMethods.signIn(SecretsManager.getSecretValue("limitedReadOnlyUser"), SecretsManager.getSecretValue("adminPassword"));
                case "readOnlyUser" -> world.globalMethods.signIn(SecretsManager.getSecretValue("readOnlyUser"), SecretsManager.getSecretValue("adminPassword"));
                case "intSystemAdmin" -> world.globalMethods.signIn(SecretsManager.getSecretValue("intSystemAdmin"), SecretsManager.getSecretValue("intEnvPassword"));
                case "intPrepUser" -> world.globalMethods.signIn(SecretsManager.getSecretValue("intPrepUser"), SecretsManager.getSecretValue("prepEnvPassword"));
                case "intProdUser" -> world.globalMethods.signIn(SecretsManager.getSecretValue("intProdUser"), SecretsManager.getSecretValue("prepEnvPassword"));
                default -> world.internalNavigation.logInAsAdmin();
            }
        }
    }
    public void navigateToAuthorisationPage() {
        if (world.licenceCreation.isLGVOnlyLicence())
            waitAndClickByLinkText("Licence authorisation");
        else
            waitAndClickByLinkText("Operating centres and authorisation");
    }

    public void loginAndGetApplication(boolean variation) throws HttpException {
        logInAsAdmin();
        if (variation) {
            getVariationApplication();
        } else {
            getApplication();
        }
    }

    public void urlViewUsers() {
        var myURL = webAppURL.build(ApplicationType.INTERNAL, world.configuration.env, "/search/user/search/").toString();
    }

    public void getCase() {
        var caseUrl = world.configuration.env.equals(EnvironmentType.PREPRODUCTION) ?
                this.url.concat("licence/318365/cases/") :
                this.url.concat(String.format("case/details/%s", world.updateLicence.getCaseId()));
        get(caseUrl);
    }

    public void getCase(String caseId) {
        get(this.url.concat(String.format("case/details/%s", caseId)));
    }

    public void getCaseNote() {
        get(this.url.concat(String.format("case/%s/processing/notes", world.updateLicence.getCaseId())));
    }

    public void getApplication() {
        get(this.url.concat(String.format("application/%s", world.createApplication.getApplicationId())));
    }

    public void getApplication(String applicationId) {
        get(this.url.concat(String.format("application/%s", applicationId)));
    }

    public void getTransportManagerDetails(String transportManagerId) {
        get(this.url.concat(String.format("transport-manager/%s/details/", transportManagerId)));
    }

    public void getLicence() {
        get(this.url.concat(String.format("licence/%s", world.createApplication.getLicenceId())));
    }

    public void getLicence(String licenceId) {
        get(this.url.concat(String.format("licence/%s", licenceId)));
    }

    public void getVariationApplication() {
        get(this.url.concat(String.format("variation/%s", world.updateLicence.getVariationApplicationId())));
    }

    public void getHearingAppeal() {
        get(this.url.concat(String.format("case/%s/hearing-appeal/", world.updateLicence.getCaseId())));
    }

    public void getPublicInquiry() {
        get(this.url.concat(String.format("case/%s/pi/", world.updateLicence.getCaseId())));
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
            world.internalNavigation.logInAsAdmin();
        }
        switch (type) {
            case "application" -> getApplication();
            case "licence" -> getLicence();
            case "variation" -> getVariationApplication();
        }
        switch (page.toString()) {
            case "View" -> {}
            case "Vehicles" -> {
                waitAndClickByLinkText("Vehicles");
                waitForTextToBePresent("Vehicle details");
            }
            case "Convictions and penalties" -> {
                waitAndClickByLinkText("Convictions and penalties");
                waitForTextToBePresent("Convictions and Penalties");
            }
            default -> {
                waitAndClickByLinkText(page.toString());
                waitForTextToBePresent(page.toString());
            }
        }
    }

    public void navigateToPrintIRHPPermits() {
        clickById("menu-admin-dashboard/admin-printing/irhp-permits");
    }

    public void searchForIRHPPermitsToPrint() {
        selectValueFromDropDownByIndex("irhpPermitType", SelectorType.ID, 1);
        waitAndClick("irhpPermitStock", SelectorType.ID);
        selectValueFromDropDownByIndex("irhpPermitStock", SelectorType.ID, 1);
        clickById("form-actions[search]");
    }

    public void navigateToLoginPage() {
        var myURL = webAppURL.build(ApplicationType.INTERNAL, world.configuration.env, "auth/login/").toString();
        navigate().get(myURL);
    }

    public String appealNumber;
    public String appealDate;
    public String appealDeadline;
    public String appealReason;
    public String appealOutlineGround;

    public void addAppeal() {
        appealNumber = String.format("APP-%d", System.currentTimeMillis() % 100000);
        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(14);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        appealDate = today.format(fmt);
        appealDeadline = deadline.format(fmt);
        appealReason = "Application";
        appealOutlineGround = "Automated test appeal outline ground - " + appealNumber;

        waitAndClickByLinkText("Add appeal");
        waitForElementToBePresent("//form[@id='appeal']");

        enterDateParts("appealDate", today);
        enterDateParts("deadlineDate", deadline);

        waitAndEnterText("//input[@id='fields[appealNo]']", SelectorType.XPATH, appealNumber);
        waitAndSelectValueFromDropDown("//select[@id='fields[reason]']", SelectorType.XPATH, appealReason);
        waitAndEnterText("//textarea[@id='fields[outlineGround]']", SelectorType.XPATH, appealOutlineGround);

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String piAgreedDate;
    public String piAgreedByRole;
    public String piType;
    public String piLegislation;
    public String piComment;

    public void addPublicInquiry() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        piAgreedDate = today.format(fmt);
        piAgreedByRole = "Traffic Commissioner";
        piComment = "Automated test PI comment - " + System.currentTimeMillis();

        waitAndClickByLinkText("Add Public Inquiry");
        waitForElementToBePresent("//form[@id='traffic-commissioner-agreement-legislation']");

        enterDateParts("agreedDate", today);

        selectFirstNonEmptyValue("//select[@id='fields[agreedByTc]']");
        waitAndSelectValueFromDropDown("//select[@id='fields[agreedByTcRole]']", SelectorType.XPATH, piAgreedByRole);
        selectFirstNonEmptyValue("//select[@id='assignedCaseworker']");

        piType = selectFirstOptionOnChosen("fields_piTypes__chosen", "//select[@id='fields[piTypes]']");
        piLegislation = selectFirstOptionOnChosen("fields_reasons__chosen", "//select[@id='fields[reasons]']");

        waitAndEnterText("//textarea[@id='fields[comment]']", SelectorType.XPATH, piComment);

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    private void selectFirstNonEmptyValue(String selectXpath) {
        var select = new org.openqa.selenium.support.ui.Select(findElement(selectXpath, SelectorType.XPATH));
        for (var opt : select.getOptions()) {
            String v = opt.getAttribute("value");
            if (v != null && !v.isEmpty()) {
                select.selectByValue(v);
                return;
            }
        }
        throw new IllegalStateException("No selectable option found for " + selectXpath);
    }

    private String selectFirstOptionOnChosen(String chosenContainerId, String underlyingSelectXpath) {
        waitAndClick(String.format("//div[@id='%s']//ul[@class='chosen-choices']", chosenContainerId), SelectorType.XPATH);
        String firstOptionXpath = String.format("//div[@id='%s']//ul[@class='chosen-results']/li[contains(concat(' ',normalize-space(@class),' '),' active-result ')][1]", chosenContainerId);
        waitForElementToBePresent(firstOptionXpath);
        String chosenText = findElement(firstOptionXpath, SelectorType.XPATH).getText();
        waitAndClick(firstOptionXpath, SelectorType.XPATH);
        return chosenText;
    }

    private void enterDateParts(String fieldName, LocalDate date) {
        waitAndEnterText(String.format("//input[@id='fields[%s]_day']", fieldName), SelectorType.XPATH,
                String.format("%02d", date.getDayOfMonth()));
        waitAndEnterText(String.format("//input[@id='fields[%s]_month']", fieldName), SelectorType.XPATH,
                String.format("%02d", date.getMonthValue()));
        waitAndEnterText(String.format("//input[@id='fields[%s]_year']", fieldName), SelectorType.XPATH,
                String.valueOf(date.getYear()));
    }
}