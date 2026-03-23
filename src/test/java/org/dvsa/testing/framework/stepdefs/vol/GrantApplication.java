package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.openqa.selenium.WebElement;


import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrantApplication extends BasePage {
    World world;
    private ValidatableResponse apiResponse;

    ReadWriteLock lock = new ReentrantReadWriteLock();

    public GrantApplication(World world) {
        this.world = world;
    }

    @When("I grant licence")
    public void iGrantLicence() throws HttpException {
        lock.writeLock().lock();
        apiResponse = world.grantApplication.grantLicence();
        lock.writeLock().unlock();
    }

    @Then("the licence should be granted")
    public void theLicenceShouldBeGranted() throws HttpException {
        if (world.licenceCreation.isGoodsLicence()) {
            apiResponse = world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        }
        assertTrue(apiResponse.extract().response().asString().contains("documents\\/Licensing\\/Other_Documents"));
    }

    @Then("the licence should be granted by a caseworker")
    public void theApplicationShouldBeGranted() throws HttpException {
        String env = world.configuration.env.toString();
        if (!env.equals("int")) {
            world.internalNavigation.logInAsAdmin();
            world.internalNavigation.getApplication(world.createApplication.getApplicationId());
            overrideOppositionAndDates();
        } else {
            String[] licenceNumber = getText("h2", SelectorType.CSS).split("/");
            world.internalNavigation.loginIntoInternal("intSystemAdmin");
            world.internalSearchJourney.internalSearchUntilTextPresent(SearchType.Licence, licenceNumber[0].trim(), licenceNumber[0].trim());
            overrideOppositionAndDates();
        }
    }

    private void overrideOppositionAndDates() {
        waitAndClick("details[overrideOppositionDate]", SelectorType.ID);
        findElements("//*[@value]", SelectorType.XPATH)
                .stream().filter(x -> x.getText().equals("Accepted")).forEach(WebElement::click);
        UniversalActions.clickSaveAndReturn();
        waitAndClickByLinkText("Operating centres");
        waitForTextToBePresent("Operating centres");
        selectValueFromDropDown("trafficArea", SelectorType.ID, "Wales");
        if (getText("dataTrafficArea[enforcementArea]", SelectorType.ID).contains("Wales")) {
            selectValueFromDropDown("dataTrafficArea[enforcementArea]", SelectorType.ID, "Wales");
        } else {
            selectValueFromDropDownByIndex("dataTrafficArea[enforcementArea]", SelectorType.ID, 1);
        }
        UniversalActions.clickSaveAndReturn();
        waitAndClickByLinkText("Review and declarations");
        waitAndClick("declarations[declarationConfirmation]", SelectorType.ID);
        UniversalActions.clickSaveAndReturn();
        waitAndClickByLinkText("Grant application");
        waitForTextToBePresent("Grant application");
        waitAndClick("//*[@value='grant_authority_tc']", SelectorType.XPATH);
        waitAndClick("form-actions[continue-to-grant]", SelectorType.ID);
        waitAndClick("//*[@value='N']", SelectorType.XPATH);
        waitAndClick("form-actions[grant]", SelectorType.ID);
    }

    @Then("I pay the grant fee")
    public void iPayTheGrantFee() {
        waitAndClickByLinkText("Fees");
        waitAndClick("checkall", SelectorType.ID);
        waitAndClick("pay", SelectorType.ID);

        List<WebElement> feeAmountElements = findElements("td[data-heading='Fee amount']", SelectorType.CSS);
        double totalFeeAmount = 0.0;
        
        for (WebElement feeElement : feeAmountElements) {
            String feeText = feeElement.getText().replace("£", "").trim();
            totalFeeAmount += Double.parseDouble(feeText);
        }
        
        // Convert back to string format for payment
        String totalFeeAmountString = String.format("%.2f", totalFeeAmount);
        world.feeAndPaymentJourney.payFee(totalFeeAmountString, "cash");
    }

    private void grantingTheApplication() {
        waitAndClickByLinkText("Grant application");
        waitForTextToBePresent("Grant application");
        waitAndClick("//*[@value='grant_authority_tc']", SelectorType.XPATH);
        waitAndClick("form-actions[continue-to-grant]", SelectorType.ID);
        waitAndClick("//*[@value='N']", SelectorType.XPATH);
        waitAndClick("form-actions[grant]", SelectorType.ID);
    }

    @When("I click the Grant Application button")
    public void iClickTheGrantApplicationButton() {
        grantingTheApplication();
    }

    @Then("validation should be checked and the application should be granted if valid")
    public void validationShouldBeCheckedAndTheApplicationShouldBeGrantedIfValid() {
        waitForTextToBePresent("Granted");
        String licenceStatus = getText("//dt[text()='Licence status']/following-sibling::dd//strong", SelectorType.XPATH);
        assertTrue(licenceStatus.contains("Valid"), "Expected licence status to be 'Valid' but was: " + licenceStatus);
    }

    @Then("the application status should be Awaiting Grant Fee")
    public void theApplicationStatusShouldBeAwaitingGrantFee() {
        waitForTextToBePresent("Awaiting grant fee");
        String licenceStatus = getText("//dt[text()='Licence status']/following-sibling::dd//strong", SelectorType.XPATH);
        assertTrue(licenceStatus.contains("Awaiting grant fee"), "Expected licence status to be 'Awaiting grant fee' but was: " + licenceStatus);
    }
}
