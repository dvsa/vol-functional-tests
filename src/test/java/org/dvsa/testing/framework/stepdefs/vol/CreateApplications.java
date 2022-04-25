package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateApplications extends BasePage implements En {
    public CreateApplications(World world) {
        When("^i choose to print and sign$", () -> {
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            waitAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        });
        Then("^the application should be submitted$", () -> {
            waitForTitleToBePresent("Application overview");
            assertTrue(isTextPresent("Your application reference number is"));
        });
        When("^i pay for my application$", () -> {
            waitAndClick("//*[@name='form-actions[pay]']", SelectorType.XPATH);
            world.feeAndPaymentJourney.customerPaymentModule();
            waitForTitleToBePresent("Application overview");
        });
        And("^i choose to pay my second application with my saved card details$", () -> {
            clickByLinkText("Home");
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().skip(1).findAny().ifPresent(WebElement::click);
            waitAndClick("//*[contains(text(),'Review and declarations')]", SelectorType.XPATH);
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            waitAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
            waitForTextToBePresent("Would you like to use a stored card?");
            selectValueFromDropDownByIndex("storedCards[card]", SelectorType.NAME, 1);
            waitAndClick("form-actions[pay]", SelectorType.NAME);
            waitAndEnterText("csc",  SelectorType.NAME,"265");
            world.feeAndPaymentJourney.enterCardHolderDetails();
            waitAndClick("_eventId_payment", SelectorType.NAME);
            waitForTitleToBePresent("Application overview");
        });
        Given("^i set to have (\\d+) lgvs on my licence$", (Integer numberOfLGVs) -> {
            world.createApplication.setTotalOperatingCentreLgvAuthority(numberOfLGVs);
        });
    }
}