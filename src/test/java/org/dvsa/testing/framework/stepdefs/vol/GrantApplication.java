package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrantApplication extends BasePage {
    private final World world;
    private ValidatableResponse apiResponse;

    public GrantApplication(World world) {
        this.world = world;
    }

    @When("I grant licence")
    public void iGrantLicence() throws HttpException {
        apiResponse = world.grantApplication.grantLicence();
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
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.internalSearchUntilTextPresent(SearchType.Application, world.submitApplicationJourney.getApplicationNumber(), world.submitApplicationJourney.getApplicationNumber());
        overrideOppositionAndDates();
    }

    private void overrideOppositionAndDates() {
        waitAndClick("details[overrideOppositionDate]", SelectorType.ID);
        findElements("//*[@value]", SelectorType.XPATH)
                .stream().filter(x -> x.getText().equals("Accepted")).forEach(WebElement::click);
        UIJourney.clickSaveAndReturn();
        clickByLinkText("Operating centres");
        waitForTextToBePresent("Operating centres");
        selectValueFromDropDown("dataTrafficArea[enforcementArea]", SelectorType.ID, "East");
        UIJourney.clickSaveAndReturn();
        clickByLinkText("Review and declarations");
        waitAndClick("declarations[declarationConfirmation]", SelectorType.ID);
        UIJourney.clickSaveAndReturn();
        clickByLinkText("Grant application");
        waitForTextToBePresent("Grant application");
        waitAndClick("//*[@value='grant_authority_tc']", SelectorType.XPATH);
        waitAndClick("form-actions[continue-to-grant]", SelectorType.ID);
        waitAndClick("//*[@value='N']", SelectorType.XPATH);
        waitAndClick("form-actions[grant]", SelectorType.ID);
        if (world.submitApplicationJourney.getLicence().equals("Goods")) {
            payGrantFees();
        }
        assertEquals("GRANTED", findElement("//*[@class='govuk-tag govuk-tag--green']", SelectorType.XPATH).getText());
    }

    private void payGrantFees() {
        clickByLinkText("Fees");
        waitAndClick("checkall", SelectorType.ID);
        waitAndClick("pay", SelectorType.ID);
        world.feeAndPaymentJourney.payFee(null, "card");
    }
}