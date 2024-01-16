package org.dvsa.testing.framework.stepdefs.vol;

import dev.failsafe.internal.util.Assert;
import io.cucumber.java.en.Then;
import org.apache.hc.core5.http.HttpException;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InternalSearch extends BasePage {
    private final World world;
    public InternalSearch(World world) {
        this.world = world;
    }
    @When("i search for and click on my licence")
    public void iSearchForAndClickOnMyLicence() throws HttpException {
        if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
            world.internalSearchJourney.searchAndViewLicence();
        } else {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewLicence();
        }
    }
    @When("i search for and click on my application")
    public void iSearchForAndClickOnMyApplication() throws HttpException {
        if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
            world.internalSearchJourney.searchAndViewApplication();
        } else {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewApplication();
        }
    }
    @When("i search for and click on my case")
    public void iSearchForAndClickOnMyCase() throws HttpException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewCase();
    }
    @When("i search for my psv disc and click on my licence and discs")
    public void iSearchForMyPsvDiscAndClickOnMyLicenceAndDiscs() throws HttpException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewPSVDisc();
    }
    @When("i search for my address and click on my licence and addresses")
    public void iSearchForMyAddressAndClickOnMyLicenceAndAddresses() throws HttpException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalSearchJourney.searchAndViewAddress();
    }
    @And("i create an admin and url search for my licence")
    public void iCreateAnAdminAndUrlSearchForMyLicence() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
    }
    @And("i create an admin and url search for my application")
    public void iCreateAnAdminAndUrlSearchForMyApplication() throws HttpException {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
    }
    @And("i create an admin and url search for my variation")
    public void iCreateAnAdminAndUrlSearchForMyVariation() throws HttpException {
        world.internalNavigation.navigateToPage("variation", SelfServeSection.VIEW);
    }
    @And("i url search for my licence")
    public void iUrlSearchForMyLicence() {
        world.internalNavigation.getLicence();
    }
    @And("i url search for my application")
    public void iUrlSearchForMyApplication() {
        world.internalNavigation.getApplication();
    }

    @When("i search for a company and click Create operator")
    public void iSearchForACompanyAndClickCreateOperator() {
        world.internalSearchJourney.searchForLicenceByName("Company Name");
        waitForTextToBePresent("Search results");
        clickByLinkText("Create operator");
    }

    @And("i enter and search for a Company number")
    public void iEnterAndSearchForACompanyNumber() {
        String companyNumber = "12345678";
        waitForTextToBePresent("Business details");
        enterText("operator-details[companyNumber][company_number]", SelectorType.ID, companyNumber);
        clickById("operator-details[companyNumber][submit_lookup_company]");
    }

    @Then("the operator details should be populated")
    public void theOperatorDetailsShouldBePopulated() {
        assertNotNull(getText("operator-details[name]", SelectorType.ID));
        assertNotNull(getText("registeredAddress[addressLine1]", SelectorType.ID));
    }
}