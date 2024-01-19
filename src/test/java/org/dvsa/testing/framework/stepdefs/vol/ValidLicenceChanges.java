package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;

import java.util.HashMap;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    public ValidLicenceChanges(World world) {this.world = world;}

    @When("i make changes to the business details page")
    public void iMakeChangesToTheBusinessDetailsPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.BUSINESS_DETAILS);
        waitAndEnterText("//*[@id='data[tradingNames][0][name]']", SelectorType.XPATH, tradingName);
        waitAndClick("Add another trading name", SelectorType.PARTIALLINKTEXT);
        waitAndEnterText("//*[@id='data[tradingNames][1][name]']", SelectorType.XPATH, tradingName2);
        replaceText("//*[@id='natureOfBusiness']", SelectorType.XPATH, natureOfBusiness);
        world.UIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "registeredAddress");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitAndEnterText("//*[@id='name']", SelectorType.XPATH,companyName);
        waitAndEnterText("//*[@id='companyNo']", SelectorType.XPATH, companyNumber);
        world.UIJourney.clickSubmit();
//        waitAndClick("//*[@name='allow-email[allowEmail]']", SelectorType.XPATH);
        UIJourney.clickSaveAndReturn();
    }

    @Then("the changes to the business details page are made")
    public void theChangesToTheBusinessDetailsPageAreMade() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.BUSINESS_DETAILS);
        world.UIJourney.checkValue("//*[@id='data[tradingNames][0][name]']",SelectorType.XPATH,tradingName);
        world.UIJourney.checkValue("//*[@id='data[tradingNames][1][name]']",SelectorType.XPATH,tradingName2);
        world.UIJourney.checkValue("//*[@id='natureOfBusiness']",SelectorType.XPATH,natureOfBusiness);
        world.UIJourney.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "registeredAddress");
        assertEquals(findElement("//td[2]", SelectorType.XPATH).getText(),companyNumber);
    }

    @When("i make changes to the addresses page")
    public void iMakeChangesToTheAddressesPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.ADDRESSES);
        replaceText("//*[@id='correspondence[fao]']", SelectorType.XPATH, forAttentionOf);
        world.UIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");
        selectValueFromDropDownByIndex("//*[@id='correspondence_address[countryCode]']",SelectorType.XPATH,5);
        replaceText("//*[@id='phone_primary']", SelectorType.XPATH, phoneNumber);
        replaceText("//*[@id='$phone_secondary']", SelectorType.XPATH, secondaryPhoneNumber);
        replaceText("//*[@id='email']", SelectorType.XPATH, email);
        world.UIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "establishment_address");
        click("//*[@id='consultant[add-transport-consultant]']", SelectorType.XPATH);
        replaceText("//*[@id='consultant[transportConsultantName]']", SelectorType.XPATH, transportConsultantName);
        world.UIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "consultantAddress");
        selectValueFromDropDownByIndex("//*[@id='consultantAddress[countryCode]']",SelectorType.XPATH,5);
        replaceText("//*[@id='tc_phone_primary']", SelectorType.XPATH, phoneNumber);
        replaceText("//*[@id='tc_phone_secondary']", SelectorType.XPATH, secondaryPhoneNumber);
        replaceText("//*[@id='consultantContact[email]']", SelectorType.XPATH, email);
        UIJourney.clickSaveAndReturn();
    }

    @Then("the changes to the addresses page are made")
    public void theChangesToTheAddressesPageAreMade() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.ADDRESSES);
        world.UIJourney.checkValue("//*[@id='correspondence[fao]']",SelectorType.XPATH,forAttentionOf);
        world.UIJourney.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");
        world.UIJourney.checkValue("//*[@id='phone_primary']",SelectorType.XPATH,phoneNumber);
        world.UIJourney.checkValue("//*[@id='$phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
        world.UIJourney.checkValue("//*[@id='email']",SelectorType.XPATH,email);
        world.UIJourney.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "establishment_address");
        assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='consultant[add-transport-consultant]']")).isSelected());
        assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='written-permission-to-engage']")).isSelected());
        world.UIJourney.checkValue("//*[@id='consultant[transportConsultantName]']",SelectorType.XPATH,transportConsultantName);
        world.UIJourney.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "consultantAddress");
        world.UIJourney.checkValue("//*[@id='tc_phone_primary']",SelectorType.XPATH,phoneNumber);
        world.UIJourney.checkValue("//*[@id='tc_phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
        world.UIJourney.checkValue("//*[@id='consultantContact[email]']",SelectorType.XPATH,email);
    }

    @When("i make changes to the vehicles page")
    public void iMakeChangesToTheVehiclesPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VEHICLES);
        for (int i = 0; i < 3; i++) {
            if (Browser.navigate().findElements(By.xpath("//input[contains(@name, 'vehicles[action][delete]')]")).size()>0) {
                waitForTextToBePresent("Vehicle details");
                waitAndClick("//input[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
                waitForTextToBePresent("Are you sure you want to remove these records?");
                world.UIJourney.clickSubmit();
                waitForElementToBeClickable("//input[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
            } else {
                waitAndClick("//button[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
                waitForTextToBePresent("Are you sure you want to remove these records?");
                world.UIJourney.clickSubmit();
                refreshPageWithJavascript();
                waitForTitleToBePresent("Vehicle details");
                waitForElementToBeClickable("//input[contains(@name, 'table[action][delete]')]", SelectorType.XPATH);
            }
        }
        waitForTextToBePresent("Tick the box");
        click("//*[@id='shareInfo[shareInfo]']", SelectorType.XPATH);
        UIJourney.clickSaveAndReturn();
    }

    @Then("the changes to the vehicles page are made")
    public void theChangesToTheVehiclesPageAreMade() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VEHICLES);
        waitForTitleToBePresent("Vehicle details");
        refreshPage();
        if (Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size()>0) {
            assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size(),2);
        } else {
            refreshPage();
            assertEquals(Browser.navigate().findElements(By.xpath("//button[contains(@name, 'vehicles[action][delete]')]")).size(),3);
        }
        assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='shareInfo[shareInfo]']")).isSelected());
    }

    @When("i make changes to the licence discs page")
    public void iMakeChangesToTheLicenceDiscsPage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.LICENCE_DISCS);
        for (int i = 0; i < 3; i++) {
            refreshPageWithJavascript();
            waitForTextToBePresent("Licence discs");
            waitAndClick("//button[contains(@name,'table[action][void]')]", SelectorType.XPATH);
            waitForTextToBePresent("Are you sure you would like to void these discs?");
            world.UIJourney.clickSubmit();
            waitForElementToBeClickable("//button[contains(@name,'table[action][void]')]", SelectorType.XPATH);
        }
        refreshPageWithJavascript();
        waitForTextToBePresent("Licence discs");
        waitAndClick("//*[@id='add']",SelectorType.XPATH);
        waitForTextToBePresent("How many additional discs are required?");
        enterText("//*[@id='data[additionalDiscs]']", SelectorType.XPATH, "2");
        world.UIJourney.clickSubmit();
    }

    @Then("the changes to the licence discs page are made")
    public void theChangesToTheLicenceDiscsPageAreMade() {
        assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name,'table[action][void]')]")).size(),0);
        waitAndClick("//*[contains(text(),'More actions')]", SelectorType.XPATH);
        waitAndClick("//*[@id='ceased-show-hide']",SelectorType.XPATH);
        assertEquals(Browser.navigate().findElements(By.xpath("//tbody//tr")).size(),7);
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
        world.UIJourney.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
        world.UIJourney.clickSubmit();
        waitForTextToBePresent("Safety inspectors");
        UIJourney.clickSaveAndReturn();
    }

    @Then("the changes to the safety and compliance page are made")
    public void theChangesToTheSafetyAndCompliancePageAreMade() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.SAFETY_AND_COMPLIANCE);
        world.UIJourney.checkValue("//*[@id='licence[safetyInsVehicles]']", SelectorType.XPATH, "6");
        assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='licence[safetyInsVaries]']")).isSelected());
        assertTrue(Browser.navigate().findElement(By.xpath("//*[contains(@value,'tach_external')]")).isSelected());
        world.UIJourney.checkValue("//*[@id='licence[tachographInsName]']", SelectorType.XPATH, externalAnalysisBureau);
        checkForFullMatch(safetyInspector);
    }
}