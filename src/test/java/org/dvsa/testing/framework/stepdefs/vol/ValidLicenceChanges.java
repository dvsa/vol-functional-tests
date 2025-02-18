package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ValidLicenceChanges extends BasePage {
    private final World world;
    String tradingName = "test trading name";
    String tradingName2 = "test trading name 2";
    String natureOfBusiness = "test nature of business";
    String companyName = "test company name";
    String companyNumber = "test1234";
    String forAttentionOf = "testName";
    String phoneNumber = "0800 001 066";
    String secondaryPhoneNumber = "0800 001 067";
    String email = "tester@testersAA.com";
    String transportConsultantName = "Harold Hastings";
    String externalAnalysisBureau = "Test Company";
    String safetyInspector = "Test Inspector";

    FakerUtils faker = new FakerUtils();

    HashMap<String, String> newAddress = faker.generateAddress();

    public ValidLicenceChanges(World world) {
        this.world = world;
    }

    @When("i make changes to the business details page")
    public void iMakeChangesToTheBusinessDetailsPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.BUSINESS_DETAILS);
        waitAndEnterText("//*[@id='data[tradingNames][0][name]']", SelectorType.XPATH, tradingName);
        waitAndClick("Add another trading name", SelectorType.PARTIALLINKTEXT);
        waitAndEnterText("//*[@id='data[tradingNames][1][name]']", SelectorType.XPATH, tradingName2);
        replaceText("//*[@id='natureOfBusiness']", SelectorType.XPATH, natureOfBusiness);
        world.selfServeUIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "registeredAddress");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitAndEnterText("//*[@id='name']", SelectorType.XPATH, companyName);
        waitAndEnterText("//*[@id='companyNo']", SelectorType.XPATH, companyNumber);
        UniversalActions.clickSubmit();
