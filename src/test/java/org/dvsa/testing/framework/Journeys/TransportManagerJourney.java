package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static junit.framework.TestCase.assertTrue;

public class TransportManagerJourney extends BasePage {

    private World world;
    static int tmCount;
    private String externalTMUser;
    private String externalTMEmail;
    private String operatorUser;
    private String operatorUserEmail;
    private String operatorForeName;
    private String operatorFamilyName;

    public TransportManagerJourney(World world){
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

    public void promptRemovalOfInternalTransportManager() {
        assertTrue(isTextPresent("Overview", 60));
        if (!isLinkPresent("Transport", 60) && isTextPresent("Granted", 60)) {
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            tmCount = returnTableRows("//*[@id='lva-transport-managers']/fieldset/div/div[2]/table/tbody/tr", SelectorType.XPATH);
        }
        clickByLinkText("Transport");
        isTextPresent("TransPort Managers", 60);
        click("//*[@value='Remove']", SelectorType.XPATH);
    }

    public void removeInternalTransportManager()  {
        promptRemovalOfInternalTransportManager();
        waitForTitleToBePresent("Are you sure you want to remove this Transport Manager?");
        findSelectAllRadioButtonsByValue("Y");
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }

    public void addTransportManagerDetails()  {
        //Add Personal Details
        String birthPlace = world.createApplication.getTransportManagerTown();
        String postCode = world.createApplication.getTransportManagerPostCode();

        HashMap<String, String> dob;
        dob = world.globalMethods.date.getDateHashMap(0, 0, -25);
        replaceDateFieldsByPartialId("dob", dob);
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
        world.UIJourney.searchAndSelectAddress("postcodeInput1", postCode, 1);
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

    public void addOperatorUserAsTransportManager(HashMap<String, String> dob, boolean applicationOrNot) {
        String user = String.format("%s %s", getOperatorForeName(), getOperatorFamilyName());
        nominateOperatorUserAsTransportManager(user, applicationOrNot);
        replaceDateFieldsByPartialId("dob", dob);
        waitForElementToBeClickable("form-actions[send]", SelectorType.ID);
        click("form-actions[send]", SelectorType.ID);
    }

    public void addOperatorAdminAsTransportManager() throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException, InterruptedException {
        String user = String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName());
        nominateOperatorUserAsTransportManager(user, true);
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    public void nominateOperatorUserAsTransportManager(String user, boolean applicationOrNot) {
        if (applicationOrNot) {
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
        } else {
            world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
            world.UIJourney.changeLicenceForVariation();
        }
        waitForTitleToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, user);
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
    }

    public void addAndCompleteOperatorUserAsTransportManager(String isOwner, boolean applicationOrNot) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException, InterruptedException {
        HashMap<String, String> dob = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        addOperatorUserAsTransportManager(dob, applicationOrNot);
        world.selfServeNavigation.navigateToLogin(getOperatorUser(), getOperatorUserEmail());
        if (applicationOrNot) {
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
        } else {
            world.selfServeNavigation.navigateToPage("variation", "Transport Managers");
        }
        clickByLinkText(getOperatorForeName() + " " + getOperatorFamilyName());
        updateTMDetailsAndNavigateToDeclarationsPage(isOwner, "N", "N", "N", "N");
    }

    public void updateTMDetailsAndNavigateToDeclarationsPage(String isOwner, String OtherLicence, String hasEmployment, String hasConvictions, String hasPreviousLicences) throws IllegalBrowserException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException {
        String hours = "8";
        findElement("//*[@value='" + OtherLicence + "'][@name='responsibilities[otherLicencesFieldset][hasOtherLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + isOwner + "'][@name='responsibilities[isOwner]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasEmployment + "'][@name='otherEmployments[hasOtherEmployment]']", SelectorType.XPATH, 20).click();
        findElement("//*[@value='" + hasConvictions + "'][@name='previousHistory[hasConvictions]']", SelectorType.XPATH, 30).click();
        findElement("//*[@value='" + hasPreviousLicences + "'][@name='previousHistory[hasPreviousLicences]']", SelectorType.XPATH, 30).click();
        findElement("//*[@id='responsibilities']//*[contains(text(),'Internal')]", SelectorType.XPATH, 30).click();
        if (findElement("emailAddress", SelectorType.ID, 10).getAttribute("value").isEmpty()) {
            waitAndEnterText("emailAddress", SelectorType.ID, getOperatorUserEmail());
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

    public void submitTMApplicationAndNavigateToTMLandingPage() throws ElementDidNotAppearWithinSpecifiedTimeException, IllegalBrowserException, MalformedURLException {
        updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
        click("form-actions[submit]", SelectorType.ID);
        clickByLinkText("Back to Transport");
        waitForTextToBePresent("Transport Managers");
    }

    public void generateOperatorValues() {
        FakerUtils faker = new FakerUtils();
        setOperatorForeName(faker.generateFirstName());
        setOperatorFamilyName(faker.generateLastName());
        setOperatorUser(String.format("%s.%s%s",
                getOperatorForeName(),
                getOperatorFamilyName(), Int.random(1000, 9999))
        );
        setOperatorUserEmail(
                getOperatorUser().concat("@dvsaUser.com")
        );
    }

    public void generateAndAddOperatorUser() {
        world.TMJourney.generateOperatorValues();
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.UIJourney.addUser(
                world.TMJourney.getOperatorUser(),
                world.TMJourney.getOperatorForeName(),
                world.TMJourney.getOperatorFamilyName(),
                world.TMJourney.getOperatorUserEmail());
    }

    public void addNewPersonAsTransportManager(String licenceType) {
        world.selfServeNavigation.navigateToPage(licenceType, "Transport Managers");
        click("add", SelectorType.ID);
        waitAndClick("addUser", SelectorType.ID);
        enterText("forename", world.TMJourney.getOperatorForeName(), SelectorType.ID);
        enterText("familyName", world.TMJourney.getOperatorFamilyName(), SelectorType.ID);
        LinkedHashMap<String, String> dob = world.globalMethods.date.getDateHashMap(0, 0, -20);
        replaceDateFieldsByPartialId("dob", dob);
        enterText("username", world.TMJourney.getOperatorUser(), SelectorType.ID);
        enterText("emailAddress", world.TMJourney.getOperatorUserEmail(), SelectorType.ID);
        enterText("emailConfirm", world.TMJourney.getOperatorUserEmail(), SelectorType.ID);
        click("form-actions[continue]", SelectorType.ID);
        waitForTextToBePresent("user account has been created and a link sent to them");
    }

    public void assertTMDetailsWithOperator() throws IllegalBrowserException {
        Assert.assertTrue(isElementPresent("//span[contains(text(),'With operator')]",SelectorType.XPATH));
        Assert.assertTrue(isLinkPresent("View details", 10));
    }

    public void assertTMDetailsIncomplete() throws IllegalBrowserException {
        Assert.assertTrue(isElementPresent("//span[contains(text(),'Incomplete')]", SelectorType.XPATH));
        Assert.assertTrue(isLinkPresent("Provide details", 10));
    }
}
