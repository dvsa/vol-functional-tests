package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.joda.time.LocalDate;
import org.junit.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication.accessibilityScanner;

public class TransportManagerJourney extends BasePage {

    private World world;
    static int tmCount;

    public TransportManagerJourney(World world){
        this.world = world;
    }


    public void promptRemovalOfInternalTransportManager() {
        assertTrue(isTextPresent("Overview"));
        if (!isLinkPresent("Transport", 60) && isTextPresent("Granted")) {
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            tmCount = returnTableRows("//*[@id='lva-transport-managers']/fieldset/div/div[2]/table/tbody/tr", SelectorType.XPATH);
        }
        clickByLinkText("Transport");
        assertTrue(isTextPresent("Transport Managers"));
        click("//*[@value='Remove']", SelectorType.XPATH);
    }

    public void removeInternalTransportManager()  {
        promptRemovalOfInternalTransportManager();
        waitForTitleToBePresent("Are you sure you want to remove this Transport Manager?");
        findSelectAllRadioButtonsByValue("Y");
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }

    public void addTransportManagerDetails()  {
        try {
            accessibilityScanner();
        } catch (IllegalBrowserException | IOException e) {
            e.printStackTrace();
        }
        //Add Personal Details
        String birthPlace = world.createApplication.getTransportManagerTown();
        String postCode = "NG1 6LP";

        HashMap<String, String> dob;
        dob = world.globalMethods.date.getDateHashMap(0, 0, -25);
        enterDateFieldsByPartialId("dob", dob);
        enterText("birthPlace", SelectorType.ID, birthPlace);

        waitForElementToBeClickable("//*[contains(text(),'External')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'External')]", SelectorType.XPATH);
        findSelectAllRadioButtonsByValue("Y");

        //Add Home Address
        enterText("postcodeInput1", SelectorType.ID, postCode);
        clickByName("homeAddress[searchPostcode][search]");
        waitAndSelectByIndex("Select an address","//*[@id='selectAddress1']",SelectorType.XPATH, 1);

        //Add Work Address
        waitAndEnterText("postcodeInput2", SelectorType.ID, postCode);
        waitAndClick("workAddress[searchPostcode][search]", SelectorType.ID);
        waitAndSelectByIndex("Select an address","//*[@id='selectAddress2']",SelectorType.XPATH, 1);

        //Hours Of Week
        waitForElementToBeClickable("//*[contains(@name,'responsibilities[hoursOfWeek]')]", SelectorType.XPATH);
        enterTextIntoMultipleFields("//*[contains(@name,'responsibilities[hoursOfWeek]')]", SelectorType.XPATH, "3");

        //Add Other Licences
        String role = "Transport Manager";
        waitAndClick("//*[contains(text(),'Add other licence')]", SelectorType.XPATH);
        refreshPageWithJavascript();
        waitAndEnterText("licNo", SelectorType.ID, "PB123456");
        selectValueFromDropDown("//*[@id='data[role]']", SelectorType.XPATH, role);
        enterText("//*[@id='operatingCentres']", SelectorType.XPATH, "Test");
        enterText("//*[@id='hoursPerWeek']", SelectorType.XPATH, "1");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        //Add Other Employment
        waitForTextToBePresent("Add other employment");
        waitAndClick("//*[contains(text(),'Add other employment')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='tm-employer-name-details[employerName]']", SelectorType.XPATH, "test");
        world.UIJourney.searchAndSelectAddress("postcodeInput1", postCode, 1);
        waitAndEnterText("//*[@id='tm-employment-details[position]']", SelectorType.XPATH, "test");
        waitAndEnterText("//*[@id='tm-employment-details[hoursPerWeek]']", SelectorType.XPATH, "Test");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);


