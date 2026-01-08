package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.joda.time.LocalDate;
import org.dvsa.testing.lib.url.utils.EnvironmentType;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransportManagerJourney extends BasePage {

    private World world;
    static int tmCount;

    Random random = new Random();
    int randomInt = random.nextInt(1000);


    public TransportManagerJourney(World world){
        this.world = world;
    }


    public void promptRemovalOfInternalTransportManager() {
        assertTrue(isTextPresent("Overview"));
        if (!isLinkPresent("Transport", 60) && isTextPresent("Granted")) {
            waitAndClickByLinkText(world.applicationDetails.getLicenceNumber());
            tmCount = returnTableRows("//*[@id='lva-transport-managers']/fieldset/div/div[2]/table/tbody/tr", SelectorType.XPATH);
        }
        waitAndClickByLinkText("Transport");
        assertTrue(isTextPresent("Transport Managers"));
        click("//button[contains(@name,'table[action][delete]')]", SelectorType.XPATH);
    }

    public void removeInternalTransportManager()  {
        promptRemovalOfInternalTransportManager();
        waitForTextToBePresent("Are you sure you want to remove this Transport Manager?");
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSubmit();
    }

    public void addTransportManagerDetails()  {
        //Add Personal Details
        waitForTitleToBePresent("Transport Manager details");
        String birthPlace = world.createApplication.getTransportManagerTown();
        String postCode = "NG1 6LP";

        HashMap<String, String> dob;
        dob = world.globalMethods.date.getDateHashMap(0, 0, -25);
        enterDateFieldsByPartialId("dob", dob);
        waitAndEnterText("birthPlace", SelectorType.ID, birthPlace);

        waitForElementToBeClickable("//*[contains(text(),'External')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'External')]", SelectorType.XPATH);
        findSelectAllRadioButtonsByValue("Y");

        //Add Home Address
        waitAndEnterText("postcodeInput1", SelectorType.ID, postCode);
        clickByName("homeAddress[searchPostcode][search]");
        waitAndSelectByIndex("//*[@id='selectAddress1']",SelectorType.XPATH, 1);

        //Add Work Address
        waitAndEnterText("postcodeInput2", SelectorType.ID, postCode);
        waitAndClick("workAddress[searchPostcode][search]", SelectorType.ID);
        waitAndSelectByIndex("//*[@id='selectAddress2']",SelectorType.XPATH, 1);

        //Hours Of Week
        waitForElementToBeClickable("//*[contains(@name,'responsibilities[hoursOfWeek][hoursPerWeekContent][hoursMon]')]", SelectorType.XPATH);
        enterTextIntoMultipleFields("//*[contains(@name,'responsibilities[hoursOfWeek][hoursPerWeekContent][hoursMon]')]", SelectorType.XPATH, "3");

        //Add Other Licences
        String role = "Transport Manager";
        waitAndClick("//*[contains(text(),'Add other licence')]", SelectorType.XPATH);
        refreshPageWithJavascript();
        waitAndEnterText("licNo", SelectorType.ID, "PB123456");
        selectValueFromDropDown("//*[@id='data[role]']", SelectorType.XPATH, role);
        waitAndEnterText("//*[@id='operatingCentres']", SelectorType.XPATH, "Test");
        waitAndEnterText("//*[@id='hoursPerWeek']", SelectorType.XPATH, "1");
        UniversalActions.clickSubmit();

        //Add Other Employment
        waitForTextToBePresent("Add other employment");
        waitAndClick("//*[contains(text(),'Add other employment')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='tm-employer-name-details[employerName]']", SelectorType.XPATH, "test");
        world.internalUIJourney.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
        waitAndEnterText("//*[@id='tm-employment-details[position]']", SelectorType.XPATH, "DVSA Tester");
        waitAndEnterText("//*[@id='tm-employment-details[hoursPerWeek]']", SelectorType.XPATH, "Testing Monday Tuesday Wednesday");
        if(isElementPresent("understoodAvailabilityAgreement", SelectorType.ID)) {
            clickById("understoodAvailabilityAgreement");
        }
        UniversalActions.clickSubmit();


        // Convictions
        waitForTextToBePresent("Add convictions and penalties");
        waitAndClick("//*[contains(text(),'Add convictions and penalties')]", SelectorType.XPATH);
        HashMap<String, String> date = new Dates(LocalDate::new).getDateHashMap(0, 0, -7);
        enterDateFieldsByPartialId("conviction-date", date);
        waitAndEnterText("//*[@id='category-text']", SelectorType.XPATH, "Test");
        waitAndEnterText("//*[@id='notes']", SelectorType.XPATH, "Test");
        waitAndEnterText("//*[@id='court-fpn']", SelectorType.XPATH, "Test");
        waitAndEnterText("//*[@id='penalty']", SelectorType.XPATH, "Test");
        UniversalActions.clickSubmit();

        waitForTextToBePresent("Add licences");
        waitAndClick("//*[contains(text(),'Add licences')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='lic-no']", SelectorType.XPATH, "PD263849");
        waitAndEnterText("//*[@id='holderName']", SelectorType.XPATH, "PD263849");
        UniversalActions.clickSubmit();
        waitForTitleToBePresent("Transport Manager details");
        UniversalActions.clickSubmit();
    } // Look where this should be used. It's good code so it'll be a waste. Definitely remember it being part of a TM journey.s

    public void addOperatorUserAsTransportManager(HashMap<String, String> dob, boolean applicationOrNot) {
        String user = String.format("%s %s", world.DataGenerator.getOperatorForeName(), world.DataGenerator.getOperatorFamilyName());
        nominateOperatorUserAsTransportManager(user, applicationOrNot);
        enterDateFieldsByPartialId("dob", dob);
        waitForElementToBeClickable("form-actions[send]", SelectorType.ID);
        UniversalActions.clickSend();
    }

    public void addOperatorAdminAsTransportManager() {
        String user = String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName());
        nominateOperatorUserAsTransportManager(user, true);
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    public void nominateOperatorUserAsTransportManager(String user, boolean applicationOrNot) {
        if (applicationOrNot) {
            UniversalActions.clickHome();
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.TRANSPORT_MANAGERS);
        } else {
            UniversalActions.clickHome();
            world.selfServeNavigation.navigateToPage("licence", SelfServeSection.TRANSPORT_MANAGERS);
            world.selfServeUIJourney.changeLicenceForVariation();
        }
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, user);
        UniversalActions.clickContinue();
    }

    public void addAndCompleteOperatorUserAsTransportManager(String isOwner, boolean applicationOrNot) {
        HashMap<String, String> dob = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        addOperatorUserAsTransportManager(dob, applicationOrNot);
        world.globalMethods.navigateToLoginWithoutCookies(world.DataGenerator.getOperatorUser(),world.DataGenerator.getOperatorUserEmail(), ApplicationType.EXTERNAL);
        if (applicationOrNot) {
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.TRANSPORT_MANAGERS);
        } else {
            world.selfServeNavigation.navigateToPage("variation", SelfServeSection.TRANSPORT_MANAGERS);
        }
        waitAndClickByLinkText(world.DataGenerator.getOperatorForeName() + " " + world.DataGenerator.getOperatorFamilyName());
        updateTMDetailsAndNavigateToDeclarationsPage(isOwner, "N", "N", "N", "N");
    }

    public void updateTMDetailsAndNavigateToDeclarationsPage(String isOwner, String OtherLicence, String hasEmployment, String hasConvictions, String hasPreviousLicences) {
        String hours = "8";
        waitForTextToBePresent("Transport Manager details ");
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
        waitAndClick("//*[@id=\"lva-transport-manager-details\"]/div/fieldset[2]/fieldset/div[3]/p[2]/a",SelectorType.XPATH);
        waitAndEnterText("homeAddress[addressLine1]", SelectorType.NAME, "1 Test Street");
        waitAndEnterText("homeAddress[town]", SelectorType.NAME, "Nottingham");
        waitAndEnterText("homeAddress[postcode]", SelectorType.NAME, "NG2 3HX");
        waitAndClick("//*[@id=\"lva-transport-manager-details\"]/div/fieldset[3]/fieldset/div[3]/p[2]/a",SelectorType.XPATH);
        waitAndEnterText("workAddress[addressLine1]", SelectorType.NAME, "2 Test Street");
        waitAndEnterText("workAddress[town]", SelectorType.NAME, "Leeds");
        waitAndEnterText("workAddress[postcode]", SelectorType.NAME, "LS9 6NF");
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursMon]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursTue]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursWed]", SelectorType.ID, hours);
        waitAndEnterText("responsibilities[hoursOfWeek][hoursPerWeekContent][hoursThu]", SelectorType.ID, hours);
        selectValueFromDropDown("homeAddress[countryCode]", SelectorType.ID, "United Kingdom");
        selectValueFromDropDown("workAddress[countryCode]", SelectorType.ID, "United Kingdom");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Check your answers");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Declaration");
    }

    public void submitTMApplicationAndNavigateToTMLandingPage() {
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
        UniversalActions.clickSubmit();
        waitAndClickByLinkText("Back to Transport");
        waitForTextToBePresent("Transport Managers");
    }

    public void addNewPersonAsTransportManager(String licenceType) {
        if (world.configuration.env.equals(EnvironmentType.PREPRODUCTION)) {
            waitForTextToBePresent("Transport Managers");
            waitAndClick("//button[@id='add']", SelectorType.XPATH);
            waitAndClick("addUser", SelectorType.ID);
            waitAndEnterText("forename", SelectorType.ID, "prep-forename");
            waitAndEnterText("familyName", SelectorType.ID, "prep-familyname");
            LinkedHashMap<String, String> dob = world.globalMethods.date.getDateHashMap(0, 0, -20);
            enterDateFieldsByPartialId("dob", dob);
            waitAndEnterText("username", SelectorType.ID, "prepusername" + System.currentTimeMillis());
            waitAndEnterText("emailAddress", SelectorType.ID, "prep-email@example.com");
            waitAndEnterText("emailConfirm", SelectorType.ID, "prep-email@example.com");
        } else {
            world.selfServeNavigation.navigateToPage(licenceType, SelfServeSection.TRANSPORT_MANAGERS);
            click("add", SelectorType.ID);
            waitAndClick("addUser", SelectorType.ID);
            waitAndEnterText("forename", SelectorType.ID, world.DataGenerator.getOperatorForeName());
            waitAndEnterText("familyName", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
            LinkedHashMap<String, String> dob = world.globalMethods.date.getDateHashMap(0, 0, -20);
            enterDateFieldsByPartialId("dob", dob);
            waitAndEnterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
            waitAndEnterText("emailAddress", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
            waitAndEnterText("emailConfirm", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        }
        UniversalActions.clickContinue();
        waitForTextToBePresent("user account has been created and a link sent to them");
    }

    public void addNewTmPrepTest() {
        waitForElementToBeClickable("//*[contains(text(),'change your licence')]", SelectorType.XPATH);
        waitAndClickByLinkText("change your licence");
        UniversalActions.clickSubmit();
        addNewPersonAsTransportManager(null);
    }

    public void assertTMDetailsWithOperator() {
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'With operator')]", SelectorType.XPATH));
        assertTrue(isLinkPresent("View details", 10));
    }

    public void assertTMDetailsIncomplete() {
        assertTrue(isElementPresent("//strong[contains(text(),'Incomplete')]", SelectorType.XPATH));
        assertTrue(isLinkPresent("Provide details", 10));
    }

    public void submitTMApplicationPrintAndSign(){
        addTransportManagerDetails();
        waitForTitleToBePresent("Check your answers");
        UniversalActions.clickSubmit();
        waitForTitleToBePresent("Declaration");
        waitAndClick("//*[contains(text(),'Print')]",SelectorType.XPATH);
        UniversalActions.clickSubmit();
        waitAndClickByLinkText("Back to Transport Managers");
        waitForTitleToBePresent("Transport Managers");
        UniversalActions.clickSaveAndContinue();
    }
}
