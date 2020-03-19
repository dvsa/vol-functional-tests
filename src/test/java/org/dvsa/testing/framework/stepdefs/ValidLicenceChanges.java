package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
 import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.By;

public class ValidLicenceChanges extends BasePage implements En {

    String tradingName = "test trading name";
    String tradingName2 = "test trading name 2";
    String natureOfBusiness = "test nature of business";
    String registeredAddress1 = "test address line 1";
    String registeredAddress2 = "test address line 2";
    String registeredAddress3 = "test address line 3";
    String registeredAddress4 = "test address line 4";
    String registeredAddressTown = "test address line town";
    String registeredAddressPostCode = "te23de";
    String companyName = "test company name";
    String companyNumber = "test1234";

    String forAttentionOf = "testName";
    String phoneNumber = "0800 001 066";
    String secondaryPhoneNumber = "0800 001 067";
    String email = "tester@testersAA.com";
    String transportConsultantName = "Harold Hastings";

    String externalAnalysisBureau = "Test Company";
    String safetyInspector = "Test Inspector";


    public ValidLicenceChanges(World world) {
        When("^i make changes to the business details page$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","business details");
            enterText("//*[@id='data[tradingNames][0][name]']",tradingName,SelectorType.XPATH);
            click("//a[@class='add-another-trigger']",SelectorType.XPATH);
            enterText("//*[@id='data[tradingNames][1][name]']",tradingName2,SelectorType.XPATH);
            replaceText("//*[@id='natureOfBusiness']",natureOfBusiness);
            replaceText("//*[@id='addressLine1']",registeredAddress1);
            replaceText("//*[@id='registeredAddress[addressLine2]']",registeredAddress2);
            replaceText("//*[@id='registeredAddress[addressLine3]']",registeredAddress3);
            replaceText("//*[@id='registeredAddress[addressLine4]']",registeredAddress4);
            replaceText("//*[@id='registeredAddress[town]']",registeredAddressTown);
            replaceText("//*[@id='registeredAddress[postcode]']",registeredAddressPostCode);
            click("//*[@id='add']",SelectorType.XPATH);
            waitAndEnterText("//*[@id='name']",SelectorType.XPATH,companyName);
            enterText("//*[@id='companyNo']",companyNumber,SelectorType.XPATH);
            click("//*[@id='form-actions[submit]']",SelectorType.XPATH);
            click("//*[@id='allow-email[allowEmail]']",SelectorType.XPATH);
            click("//*[@id='form-actions[save]']",SelectorType.XPATH);
        });
        Then("^the changes to the business details page are made$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","business details");
            checkValue("//*[@id='data[tradingNames][0][name]']",SelectorType.XPATH,tradingName);
            checkValue("//*[@id='data[tradingNames][1][name]']",SelectorType.XPATH,tradingName2);
            checkValue("//*[@id='natureOfBusiness']",SelectorType.XPATH,natureOfBusiness);
            checkValue("//*[@id='addressLine1']",SelectorType.XPATH,registeredAddress1);
            checkValue("//*[@id='registeredAddress[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            checkValue("//*[@id='registeredAddress[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            checkValue("//*[@id='registeredAddress[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            checkValue("//*[@id='registeredAddress[town]']",SelectorType.XPATH,registeredAddressTown);
            checkValue("//*[@id='registeredAddress[postcode]']",SelectorType.XPATH,registeredAddressPostCode);
            checkValue("//td[1]//input[@type='submit']",SelectorType.XPATH,companyName);
            Assert.assertEquals(Browser.navigate().findElement(By.xpath("//td[2]")).getText(),companyNumber);
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='allow-email[allowEmail]']")).isSelected());
        });
        When("^i make changes to the addresses page$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","addresses");
            replaceText("//*[@id='correspondence[fao]']",forAttentionOf);
            replaceText("//*[@id='addressLine1']",registeredAddress1);
            replaceText("//*[@id='correspondence_address[addressLine2]']",registeredAddress2);
            replaceText("//*[@id='correspondence_address[addressLine3]']",registeredAddress3);
            replaceText("//*[@id='correspondence_address[addressLine4]']",registeredAddress4);
            replaceText("//*[@id='addressTown']",registeredAddressTown);
            replaceText("//*[@id='postcode']",registeredAddressPostCode);
            selectValueFromDropDownByIndex("//*[@id='correspondence_address[countryCode]']",SelectorType.XPATH,5);
            replaceText("//*[@id='phone_primary']",phoneNumber);
            replaceText("//*[@id='$phone_secondary']",secondaryPhoneNumber);
            replaceText("//*[@id='email']",email);
            replaceText("//*[@id='establishment_address[addressLine1]']",registeredAddress1);
            replaceText("//*[@id='establishment_address[addressLine2]']",registeredAddress2);
            replaceText("//*[@id='establishment_address[addressLine3]']",registeredAddress3);
            replaceText("//*[@id='establishment_address[addressLine4]']",registeredAddress4);
            replaceText("//*[@id='establishment_address[town]']",registeredAddressTown);
            replaceText("//*[@id='postcodeOptional']",registeredAddressPostCode);
            click("//*[@id='consultant[add-transport-consultant]']",SelectorType.XPATH);
            replaceText("//*[@id='consultant[transportConsultantName]']",transportConsultantName);
            replaceText("//*[@name='consultantAddress[addressLine1]']",registeredAddress1);
            replaceText("//*[@id='consultantAddress[addressLine2]']",registeredAddress2);
            replaceText("//*[@id='consultantAddress[addressLine3]']",registeredAddress3);
            replaceText("//*[@id='consultantAddress[addressLine4]']",registeredAddress4);
            replaceText("//*[@name='consultantAddress[town]']",registeredAddressTown);
            replaceText("//*[@name='consultantAddress[postcode]']",registeredAddressPostCode);
            selectValueFromDropDownByIndex("//*[@id='consultantAddress[countryCode]']",SelectorType.XPATH,5);
            replaceText("//*[@id='tc_phone_primary']",phoneNumber);
            replaceText("//*[@id='tc_phone_secondary']",secondaryPhoneNumber);
            replaceText("//*[@id='consultantContact[email]']",email);
            click("//*[@id='form-actions[save]']",SelectorType.XPATH);
        });
        Then("^the changes to the addresses page are made$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","addresses");
            checkValue("//*[@id='correspondence[fao]']",SelectorType.XPATH,forAttentionOf);
            checkValue("//*[@id='addressLine1']",SelectorType.XPATH,registeredAddress1);
            checkValue("//*[@id='correspondence_address[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            checkValue("//*[@id='correspondence_address[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            checkValue("//*[@id='correspondence_address[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            checkValue("//*[@id='addressTown']",SelectorType.XPATH,registeredAddressTown);
            checkValue("//*[@id='postcode']",SelectorType.XPATH,registeredAddressPostCode);
            checkValue("//*[@id='phone_primary']",SelectorType.XPATH,phoneNumber);
            checkValue("//*[@id='$phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
            checkValue("//*[@id='email']",SelectorType.XPATH,email);
            checkValue("//*[@id='establishment_address[addressLine1]']",SelectorType.XPATH,registeredAddress1);
            checkValue("//*[@id='establishment_address[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            checkValue("//*[@id='establishment_address[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            checkValue("//*[@id='establishment_address[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            checkValue("//*[@id='establishment_address[town]']",SelectorType.XPATH,registeredAddressTown);
            checkValue("//*[@id='postcodeOptional']",SelectorType.XPATH,registeredAddressPostCode);
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='consultant[add-transport-consultant]']")).isSelected());
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='written-permission-to-engage']")).isSelected());
            checkValue("//*[@id='consultant[transportConsultantName]']",SelectorType.XPATH,transportConsultantName);
            checkValue("//*[@name='consultantAddress[addressLine1]']",SelectorType.XPATH,registeredAddress1);
            checkValue("//*[@id='consultantAddress[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            checkValue("//*[@id='consultantAddress[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            checkValue("//*[@id='consultantAddress[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            checkValue("//*[@name='consultantAddress[town]']",SelectorType.XPATH,registeredAddressTown);
            checkValue("//*[@name='consultantAddress[postcode]']",SelectorType.XPATH,registeredAddressPostCode);
            checkValue("//*[@id='tc_phone_primary']",SelectorType.XPATH,phoneNumber);
            checkValue("//*[@id='tc_phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
            checkValue("//*[@id='consultantContact[email]']",SelectorType.XPATH,email);
        });
        When("^i make changes to the vehicles page$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","vehicles");
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
            world.UIJourneySteps.navigateToSelfServePage("licence","vehicles");
            if (Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size()>0) {
                Assert.assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name, 'table[action][delete]')]")).size(),2);
            } else {
                Assert.assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name, 'vehicles[action][delete]')]")).size(),2);
            }
                Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='shareInfo[shareInfo]']")).isSelected());
        });
        When("^i make changes to the licence discs page$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","licence discs");
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
            world.UIJourneySteps.navigateToSelfServePage("licence","licence discs");
            Assert.assertEquals(Browser.navigate().findElements(By.xpath("//input[contains(@name,'table[action][void]')]")).size(),4);
            click("//*[contains(text(),'More actions')]", SelectorType.XPATH);
            click("//*[@id='ceased-show-hide']",SelectorType.XPATH);
            Assert.assertEquals(Browser.navigate().findElements(By.xpath("//tbody//tr")).size(),7);

        });
        When("^i make changes to the safety and compliance page$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","safety and compliance");
            replaceText("//*[@id='licence[safetyInsVehicles]']", "6");
            click("//*[@id='licence[safetyInsVaries]']", SelectorType.XPATH);
            click("//*[contains(@value,'tach_external')]", SelectorType.XPATH);
            enterText("//*[@id='licence[tachographInsName]']", externalAnalysisBureau, SelectorType.XPATH);
            click("//*[@id='add']", SelectorType.XPATH);
            click("//*[contains(text(),'An owner or employee')]", SelectorType.XPATH);
            enterText("//*[@id='contactDetails[fao]']",safetyInspector,SelectorType.XPATH);
            clickByLinkText("Enter the address yourself");
            enterText("//*[@id='addressLine1']",registeredAddress1,SelectorType.XPATH);
            enterText("//*[@id='address[addressLine2]']",registeredAddress2,SelectorType.XPATH);
            enterText("//*[@id='address[addressLine3]']",registeredAddress3,SelectorType.XPATH);
            enterText("//*[@id='address[addressLine4]']",registeredAddress4,SelectorType.XPATH);
            enterText("//*[@id='addressTown']",registeredAddressTown,SelectorType.XPATH);
            enterText("//*[@id='postcode']",registeredAddressPostCode,SelectorType.XPATH);
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForTextToBePresent("Safety inspectors");
            click("//*[@id='form-actions[save]']", SelectorType.XPATH);
        });
        Then("^the changes to the safety and compliance page are made$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","safety and compliance");

            checkValue("//*[@id='licence[safetyInsVehicles]']", SelectorType.XPATH, "6");
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[@id='licence[safetyInsVaries]']")).isSelected());
            Assert.assertTrue(Browser.navigate().findElement(By.xpath("//*[contains(@value,'tach_external')]")).isSelected());
            checkValue("//*[@id='licence[tachographInsName]']", SelectorType.XPATH, externalAnalysisBureau);
            checkForFullMatch(safetyInspector);
            checkForFullMatch(registeredAddress1 + ", " + registeredAddressTown);

        });
    }
}
