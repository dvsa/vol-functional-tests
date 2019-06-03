package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import enums.TrafficArea;
import org.dvsa.testing.framework.Utils.Generic.EnforcementArea;
import org.dvsa.testing.framework.Utils.Generic.PostCode;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateApplications extends BasePage implements En {
    public CreateApplications(World world) {
        Given("^i have a \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" licence in traffic area$", (String operatorType, String licenceType, String Region,String trafficArea) -> {
            if(Region.equals("NI".toUpperCase())){
                Region = "Y";
            }else{
                Region = "N";
            }
            world.APIJourneySteps.registerAndGetUserDetails();
            world.createLicence.setNiFlag(Region);
            world.createLicence.setPostcode(PostCode.getPostCode(TrafficArea.valueOf(trafficArea.toUpperCase())));
            world.createLicence.setOperatorType(operatorType);
            world.createLicence.setLicenceType(licenceType);
            world.createLicence.setEnforcementArea(EnforcementArea.getEnforcementArea(TrafficArea.valueOf(trafficArea.toUpperCase())));
            world.createLicence.setTrafficArea(String.valueOf(TrafficArea.valueOf(trafficArea.toUpperCase())));
            world.APIJourneySteps.createApplication();
            world.APIJourneySteps.submitApplication();
        });
        When("^i choose to print and sign$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().findFirst().ifPresent(WebElement::click);
            world.UIJourneySteps.navigateThroughApplication();
            click("//*[contains(text(),'Print')]", SelectorType.XPATH);
            click("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        });
        Then("^the application should be submitted$", () -> {
            assertTrue(isTextPresent("Thank you, your application has been submitted.", 20));
        });
        When("^i pay for my application$", () -> {
            click("//*[@name='form-actions[pay]']", SelectorType.XPATH);
            world.UIJourneySteps.payFee(null, "card", "4006000000000600", "10", "20");
        });
        And("^i choose to pay my second application with my saved card details$", () -> {
            clickByLinkText("Home");
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().skip(1).findAny().ifPresent(WebElement::click);
            waitAndClick("submitPay", SelectorType.NAME);
            selectValueFromDropDownByIndex("storedCards[card]", SelectorType.NAME, 1);
            waitAndClick("form-actions[pay]", SelectorType.NAME);
            enterText("scp_additionalInformationPage_csc_input","265", SelectorType.ID);
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
        });
    }
}