        // Convictions
        waitForTextToBePresent("Add convictions and penalties");
        waitAndClick("//*[contains(text(),'Add convictions and penalties')]", SelectorType.XPATH);
        HashMap<String, String> date = new Dates(LocalDate::new).getDateHashMap(0, 0, -7);
        enterDateFieldsByPartialId("conviction-date", date);
        enterText("//*[@id='category-text']", SelectorType.XPATH, "Test");
        enterText("//*[@id='notes']", SelectorType.XPATH, "Test");
        enterText("//*[@id='court-fpn']", SelectorType.XPATH, "Test");
        enterText("//*[@id='penalty']", SelectorType.XPATH, "Test");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        waitForTextToBePresent("Add licences");
        waitAndClick("//*[contains(text(),'Add licences')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='lic-no']", SelectorType.XPATH, "PD263849");
        waitAndEnterText("//*[@id='holderName']", SelectorType.XPATH, "PD263849");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        waitAndClick("form-actions[submit]",SelectorType.ID);
    } // Look where this should be used. It's good code so it'll be a waste. Definitely remember it being part of a TM journey.s

    public void addOperatorUserAsTransportManager(HashMap<String, String> dob, boolean applicationOrNot) {
        String user = String.format("%s %s", world.DataGenerator.getOperatorForeName(), world.DataGenerator.getOperatorFamilyName());
        nominateOperatorUserAsTransportManager(user, applicationOrNot);
        enterDateFieldsByPartialId("dob", dob);
        waitForElementToBeClickable("form-actions[send]", SelectorType.ID);
        click("form-actions[send]", SelectorType.ID);
    }

    public void addOperatorAdminAsTransportManager() {
        String user = String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName());
        nominateOperatorUserAsTransportManager(user, true);
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    public void nominateOperatorUserAsTransportManager(String user, boolean applicationOrNot) {
        if (applicationOrNot) {
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.TRANSPORT_MANAGERS);
        } else {
            world.selfServeNavigation.navigateToPage("licence", SelfServeSection.TRANSPORT_MANAGERS);
            world.UIJourney.changeLicenceForVariation();
        }
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
    }

    public void addAndCompleteOperatorUserAsTransportManager(String isOwner, boolean applicationOrNot) {
        HashMap<String, String> dob = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        addOperatorUserAsTransportManager(dob, applicationOrNot);
        world.selfServeNavigation.navigateToLogin(world.DataGenerator.getOperatorUser(), world.DataGenerator.getOperatorUserEmail());
        if (applicationOrNot) {
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.TRANSPORT_MANAGERS);
        } else {
            world.selfServeNavigation.navigateToPage("variation", SelfServeSection.TRANSPORT_MANAGERS);
        }
        clickByLinkText(world.DataGenerator.getOperatorForeName() + " " + world.DataGenerator.getOperatorFamilyName());
        updateTMDetailsAndNavigateToDeclarationsPage(isOwner, "N", "N", "N", "N");
    }

    public void updateTMDetailsAndNavigateToDeclarationsPage(String isOwner, String OtherLicence, String hasEmployment, String hasConvictions, String hasPreviousLicences) {
        String hours = "8";
        findElement("//*[@value='Y'][@name='details[hasUndertakenTraining]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + OtherLicence + "'][@name='responsibilities[otherLicencesFieldset][hasOtherLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + isOwner + "'][@name='responsibilities[isOwner]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasEmployment + "'][@name='otherEmployments[hasOtherEmployment]']", SelectorType.XPATH, 20).click();
        findElement("//*[@value='" + hasConvictions + "'][@name='previousHistory[hasConvictions]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasPreviousLicences + "'][@name='previousHistory[hasPreviousLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@id='responsibilities']//*[contains(text(),'Internal')]", SelectorType.XPATH, 30).click();
        if (findElement("emailAddress", SelectorType.ID, 10).getAttribute("value").isEmpty()) {
            waitAndEnterText("emailAddress", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        }
        waitAndEnterText("birthPlace", SelectorType.ID, "Nottingham");
        waitAndEnterText("postcodeInput1", SelectorType.ID, "NG23HX");
        clickByName("homeAddress[searchPostcode][search]");
        waitForElementToBeClickable("//*[@name='homeAddress[searchPostcode][search]']", SelectorType.XPATH);
        waitForElementToBePresent("//*[@name='homeAddress[searchPostcode][addresses]']");
        selectValueFromDropDownByIndex("//*[@name='homeAddress[searchPostcode][addresses]']", SelectorType.XPATH, 1);
        waitAndEnterText("postcodeInput2", SelectorType.ID, "NG23HX");
        waitAndClick("//*[@id='workAddress[searchPostcode][search]']", SelectorType.XPATH);
        waitForElementToBeClickable("//*[@id='workAddress[searchPostcode][search]']", SelectorType.XPATH);
        waitForElementToBePresent("//*[@name='workAddress[searchPostcode][addresses]']");
        selectValueFromDropDownByIndex("//*[@name='workAddress[searchPostcode][addresses]']", SelectorType.XPATH, 1);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursMon]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursTue]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursWed]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursThu]", SelectorType.ID, hours);
        click("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Check your answers");
        click("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Declaration");
    }

    public void submitTMApplicationAndNavigateToTMLandingPage() {
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
        click("form-actions[submit]", SelectorType.ID);
        clickByLinkText("Back to Transport");
        waitForTextToBePresent("Transport Managers");
    }

    public void addNewPersonAsTransportManager(String licenceType) {
        world.selfServeNavigation.navigateToPage(licenceType, SelfServeSection.TRANSPORT_MANAGERS);
        click("add", SelectorType.ID);
        waitAndClick("addUser", SelectorType.ID);
        enterText("forename", SelectorType.ID, world.DataGenerator.getOperatorForeName());
        enterText("familyName", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
        LinkedHashMap<String, String> dob = world.globalMethods.date.getDateHashMap(0, 0, -20);
        enterDateFieldsByPartialId("dob", dob);
        enterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
        enterText("emailAddress", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        enterText("emailConfirm", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        click("form-actions[continue]", SelectorType.ID);
        waitForTextToBePresent("user account has been created and a link sent to them");
    }

    public void assertTMDetailsWithOperator() {
        Assert.assertTrue(isElementPresent("//span[contains(text(),'With operator')]",SelectorType.XPATH));
        Assert.assertTrue(isLinkPresent("View details", 10));
    }

    public void assertTMDetailsIncomplete() {
        Assert.assertTrue(isElementPresent("//span[contains(text(),'Incomplete')]", SelectorType.XPATH));
        Assert.assertTrue(isLinkPresent("Provide details", 10));
    }

    public void submitTMApplicationAndSignWithVerify(){
        addTransportManagerDetails();
        waitForTitleToBePresent("Check your answers");
        waitAndClick("form-actions[submit]",SelectorType.ID);
        waitForTitleToBePresent("Declaration");
        waitAndClick("form-actions[submit]",SelectorType.ID);
        world.UIJourney.signWithVerify();
        waitAndClick("//*[contains(text(),'Finish')]",SelectorType.XPATH);
    }

    public void submitTMApplicationPrintAndSign(){
        addTransportManagerDetails();
        waitForTitleToBePresent("Check your answers");
        waitAndClick("form-actions[submit]",SelectorType.ID);
        waitForTitleToBePresent("Declaration");
        waitAndClick("//*[contains(text(),'Print')]",SelectorType.XPATH);
        waitAndClick("form-actions[submit]",SelectorType.ID);
        clickByLinkText("Back to Transport Managers");
        waitForTitleToBePresent("Transport Managers");
        waitAndClick("form-actions[saveAndContinue]",SelectorType.ID);
    }
}