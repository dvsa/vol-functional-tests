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
    }
}
