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


import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnlicensedOperatorSteps extends BasePage {

    World world;
    private String operatorName;

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
    }

    @And("searchable in internal")
    public void searchableInInternal() {
        world.internalSearchJourney.internalSearchUntilTextPresent(SearchType.Licence,operatorName,operatorName);
        assertTrue(isTitlePresent(operatorName, 4));
    }

    @Then("i should be able to add vehicles")
    public void iShouldBeAbleToAddVehicles() {
        waitAndClick("Vehicles", SelectorType.PARTIALLINKTEXT);
        clickById("add");
        waitForTextToBePresent("Add vehicle");
        String num = String.valueOf(Int.random(10,99));
        waitAndEnterText("vrm", SelectorType.ID, String.format("P%sCUX",num));
        waitAndEnterText("plated_weight", SelectorType.ID, "5000");
        world.UIJourney.clickSubmit();
        assertTrue(isTextPresent("Created record"));
    }
}