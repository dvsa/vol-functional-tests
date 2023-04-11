package org.dvsa.testing.framework.stepdefs.lgv;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.number.Int;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.licence.objects.FinancialStandingRate;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FinancialStandingRates extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(FinancialStandingRates.class);
    World world;
    FinancialStandingRate rowBeforeAdding;
    FinancialStandingRate rowBeforeChange;
    int firstVehicleRate = Int.random(1, 9999);
    int additionalVehicleRate = Int.random(1, 9999);
    HashMap<String, String> effectiveDate;

    String addButton = "//button[@id='add']";
    String editButton = "//button[@id='edit']";
    String deleteButton = "//button[@id='delete']";
    String modalTitle = "//*[@id='modal-title']";
    String operatorTypeRadio = "//*[@value='lcat_%s']";
    String licenceTypeRadio = "//label[text()='%s']";
    String vehicleTypeRadio = "//*[@name='details[vehicleType]' and not(@disabled)]";
    String unselectedOperatorType = "//*[@name='details[goodsOrPsv]' and not(@checked)]";
    String unselectedLicenceTypeRadio = "//*[@name='details[licenceType]' and not(@checked)]";
    String heavyGoodsVehicleVehicleType = "//input[@value='fin_sta_veh_typ_hgv']";
    String lightGoodsVehicleVehicleType = "//input[@value='fin_sta_veh_typ_lgv']";
    String firstVehicleRateField = "//*[@name='details[firstVehicleRate]']";
    String additionalVehicleRateField = "//*[@name='details[additionalVehicleRate]']";
    String effectiveDateFieldPartialSelector = "details[effectiveFrom]";
    String successfulAddedRecordAlert = "//p[@role='alert' and contains(text(),'Created record')]";
    String successfullyEditedRecordAlert = "//p[@role='alert' and contains(text(),'Updated record')]";
    String successfullyDeletedRecordAlert = "//p[@role='alert' and contains(text(),'Rate(s) deleted')]";

    public FinancialStandingRates(World world) {
        this.world = world;
        this.effectiveDate = world.globalMethods.date.getDateHashMap(Int.random(-20, 20), Int.random(-20, 20), Int.random(-20, 20));
    }

    public void setRowBeforeChange(FinancialStandingRate rowBeforeChange) {
        this.rowBeforeChange = rowBeforeChange;
    }

    public void setRowBeforeAdding(FinancialStandingRate rowBeforeAdding) {
        this.rowBeforeAdding = rowBeforeAdding;
    }

    @When("i am on the financial standing rates page")
    public void iAmOnTheFinancialStandingRatesPage() throws HttpException {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.adminNavigation(AdminOption.FINANCIAL_STANDING_RATES);
    }

    @When("i add a {string} {string} {string} financial standing rate")
    public void iAddAFinancialStandingRate(String operatorType, String licenceType, String vehicleType) {
        setRowBeforeAdding(new FinancialStandingRate(operatorType, licenceType, vehicleType, firstVehicleRate, additionalVehicleRate, effectiveDate));
        click(addButton, SelectorType.XPATH);
        waitForElementToBePresent(modalTitle);
        click(getOperatorTypeSelector(operatorType), SelectorType.XPATH);
        click(getLicenceTypeSelector(licenceType), SelectorType.XPATH);
        click(String.format("//label[contains(text(),'%s')]", vehicleType), SelectorType.XPATH);
        replaceText(firstVehicleRateField, SelectorType.XPATH, String.valueOf(firstVehicleRate));
        replaceText(additionalVehicleRateField, SelectorType.XPATH, String.valueOf(additionalVehicleRate));
        enterDateFieldsByPartialId(effectiveDateFieldPartialSelector, effectiveDate);
        world.UIJourney.clickSubmit();
        waitForElementToBePresent(successfulAddedRecordAlert);
    }

    @Then("the table displays the correct financial standing rate information")
    public void theTableDisplaysTheCorrectFinancialStandingRateInformation() {
        String mostRecentRow = String.format("//input[@name='action[edit][%s]']/../..", getMostRecentRowId());
        FinancialStandingRate rowAfterAdding = new FinancialStandingRate(mostRecentRow);
        assertTrue(rowAfterAdding.equals(rowBeforeAdding));
    }

    @When("i edit and save a financial standing rate")
    public void iEditAndSaveAFinancialStandingRate() {
        String mostRecentRow = String.format("//input[@name='action[edit][%s]']/../..", getMostRecentRowId());
        setRowBeforeChange(new FinancialStandingRate(mostRecentRow));

        click(mostRecentRow.concat("/td/input[@type='checkbox']"), SelectorType.XPATH);
        click(editButton, SelectorType.XPATH);
        waitForElementToBePresent(modalTitle);
        click(unselectedOperatorType, SelectorType.XPATH);
        click(unselectedLicenceTypeRadio, SelectorType.XPATH);
        click(vehicleTypeRadio, SelectorType.XPATH);
        replaceText(firstVehicleRateField, SelectorType.XPATH, String.valueOf(firstVehicleRate + 100));
        replaceText(additionalVehicleRateField, SelectorType.XPATH, String.valueOf(additionalVehicleRate + 100));
        enterDateFieldsByPartialId(effectiveDateFieldPartialSelector, effectiveDate);
        world.UIJourney.clickSubmit();
        waitForElementToBePresent(successfullyEditedRecordAlert);
    }

    @Then("the table displays the correct edited financial standing rate information")
    public void theTableDisplaysTheCorrectEditedFinancialStandingRateInformation() {
        String rowSelector = String.format("//input[@name='action[edit][%s]']/../..", rowBeforeChange.getId());
        FinancialStandingRate rowAfterEdit = new FinancialStandingRate(rowSelector);
        assertFalse(rowAfterEdit.equals(rowBeforeChange));
    }

    @When("i delete a financial standing rate")
    public void iDeleteAFinancialStandingRate() {
        String mostRecentRow = String.format("//input[@name='action[edit][%s]']/../..", getMostRecentRowId());
        setRowBeforeChange(new FinancialStandingRate(mostRecentRow));
        click(mostRecentRow.concat("/td/input[@type='checkbox']"), SelectorType.XPATH);
        click(deleteButton, SelectorType.XPATH);
        waitForElementToBePresent(modalTitle);
        world.UIJourney.clickConfirm();
        waitForElementToBePresent(successfullyDeletedRecordAlert);
    }

    @Then("the table no longer displays the deleted financial standing rate")
    public void theTableNoLongerDisplayedTheDeletedFinancialStandingRate() {
        String rowSelector = String.format("//input[@name='action[edit][%s]']/../../td", rowBeforeChange.getId());
        assertFalse(isElementPresent(rowSelector, SelectorType.XPATH));
    }

    @When("i choose to add a {string} {string} financial standing rate")
    public void iChooseToAddAFinancialStandingRate(String operatorType, String licenceType) {
        click(addButton, SelectorType.XPATH);
        waitForElementToBePresent(modalTitle);
        click(getOperatorTypeSelector(operatorType), SelectorType.XPATH);
        click(getLicenceTypeSelector(licenceType), SelectorType.XPATH);
    }

    @Then("the HGV and LGV vehicle types are not selectable")
    public void theHGVAndLGVVehicleTypesAreNotSelectable() {
        assertEquals("true", findElement(heavyGoodsVehicleVehicleType, SelectorType.XPATH).getAttribute("disabled"));
        assertEquals("true", findElement(lightGoodsVehicleVehicleType, SelectorType.XPATH).getAttribute("disabled"));
    }

    @When("i submit no information on a financial standing rate")
    public void iSubmitNoInformationOnAFinancialStandingRate() {
        click(addButton, SelectorType.XPATH);
        waitForElementToBePresent(modalTitle);
        world.UIJourney.clickSubmit();
        waitForTextToBePresent("There is a problem");
    }

    @Then("i should receive the correct financial standing modal errors")
    public void iShouldReceiveTheCorrectFinancialStandingErrors() {
        List<WebElement> summaryErrors = findElements("//li[@class='validation-summary__item']/a", SelectorType.XPATH);
        String[] inputTypes = {"Operator type", "Licence type", "First vehicle", "Additional vehicle", "Effective date"};
        for(int i = 0; i < inputTypes.length; i++) {
            String errorMessage = String.format("%s: value is required", inputTypes[i]);
            assertEquals(errorMessage, summaryErrors.get(i).getText());
        }
    }

    private String getOperatorTypeSelector(String operatorType) {
        String modifier = operatorType.equals("Goods Vehicle") ? "gv" : "psv";
        return String.format(operatorTypeRadio, modifier);
    }

    private String getLicenceTypeSelector(String licenceType) {
        return String.format(licenceTypeRadio, licenceType);
    }

    private int getMostRecentRowId() {
        List<WebElement> rows = findElements("//tbody//input[@type='submit']", SelectorType.XPATH);
        List<Integer> rowIds = rows.stream().map(
                x -> Integer.parseInt(x.getAttribute("name").replaceAll("\\D+",""))
        ).sorted().collect(Collectors.toList());
        return Collections.max(rowIds);
    }
}