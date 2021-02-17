package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.util.HashMap;

public class ValidLicenceChanges extends BasePage implements En {

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
        When("^i make changes to the business details page$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Business details");
            enterText("//*[@id='data[tradingNames][0][name]']", tradingName,SelectorType.XPATH);
            click("//a[@class='add-another-trigger']", SelectorType.XPATH);
            enterText("//*[@id='data[tradingNames][1][name]']", tradingName2, SelectorType.XPATH);
            replaceText("//*[@id='natureOfBusiness']", natureOfBusiness);
            world.UIJourneySteps.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "registeredAddress");
            click("//*[@id='add']", SelectorType.XPATH);
            waitAndEnterText("//*[@id='name']", SelectorType.XPATH,companyName);
            enterText("//*[@id='companyNo']", companyNumber, SelectorType.XPATH);
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            click("//*[@id='allow-email[allowEmail]']", SelectorType.XPATH);
            click("//*[@id='form-actions[save]']", SelectorType.XPATH);
        });
        Then("^the changes to the business details page are made$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Business details");
            checkValue("//*[@id='data[tradingNames][0][name]']",SelectorType.XPATH,tradingName);
            checkValue("//*[@id='data[tradingNames][1][name]']",SelectorType.XPATH,tradingName2);
            checkValue("//*[@id='natureOfBusiness']",SelectorType.XPATH,natureOfBusiness);
            world.UIJourneySteps.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "registeredAddress");
            checkValue("//td[1]//input[@type='submit']",SelectorType.XPATH,companyName);
            Assert.assertEquals(Browser.navigate().findElement(By.xpath("//td[2]")).getText(),companyNumber);
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='allow-email[allowEmail]']")).isSelected());
        });
        When("^i make changes to the addresses page$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Addresses");
            replaceText("//*[@id='correspondence[fao]']",forAttentionOf);
            world.UIJourneySteps.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");
            selectValueFromDropDownByIndex("//*[@id='correspondence_address[countryCode]']",SelectorType.XPATH,5);
            replaceText("//*[@id='phone_primary']", phoneNumber);
            replaceText("//*[@id='$phone_secondary']", secondaryPhoneNumber);
            replaceText("//*[@id='email']", email);
            world.UIJourneySteps.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "establishment_address");
            click("//*[@id='consultant[add-transport-consultant]']", SelectorType.XPATH);
            replaceText("//*[@id='consultant[transportConsultantName]']", transportConsultantName);
            world.UIJourneySteps.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "consultantAddress");
            selectValueFromDropDownByIndex("//*[@id='consultantAddress[countryCode]']",SelectorType.XPATH,5);
            replaceText("//*[@id='tc_phone_primary']",phoneNumber);
            replaceText("//*[@id='tc_phone_secondary']",secondaryPhoneNumber);
            replaceText("//*[@id='consultantContact[email]']",email);
            click("//*[@id='form-actions[save]']",SelectorType.XPATH);
        });
        Then("^the changes to the addresses page are made$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Addresses");
            checkValue("//*[@id='correspondence[fao]']",SelectorType.XPATH,forAttentionOf);
            world.UIJourneySteps.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "correspondence_address");
            checkValue("//*[@id='phone_primary']",SelectorType.XPATH,phoneNumber);
            checkValue("//*[@id='$phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
            checkValue("//*[@id='email']",SelectorType.XPATH,email);
            world.UIJourneySteps.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "establishment_address");
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='consultant[add-transport-consultant]']")).isSelected());
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='written-permission-to-engage']")).isSelected());
            checkValue("//*[@id='consultant[transportConsultantName]']",SelectorType.XPATH,transportConsultantName);
            world.UIJourneySteps.checkAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "consultantAddress");
            checkValue("//*[@id='tc_phone_primary']",SelectorType.XPATH,phoneNumber);
            checkValue("//*[@id='tc_phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
            checkValue("//*[@id='consultantContact[email]']",SelectorType.XPATH,email);
        });
        When("^i make changes to the vehicles page$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Vehicles");
            for (int i = 0; i < 3; i++) {
                if (Browser.navigate().findElements(By.xpath("//input[contains(@name, 'vehicles[action][delete]')]")).size()>0) {
                    click("//input[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
                    waitForTextToBePresent("Are you sure you want to remove these records?");
                    click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
                    waitForElementToBeClickable("//input[contains(@name, 'vehicles[action][delete]')]", SelectorType.XPATH);
                } else {
                    click("//input[contains(@name, 'table[action][delete]')]", SelectorType.XPATH);
                    waitForTextToBePresent("Are you sure you want to remove these vehicle(s)");
                    click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
                    waitForElementToBeClickable("//input[contains(@name, 'table[action][delete]')]", SelectorType.XPATH);
                }
            }
            click("//*[@id='shareInfo[shareInfo]']", SelectorType.XPATH);
            click("//*[@id='form-actions[save]']", SelectorType.XPATH);
        });
        Then("^the changes to the vehicles page are made$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Vehicles");
            if (Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size()>0) {
                Assert.assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size(),2);
            } else {
                Assert.assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name, 'vehicles[action][delete]')]")).size(),2);
            }
                Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='shareInfo[shareInfo]']")).isSelected());
        });
        When("^i make changes to the licence discs page$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Licence discs");
            for (int i = 0; i < 3; i++) {
                click("//input[contains(@name,'table[action][void]')]", SelectorType.XPATH);
                waitForTextToBePresent("Are you sure you would like to void these discs?");
                click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
                waitForElementToBeClickable("//input[contains(@name,'table[action][void]')]", SelectorType.XPATH);
            }
            click("//*[@id='add']",SelectorType.XPATH);
            waitForTextToBePresent("How many additional discs are required?");
            enterText("//*[@id='data[additionalDiscs]']","2",SelectorType.XPATH);
            click("//*[@id='form-actions[submit]']",SelectorType.XPATH);
        });
        Then("^the changes to the licence discs page are made$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Licence discs");
            Assert.assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name,'table[action][void]')]")).size(),4);
            click("//*[contains(text(),'More actions')]", SelectorType.XPATH);
            click("//*[@id='ceased-show-hide']",SelectorType.XPATH);
            Assert.assertEquals(Browser.navigate().findElements(By.xpath("//tbody//tr")).size(),7);

        });
        When("^i make changes to the safety and compliance page$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Safety and compliance");
            replaceText("//*[@id='licence[safetyInsVehicles]']", "6");
            click("//*[@id='licence[safetyInsVaries]']", SelectorType.XPATH);
            click("//*[contains(@value,'tach_external')]", SelectorType.XPATH);
            enterText("//*[@id='licence[tachographInsName]']", externalAnalysisBureau, SelectorType.XPATH);
            click("//*[@id='add']", SelectorType.XPATH);
            click("//*[contains(text(),'An owner or employee')]", SelectorType.XPATH);
            enterText("//*[@id='contactDetails[fao]']",safetyInspector,SelectorType.XPATH);
            clickByLinkText("Enter the address yourself");
            world.UIJourneySteps.addNewAddressDetails(newAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Safety inspectors");
            click("//*[@id='form-actions[save]']", SelectorType.XPATH);
        });
        Then("^the changes to the safety and compliance page are made$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Safety and compliance");

            checkValue("//*[@id='licence[safetyInsVehicles]']", SelectorType.XPATH, "6");
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='licence[safetyInsVaries]']")).isSelected());
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[contains(@value,'tach_external')]")).isSelected());
            checkValue("//*[@id='licence[tachographInsName]']", SelectorType.XPATH, externalAnalysisBureau);
            checkForFullMatch(safetyInspector);
//            checkForFullMatch(registeredAddress1 + ", " + registeredAddressTown);
        });
    }
}
