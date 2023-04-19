package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.WebElement;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExternalSearch extends BasePage {

    private final World world;

    public ExternalSearch(World world) {
        this.world = world;
    }

    @And("i login as a partner user")
    public void iLoginAsAPartnerUser() {
        String user = world.configuration.config.getString("partnerUser");
        String password = world.configuration.config.getString("partnerUserPassword");
        String intUser = world.configuration.config.getString("intPartnerUser");
        String intPassword = world.configuration.config.getString("intEnvPassword");

        if (getDriver().getCurrentUrl().contains("dashboard")) {
            clickByLinkText("Sign out");
        }
        String externalURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
        get(externalURL);
        waitForTextToBePresent("Password");
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            world.globalMethods.signIn(intUser, intPassword);
        } else
        {world.globalMethods.signIn(user, password);
        waitAndClick("Lorry and bus operators", SelectorType.PARTIALLINKTEXT);}
    }

    @And("I am on the external search page")
    public void iAmOnTheExternalSearchPage() {
        world.selfServeNavigation.navigateToFindLorryAndBusOperatorsSearch();
    }

    @Then("search results page addresses should only display address belonging to our post code")
    public void searchResultsPageAddressesShouldOnlyDisplayAddressBelongingToOurPostCode() {
        String clippedCorrespondenceAddress = String.format("%s, %s, %s, %s",
                world.createApplication.getCorrespondenceAddressLine3(),
                world.createApplication.getCorrespondenceAddressLine4(),
                world.createApplication.getCorrespondenceTown(),
                world.createApplication.getCorrespondencePostCode()
        );
        String clippedOperatingCentreAddress = String.format("%s, %s, %s, %s",
                world.createApplication.getOperatingCentreAddressLine3(),
                world.createApplication.getOperatingCentreAddressLine4(),
                world.createApplication.getOperatingCentreTown(),
                world.createApplication.getOperatingCentrePostCode()
        );
        world.selfServeNavigation.clickSearchWhileCheckingTextPresent(clippedCorrespondenceAddress, 240, "KickOut reached. Correspondence and operating centre address external search failed.");
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", clippedCorrespondenceAddress), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.createApplication.getOrganisationName()));
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
        tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", clippedOperatingCentreAddress), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.createApplication.getOrganisationName()));
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }

    @Then("search results page should display operator names containing our {string}")
    public void searchResultsPageShouldDisplayOperatorNamesContainingOurBusinessName(String businessName) {
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(businessName, 300, "KickOut reached. Operator name external search failed.");
            assertTrue(isTextPresent(businessName));
        }
        world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.createApplication.getOrganisationName(), 300, "KickOut reached. Operator name external search failed.");
    }

    @And("I am able to view the applicants licence number")
    public void iAmAbleToViewTheApplicantsLicenceNumber() {
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createApplication.getOrganisationName()), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }

    @And("I am able to view the licence number")
    public void iAmAbleToViewTheLicenceNumber() {
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", world.createApplication.getOrganisationName()), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }


    @Then("search results page should display names containing our operator name")
    public void searchResultsPageShouldDisplayNamesContainingOurOperatorName() {
        String operatorName = String.format("%s %s", world.createApplication.getDirectorForeName(), world.createApplication.getDirectorFamilyName());
        world.selfServeNavigation.clickSearchWhileCheckingTextPresent(operatorName, 420, "KickOut reached. Operator name external search failed.");
        WebElement tableRow = findElement(String.format("//tr[td[contains(text(),\"%s\")]]", operatorName), SelectorType.XPATH);
        assertTrue(tableRow.getText().contains(world.createApplication.getOrganisationName()));
        assertTrue(tableRow.getText().contains(world.applicationDetails.getLicenceNumber()));
    }


    @When("I search for a lorry and bus operator by {string} with licence number {string}, business name {string}, person {string} and address {string}")
    @When("I search for a lorry and bus operator by {string},{string},{string},{string},{string}")
    public void iSearchForALorryAndBusOperatorBy(String searchType, String licenceNumber, String businessName, String person, String address) {
        findSelectAllRadioButtonsByValue(searchType);
        String addressToSearch;
        String businessNameToSearch;
        String licenceNumberToSearch;
        String personToSearch;

        if (Objects.equals(world.configuration.env.toString(), "int")) {
            addressToSearch = address;
            businessNameToSearch = businessName;
            licenceNumberToSearch = licenceNumber;
            personToSearch = person;
        } else {
            addressToSearch = world.createApplication.getPostCodeByTrafficArea();
            businessNameToSearch = world.createApplication.getOrganisationName();
            licenceNumberToSearch = world.applicationDetails.getLicenceNumber();
            personToSearch = String.format("%s %s", world.createApplication.getDirectorForeName(), world.createApplication.getDirectorFamilyName());
        }

        switch (searchType) {
            case "address":
                enterText("search", SelectorType.NAME, addressToSearch);
                break;
            case "business":
                enterText("search", SelectorType.NAME, businessNameToSearch);
                break;
            case "licence":
                enterText("search", SelectorType.NAME, licenceNumberToSearch);
                break;
            case "person":
                enterText("search", SelectorType.NAME, personToSearch);
        }
    }

    @Then("search results page should only display our licence number")
    @Then("search results page should only display our {string}")
    public void searchResultsPageShouldOnlyDisplayOurLicenceNumber(String licenceNumber) {
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(licenceNumber, 300, "KickOut reached. Operator name external search failed.");
            assertTrue(isTextPresent(licenceNumber));
        } else {
            world.selfServeNavigation.clickSearchWhileCheckingTextPresent(world.applicationDetails.getLicenceNumber(), 300, "KickOut reached. Licence number external search failed.");
        }
    }

    @And("i search for a vehicle")
    public void iSearchForAVehicle() {
        waitForTitleToBePresent("Find vehicles");
        enterText("search", SelectorType.ID, "ABC123");
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
}