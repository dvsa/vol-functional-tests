package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;

import java.net.MalformedURLException;
import java.util.HashMap;

import static activesupport.driver.Browser.navigate;
import static junit.framework.TestCase.assertTrue;

public class TransportManagerJourneySteps extends BasePage {

    private World world;
    static int tmCount;
    private String externalTMUser;
    private String externalTMEmail;
    private String operatorUser;
    private String operatorUserEmail;
    private String operatorForeName;
    private String operatorFamilyName;

    public TransportManagerJourneySteps(World world){
        this.world = world;
    }

    public String getExternalTMUser() { return externalTMUser; }

    public void setExternalTMUser(String externalTMUser) { this.externalTMUser = externalTMUser; }

    public String getExternalTMEmail() {
        return externalTMEmail;
    }

    public void setExternalTMEmail(String externalTMEmail) {
        this.externalTMEmail = externalTMEmail;
    }

    public String getOperatorForeName() {
        return operatorForeName;
    }

    public void setOperatorForeName(String operatorForeName) {
        this.operatorForeName = operatorForeName;
    }

    public String getOperatorFamilyName() {
        return operatorFamilyName;
    }

    public void setOperatorFamilyName(String operatorFamilyName) {
        this.operatorFamilyName = operatorFamilyName;
    }

    public String getOperatorUser() { return operatorUser; }

    public void setOperatorUser(String operatorUser) {
        this.operatorUser = operatorUser;
    }

    public String getOperatorUserEmail() {
        return operatorUserEmail;
    }

    public void setOperatorUserEmail(String operatorUserEmail) {
        this.operatorUserEmail = operatorUserEmail;
    }


    public void removeInternalTransportManager() throws IllegalBrowserException, MalformedURLException {
        assertTrue(isTextPresent("Overview", 60));
        if (!isLinkPresent("Transport", 60) && isTextPresent("Granted", 60)) {
            clickByLinkText(world.createLicence.getLicenceNumber());
            tmCount = returnTableRows("//*[@id='lva-transport-managers']/fieldset/div/div[2]/table/tbody/tr", SelectorType.XPATH);
        }
        clickByLinkText("Transport");
        isTextPresent("TransPort Managers", 60);
        click("//*[@value='Remove']", SelectorType.XPATH);
    }


    public void addNewPersonAsTransportManager(String forename, String familyName, String emailAddress) throws IllegalBrowserException, MalformedURLException {
        setExternalTMEmail(emailAddress);
        setExternalTMUser("TM".concat(Str.randomWord(3)));
        waitForTitleToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        waitAndClick("addUser", SelectorType.ID);
        enterText("forename", forename, SelectorType.ID);
        enterText("familyName", familyName, SelectorType.ID);

        HashMap<String, Integer> dates;
        dates = world.globalMethods.date.getDate(0, 0, 25);
        enterText("dob_day", dates.get("day").toString(), SelectorType.ID);
        enterText("dob_month", dates.get("month").toString(), SelectorType.ID);
        enterText("dob_year", dates.get("year").toString(), SelectorType.ID);

        enterText("username", getExternalTMUser(), SelectorType.ID);
        enterText("emailAddress", getExternalTMEmail(), SelectorType.ID);
        enterText("emailConfirm", getExternalTMEmail(), SelectorType.ID);
        waitAndClick("form-actions[continue]", SelectorType.ID);
    }

    public void addTransportManagerDetails() throws IllegalBrowserException, InterruptedException, MalformedURLException {
        //Add Personal Details
        String birthPlace = world.createLicence.getTown();
        String postCode = world.createLicence.getPostcode();

        HashMap<String, Integer> dates;
        dates = world.globalMethods.date.getDate(0, 0, -25);

        enterText("dob_day", dates.get("day").toString(), SelectorType.ID);
        enterText("dob_month", dates.get("month").toString(), SelectorType.ID);
        enterText("dob_year", dates.get("year").toString(), SelectorType.ID);
        enterText("birthPlace", birthPlace, SelectorType.ID);

        waitForElementToBeClickable("//*[contains(text(),'External')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'External')]", SelectorType.XPATH);
        world.genericUtils.findSelectAllRadioButtonsByValue("Y");

        //Add Home Address
        enterText("postcodeInput1", postCode, SelectorType.ID);
        clickByName("homeAddress[searchPostcode][search]");
        waitAndClick("homeAddress[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("homeAddress[searchPostcode][addresses]", SelectorType.ID, 1);

        //Add Work Address
        waitAndEnterText("postcodeInput2", SelectorType.ID, postCode);
        waitAndClick("workAddress[searchPostcode][search]", SelectorType.ID);
        waitAndClick("workAddress[searchPostcode][addresses]", SelectorType.ID);
        selectValueFromDropDownByIndex("workAddress[searchPostcode][addresses]", SelectorType.ID, 1);

        //Hours Of Week
        waitForElementToBeClickable("//*[contains(@name,'responsibilities[hoursOfWeek]')]", SelectorType.XPATH);
        enterSameTextIntoMultipleFieldsPartialMatch("//*[contains(@name,'responsibilities[hoursOfWeek]')]", SelectorType.XPATH, "3");

        //Add Other Licences
        String role = "Transport Manager";
        waitAndClick("//*[contains(text(),'Add other licence')]", SelectorType.XPATH);
        javaScriptExecutor("location.reload(true)");
        waitAndEnterText("licNo", SelectorType.ID, "PB123456");
        selectValueFromDropDown("//*[@id='data[role]']", SelectorType.XPATH, role);
        enterText("//*[@id='operatingCentres']", "Test", SelectorType.XPATH);
        enterText("//*[@id='hoursPerWeek']", "1", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        //Add Other Employment
        waitForTextToBePresent("Add other employment");
        waitAndClick("//*[contains(text(),'Add other employment')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='tm-employer-name-details[employerName]']", SelectorType.XPATH, "test");
        world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", postCode, 1);
        waitAndEnterText("//*[@id='tm-employment-details[position]']", SelectorType.XPATH, "test");
        waitAndEnterText("//*[@id='tm-employment-details[hoursPerWeek]']", SelectorType.XPATH, "Test");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);


        // Convictions
        waitForTextToBePresent("Add convictions and penalties");
        waitAndClick("//*[contains(text(),'Add convictions and penalties')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='conviction-date_day']", SelectorType.XPATH, "03");
        enterText("//*[@id='conviction-date_month']", "03", SelectorType.XPATH);
        enterText("//*[@id='conviction-date_year']", "2014", SelectorType.XPATH);
        enterText("//*[@id='category-text']", "Test", SelectorType.XPATH);
        enterText("//*[@id='notes']", "Test", SelectorType.XPATH);
        enterText("//*[@id='court-fpn']", "Test", SelectorType.XPATH);
        enterText("//*[@id='penalty']", "Test", SelectorType.XPATH);
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);

        waitForTextToBePresent("Add licences");
        waitAndClick("//*[contains(text(),'Add licences')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='lic-no']", SelectorType.XPATH, "PD263849");
        waitAndEnterText("//*[@id='holderName']", SelectorType.XPATH, "PD263849");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    } // Look where this should be used. It's good code so it'll be a waste. Definitely remember it being part of a TM journey.s

