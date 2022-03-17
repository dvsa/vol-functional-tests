package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.aws.s3.SecretsManager;
import activesupport.driver.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.WebElement;
import scanner.AXEScanner;
import scanner.ReportGenerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class SubmitSelfServeApplication extends BasePage {

    World world;
    static AXEScanner scanner = new AXEScanner();
    static ReportGenerator reportGenerator = new ReportGenerator();
    private static final Logger LOGGER = LogManager.getLogger(ManagerUsersPage.class);

    public SubmitSelfServeApplication(World world) {
        this.world = world;
    }

    @And("i start a new {string} licence application")
    public void iStartANewLicenceApplication(String licenceType){
        waitForTitleToBePresent("Licences");
        waitAndClick("//*[contains(text(),'Apply for a new licence')]", SelectorType.XPATH);
        chooseLicenceType(licenceType);
        String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        //business details
        world.businessDetailsJourney.addBusinessDetails();
        if (isTitlePresent("Directors", 10) || isTitlePresent("Responsible people", 10)) {
            if (isTextPresent("You haven't added any Directors yet")) {
                world.directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
            }
            waitAndClick(saveAndContinue, SelectorType.XPATH);
        }
        //operating centre
        String authority = "2";
        String trailers = "4";
        if(licenceType.equals("Goods")) {
            world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(authority, "0", trailers);
        }else{
            world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(authority,"0","0");
        }
        world.operatingCentreJourney.addNewOperatingCentre(authority, trailers);
        waitAndSelectByIndex("//*[@id='trafficArea']", SelectorType.XPATH, 1);
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Send documents')]", SelectorType.XPATH);
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        //transport manager
        clickById("add");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
        clickById("form-actions[continue]");

        //transport manager details
        if (isTextPresent("An online form will now be sent to the following email address for the Transport Manager to complete.")) {
            clickByName("form-actions[send]");
        } else {
            world.transportManagerJourney.submitTMApplicationPrintAndSign();
        }
        //vehicleDetails
        boolean add = licenceType.equals("Goods");
        world.vehicleDetailsJourney.addAVehicle(add);
        world.safetyComplianceJourney.addSafetyAndComplianceData();
        world.safetyInspectorJourney.addASafetyInspector();
        clickById("application[safetyConfirmation]");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        //Financial History
        world.financialHistoryJourney.answerNoToAllQuestionsAndSubmit();
        //Licence details
        world.licenceDetailsJourney.answerNoToAllQuestionsAndSubmit();
        //Convictions
        world.convictionsAndPenaltiesJourney.answerNoToAllQuestionsAndSubmit();
    }

    @Given("i have a self serve account")
    public void iHaveASelfServeAccount() {
        String intUsername = world.configuration.config.getString("intUsername");
        String secretKey = world.configuration.config.getString("secretKey");
        String region = world.configuration.config.getString("region");

        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            SecretsManager secretsManager = new SecretsManager();
            secretsManager.setRegion(region);
            String intPassword = secretsManager.getSecretValue(secretKey);
            String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
            DriverUtils.get(myURL);
            world.globalMethods.signIn(intUsername, intPassword);
        } else {
            world.userRegistrationJourney.registerUserWithNoLicence();
            world.globalMethods.navigateToLoginWithoutCookies(world.DataGenerator.getOperatorUser(), world.DataGenerator.getOperatorUserEmail(), ApplicationType.EXTERNAL);
        }
    }

    @And("i have no existing accounts")
    public void iHaveNoExistingAccounts() throws IllegalBrowserException, IOException {
        if (isElementPresent("//tbody/tr/td/a", SelectorType.XPATH)) {
            List<WebElement> applications = findElements("//tbody/tr/td/a", SelectorType.XPATH);
            for (WebElement element : applications) {
                element.click();
                if (isTitlePresent("Application overview", 60)) {
                    clickByLinkText("Withdraw application");

                } else {
                    clickByLinkText("Cancel application");
                }
                waitAndClick("form-actions[submit]", SelectorType.NAME);
            }
            waitForTitleToBePresent("Licences");
        }
    }

    public static void accessibilityScanner() throws IllegalBrowserException, IOException {
        scanner.scan(false);
        if (scanner.getTotalViolationsCount() != 0) {
            LOGGER.info("ERROR: Violation found");
            reportGenerator.urlScannedReportSection(Browser.navigate().getCurrentUrl());
            reportGenerator.violationsReportSectionHTML(Browser.navigate().getCurrentUrl(), scanner);
        }
    }

    public static void generateAccessibilityReport() {
        reportGenerator.createReport(scanner);
    }

    private void chooseLicenceType(String licenceType) {
        waitForTitleToBePresent("Type of licence");
        waitAndClick("//*[contains(text(),'Great Britain')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'" + licenceType + "')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Standard National')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Save')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Business type')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Limited Company')]", SelectorType.XPATH);
    }
}