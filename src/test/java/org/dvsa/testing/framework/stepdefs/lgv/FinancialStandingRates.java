package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;

public class FinancialStandingRates extends BasePage {
    World world;
    HashMap<String, String> effectiveDate = world.globalMethods.date.getDateHashMap(0, 0, 0);

    String adminDropdown = "//li[@class='admin__title']";
    String financialStandingAdminLink = "//a[@id='menu-admin-dashboard/admin-financial-standing']";

    String financialStandingTitle = "Financial standing rates";
    String addButton = "//button[@id='add']";
    String addedAlert = "//p[@role='alert' and contains(text(),'Created record')]";

    String addModalTitle = "//*[@id='modal-title']";
    String goodsRadioButton = "//*[@name='details[goodsOrPsv]' and @value='lcat_gv']";
    String standardInternationalRadioButton = "//*[@name='details[licenceType]' and @value='ltyp_si']";
    String LGVRadioButton = "//*[@name='details[vehicleType]' and @value='fin_sta_veh_typ_lgv']";

    String firstVehicleRate = "//*[@name='details[firstVehicleRate]']";
    String additionalVehicleRate = "//*[@name='details[additionalVehicleRate]']";

    String effectiveDateFieldPartialSelector = "details[effectiveFrom]";
    String saveButton = "//*[@id='form-actions[submit]']";



    public FinancialStandingRates(World world) { this.world = world; }

    @When("i am on the financial standing rates page")
    public void iAmOnTheFinancialStandingRatesPage() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        click(adminDropdown, SelectorType.XPATH);
        click(financialStandingAdminLink, SelectorType.XPATH);
        waitForTitleToBePresent(financialStandingTitle);
    }

    @When("i add a financial standing rate")
    public void iAddAFinancialStandingRate() {
        click(addButton, SelectorType.XPATH);
        waitForElementToBePresent(addModalTitle);
        click(goodsRadioButton, SelectorType.XPATH);
        click(standardInternationalRadioButton, SelectorType.XPATH);
        click(LGVRadioButton, SelectorType.XPATH);
        enterText(firstVehicleRate, SelectorType.XPATH, "2000");
        enterText(additionalVehicleRate, SelectorType.XPATH, "500");
        replaceDateFieldsByPartialId(effectiveDateFieldPartialSelector, effectiveDate);
        click(saveButton, SelectorType.XPATH);
        waitForElementToBePresent(addedAlert);
    }

    @Then("the table displays the correct financial standing rate information")
    public void theTableDisplaysTheCorrectFinancialStandingRateInformation() {

    }

    @And("the database contains the correct financial standing rate information")
    public void theDatabaseContainsTheCorrectFinancialStandingRateInformation() {

    }
}
