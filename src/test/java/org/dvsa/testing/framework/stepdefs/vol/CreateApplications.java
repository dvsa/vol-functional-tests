package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateApplications extends BasePage {
    World world;

    public CreateApplications(World world) {
        this.world = world;
    }

    @When("i choose to print and sign")
    public void iChooseToPrintAndSign() {
        refreshPageWithJavascript();
        if(!getCurrentUrl().contains("tm-declaration")) {
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            waitAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        }else {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            waitAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        }
    }

    @Then("the application should be submitted")
    public void theApplicationShouldBeSubmitted() {
        waitForTitleToBePresent("Application overview");
        assertTrue(isTextPresent("Your application reference number is"));
    }

    @When("i pay for my application")
    public void iPayForMyApplication() {
        world.UIJourney.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        waitForTitleToBePresent("Application overview");
    }

    @And("i pay my second application with my saved card details")
    public void iPayMySecondApplicationWithMySavedCardDetails() {
        String app = String.valueOf(Integer.parseInt(world.createApplication.getApplicationId()) - 1);
        clickByLinkText("Home");
        getDriver().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td"))
                .stream()
                .distinct()
                .filter(x -> x.getText().contains(app))
                .findAny().ifPresent(WebElement::click);

        waitAndClick("//*[contains(text(),'Review and declarations')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        waitAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        waitForTextToBePresent("Would you like to use a stored card?");
        selectValueFromDropDownByIndex("storedCards[card]", SelectorType.NAME, 1);
        world.UIJourney.clickPay();
        waitAndEnterText("csc", SelectorType.NAME, "265");
        world.feeAndPaymentJourney.enterCardHolderDetails();
        waitAndClick("_eventId_payment", SelectorType.NAME);
        waitForTitleToBePresent("Application overview");
    }

    @Given("i set to have {int} lgvs on my licence")
    public void iSetToHaveLGVSOnMyLicence(Integer numberOfLGVs) {
        world.createApplication.setTotalOperatingCentreLgvAuthority(numberOfLGVs);
    }
}