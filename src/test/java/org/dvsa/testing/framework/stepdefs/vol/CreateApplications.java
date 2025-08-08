package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateApplications extends BasePage {
    World world;

    public CreateApplications(World world) {
        this.world = world;
    }

    @When("i choose to print and sign")
    public void iChooseToPrintAndSign() {
        refreshPageWithJavascript();
        if (!getCurrentUrl().contains("tm-declaration") && (getCurrentUrl().contains("dashboard"))) {
            waitForTextToBePresent("Applications for a new licence");
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
            scrollAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            scrollAndClick("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        } else {
            refreshPageWithJavascript();
            waitForTitleToBePresent("Declaration");
            scrollAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            scrollAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        }
    }

    @Then("the application should be submitted")
    public void theApplicationShouldBeSubmitted() {
        refreshPage();
        if (isElementPresent("confirm", SelectorType.ID)) {
            waitAndClick("confirm", SelectorType.ID);
        } else {
            waitForTitleToBePresent("Application overview");
        }
        assertTrue(isTextPresent("Your application reference number is"));
    }

    @When("i pay for my application")
    public void iPayForMyApplication() {
        UniversalActions.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        waitForTextToBePresent("Confirm your payment");
        waitAndClick("confirm", SelectorType.ID);
    }

    @And("i pay my second application with my saved card details")
    public void iPayMySecondApplicationWithMySavedCardDetails() {
        if (!world.configuration.env.equals(EnvironmentType.PREPRODUCTION)) {
            waitForTitleToBePresent("Application overview");

            String previousAppId = String.valueOf(Integer.parseInt(world.createApplication.getApplicationId()) - 1);

              UniversalActions.clickHome();

            getDriver().findElements(By.xpath("//*[@class='table__wrapper'][last()]//td"))
                    .stream()
                    .distinct()
                    .filter(element -> element.getText().contains(previousAppId))
                    .findFirst()
                    .ifPresent(WebElement::click);
        }

        waitForTextToBePresent("Review and declarations");
        if (!world.configuration.env.equals(EnvironmentType.PREPRODUCTION)) {
            waitAndClick("//*[contains(text(),'Review and declarations')]", SelectorType.XPATH);
        }

        waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        UniversalActions.ClickPayAndSubmit();
        waitForTextToBePresent("Would you like to use a stored card?");
        selectValueFromDropDownByIndex("storedCards[card]", SelectorType.NAME, 1);
        UniversalActions.clickPay();
        waitAndEnterText("csc", SelectorType.NAME, "265");
        world.feeAndPaymentJourney.enterCardHolderDetails();
        waitAndClick("_eventId_payment", SelectorType.NAME);

        if (world.configuration.env.equals(EnvironmentType.PREPRODUCTION)) {
            switchToIframe("scp_threeDSecure_iframe");
            waitAndClick("//*[@id='authenticateSubmit']", SelectorType.XPATH);
        }

        waitForTitleToBePresent("Application overview");
    }

    @And("i create a new {string} {string} application")
    public void iCreateANewApplication(String operatorType, String licenceType) {
        click("//*[@id=\"main-content\"]/div[2]/div[1]/div[4]/a", SelectorType.XPATH);
        waitForTitleToBePresent("Type of licence");
        findSelectAllRadioButtonsByValue("N");
            if(operatorType.equals("public")){
                findSelectAllRadioButtonsByValue("lcat_psv");}
            else if(operatorType.equals("goods")){
                findSelectAllRadioButtonsByValue("lcat_gv");}
            switch (licenceType) {
                case "restricted" -> findSelectAllRadioButtonsByValue("ltyp_r");
                case "standard_national" -> findSelectAllRadioButtonsByValue("ltyp_sn");
                case "standard_international" -> findSelectAllRadioButtonsByValue("ltyp_si");
                case "special_restricted" -> findSelectAllRadioButtonsByValue("ltyp_sr");
            }
        UniversalActions.clickSaveAndReturn();
    }
}
