package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.clickContinue;

public class SubmitApplicationJourney extends BasePage {
    public World world;
    private String licence;
    List<WebElement> applications;

    private static final Logger LOGGER = LogManager.getLogger(SubmitApplicationJourney.class);

    public SubmitApplicationJourney(World world) {
        this.world = world;
    }

    public void cancelAndWithdrawExistingApplications() {
        if (isElementPresent("//table", SelectorType.XPATH)) {
            applications = findElements("//table/tbody/tr", SelectorType.XPATH);
            int attempts = 0;

            while (attempts < applications.size()) {
                try {
                    //Need to repopulate list after removing values
                    applications = findElements("//table/tbody/tr", SelectorType.XPATH);
                    String applicationId = applications.get(applications.size() - 1).getText().split(" ")[0];

                    if (applications.get(applications.size() - 1).getText().contains("Not Yet Submitted")) {
                        waitAndClickByLinkText(applicationId);
                        waitAndClick("Cancel application", SelectorType.LINKTEXT);
                        UniversalActions.clickSubmit();
                    } else if (applications.get(applications.size() - 1).getText().contains("Under Consideration")) {
                        waitAndClickByLinkText(applicationId);
                        waitAndClick("Withdraw application", SelectorType.LINKTEXT);
                        UniversalActions.clickSubmit();
                    }
                } catch (StaleElementReferenceException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                if (applications.isEmpty()) {
                    break;
                }
                waitForTitleToBePresent("Licences");
                attempts++;
            }
        }
    }

    public void startANewLicenceApplication(String licenceType) throws IllegalBrowserException, IOException {
        setLicence(licenceType);
        waitForTitleToBePresent("Licences");
        waitAndClick("//*[contains(text(),'Apply for a new licence')]", SelectorType.XPATH);
        chooseLicenceType(licenceType);
        UniversalActions.clickSaveAndContinue();
        //business details
        world.businessDetailsJourney.addBusinessDetails(true);
        if (isTitlePresent("Directors", 2) || isTitlePresent("Responsible people", 2)) {
            if (isElementPresent("add", SelectorType.ID)) {
                world.directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties(true);
            }
            UniversalActions.clickSaveAndContinue();
        }
        //operating centre
        String authority = "2";
        String trailers = "4";
        if (licenceType.equals("Goods")) {
            //      world.createApplication.setOperatorType(OperatorType.GOODS.name());
            world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(authority, "0", trailers);
        } else {
            //     world.createApplication.setOperatorType(OperatorType.PUBLIC.name());
            world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(authority, "0", "0");
        }
        world.operatingCentreJourney.addNewOperatingCentre(authority, trailers);
        if (isElementPresent("trafficArea", SelectorType.ID)) {
            waitAndSelectValueFromDropDown("//*[@id='trafficArea']", SelectorType.XPATH, "Wales");
        }
        UniversalActions.clickSaveAndContinue();
        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Upload documents later')]", SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();

        //transport manager
        waitAndClick("add", SelectorType.ID);
        waitForTextToBePresent("Add Transport Manager");
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
        clickContinue();

        //transport manager details
        if (isTextPresent("An online form will now be sent to the following email address for the Transport Manager to complete.")) {
            UniversalActions.clickSend();
        } else {
            world.transportManagerJourney.submitTMApplicationPrintAndSign();
        }
        if(isTitlePresent("Transport Managers", 2)){
            UniversalActions.clickSaveAndContinue();
        }
        //vehicleDetails
        boolean vehicleType = licenceType.equals("Goods");
        world.vehicleDetailsJourney.addAVehicle(vehicleType);
        if (licenceType.equals("Public")) {
            world.psvJourney.completeVehicleDeclarationsPage();
            waitAndClick("overview-item__safety", SelectorType.ID);
        }
        world.safetyComplianceJourney.addSafetyAndComplianceData();
        world.safetyInspectorJourney.addASafetyInspector();
        clickAllCheckboxes();
        UniversalActions.clickSaveAndContinue();
        //Financial History
        world.financialHistoryJourney.answerNoToAllQuestionsAndSubmit("application");
        //Licence details
        world.licenceDetailsJourney.answerNoToAllQuestionsAndSubmit();
        //Convictions
        world.convictionsAndPenaltiesJourney.answerNoToAllQuestionsAndSubmit("application");
    }

    public void submitAndPayForApplication() {
        waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        clickById("submitAndPay");
        UniversalActions.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        waitForTitleToBePresent("Application overview");
    }

    private void chooseLicenceType(String licenceType) {
        waitForTitleToBePresent("Type of licence");
        String radioBtnDisabled = getAttribute("//*[@id='type-of-licence[operator-location]']", SelectorType.XPATH, "disabled");
        if (!"true".equals(radioBtnDisabled)) {
            waitAndClick("//*[contains(text(),'Great Britain')]", SelectorType.XPATH);
        }
        waitAndClick("//*[contains(text(),'" + licenceType + "')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Goods vehicles')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Standard National')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Save')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Business type')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Limited Company')]", SelectorType.XPATH);
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }
}
