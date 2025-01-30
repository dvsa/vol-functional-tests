package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import activesupport.aws.s3.SecretsManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

public class PrepTestsStepDefs extends BasePage {

    private final World world;

    Initialisation initialisation;

    public PrepTestsStepDefs(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }


    @Given("I have a prep self serve account")
    public void iHaveAPrepSelfServeAccount() {
        world.selfServeNavigation.navigateToLoginPage();
        world.globalMethods.signIn(SecretsManager.getSecretValue("prepUser"),
                SecretsManager.getSecretValue("intEnvPassword"));
    }

    @And("I submit a {string} licence application")
    public void iSubmitALicenceApplication(String licenceType) throws IllegalBrowserException, IOException {
        world.submitApplicationJourney.startANewLicenceApplication(licenceType);
    }

    @When("I navigate to an existing licence {string}")
    public void navigateToExistingLicence(String licenceNumber) {
        clickByLinkText(licenceNumber);
    }

    @Then("that new transport manager is showing in the list")
    public void thatNewTransportManagerIsShowingInTheList() {
        WebElement transportManagerRow = getDriver().findElement(By.xpath("//td[@data-heading='Name']/a[contains(text(), 'prep-forename prep-familyname')]"));
        assertTrue(transportManagerRow.isDisplayed(), "Transport manager's name is not displayed in the list");
        WebElement emailCell = getDriver().findElement(By.xpath("//td[@data-heading='Email' and contains(text(), 'prep-email@example.com')]"));
        assertTrue(emailCell.isDisplayed(), "Transport manager's email is not displayed in the list");
    }
}