package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.number.Int;
import activesupport.string.Str;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.DataGenerator;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.openqa.selenium.WebElement;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnlicensedOperatorSteps extends BasePage {

    World world;
    private String operatorName;
    private String vrm;
    private String platedWeight;

    public UnlicensedOperatorSteps(World world) {
        this.world = world;
    }

    @And("i create an unlicensed operator")
    public void iCreateAnUnlicensedOperator() {
        createdUnlicensedOperator();
    }

    private void createdUnlicensedOperator() {
        operatorName = Str.randomWord(7);
        DataGenerator dataGenerator = new DataGenerator(world);
        waitAndClick("//*[@class='search__button']", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Create unlicensed operator')]", SelectorType.XPATH);
        waitAndEnterText("operator-details[name]", SelectorType.NAME, operatorName);
        waitAndClick("//*[contains(text(),'Goods')]", SelectorType.XPATH);
        waitAndEnterText("correspondenceAddress[addressLine1]", SelectorType.NAME, dataGenerator.getOperatorAddressLine1());
        waitAndEnterText("correspondenceAddress[addressLine2]", SelectorType.NAME, dataGenerator.getOperatorAddressLine2());
        waitAndEnterText("correspondenceAddress[addressLine2]", SelectorType.NAME, dataGenerator.getOperatorTown());
        waitAndEnterText("correspondenceAddress[postcode]", SelectorType.NAME, dataGenerator.getOperatorPostCode());
        waitAndEnterText("contact[email]", SelectorType.NAME, dataGenerator.getOperatorUserEmail());
        waitAndClick("form-actions[save]", SelectorType.NAME);
    }

    @Then("the operator should be created")
    public void theOperatorShouldBeCreated() {
        assertTrue(isTextPresent("The operator has been created successfully"));
        assertTrue(isTextPresent(operatorName));
    }

    @And("searchable in internal")
    public void searchableInInternal() {
        if (!world.configuration.env.toString().equals("local")) {
            world.internalSearchJourney.internalSearchUntilTextPresent(SearchType.Licence, operatorName, operatorName);
            assertTrue(isTitlePresent(operatorName, 4));
        }
    }

    @Then("i should be able to add vehicles")
    public void iShouldBeAbleToAddVehicles() {
        vrm = String.format("P%sCUX",Int.random(10,99));
        platedWeight = "5000";

        waitAndClick("Vehicles", SelectorType.PARTIALLINKTEXT);
        clickById("add");
        waitForTextToBePresent("Add vehicle");
        waitAndEnterText("vrm", SelectorType.ID, vrm);
        waitAndEnterText("plated_weight", SelectorType.ID, platedWeight);
        world.UIJourney.clickSubmit();
        assertTrue(isTextPresent("Created record"));
    }

    @And("the details should be displayed in the vehicles table")
    public void theDetailsShouldBeDisplayedInTheVehiclesTable() {
        List<WebElement> vehicles = findElements("//*[@class='govuk-table__body']", SelectorType.XPATH);
        for (WebElement vehicle : vehicles){
            assertTrue(vehicle.getText().contains(platedWeight));
            assertTrue(vehicle.getText().contains(vrm));
        }
    }
}