    public void nominateOperatorUserAsTransportManager(String user, HashMap<String, Integer> dob, boolean applicationOrNot) throws IllegalBrowserException, MalformedURLException, InterruptedException {
        if (applicationOrNot) {
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
        } else {
            world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
            world.UIJourneySteps.changeLicenceForVariation(); // If licence already created then this creates variational
        }
        waitForTitleToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);

        replaceDateById("dob", dob);

        waitForElementToBeClickable("form-actions[send]", SelectorType.ID);
        click("form-actions[send]", SelectorType.ID);
    }

    public void addOperatorAdminAsTransportManager(String user) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException {
        world.selfServeNavigation.navigateToPage("application", "Transport Managers");
        click("//*[@name='table[action]']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    public void updateTMDetailsAndNavigateToDeclarationsPage(String isOwner, String OtherLicence, String hasEmployment, String hasConvictions, String hasPreviousLicences) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException {
        String tmEmailAddress = "externalTM@vol.com";
        String hours = "8";
        findElement("//*[@value='" + OtherLicence + "'][@name='responsibilities[otherLicencesFieldset][hasOtherLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + isOwner + "'][@name='responsibilities[isOwner]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasEmployment + "'][@name='otherEmployments[hasOtherEmployment]']", SelectorType.XPATH, 20).click();
        findElement("//*[@value='" + hasConvictions + "'][@name='previousHistory[hasConvictions]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasPreviousLicences + "'][@name='previousHistory[hasPreviousLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@id='responsibilities']//*[contains(text(),'Internal')]", SelectorType.XPATH, 30).click();
        findElement("emailAddress", SelectorType.ID, 10).clear();
        if (findElement("emailAddress", SelectorType.ID, 10).getText().isEmpty()) {
            waitAndEnterText("emailAddress", SelectorType.ID, tmEmailAddress);
        }
        waitAndEnterText("birthPlace", SelectorType.ID, "Nottingham");
        waitAndEnterText("postcodeInput1", SelectorType.ID, "NG23HX");
        clickByName("homeAddress[searchPostcode][search]");
        waitForElementToBeClickable("//*[@name='homeAddress[searchPostcode][search]']", SelectorType.XPATH);
        untilElementPresent("//*[@name='homeAddress[searchPostcode][addresses]']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("//*[@name='homeAddress[searchPostcode][addresses]']", SelectorType.XPATH, 1);
        waitAndEnterText("postcodeInput2", SelectorType.ID, "NG23HX");
        waitAndClick("//*[@id='workAddress[searchPostcode][search]']", SelectorType.XPATH);
        waitForElementToBeClickable("//*[@id='workAddress[searchPostcode][search]']", SelectorType.XPATH);
        untilElementPresent("//*[@name='workAddress[searchPostcode][addresses]']", SelectorType.XPATH);
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

    public void addOperatorUserAsTransportManager(String user, String isOwner, boolean applicationOrNot) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException, InterruptedException {
        HashMap<String, Integer> dob = world.globalMethods.date.getDate(-5, 0, -20);
        nominateOperatorUserAsTransportManager(user, dob, applicationOrNot);
        world.selfServeNavigation.navigateToLogin(getOperatorUser(), getOperatorUserEmail());
        if (applicationOrNot) {
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
        } else {
            world.selfServeNavigation.navigateToPage("variation", "Transport Managers");
        }
        clickByLinkText(getOperatorForeName() + " " + getOperatorFamilyName());
        updateTMDetailsAndNavigateToDeclarationsPage(isOwner, "N", "N", "N", "N");
    }

    public void submitTMApplicationAndNavigateToTMLandingPage() throws ElementDidNotAppearWithinSpecifiedTimeException, IllegalBrowserException, MalformedURLException {
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
        click("form-actions[submit]", SelectorType.ID);
        clickByLinkText("Back to Transport");
        waitForTextToBePresent("Transport Managers");
    }

}
