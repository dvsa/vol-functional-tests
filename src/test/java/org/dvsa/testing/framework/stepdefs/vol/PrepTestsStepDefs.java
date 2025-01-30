package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrepTestsStepDefs extends BasePage {

    private final World world;

    Initialisation initialisation;

    public PrepTestsStepDefs(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Before
    public void setEnvironmentToInt() {
        world.configuration.env = EnvironmentType.INTEGRATION;
    }

    @Given("I have a prep {string} account")
    public void iHaveAPrepAccount(String accountType) {
        if (accountType.equalsIgnoreCase("internal")) {
            world.internalNavigation.navigateToLoginPage();
            world.globalMethods.signIn(SecretsManager.getSecretValue("intPrepUser"),
                    SecretsManager.getSecretValue("intEnvPassword"));
        } else if (accountType.equalsIgnoreCase("self serve")) {
            world.selfServeNavigation.navigateToLoginPage();
            world.globalMethods.signIn(SecretsManager.getSecretValue("prepUser"),
                    SecretsManager.getSecretValue("intEnvPassword"));
        } else {
            throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
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

    @Then("that user should be displayed in the list")
    public void thatUserShouldBeDisplayedInTheList() {
        String expectedEmail = world.DataGenerator.getOperatorUserEmail();
        WebElement emailCell = getDriver().findElement(By.xpath("//td[@data-heading='Email address' and contains(text(), '" + expectedEmail + "')]"));
        assertTrue(emailCell.isDisplayed(), "User's email is not displayed in the list");
    }


    @And("I add a case")
    public void iAddACase() {
        world.internalUIJourney.addAPrepCase();
    }

    @Then("that case has been created")
    public void thatCaseHasBeenCreated() {
        assertTrue(isTextPresent("Created record"));
        WebElement descriptionElement = getDriver().findElement(By.xpath("//dt[text()='Description']/following-sibling::dd"));
        assertTrue(descriptionElement.getText().contains("testing"), "Description 'testing' is not present in the case details");
    }

    @Then("that licence number and conversation should be visible")
    public void thatLicenceNumberAndConversationShouldBeVisible() {
        assertTrue(isTextPresent("OB1057273"));
        WebElement messageContentElement = getDriver().findElement(By.xpath("//div[@class='govuk-summary-card__content']/p"));
        assertTrue(messageContentElement.getText().contains(world.messagingJourney.getRandomWord()), "Message content 'GnHDzYfVZx' is not present in the summary card");

    }
}
