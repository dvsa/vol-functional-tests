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
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.dvsa.testing.lib.url.webapp.webAppURL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public String transportManagerId;
    public String transportManagerIdOne;
    public String transportManagerIdTwo;
    public String licenceIdOne;
    public String licenceIdTwo;

    public String captureFirstTransportManagerIdFromLicence() {
        get(this.url.concat(String.format("licence/%s/transport-managers", world.createApplication.getLicenceId())));
        waitForElementToBePresent("//a[starts-with(@href,'/transport-manager/')]");
        String href = findElement(
                "//a[starts-with(@href,'/transport-manager/')][1]",
                SelectorType.XPATH).getAttribute("href");
        transportManagerId = href.replaceAll(".*/transport-manager/(\\d+)/.*", "$1");
        return transportManagerId;
    }

    private String tmUrl(String tail) {
        return this.url.concat(String.format("transport-manager/%s/%s", transportManagerId, tail));
    }

    public void getTmDetails()               { get(tmUrl("details/")); }
    public void getTmCompetences()           { get(tmUrl("details/competences/")); }
    public void getTmCompetencesEdit(String competenceId) { get(tmUrl("details/competences/edit/" + competenceId + "/")); }
    public void getTmEmployment()            { get(tmUrl("details/employment/")); }
    public void getTmPreviousHistory()       { get(tmUrl("details/previous-history/")); }
    public void getTmResponsibilitiesEdit(String tmLicenceId) { get(tmUrl("details/responsibilities/edit-tm-licence/" + tmLicenceId + "/")); }
    public void getTmDocuments()             { get(tmUrl("documents/")); }
    public void getTmCases()                 { get(tmUrl("cases/")); }
    public void getTmCasesEdit(String caseId){ get(tmUrl("cases/edit/" + caseId + "/")); }
    public void getTmProcessingNotes()       { get(tmUrl("processing/notes/")); }
    public void getTmProcessingNotesEdit(String noteId) { get(tmUrl("processing/notes/edit/" + noteId + "/")); }
    public void getTmProcessingEventHistory(){ get(tmUrl("processing/event-history/")); }
    public void getTmProcessingPublication() { get(tmUrl("processing/publication/")); }
    public void getTmProcessingReadHistory() { get(tmUrl("processing/read-history/")); }
    public void getTmCheckRepute()           { get(tmUrl("check-repute/")); }
    public void getTmMerge()                 { get(tmUrl("merge/")); }
    public void getTmUndoDisqualification()  { get(tmUrl("undo-disqualification/")); }

    public void addTmProcessingNote(String comment, boolean priority) {
        getTmProcessingNotes();
        waitAndClick("//button[@id='add']", SelectorType.XPATH);
        waitForElementToBePresent("//textarea[@name='fields[comment]']");
        waitAndEnterText("//textarea[@name='fields[comment]']", SelectorType.XPATH, comment);
        if (priority) {
            click("//input[@type='checkbox' and @name='fields[priority]']", SelectorType.XPATH);
        }
        clickModalSubmit();
        waitForElementToBePresent(
                "//table//td[contains(normalize-space(),\"" + comment + "\")]");
    }

    public void addTmCompetence(String qualificationTypeValue, String serialNo,
                                String day, String month, String year) {
        getTmCompetences();
        waitAndClick("//button[@id='add']", SelectorType.XPATH);
        waitForElementToBePresent("//select[@name='qualification-details[qualificationType]']");
        selectValueFromDropDownByValue("//select[@name='qualification-details[qualificationType]']",
                SelectorType.XPATH, qualificationTypeValue);
        waitAndEnterText("//input[@name='qualification-details[serialNo]']", SelectorType.XPATH, serialNo);
        waitAndEnterText("//input[@name='qualification-details[issuedDate][day]']", SelectorType.XPATH, day);
        waitAndEnterText("//input[@name='qualification-details[issuedDate][month]']", SelectorType.XPATH, month);
        waitAndEnterText("//input[@name='qualification-details[issuedDate][year]']", SelectorType.XPATH, year);
        clickModalSubmit();
        waitForElementToBePresent(
                "//table//td[contains(normalize-space(),\"" + serialNo + "\")]");
    }

    public void addTmEmployer(String employerName, String position, String hoursPerWeek,
                              String addressLine1, String town, String postcode) {
        getTmEmployment();
        waitAndClick("//button[@id='add']", SelectorType.XPATH);
        waitForElementToBePresent("//input[@name='tm-employer-name-details[employerName]']");
        waitAndEnterText("//input[@name='tm-employer-name-details[employerName]']",
                SelectorType.XPATH, employerName);
        String manualLink = "//a[normalize-space()='Enter the address manually'"
                + " or normalize-space()='Enter address manually'"
                + " or normalize-space()='Enter the address yourself']";
        if (isElementPresent(manualLink, SelectorType.XPATH)) {
            waitAndClick(manualLink, SelectorType.XPATH);
        }
        waitAndEnterText("//input[@name='address[addressLine1]']", SelectorType.XPATH, addressLine1);
        waitAndEnterText("//input[@name='address[town]']", SelectorType.XPATH, town);
        waitAndEnterText("//input[@name='address[postcode]']", SelectorType.XPATH, postcode);
        waitAndEnterText("//input[@name='tm-employment-details[position]']", SelectorType.XPATH, position);
        waitAndEnterText("//input[@name='tm-employment-details[hoursPerWeek]']", SelectorType.XPATH, hoursPerWeek);
        clickModalSubmit();
        waitForElementToBePresent(
                "//table//td[contains(normalize-space(),\"" + employerName + "\")]");
    }

    public void uploadTmDocument(String description, String absoluteFilePath) {
        getTmDocuments();
        waitAndClick("//button[@id='upload']", SelectorType.XPATH);
        waitForElementToBePresent("//select[@name='details[category]']");
        waitAndEnterText("//input[@name='details[description]']", SelectorType.XPATH, description);
        selectValueFromDropDownByIndex("//select[@name='details[documentSubCategory]']",
                SelectorType.XPATH, 1);
        uploadFileToInputByName("details[file]", absoluteFilePath);
        clickModalSubmit();
        waitForElementToBePresent(
                "//table//td//a[contains(normalize-space(),\"" + description + "\")]");
    }

    public String lastRelinkedDocumentDescription;

    public String copyFirstLicenceDocumentToTransportManagerViaRelink() {
        get(this.url.concat(String.format("licence/%s/documents/",
                world.createApplication.getLicenceId())));
        String firstDocLink = "//table//tbody//tr[1]//td//a[not(contains(@class,'js-modal-ajax'))]";
        waitForElementToBePresent(firstDocLink);
        lastRelinkedDocumentDescription = getText(firstDocLink, SelectorType.XPATH).trim();
        waitAndClick("//table//tbody//tr[1]//input[@type='checkbox']", SelectorType.XPATH);
        waitAndClick(
                "//button[normalize-space()='Relink'] | //a[normalize-space()='Relink']",
                SelectorType.XPATH);
        waitForElementToBePresent("//h2[normalize-space()='Relink documents']");
        String entitySelect = "//select[.//option[normalize-space()='Transport Manager']]";
        waitForElementToBePresent(entitySelect);
        selectValueFromDropDown(entitySelect, SelectorType.XPATH, "Transport Manager");
        String tmIdInput = "//label[contains(normalize-space(),'Transport manager ID')]"
                + "/following::input[not(@type='hidden')][1]";
        waitForElementToBePresent(tmIdInput);
        clearAndEnter(tmIdInput, SelectorType.XPATH, transportManagerId);
        waitAndClick(
                "//button[normalize-space()='Copy'] | //input[@type='submit' and @value='Copy']",
                SelectorType.XPATH);
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(d -> !isElementPresent(
                            "//h2[normalize-space()='Relink documents']", SelectorType.XPATH));
        } catch (TimeoutException ignored) {
        }
        return lastRelinkedDocumentDescription;
    }

    public void editTmProcessingNote(String newComment) {
        getTmProcessingNotes();
        waitAndClick("//table//tbody//tr[1]//input[@type='radio' or @type='checkbox']",
                SelectorType.XPATH);
        waitAndClick("//button[@id='edit' or normalize-space()='Edit']", SelectorType.XPATH);
        waitForElementToBePresent("//textarea[@name='fields[comment]']");
        clearAndEnter("//textarea[@name='fields[comment]']", SelectorType.XPATH, newComment);
        clickModalSubmit();
        waitForElementToBePresent(
                "//table//td[contains(normalize-space(),\"" + newComment + "\")]");
    }

    public void editTmCompetenceSerial(String newSerial) {
        getTmCompetences();
        waitAndClick("//a[contains(@class,'js-modal-ajax') and contains(@href,'/competences/edit/')]",
                SelectorType.XPATH);
        waitForElementToBePresent("//input[@name='qualification-details[serialNo]']");
        clearAndEnter("//input[@name='qualification-details[serialNo]']", SelectorType.XPATH, newSerial);
        clickModalSubmit();
        waitForElementToBePresent(
                "//table//td[contains(normalize-space(),\"" + newSerial + "\")]");
    }

    public String capturedTmCaseId;

    public void addTmCase(String description) {
        getTmCases();
        waitAndClick("//button[@id='add']", SelectorType.XPATH);
        waitForElementToBePresent("//select[@id='fields[caseType]']");
        // Chosen widget id normalises brackets: fields[categorys] -> fields_categorys__chosen
        selectRandomOptionOnChosen("fields_categorys__chosen");
        waitAndEnterText("//textarea[@id='fields[description]']", SelectorType.XPATH, description);
        clickModalSubmit();
        waitForElementToBePresent(
                "//li[contains(@class,'definition-list__item')]"
                        + "//dt[normalize-space()='Description']"
                        + "/following-sibling::dd[contains(normalize-space(),\"" + description + "\")]");
        try {
            String href = findElement(
                    "//a[contains(@href,'/case/') or contains(@href,'/cases/')][contains(@href,'edit') or contains(@href,'details')][1]",
                    SelectorType.XPATH).getAttribute("href");
            capturedTmCaseId = href.replaceAll(".*?/(?:case|cases)(?:/[^/]+)?/(\\d+)/?.*", "$1");
        } catch (Exception ignored) {
        }
    }

    public void openTmCasePi() {
        if (capturedTmCaseId == null || capturedTmCaseId.isBlank()) {
            throw new IllegalStateException("Cannot open TM case PI: no TM case id captured. Was addTmCase run first?");
        }
        get(this.url.concat(String.format("case/%s/pi/", capturedTmCaseId)));
    }

    public void editTmCaseDescription(String newDescription) {
        waitAndClick("//a[contains(@class,'js-modal-ajax') and contains(@href,'/case/edit/')]",
                SelectorType.XPATH);
        waitForElementToBePresent("//textarea[@id='fields[description]']");
        clearAndEnter("//textarea[@id='fields[description]']", SelectorType.XPATH, newDescription);
        clickModalSubmit();
        waitForElementToBePresent(
                "//li[contains(@class,'definition-list__item')]"
                        + "//dt[normalize-space()='Description']"
                        + "/following-sibling::dd[contains(normalize-space(),\"" + newDescription + "\")]");
    }

    public void addTmPreviousLicence(String licNo, String holderName) {
        getTmPreviousHistory();
        waitAndClick("//button[@id='add-previous-licence']", SelectorType.XPATH);
        waitForElementToBePresent("//h2[normalize-space()='Add previous licence']");
        waitAndEnterText("//input[@id='lic-no']", SelectorType.XPATH, licNo);
        waitAndEnterText("//input[@id='holderName']", SelectorType.XPATH, holderName);
        clickModalSubmit();
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(d -> !isElementPresent(
                            "//h2[normalize-space()='Add previous licence']", SelectorType.XPATH));
        } catch (TimeoutException ignored) {
        }
        waitForElementToBePresent(
                "//fieldset[@id='previousLicences']//*[contains(normalize-space(),\"" + licNo + "\")]");
    }

    public void openTmResponsibilityEditForFirstLicence() {
        get(tmUrl("details/responsibilities/"));
        waitAndClick("//table//a[contains(@href,'/details/responsibilities/edit-tm-licence/')]",
                SelectorType.XPATH);
        waitForElementToBePresent("//input[@type='radio' and @name='details[tmType]']");
    }

    public void setTmResponsibilityManagerTypeAndSave(String tmTypeValue) {
        // govuk-frontend hides native radios — set state via JS + change event
        javaScriptExecutor(
                "function setRadio(name, value) {"
                        + " var r = document.querySelector(\"input[name='\" + name + \"'][value='\" + value + \"']\");"
                        + " if (r) { r.checked = true; r.dispatchEvent(new Event('change', {bubbles: true})); } }"
                        + " setRadio('details[tmType]', '" + tmTypeValue + "');"
                        + " setRadio('details[hasUndertakenTraining]', 'N');");
        clickModalSubmit();
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(20))
                    .until(d -> !d.getCurrentUrl().contains("/edit-tm-licence/"));
        } catch (org.openqa.selenium.TimeoutException te) {
            String errorSummary = "";
            var errs = findElements(
                    "//*[contains(@class,'error-summary')"
                            + " or contains(@class,'notice--error')"
                            + " or contains(@class,'validation-summary')"
                            + " or contains(@class,'govuk-error-summary')]",
                    SelectorType.XPATH);
            if (errs != null && !errs.isEmpty()) {
                errorSummary = errs.get(0).getText();
            }
            throw new RuntimeException(
                    "Save of TM responsibility did not navigate away from edit page."
                            + " URL still: " + getDriver().getCurrentUrl()
                            + (errorSummary.isEmpty()
                            ? " (no error banner detected)"
                            : " Validation banner: " + errorSummary),
                    te);
        }
    }

    public void captureFirstTmAsSource() throws org.apache.hc.core5.http.HttpException {
        transportManagerIdOne = captureFirstTransportManagerIdFromLicence();
        licenceIdOne = world.createApplication.getLicenceId();
    }

    public void createSecondApplicationAndCaptureSecondTm() throws org.apache.hc.core5.http.HttpException {
        randomiseRegisterUserIdentity();
        logOutOfSelfServe();
        world.APIJourney.registerAndGetUserDetails("operator");
        world.licenceCreation.createLicence("goods", "standard_national");
        loginIntoInternal("admin");
        transportManagerIdTwo = captureFirstTransportManagerIdFromLicence();
        licenceIdTwo = world.createApplication.getLicenceId();
    }

    private void logOutOfSelfServe() {
        String selfServeLogout = webAppURL.build(
                ApplicationType.EXTERNAL, world.configuration.env, "auth/logout/").toString();
        try {
            get(selfServeLogout);
        } catch (Exception ignored) {
        }
    }

    private void randomiseRegisterUserIdentity() {
        var faker = new activesupport.faker.FakerUtils();
        var rng = java.util.concurrent.ThreadLocalRandom.current();
        String fore = faker.generateFirstName() + rng.nextInt(100, 1000);
        String family = faker.generateLastName() + rng.nextInt(100, 1000);
        world.registerUser.setForeName(fore);
        world.registerUser.setFamilyName(family);
        world.registerUser.setUserName("%s%s%d".formatted(fore, family, rng.nextInt(1000, 10000)));
        world.registerUser.setEmailAddress(
                "%s_%s%d.tester@dvsa.com".formatted(fore, family, rng.nextInt(10000, 100000)));
        world.registerUser.setOrganisationName(faker.generateCompanyName());
    }

    public void mergeTransportManagerIntoTarget(String targetTmId) {
        transportManagerId = transportManagerIdOne;
        getTmDetails();
        waitAndClick("//a[@id='menu-transport-manager-quick-actions-merge']", SelectorType.XPATH);
        waitForElementToBePresent("//form[@id='tm-merge']//input[@id='toTmId']");
        waitAndEnterText("//form[@id='tm-merge']//input[@id='toTmId']", SelectorType.XPATH, targetTmId);
        javaScriptExecutor(
                "var el = document.getElementById('toTmId');"
                        + " if (el) { el.dispatchEvent(new Event('blur', {bubbles: true})); }");
        waitAndClick("//input[@type='checkbox' and @id='confirm']", SelectorType.XPATH);
        clickModalSubmit();
        if (waitForElementToBePresentSafely(
                "//form[@id='generic-confirmation']//button[@name='form-actions[submit]']", 15)) {
            waitAndClick("//form[@id='generic-confirmation']//button[@name='form-actions[submit]']",
                    SelectorType.XPATH);
        }
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                    .until(d -> !isElementPresent("//div[contains(@class,'modal__wrapper')]"
                                    + "//form[@id='tm-merge' or @id='generic-confirmation']",
                            SelectorType.XPATH));
        } catch (TimeoutException te) {
            String modalHtml = "";
            var modals = findElements("//div[contains(@class,'modal__wrapper')]", SelectorType.XPATH);
            if (modals != null && !modals.isEmpty()) {
                modalHtml = modals.get(0).getAttribute("innerHTML");
                if (modalHtml != null && modalHtml.length() > 2000) {
                    modalHtml = modalHtml.substring(0, 2000) + "...[truncated]";
                }
            }
            throw new RuntimeException(
                    "Merge modal did not close after submit."
                            + " Current URL: " + getDriver().getCurrentUrl()
                            + " Modal HTML: " + modalHtml,
                    te);
        }
    }

    private boolean waitForElementToBePresentSafely(String xpath, int seconds) {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(seconds))
                    .until(d -> isElementPresent(xpath, SelectorType.XPATH));
            return true;
        } catch (TimeoutException te) {
            return false;
        }
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

    public void getNonPublicInquiry() {
        get(this.url.concat(String.format("case/%s/non-pi/details/", world.updateLicence.getCaseId())));
    }

    public void getImpoundings() {
        get(this.url.concat(String.format("case/%s/impounding/", world.updateLicence.getCaseId())));
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
        long uniqueId = System.currentTimeMillis() % 100000;
        appealNumber = String.format("APP-%d", uniqueId);
        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(java.util.concurrent.ThreadLocalRandom.current().nextInt(7, 30));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        appealDate = today.format(fmt);
        appealDeadline = deadline.format(fmt);
        appealOutlineGround = "Automated test appeal outline ground - " + uniqueId;

        waitAndClickByLinkText("Add appeal");
        waitForElementToBePresent("//form[@id='appeal']");

        enterDateParts("appealDate", today);
        enterDateParts("deadlineDate", deadline);

        waitAndEnterText("//input[@id='fields[appealNo]']", SelectorType.XPATH, appealNumber);
        appealReason = selectRandomOption("//select[@id='fields[reason]']");
        waitAndEnterText("//textarea[@id='fields[outlineGround]']", SelectorType.XPATH, appealOutlineGround);

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String piAgreedDate;
    public String piAgreedByTc;
    public String piAgreedByRole;
    public String piAssignedCaseworker;
    public String piType;
    public String piLegislation;
    public String piComment;

    public void addPublicInquiry() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        piAgreedDate = today.format(fmt);
        piComment = "Automated test PI comment - " + System.currentTimeMillis();

        waitAndClickByLinkText("Add Public Inquiry");
        waitForElementToBePresent("//form[@id='traffic-commissioner-agreement-legislation']");

        enterDateParts("agreedDate", today);

        piAgreedByTc = selectRandomOption("//select[@id='fields[agreedByTc]']");
        piAgreedByRole = selectRandomOption("//select[@id='fields[agreedByTcRole]']");
        piAssignedCaseworker = selectRandomOption("//select[@id='assignedCaseworker']");

        piType = selectRandomOptionOnChosen("fields_piTypes__chosen");
        piLegislation = selectRandomOptionOnChosen("fields_reasons__chosen");

        waitAndEnterText("//textarea[@id='fields[comment]']", SelectorType.XPATH, piComment);

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String nonPiAgreedByTcDate;
    public String nonPiHearingType;
    public String nonPiHearingDate;
    public String nonPiHearingTime;
    public String nonPiVenue;
    public String nonPiWitnessCount;
    public String nonPiPresidingStaff;
    public String nonPiOutcome;

    public void addNonPublicInquiry() {
        var rnd = java.util.concurrent.ThreadLocalRandom.current();
        LocalDate today = LocalDate.now();
        LocalDate hearing = today.plusDays(rnd.nextInt(3, 30));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        nonPiAgreedByTcDate = today.format(fmt);
        nonPiHearingDate = hearing.format(fmt);
        String hour = String.format("%02d", rnd.nextInt(9, 17));
        String[] minuteValues = {"00", "15", "30", "45"};
        String minute = minuteValues[rnd.nextInt(minuteValues.length)];
        nonPiHearingTime = hour + ":" + minute;
        nonPiWitnessCount = String.valueOf(rnd.nextInt(1, 10));
        nonPiPresidingStaff = "Automated test presiding staff - " + System.currentTimeMillis();

        waitAndClickByLinkText("Add Non-Public Inquiry");
        waitForElementToBePresent("//form[@id='Non-Public Inquiry']");

        enterDateParts("agreedByTcDate", today);

        nonPiHearingType = selectRandomOption("//select[@id='hearingType']");

        enterDateParts("hearingDate", hearing);
        waitAndSelectValueFromDropDown("//select[@id='hearingDate_hour']", SelectorType.XPATH, hour);
        waitAndSelectValueFromDropDown("//select[@id='hearingDate_minute']", SelectorType.XPATH, minute);

        nonPiVenue = selectRandomOption("//select[@id='venue']");
        waitAndEnterText("//input[@id='fields[witnessCount]']", SelectorType.XPATH, nonPiWitnessCount);
        waitAndEnterText("//textarea[@id='fields[presidingStaffName]']", SelectorType.XPATH, nonPiPresidingStaff);
        nonPiOutcome = selectRandomOption("//select[@id='outcome']");

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String impoundingType;
    public String impoundingApplicationDate;
    public String impoundingVrm;
    public String impoundingLegislation;
    public String impoundingHearingDate;
    public String impoundingHearingTime;
    public String impoundingAgreedBy;
    public String impoundingOutcome;
    public String impoundingOutcomeSentDate;
    public String impoundingNotes;

    public void addImpounding() {
        var rnd = java.util.concurrent.ThreadLocalRandom.current();
        LocalDate today = LocalDate.now();
        LocalDate hearing = today.plusDays(rnd.nextInt(7, 60));
        LocalDate outcomeSent = today;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        impoundingApplicationDate = today.format(fmt);
        impoundingHearingDate = hearing.format(fmt);
        impoundingOutcomeSentDate = outcomeSent.format(fmt);
        impoundingVrm = String.format("AB%05dCD", rnd.nextInt(100000));
        String hour = String.format("%02d", rnd.nextInt(9, 17));
        String[] minuteValues = {"00", "15", "30", "45"};
        String minute = minuteValues[rnd.nextInt(minuteValues.length)];
        impoundingHearingTime = hour + ":" + minute;
        impoundingNotes = "Automated test impounding notes - " + System.currentTimeMillis();

        waitAndClick("//button[@id='add' and @value='Add']", SelectorType.XPATH);
        waitForElementToBePresent("//form[@id='Impounding']");

        impoundingType = selectRandomOption("//select[@id='impoundingType']");
        enterDateParts("applicationReceiptDate", today);
        waitAndEnterText("//input[@id='vrm']", SelectorType.XPATH, impoundingVrm);

        impoundingLegislation = selectRandomOptionOnChosen("impoundingLegislationTypes_chosen");

        enterDateParts("hearingDate", hearing);
        waitAndSelectValueFromDropDown("//select[@id='hearingDate_hour']", SelectorType.XPATH, hour);
        waitAndSelectValueFromDropDown("//select[@id='hearingDate_minute']", SelectorType.XPATH, minute);

        impoundingAgreedBy = selectRandomOption("//select[@id='presidingTc']");
        impoundingOutcome = selectRandomOption("//select[@id='outcome']");
        enterDateParts("outcomeSentDate", outcomeSent);
        waitAndEnterText("//textarea[@id='fields[notes]']", SelectorType.XPATH, impoundingNotes);

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String piHearingVenue;
    public String piHearingDate;
    public String piHearingTime;
    public String piHearingLength;
    public String piHearingPresidingTc;
    public String piHearingPresidedByRole;
    public String piHearingWitnesses;
    public String piHearingDrivers;
    public String piHearingDefinition;
    public String piHearingDetails;

    public void addPiHearing() {
        var rnd = java.util.concurrent.ThreadLocalRandom.current();
        LocalDate hearing = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        piHearingDate = hearing.format(fmt);
        String hour = String.format("%02d", rnd.nextInt(9, 17));
        String[] minuteValues = {"00", "15", "30", "45"};
        String minute = minuteValues[rnd.nextInt(minuteValues.length)];
        piHearingTime = hour + ":" + minute;
        piHearingWitnesses = String.valueOf(rnd.nextInt(1, 10));
        piHearingDrivers = String.valueOf(rnd.nextInt(1, 10));
        piHearingDetails = "Automated test hearing details - " + System.currentTimeMillis();

        waitAndClickByLinkText("Add hearing");
        waitForElementToBePresent("//form[@id='Hearing']");

        piHearingVenue = selectRandomOption("//select[@id='venue']");
        enterDateParts("hearingDate", hearing);
        waitAndSelectValueFromDropDown("//select[@id='hearingDate_hour']", SelectorType.XPATH, hour);
        waitAndSelectValueFromDropDown("//select[@id='hearingDate_minute']", SelectorType.XPATH, minute);

        String[] lengthChoices = {"Half day", "Full day"};
        piHearingLength = lengthChoices[rnd.nextInt(lengthChoices.length)];
        waitAndClick(String.format("//div[contains(@class,'govuk-radios__item')]/label[normalize-space()='%s']", piHearingLength), SelectorType.XPATH);

        piHearingPresidingTc = selectRandomOption("//select[@id='presidingTc']");
        piHearingPresidedByRole = selectRandomOption("//select[@id='presidedByRole']");

        waitAndEnterText("//input[@id='fields[witnesses]']", SelectorType.XPATH, piHearingWitnesses);
        waitAndEnterText("//input[@id='fields[drivers]']", SelectorType.XPATH, piHearingDrivers);

        piHearingDefinition = selectRandomOptionOnUnderlyingSelect("//select[@id='fields[definition]']");
        waitAndEnterText("//textarea[@id='fields[details]']", SelectorType.XPATH, piHearingDetails);

        waitAndClick("//button[@id='form-actions[publish]']", SelectorType.XPATH);
    }

    public String piDecisionPresidingTc;
    public String piDecisionPresidingTcRole;
    public String piDecisionDecision;
    public String piDecisionWitnesses;
    public String piDecisionDate;
    public String piDecisionNotificationDate;
    public String piDecisionDefinition;
    public String piDecisionNotes;

    public void addPiDecision() {
        var rnd = java.util.concurrent.ThreadLocalRandom.current();
        LocalDate today = LocalDate.now();
        LocalDate notification = today;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        piDecisionDate = today.format(fmt);
        piDecisionNotificationDate = notification.format(fmt);
        piDecisionWitnesses = String.valueOf(rnd.nextInt(1, 10));
        piDecisionNotes = "Automated test details to be published - " + System.currentTimeMillis();

        waitAndClickByLinkText("Add decision");
        waitForElementToBePresent("//form[@id='Register decision']");

        piDecisionPresidingTc = selectRandomOption("//select[@id='fields[decidedByTc]']");
        piDecisionPresidingTcRole = selectRandomOption("//select[@id='fields[decidedByTcRole]']");

        piDecisionDecision = selectRandomOptionOnChosen("fields_decisions__chosen");

        waitAndEnterText("//input[@id='fields[witnesses]']", SelectorType.XPATH, piDecisionWitnesses);

        enterDateParts("decisionDate", today);
        enterDateParts("notificationDate", notification);

        piDecisionDefinition = selectRandomOptionOnUnderlyingSelect("//select[@id='fields[definition]']");
        waitAndEnterText("//textarea[@id='fields[decisionNotes]']", SelectorType.XPATH, piDecisionNotes);

        waitAndClick("//button[@id='form-actions[publish]']", SelectorType.XPATH);
    }

    public String slaCallUpLetterDate;
    public String slaBriefSentDate;
    public String slaWrittenOutcome;

    public void editServiceLevelAgreement() {
        var rnd = java.util.concurrent.ThreadLocalRandom.current();
        LocalDate today = LocalDate.now();
        LocalDate callUp = today.minusDays(rnd.nextInt(14, 60));
        LocalDate brief = today.minusDays(rnd.nextInt(1, 13));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        slaCallUpLetterDate = callUp.format(fmt);
        slaBriefSentDate = brief.format(fmt);
        slaWrittenOutcome = "Verbal decision only";

        waitAndClick("//a[normalize-space()='Edit' and contains(@href,'/pi/sla/')]", SelectorType.XPATH);
        waitForElementToBePresent("//form[@id='Service level agreement']");

        enterDateParts("callUpLetterDate", callUp);
        enterDateParts("briefToTcDate", brief);
        waitAndSelectValueFromDropDown("//select[@id='fields[writtenOutcome]']", SelectorType.XPATH, slaWrittenOutcome);

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String slaException;

    public void addSlaException() {
        waitAndClick("//button[@id='addCasePiSlaException']", SelectorType.XPATH);
        waitForElementToBePresent("//form[@id='SLA Exception']");
        slaException = selectRandomOptionOnUnderlyingSelect("//select[@id='slaException']");
        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String tcStayRequestDate;
    public String tcStayDecisionDate;
    public String tcStayOutcome;
    public String tcStayNotes;
    public String tcStayDvsaNotified;
    public String tcStayIsWithdrawn;
    public String tcStayWithdrawnDate;

    public String utStayRequestDate;
    public String utStayDecisionDate;
    public String utStayOutcome;
    public String utStayNotes;
    public String utStayDvsaNotified;
    public String utStayIsWithdrawn;
    public String utStayWithdrawnDate;

    public static class StayOptions {
        public String outcome;
        public boolean dvsaNotified;
        public boolean withdrawn;

        public StayOptions(String outcome, boolean dvsaNotified, boolean withdrawn) {
            this.outcome = outcome;
            this.dvsaNotified = dvsaNotified;
            this.withdrawn = withdrawn;
        }
    }

    public void addTcStay(StayOptions opts) {
        var values = addStay("stay_t_tc", opts);
        tcStayRequestDate = values[0];
        tcStayDecisionDate = values[1];
        tcStayOutcome = values[2];
        tcStayNotes = values[3];
        tcStayDvsaNotified = opts.dvsaNotified ? "Yes" : "No";
        tcStayIsWithdrawn = opts.withdrawn ? "Yes" : "No";
        tcStayWithdrawnDate = values[4];
    }

    public void addUtStay(StayOptions opts) {
        var values = addStay("stay_t_ut", opts);
        utStayRequestDate = values[0];
        utStayDecisionDate = values[1];
        utStayOutcome = values[2];
        utStayNotes = values[3];
        utStayDvsaNotified = opts.dvsaNotified ? "Yes" : "No";
        utStayIsWithdrawn = opts.withdrawn ? "Yes" : "No";
        utStayWithdrawnDate = values[4];
    }

    private String[] addStay(String stayType, StayOptions opts) {
        var rnd = java.util.concurrent.ThreadLocalRandom.current();
        LocalDate today = LocalDate.now();
        LocalDate requestDate = today.minusDays(rnd.nextInt(7, 30));
        LocalDate decisionDate = today.minusDays(rnd.nextInt(0, 6));
        LocalDate withdrawnDate = today.minusDays(rnd.nextInt(0, 3));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String requestDateStr = requestDate.format(fmt);
        String decisionDateStr = decisionDate.format(fmt);
        String withdrawnDateStr = opts.withdrawn ? withdrawnDate.format(fmt) : "";
        String notes = "Automated test stay notes - " + System.currentTimeMillis();

        waitAndClick(String.format("//a[contains(@href,'/stay/add/%s/') and normalize-space()='Add stay']", stayType), SelectorType.XPATH);
        waitForElementToBePresent("//form[@id='case-stay']");

        enterDateParts("requestDate", requestDate);
        enterDateParts("decisionDate", decisionDate);
        waitAndSelectValueFromDropDown("//select[@id='fields[outcome]']", SelectorType.XPATH, opts.outcome);
        if (opts.dvsaNotified) {
            waitAndClick("//input[@type='checkbox' and @id='fields[dvsaNotified]']", SelectorType.XPATH);
        }
        waitAndEnterText("//textarea[@id='fields[notes]']", SelectorType.XPATH, notes);
        if (opts.withdrawn) {
            waitAndClick("//input[@type='checkbox' and @id='fields[isWithdrawn]']", SelectorType.XPATH);
            enterDateParts("withdrawnDate", withdrawnDate);
        }

        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
        getHearingAppeal();
        return new String[]{requestDateStr, decisionDateStr, opts.outcome, notes, withdrawnDateStr};
    }
}