//        waitAndClick("//*[@name='allow-email[allowEmail]']", SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
    }

    @Then("the changes to the business details page are made")
    public void theChangesToTheBusinessDetailsPageAreMade() {
        clickByLinkText("Home");
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.BUSINESS_DETAILS);

        String expectedChangedText1 = tradingName;
        String actualChangeText1 = getValue("//*[@id='data[tradingNames][0][name]']", SelectorType.XPATH);
        assertEquals(expectedChangedText1, actualChangeText1);

        String expectedChangedText2 = tradingName2;
        String actualChangeText2 = getValue("//*[@id='data[tradingNames][1][name]']", SelectorType.XPATH);
        assertEquals(expectedChangedText2, actualChangeText2);

        String expectedChangedText3 = natureOfBusiness;
        String actualChangeText3 = getValue("//*[@id='natureOfBusiness']", SelectorType.XPATH);
        assertEquals(expectedChangedText3, actualChangeText3);

        world.selfServeUIJourney.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "registeredAddress");

        String expectedChangedText4 = companyNumber;
        String actualChangeText4 = findElement("//td[2]", SelectorType.XPATH).getText();
        assertEquals(expectedChangedText4, actualChangeText4);
    }

    @When("i make changes to the addresses page")
    public void iMakeChangesToTheAddressesPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.ADDRESSES);
        replaceText("//*[@id='correspondence[fao]']", SelectorType.XPATH, forAttentionOf);
        world.selfServeUIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");
        selectValueFromDropDownByIndex("//*[@id='correspondence_address[countryCode]']", SelectorType.XPATH, 5);
        replaceText("//*[@id='phone_primary']", SelectorType.XPATH, phoneNumber);
        replaceText("//*[@id='$phone_secondary']", SelectorType.XPATH, secondaryPhoneNumber);
        replaceText("//*[@id='email']", SelectorType.XPATH, email);
        world.selfServeUIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "establishment_address");
        click("//*[@id='consultant[add-transport-consultant]']", SelectorType.XPATH);
        replaceText("//*[@id='consultant[transportConsultantName]']", SelectorType.XPATH, transportConsultantName);
        world.selfServeUIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "consultantAddress");
        selectValueFromDropDownByIndex("//*[@id='consultantAddress[countryCode]']", SelectorType.XPATH, 5);
        replaceText("//*[@id='tc_phone_primary']", SelectorType.XPATH, phoneNumber);
        replaceText("//*[@id='tc_phone_secondary']", SelectorType.XPATH, secondaryPhoneNumber);
        replaceText("//*[@id='consultantContact[email]']", SelectorType.XPATH, email);
        UniversalActions.clickSaveAndReturn();
    }

    @Then("the changes to the addresses page are made")
    public void theChangesToTheAddressesPageAreMade() {
        clickByLinkText("Home");
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.ADDRESSES);

        String correspondenceFaoXpath = "//*[@id='correspondence[fao]']";
        String actualCorrespondenceFaoText = getValue(correspondenceFaoXpath, SelectorType.XPATH);
        assertEquals(actualCorrespondenceFaoText, forAttentionOf);

        HashMap<String, String> expectedNewAddress = newAddress;
        world.selfServeUIJourney.checkAddressDetails(expectedNewAddress, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");

        String phonePrimaryText = phoneNumber;
        String phonePrimaryXpath = "//*[@id='phone_primary'][@value='" + phonePrimaryText + "']";
        assertEquals(getValue(phonePrimaryXpath, SelectorType.XPATH), phonePrimaryText);

        String phoneSecondaryText = secondaryPhoneNumber;
        String phoneSecondaryXpath = "//*[@id='$phone_secondary'][@value='" + phoneSecondaryText + "']";
        assertEquals(getValue(phoneSecondaryXpath, SelectorType.XPATH), phoneSecondaryText);

        String emailText = email;
        String emailXpath = "//*[@id='consultantContact[email]'][contains(@value, '" + emailText + "')]";
        assertEquals(getValue(emailXpath, SelectorType.XPATH), emailText);

        String transportConsultantXpath = "//*[@id='consultant[add-transport-consultant]']";
        assertTrue(Browser.navigate().findElement(By.xpath(transportConsultantXpath)).isSelected());

        String writtenPermissionXpath = "//*[@id='written-permission-to-engage']";
        assertTrue(Browser.navigate().findElement(By.xpath(writtenPermissionXpath)).isSelected());

        String consultantNameText = transportConsultantName;
        String consultantNameXpath = "//*[@id='consultant[transportConsultantName]'][contains(@value, '" + consultantNameText + "')]";
        assertEquals(getValue(consultantNameXpath, SelectorType.XPATH), consultantNameText);

        world.selfServeUIJourney.checkAddressDetails(expectedNewAddress, world.createApplication.getPostCodeByTrafficArea(), "consultantAddress");

        String consultantPhonePrimaryText = phoneNumber;
        String consultantPhonePrimaryXpath = "//*[@id='tc_phone_primary'][contains(@value, '" + consultantPhonePrimaryText + "')]";
        assertEquals(getValue(consultantPhonePrimaryXpath, SelectorType.XPATH), consultantPhonePrimaryText);

        String consultantPhoneSecondaryText = secondaryPhoneNumber;
        String consultantPhoneSecondaryXpath = "//*[@id='tc_phone_secondary'][contains(@value, '" + consultantPhoneSecondaryText + "')]";
        assertEquals(getValue(consultantPhoneSecondaryXpath, SelectorType.XPATH), consultantPhoneSecondaryText);

        String consultantEmailText = email;
        String consultantEmailXpath = "//*[@id='consultantContact[email]'][contains(@value, '" + consultantEmailText + "')]";
        assertEquals(getValue(consultantEmailXpath, SelectorType.XPATH), consultantEmailText);

    }

    @When("i make changes to the vehicles page")
    public void iMakeChangesToTheVehiclesPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VEHICLES);
        for (int i = 0; i < 3; i++) {
            if (Browser.navigate().findElements(By.xpath("//input[contains(@name, 'vehicles[action][delete]')]")).size() > 0) {
                waitForTextToBePresent("Vehicle details");
                waitAndClick("//input[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
                waitForTextToBePresent("Are you sure you want to remove these records?");
                UniversalActions.clickSubmit();
                refreshPage();
                waitForElementToBeClickable("//input[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
            } else {
                waitAndClick("//button[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
                waitForTextToBePresent("Are you sure you want to remove these records?");
                UniversalActions.clickSubmit();
                refreshPage();
                waitForTitleToBePresent("Vehicle details");
                waitForElementToBeClickable("//input[contains(@name, 'table[action][delete]')]", SelectorType.XPATH);
            }
        }
        waitForTextToBePresent("Tick the box");
        click("//*[@id='shareInfo[shareInfo]']", SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
    }

    @Then("the changes to the vehicles page are made")
    public void theChangesToTheVehiclesPageAreMade() {
        clickByLinkText("Home");
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VEHICLES);
        waitForTitleToBePresent("Vehicle details");
        UniversalActions.refreshPageWithJavascript();
        if (Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size() > 0) {
            assertTrue(Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size() < 5);
        } else {
            UniversalActions.refreshPageWithJavascript();
            assertTrue(Browser.navigate().findElements(By.xpath("//button[contains(@name, 'vehicles[action][delete]')]")).size() < 5);
        }
    }

    @When("i make changes to the licence discs page")
    public void iMakeChangesToTheLicenceDiscsPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.LICENCE_DISCS);
        for (int i = 0; i < 3; i++) {
            UniversalActions.refreshPageWithJavascript();
            waitForTextToBePresent("Licence discs");
            waitAndClick("//button[contains(@name,'table[action][void]')]", SelectorType.XPATH);
            waitForTextToBePresent("Are you sure you would like to void these discs?");
            UniversalActions.clickSubmit();
            waitForElementToBeClickable("//button[contains(@name,'table[action][void]')]", SelectorType.XPATH);
        }
        UniversalActions.refreshPageWithJavascript();
        waitForTextToBePresent("Licence discs");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTextToBePresent("How many additional discs are required?");
        enterText("//*[@id='data[additionalDiscs]']", SelectorType.XPATH, "2");
        UniversalActions.clickSubmit();
    }

    @Then("the changes to the licence discs page are made")
    public void theChangesToTheLicenceDiscsPageAreMade() {
        assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name,'table[action][void]')]")).size(), 0);
        waitAndClick("//*[contains(text(),'More actions')]", SelectorType.XPATH);
        waitAndClick("//*[@id='ceased-show-hide']", SelectorType.XPATH);
        assertEquals(Browser.navigate().findElements(By.xpath("//tbody//tr")).size(), 7);
    }

    @When("i make changes to the safety and compliance page")
    public void iMakeChangesToTheSafetyAndCompliancePage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.SAFETY_AND_COMPLIANCE);
        replaceText("//*[@id='licence[safetyInsVehicles]']", SelectorType.XPATH, "6");
        click("//*[@id='licence[safetyInsVaries]']", SelectorType.XPATH);
        click("//*[contains(@value,'tach_external')]", SelectorType.XPATH);
        enterText("//*[@id='licence[tachographInsName]']", SelectorType.XPATH, externalAnalysisBureau);
        click("//*[@id='add']", SelectorType.XPATH);
        click("//*[contains(text(),'An owner or employee')]", SelectorType.XPATH);
        enterText("//*[@id='contactDetails[fao]']", SelectorType.XPATH, safetyInspector);
        clickByLinkText("Enter the address yourself");
        world.selfServeUIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Safety inspectors");
        UniversalActions.clickSaveAndReturn();
    }

    @Then("the changes to the safety and compliance page are made")
    public void theChangesToTheSafetyAndCompliancePageAreMade() {
        clickByLinkText("Home");
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.SAFETY_AND_COMPLIANCE);
        assertEquals("6", getValue("//*[@id='licence[safetyInsVehicles]']", SelectorType.XPATH));
        assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='licence[safetyInsVaries]']")).isSelected());
        assertTrue(Browser.navigate().findElement(By.xpath("//*[contains(@value,'tach_external')]")).isSelected());
        assertEquals(getValue("//*[@id='licence[tachographInsName]']", SelectorType.XPATH), externalAnalysisBureau);
        checkForFullMatch(safetyInspector);
    }

    @Then("i cannot make changes to the business details page")
    public void iCannotMakeChangesToTheBusinessDetailsPage() {
        waitAndClick("Business details", SelectorType.LINKTEXT);
        assertFalse(isElementPresent("data[tradingNames][0][name]", SelectorType.NAME));
        assertFalse(isElementPresent("data[companyNumber][company_number]", SelectorType.NAME));
    }

    @Then("i cannot make changes to the addresses page")
    public void iCannotMakeChangesToTheAddressesPage() {
        waitAndClick("Addresses", SelectorType.LINKTEXT);
        waitForTextToBePresent("Addresses");
        assertFalse(isElementPresent("correspondence_address[searchPostcode][postcode]", SelectorType.NAME));
        assertFalse(isElementPresent("correspondence_address[searchPostcode][search]", SelectorType.NAME));
        assertFalse(isElementPresent("Enter the address yourself", SelectorType.LINKTEXT));
        assertFalse(isElementPresent("consultantAddress[searchPostcode][postcode]", SelectorType.NAME));
    }

    @Then("directors names should not be displayed as links")
    public void directorsNamesShouldNotBeDisplayedAsLinks() {
        waitAndClick("People", SelectorType.LINKTEXT);
        List<WebElement> names = findElements("//*[@data-heading='Name']", SelectorType.XPATH);
        for (WebElement element : names) {
            assertFalse(isElementClickable(Objects.requireNonNull(element.getAccessibleName()), SelectorType.LINKTEXT));
        }
    }

    @Then("operating centre names should not be displayed as links")
    public void operatingCentreNamesShouldNotBeDisplayedAsLinks() {
        waitAndClick("Operating centres and authorisation", SelectorType.LINKTEXT);
        waitForTextToBePresent("Operating centres");
        List<WebElement> operatingCentreName = findElements("//*[@data-heading='Address']", SelectorType.XPATH);
        for (WebElement element : operatingCentreName) {
            assertFalse(isElementClickable(element.getAccessibleName(), SelectorType.LINKTEXT));
        }
        assertFalse(isElementPresent("data[totAuthHgvVehiclesFieldset][totAuthHgvVehicles]", SelectorType.NAME));
        assertFalse(isElementPresent("data[totAuthTrailersFieldset][totAuthTrailers]", SelectorType.NAME));
    }

    @Then("safety inspector names should not be displayed as links")
    public void safetyInspectorNamesShouldNotBeDisplayedAsLinks() {
        waitAndClick("Safety and compliance", SelectorType.LINKTEXT);
        waitForTextToBePresent("Safety and compliance");
        List<WebElement> names = findElements("//*[@data-heading='Name']", SelectorType.XPATH);
        for (WebElement element : names) {
            assertFalse(isElementClickable(Objects.requireNonNull(element.getAccessibleName()), SelectorType.LINKTEXT));
        }
    }

    @Then("permit reference number should not be displayed as links")
    public void permitReferenceNumberShouldNotBeDisplayedAsLinks() {
        waitAndClick("ANNULAR LIMITED", SelectorType.LINKTEXT);
        waitForTextToBePresent("ANNULAR LIMITED");
        waitAndClick("IRFO", SelectorType.LINKTEXT);
        waitAndClick("GV Permits", SelectorType.LINKTEXT);
        List<WebElement> names = findElements("//*[@data-heading='Reference number']", SelectorType.XPATH);
        for (WebElement element : names) {
            assertTrue(isElementClickable(Objects.requireNonNull(element.getAccessibleName()), SelectorType.LINKTEXT));
        }
    }

    @Then("tm details page should not display buttons and links")
    public void tmDetailsPageShouldNotDisplayButtonsAndLinks() {
        waitForTextToBePresent("Details");
        assertFalse(isElementPresent("home-address[searchPostcode][postcode]", SelectorType.NAME));
        assertFalse(isElementPresent("home-address[searchPostcode][search]", SelectorType.NAME));
        assertFalse(isElementPresent("Enter the address yourself", SelectorType.LINKTEXT));
        assertFalse(isElementPresent("work-address[searchPostcode][postcode]", SelectorType.NAME));
        assertFalse(isElementPresent("work-address[searchPostcode][search]", SelectorType.NAME));
    }

    @And("responsibilities page should not display input fields")
    public void responsibilitiesPageShouldNotDisplayInputFields() {
        waitAndClick("Responsibilities", SelectorType.LINKTEXT);
        waitForTextToBePresent("Responsibilities");
        waitAndClick("Not set", SelectorType.LINKTEXT);
        waitForTextToBePresent("Operator name");
        assertFalse(isElementPresent("details[hoursOfWeek][hoursPerWeekContent][hoursMon]", SelectorType.NAME));
        assertFalse(isElementPresent("details[hoursOfWeek][hoursPerWeekContent][hoursTue]", SelectorType.NAME));
        assertFalse(isElementPresent("details[hoursOfWeek][hoursPerWeekContent][hoursWed]", SelectorType.NAME));
        assertFalse(isElementPresent("details[hoursOfWeek][hoursPerWeekContent][hoursThu]", SelectorType.NAME));
        assertFalse(isElementPresent("details[hoursOfWeek][hoursPerWeekContent][hoursFri]", SelectorType.NAME));
        assertFalse(isElementPresent("details[hoursOfWeek][hoursPerWeekContent][hoursSat]", SelectorType.NAME));
        assertFalse(isElementPresent("details[hoursOfWeek][hoursPerWeekContent][hoursSun]", SelectorType.NAME));
    }

    @Then("irfo page should not displayed any input fields")
    public void irfoPageShouldNotDisplayedAnyInputFields() {
        assertFalse(isElementPresent("fields_irfoNationality__chosen", SelectorType.ID));
        assertFalse(isElementPresent("fields[tradingNames][0][name]", SelectorType.NAME));
        assertFalse(isElementPresent("Add another trading name", SelectorType.LINKTEXT));
        assertFalse(isElementPresent("Add another partner detail", SelectorType.LINKTEXT));
        assertFalse(isElementPresent("fields[irfoPartners][0][name]", SelectorType.NAME));
        assertFalse(isElementPresent("address[searchPostcode][postcode]", SelectorType.NAME));
        assertFalse(isElementPresent("contact[phone_secondary]", SelectorType.NAME));
        assertFalse(isElementPresent("contact[email]", SelectorType.NAME));
        assertFalse(isElementPresent("form-actions[submit]", SelectorType.NAME));
        assertFalse(isElementPresent("form-actions[cancel]", SelectorType.NAME));
    }

    @Then("i should not be able to add case details")
    public void iShouldNotBeAbleToAddCaseDetails() {
        waitAndClick("Convictions", SelectorType.LINKTEXT);
        waitForTextToBePresent("Convictions");
        assertFalse(isElementPresent("add", SelectorType.ID));
        assertFalse(isElementPresent("fields[comment]", SelectorType.NAME));
        assertFalse(isElementPresent("form-actions[submit]", SelectorType.NAME));
        assertFalse(isElementPresent("fields[description]", SelectorType.NAME));
        waitAndClick("Annual test history", SelectorType.LINKTEXT);
        waitForTextToBePresent("Annual test history");
        assertFalse(isElementPresent("fields[ecmsNo]", SelectorType.NAME));
        assertFalse(isElementPresent("fields[annualTestHistory]", SelectorType.NAME));
        assertFalse(isElementPresent("form-actions[submit]", SelectorType.NAME));
        waitAndClick("Serious infringements", SelectorType.LINKTEXT);
        waitForTextToBePresent("Serious infringements");
        assertFalse(isElementPresent("fields[comment]", SelectorType.NAME));
        assertFalse(isElementPresent("add", SelectorType.ID));
        assertFalse(isElementPresent("form-actions[submit]", SelectorType.NAME));
        waitAndClick("Prohibitions", SelectorType.LINKTEXT);
        waitForTextToBePresent("Prohibitions");
        assertFalse(isElementPresent("fields[comment]", SelectorType.NAME));
        assertFalse(isElementPresent("add", SelectorType.ID));
        assertFalse(isElementPresent("form-actions[submit]", SelectorType.NAME));
        assertFalse(isElementPresent("form-actions[cancel]", SelectorType.NAME));
        waitAndClick("Section statements", SelectorType.LINKTEXT);
        waitForTextToBePresent("Statements");
        assertFalse(isElementPresent("add", SelectorType.ID));
        waitAndClick("Complaints", SelectorType.LINKTEXT);
        waitForTextToBePresent("Complaints");
        assertFalse(isElementPresent("add", SelectorType.ID));
        waitAndClick("Conditions & undertakings", SelectorType.LINKTEXT);
        waitForTextToBePresent("Conditions & undertakings");
        assertFalse(isElementPresent("add", SelectorType.ID));
    }

    @And("i should not be able to create snapshots")
    public void iShouldNotBeAbleToCreateSnapshots() {
        waitAndClick("Submissions", SelectorType.LINKTEXT);
        waitForTextToBePresent("Submission");
        waitAndClick("6648", SelectorType.LINKTEXT);
        waitForTextToBePresent("Licensing (G) Submission");
        assertFalse(isElementPresent("//*[contains(text(),'Reopen')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//*[contains(text(),'Return')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//*[contains(text(),'Print submission')]", SelectorType.XPATH));
        assertFalse(isElementPresent("//*[contains(text(),'Create snapshot')]", SelectorType.XPATH));
    }

    @And("i should not be able to edit case details")
    public void iShouldNotBeAbleToEditCaseDetails() {
        waitAndClick("Case details", SelectorType.LINKTEXT);
        waitForTextToBePresent("Overview");
        assertFalse(isElementPresent("fields[caseType]", SelectorType.ID));
        assertFalse(isElementPresent("//*[@id='fields_categorys__chosen']", SelectorType.XPATH));
        assertFalse(isElementPresent("fields[description]", SelectorType.NAME));
        assertFalse(isElementPresent("fields[ecmsNo]", SelectorType.NAME));
        assertFalse(isElementPresent("//*[@id='fields_outcomes__chosen']", SelectorType.XPATH));
        assertFalse(isElementPresent("form-actions[submit]", SelectorType.NAME));
        assertFalse(isElementPresent("form-actions[cancel]", SelectorType.NAME));
    }
}