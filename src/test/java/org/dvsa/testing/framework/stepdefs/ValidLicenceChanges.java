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
    String registeredAddressPostCode = "tes23de";
    String companyName = "test company name";
    String companyNumber = "test1234";

    String forAttentionOf = "testName";
    String phoneNumber = "0800 001 066";
    String secondaryPhoneNumber = "0800 001 067";
    String email = "tester@testersAA.com";
    String transportConsultantName = "Harold Hastings";


    public ValidLicenceChanges(World world) {
        When("^i make changes to the business details page$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","business details");
            enterText("//*[@id='data[tradingNames][0][name]']",tradingName,SelectorType.XPATH);
            click("//a[@class='add-another-trigger']",SelectorType.XPATH);
            enterText("//*[@id='data[tradingNames][1][name]']",tradingName2,SelectorType.XPATH);
            clearAndEnterText("//*[@id='natureOfBusiness']",SelectorType.XPATH,natureOfBusiness);
            clearAndEnterText("//*[@id='addressLine1']",SelectorType.XPATH,registeredAddress1);
            clearAndEnterText("//*[@id='registeredAddress[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            clearAndEnterText("//*[@id='registeredAddress[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            clearAndEnterText("//*[@id='registeredAddress[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            clearAndEnterText("//*[@id='registeredAddress[town]']",SelectorType.XPATH,registeredAddressTown);
            clearAndEnterText("//*[@id='registeredAddress[postcode]']",SelectorType.XPATH,registeredAddressPostCode);
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
            Assert.assertEquals(Browser.getDriver().findElement(By.xpath("//td[2]")).getText(),companyNumber);
            Assert.assertTrue(Browser.getDriver().findElement(By.xpath("//*[@id='allow-email[allowEmail]']")).isSelected());
        });
        When("^i make changes to the addresses page$", () -> {
            world.UIJourneySteps.navigateToSelfServePage("licence","addresses");
            clearAndEnterText("//*[@id='correspondence[fao]']",SelectorType.XPATH,forAttentionOf);
            clearAndEnterText("//*[@id='addressLine1']",SelectorType.XPATH,registeredAddress1);
            clearAndEnterText("//*[@id='correspondence_address[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            clearAndEnterText("//*[@id='correspondence_address[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            clearAndEnterText("//*[@id='correspondence_address[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            clearAndEnterText("//*[@id='addressTown']",SelectorType.XPATH,registeredAddressTown);
            clearAndEnterText("//*[@id='postcode']",SelectorType.XPATH,registeredAddressPostCode);
            selectValueFromDropDownByIndex("//*[@id='correspondence_address[countryCode]']",SelectorType.XPATH,5);
            clearAndEnterText("//*[@id='phone_primary']",SelectorType.XPATH,phoneNumber);
            clearAndEnterText("//*[@id='$phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
            clearAndEnterText("//*[@id='email']",SelectorType.XPATH,email);
            clearAndEnterText("//*[@id='establishment_address[addressLine1]']",SelectorType.XPATH,registeredAddress1);
            clearAndEnterText("//*[@id='establishment_address[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            clearAndEnterText("//*[@id='establishment_address[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            clearAndEnterText("//*[@id='establishment_address[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            clearAndEnterText("//*[@id='establishment_address[town]']",SelectorType.XPATH,registeredAddressTown);
            clearAndEnterText("//*[@id='postcodeOptional']",SelectorType.XPATH,registeredAddressPostCode);
            click("//*[@id='consultant[add-transport-consultant]']",SelectorType.XPATH);
            click("//*[@id='written-permission-to-engage']",SelectorType.XPATH);
            clearAndEnterText("//*[@id='consultant[transportConsultantName]']",SelectorType.XPATH,transportConsultantName);
            clickByLinkText("Enter the address yourself");
            clearAndEnterText("//*[@id='addressLine1']",SelectorType.XPATH,registeredAddress1);
            clearAndEnterText("//*[@id='consultantAddress[addressLine2]']",SelectorType.XPATH,registeredAddress2);
            clearAndEnterText("//*[@id='consultantAddress[addressLine3]']",SelectorType.XPATH,registeredAddress3);
            clearAndEnterText("//*[@id='consultantAddress[addressLine4]']",SelectorType.XPATH,registeredAddress4);
            clearAndEnterText("//*[@id='addressTown']",SelectorType.XPATH,registeredAddressTown);
            clearAndEnterText("//*[@id='postcode']",SelectorType.XPATH,registeredAddressPostCode);
            selectValueFromDropDownByIndex("consultantAddress[countryCode]",SelectorType.XPATH,5);
            clearAndEnterText("//*[@id='tc_phone_primary']",SelectorType.XPATH,phoneNumber);
            clearAndEnterText("//*[@id='tc_phone_secondary']",SelectorType.XPATH,secondaryPhoneNumber);
            clearAndEnterText("//*[@id='consultantContact[email]",SelectorType.XPATH,email);
            click("form-actions[save]");
            wait();
        });
        Then("^the changes to the addresses page are made$", () -> {
        });
    }
}
