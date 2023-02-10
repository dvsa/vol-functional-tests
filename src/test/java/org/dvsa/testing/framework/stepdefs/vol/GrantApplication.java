package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

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


        List<WebElement> applications = Browser.navigate().findElements(By.xpath("//*[@class='govuk-table__cell']"));
        for (WebElement application : applications) {
            if (application.getText().equals(""))
                application.click();
        }
    }
}