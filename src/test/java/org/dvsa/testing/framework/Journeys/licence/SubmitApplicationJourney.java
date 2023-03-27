package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class SubmitApplicationJourney extends BasePage {

    private final World world;
    private String applicationNumber;
    private String licence;

    public SubmitApplicationJourney(World world) {
        this.world = world;
    }

    public void cancelAndWithdrawExistingApplications() throws InterruptedException {
        List<WebElement> applications;
        List<WebElement> applicationsForNew;

        if (isElementPresent("//*/div[5]/table", SelectorType.XPATH) || isElementPresent("//*/div[7]/table", SelectorType.XPATH)) {
            applications = findElements("//*/div[5]/table/tbody/tr/td/a", SelectorType.XPATH);
            applicationsForNew = findElements("//*/div[7]/table/tbody/tr/td/a", SelectorType.XPATH);
            assert applications != null;
            assert applicationsForNew != null;
            for (int i = 0; i < applications.size(); i++) {
                applications.get(i).click();
                if (isTitlePresent("Apply for a new licence",3)) {
                    clickByLinkText("Cancel application");
                } else {
                    clickByLinkText("Withdraw application");
                }
                world.UIJourney.clickSubmit();
                refreshPageWithJavascript();
                applications = findElements("//*/div[5]/table/tbody/tr/td/a", SelectorType.XPATH);
                for (int j = 0; j < applicationsForNew.size(); j++) {
                    applicationsForNew.get(j).click();
                    if (isTitlePresent("Apply for a new licence",3)) {
                        clickByLinkText("Cancel application");
                    } else {
                        clickByLinkText("Withdraw application");
                    }
                    world.UIJourney.clickSubmit();
                    refreshPageWithJavascript();
                    applicationsForNew = findElements("//*/div[7]/table/tbody/tr/td/a", SelectorType.XPATH);
                }
            }
        }
        waitForTitleToBePresent("Licences");
    }

    public void startANewLicenceApplication(String licenceType) {
        setLicence(licenceType);
        waitForTitleToBePresent("Licences");
        waitAndClick("//*[contains(text(),'Apply for a new licence')]", SelectorType.XPATH);
        chooseLicenceType(licenceType);
        UIJourney.clickSaveAndContinue();
        //business details
        world.businessDetailsJourney.addBusinessDetails();
        if (isTitlePresent("Directors", 10) || isTitlePresent("Responsible people", 10)) {
            if (isElementPresent("add", SelectorType.ID)) {
                world.directorJourney.addDirectorWithNoFinancialHistoryConvictionsOrPenalties();
            }
            UIJourney.clickSaveAndContinue();
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
        waitAndSelectValueFromDropDown("//*[@id='trafficArea']", SelectorType.XPATH, "Wales");
        UIJourney.clickSaveAndContinue();

        waitForTitleToBePresent("Financial evidence");
        waitAndClick("//*[contains(text(),'Send documents')]", SelectorType.XPATH);
        UIJourney.clickSaveAndContinue();

        //transport manager
        clickById("add");
        selectValueFromDropDown("data[registeredUser]",SelectorType.ID,"Test Account");
        world.UIJourney.clickContinue();

        //transport manager details
        if (isTextPresent("An online form will now be sent to the following email address for the Transport Manager to complete.")) {
            world.UIJourney.clickSend();
        } else {
            world.transportManagerJourney.submitTMApplicationPrintAndSign();
        }
        //vehicleDetails
        boolean add = licenceType.equals("Goods");
        world.vehicleDetailsJourney.addAVehicle(add);
        if (licenceType.equals("Public")) {
            world.psvJourney.completeVehicleDeclarationsPage();
            waitAndClick("overview-item__safety", SelectorType.ID);
        }
        world.safetyComplianceJourney.addSafetyAndComplianceData();
        world.safetyInspectorJourney.addASafetyInspector();
        clickById("application[safetyConfirmation]");
        UIJourney.clickSaveAndContinue();
        //Financial History
        world.financialHistoryJourney.answerNoToAllQuestionsAndSubmit();
        //Licence details
        world.licenceDetailsJourney.answerNoToAllQuestionsAndSubmit();
        //Convictions
        world.convictionsAndPenaltiesJourney.answerNoToAllQuestionsAndSubmit();
    }

    public void submitAndPayForApplication() {
        waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        clickById("submitAndPay");
        world.UIJourney.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        waitForTitleToBePresent("Application overview");
        List<String> licenceApplicationArray = Arrays.asList(Browser.navigate().findElement(By.xpath("//h2")).getText().split("/"));
        setApplicationNumber(licenceApplicationArray.get(1));
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

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }
}