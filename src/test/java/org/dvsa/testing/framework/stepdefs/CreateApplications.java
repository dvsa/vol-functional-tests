package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateApplications extends BasePage implements En {
    public CreateApplications(World world) {
        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

        When("^i choose to print and sign$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            Browser.navigate().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td")).stream().findFirst().ifPresent(WebElement::click);
            clickByLinkText("Review and declarations");
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            waitAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        });
        Then("^the application should be submitted$", () -> {
            waitForTitleToBePresent("Application overview");
            assertTrue(isTextPresent("Your application reference number is",30));
        });
        When("^i pay for my application$", () -> {
            waitAndClick("//*[@name='form-actions[pay]']", SelectorType.XPATH);
            world.feeAndPaymentJourneySteps.customerPaymentModule();
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
            waitAndEnterText("scp_additionalInformationPage_csc_input",  SelectorType.ID,"265");
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
            waitAndClick("//*[@type='submit']", SelectorType.XPATH);
            waitForTitleToBePresent("Application overview");
        });
    }
}