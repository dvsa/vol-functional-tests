package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import com.sun.istack.NotNull;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExternalSearch extends BasePage {

    private final World world;

    public ExternalSearch(World world) {
        this.world = world;
    }

    @And("i login as a partner user")
    public void iLoginAsAPartnerUser() {
        String user = SecretsManager.getSecretValue("partnerUser");
        String password = SecretsManager.getSecretValue("partnerUserPassword");
        String intUser = SecretsManager.getSecretValue("intPartnerUser");
        String intPassword = SecretsManager.getSecretValue("intEnvPassword");

        if (Objects.requireNonNull(getDriver().getCurrentUrl()).contains("dashboard")) {
            clickByLinkText("Sign out");
        }
        String externalURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
        get(externalURL);
        waitForTextToBePresent("Password");
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            world.globalMethods.signIn(intUser, intPassword);
        } else {
            world.globalMethods.signIn(user, password);
        }
        if (isTextPresent("Welcome to your account")) {
            click("termsAgreed", SelectorType.ID);
            UniversalActions.clickSubmit();
        }
        waitAndClick("Lorry and bus operators", SelectorType.PARTIALLINKTEXT);
    }

    @And("I am on the external search page")
    public void iAmOnTheExternalSearchPage() {
        world.selfServeNavigation.navigateToFindLorryAndBusOperatorsSearch();
    }

    @Then("search results page addresses {string} should only display address belonging to our licence {string}")
    public void searchResultsPageAddressesShouldOnlyDisplayAddressBelongingToOurLicence(String address, String licence) {
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", address), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(licence));
    }

    @Then("search results page should display operator names containing our {string}")
    public void searchResultsPageShouldDisplayOperatorNamesContainingOurBusinessName(String businessName) {
        assertTrue(isTextPresent(businessName));
    }

    @And("I am able to view the applicants licence number")
    public void iAmAbleToViewTheApplicantsLicenceNumber() {
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createApplication.getOrganisationName()), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }

    @And("I am able to view the licence number")
    public void iAmAbleToViewTheLicenceNumber() {
        String licenceNumber = world.applicationDetails.getLicenceNumber();
        String orgName = world.createApplication.getOrganisationName();
        waitForTextToBePresent(orgName);
        String rowText = getText(String.format("//tr[td[contains(text(), '%s')]]", orgName), SelectorType.XPATH);
        assertTrue(rowText.contains(licenceNumber));
    }

    @Then("search results page should display the name {string}")
    public void searchResultsPageShouldDisplayTheName(String name) {
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", name), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(String.valueOf(name.equalsIgnoreCase(name.toUpperCase()))));
    }


    @When("I search for a lorry and bus operator by {string} with licence number {string}, business name {string}, person {string} and address {string}")
    @When("I search for a lorry and bus operator by {string},{string},{string},{string},{string}")
    public void iSearchForALorryAndBusOperatorBy(String searchType, String licenceNumber, String businessName, String person, String address) {
        findSelectAllRadioButtonsByValue(searchType);
        String addressToSearch;
        String businessNameToSearch;
        String licenceNumberToSearch;
        String personToSearch;

        addressToSearch = address;
        businessNameToSearch = businessName;
        licenceNumberToSearch = licenceNumber;
        personToSearch = person;

        switch (searchType) {
            case "address":
                world.selfServeNavigation.enterAndSearchUntilTextIsPresent("search", SelectorType.ID, addressToSearch);
                break;
            case "business":
                world.selfServeNavigation.enterAndSearchUntilTextIsPresent("search", SelectorType.ID, businessNameToSearch);
                break;
            case "licence":
                world.selfServeNavigation.enterAndSearchUntilTextIsPresent("search", SelectorType.ID, licenceNumberToSearch);
                break;
            case "person":
                world.selfServeNavigation.enterAndSearchUntilTextIsPresent("search", SelectorType.NAME, personToSearch);
        }
    }

    @Then("search results page should only display our licence number")
    @Then("search results page should only display our {string}")
    public void searchResultsPageShouldOnlyDisplayOurLicenceNumber(String licenceNumber) {
        assertTrue(isTextPresent(licenceNumber));
    }

    @And("i search for a vehicle")
    public void iSearchForAVehicle() {
        String searchVrm = SecretsManager.getSecretValue("testLicenceVrm");
        waitForTitleToBePresent("Find vehicles");
        enterText("search", SelectorType.ID, searchVrm);
        clickById("submit");
    }

    @And("i navigate to partner vehicle search")
    public void iNavigateToPartnerVehicleSearch() {
        waitAndClick("menu-search-vehicle-external", SelectorType.ID);
    }

    @Then("the expected licence results should be shown")
    public void theExpectedLicenceResultsShouldBeShown() {
        assertTrue(isTextPresent("TEST USER (SELF SERVICE) (12345)"));
    }

    @And("i check if the licence contains any interim vehicles")
    public void iCheckIfTheLicenceContainsAnyInterimVehicles() {
        world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.applicationDetails.getLicenceNumber(), 300, "KickOut reached. Licence number external search failed.");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    @Then("the vehicle table should contain the interim status")
    public void theVehicleTableShouldContainTheInterimStatus() {
        List<WebElement> interimStatus = findElements("//*[@data-heading='Interim']", SelectorType.XPATH);
        for (WebElement interim : interimStatus) {
            assertEquals("No", interim.getText());
        }
    }
}