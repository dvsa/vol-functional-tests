package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.withDrawApplication;

public class SubmitApplicationJourney extends BasePage {

    private final World world;
    private String applicationNumber;
    private String licence;

    List<WebElement> applications;

    public SubmitApplicationJourney(World world) {
        this.world = world;
    }

    public void cancelAndWithdrawExistingApplications() {
        if (isElementPresent("//*/div[5]/table", SelectorType.XPATH)) {
            applications = findElements("//*/div[5]/table/tbody/tr/td/a", SelectorType.XPATH);
            assert applications != null;
            int attempts = 0;
            while (attempts < 100) {
                for (WebElement apps : applications) {
                    applications = findElements("//*/div[5]/table/tbody/tr/td/a", SelectorType.XPATH);
                    try {
                        if (isTextPresent("Not Yet Submitted")) {
                            apps.click();
                            waitAndClick("Cancel application", SelectorType.LINKTEXT);
                            world.UIJourney.clickSubmit();
                        }
                        if (isTextPresent("Under Consideration")) {
                            apps.click();
                            waitAndClick("Withdraw application", SelectorType.LINKTEXT);
                            world.UIJourney.clickSubmit();
                        }
                        if (isTextPresent("Awaiting grant fee")) {
                            apps.click();
                            String[] licenceNumber = getText("//*[@class='page-header__subtitle page-header__h3']", SelectorType.XPATH).split("/");
                            world.internalNavigation.loginIntoInternal();
                            world.internalSearchJourney.internalSearchUntilTextPresent(SearchType.Application, licenceNumber[0].trim(), licenceNumber[0].trim());
                            withDrawApplication();
                            String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
                            DriverUtils.get(myURL);
                            refreshPageWithJavascript();
                        }
                    } catch (StaleElementReferenceException | HttpException e) {
                        e.fillInStackTrace();
                    }
                    attempts++;
                }
                if (applications.size() == 0) {
                    break;
                }
                waitForTitleToBePresent("Licences");
                refreshPageWithJavascript();
            }
        }
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
        selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
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