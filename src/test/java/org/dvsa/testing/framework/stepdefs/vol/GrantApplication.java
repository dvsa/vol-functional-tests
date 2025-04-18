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
        }else{
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
        clickByLinkText("Operating centres");
        waitForTextToBePresent("Operating centres");
        selectValueFromDropDown("trafficArea", SelectorType.ID, "Wales");
        if(getText("dataTrafficArea[enforcementArea]", SelectorType.ID).contains("Wales")) {
            selectValueFromDropDown("dataTrafficArea[enforcementArea]", SelectorType.ID, "Wales");
        }else{
            selectValueFromDropDownByIndex("dataTrafficArea[enforcementArea]", SelectorType.ID,1);
        }
        UniversalActions.clickSaveAndReturn();
        clickByLinkText("Review and declarations");
        waitAndClick("declarations[declarationConfirmation]", SelectorType.ID);
        UniversalActions.clickSaveAndReturn();
        clickByLinkText("Grant application");
        waitForTextToBePresent("Grant application");
        waitAndClick("//*[@value='grant_authority_tc']", SelectorType.XPATH);
        waitAndClick("form-actions[continue-to-grant]", SelectorType.ID);
        waitAndClick("//*[@value='N']", SelectorType.XPATH);
        waitAndClick("form-actions[grant]", SelectorType.ID);
    }

    private void payGrantFees() {
        clickByLinkText("Fees");
        waitAndClick("checkall", SelectorType.ID);
        waitAndClick("pay", SelectorType.ID);
        world.feeAndPaymentJourney.payFee(null, "card");
    }
}