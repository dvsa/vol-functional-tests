package org.dvsa.testing.framework.stepdefs.lgv;

import Injectors.World;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LicenceAuthorisation extends BasePage {

    World world;

    public LicenceAuthorisation (World world) {
        this.world = world;
    }

    @Then("the only field and information is for LGVs only")
    public void theOnlyFieldAndInformationIsForLGVsOnly() {
        assertTrue(isTitlePresent("Licence authorisation", 10));
        assertFalse(isTitlePresent("Operating centres and authorisation", 10));

        assertFalse(isTextPresent("Vehicle authorisation"));
        assertFalse(isTextPresent("How many vehicles do you want to authorise on the licence?"));

        assertFalse(isTextPresent("Heavy goods vehicle authorisation"));
        assertFalse(isTextPresent("How many heavy goods vehicles do you want to authorise on the licence?"));
        assertFalse(isElementPresent(world.operatingCentreJourney.totalHGVAuthorisationField, SelectorType.XPATH));
        assertFalse(isElementPresent(world.operatingCentreJourney.vehicleAuthorisationHelpLink, SelectorType.XPATH));

        assertTrue(isTextPresent("Light goods vehicle authorisation"));
        assertTrue(isTextPresent("How many light goods vehicles do you want to authorise on the licence for international haulage?"));
        assertTrue(isTextPresent("These are vehicles that have a gross plated weight of over 2,500 Kilograms (kg) and up to and including 3,500 Kilograms (kg) including when combined with a trailer"));
        assertTrue(isElementPresent(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH));

        assertTrue(isTextPresent("UK Licence for the Community"));
        assertTrue(isTextPresent("How many UK licence for the community documents do you require for travelling to Europe?"));
        assertTrue(isTextPresent("You need a UK Licence for the Community if you make journeys for hire or reward within the EU, Liechtenstein, Norway or Switzerland"));
        assertTrue(isElementPresent(world.operatingCentreJourney.totalCommunityAuthorisationField, SelectorType.XPATH));
    }

    @Then("there is no add operating centre button")
    public void thereIsNoAddOperatingCentreButton() {
        assertFalse(isTextPresent("Add an operating centre"));
        assertFalse(isElementPresent(world.operatingCentreJourney.addOperatingCentre, SelectorType.XPATH));
    }

    @When("i try to save an authorisation exceeding the valid values")
    public void iTryToSaveAnAuthorisationExceedingTheValidValues() {
        replaceText(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH, "5001");
        UIJourney.clickSaveAndContinue();
    }

    @When("i try to save a community authorisation exceeding the valid values")
    public void iTryToSaveACommunityAuthorisationExceedingTheValidValues() {
        replaceText(world.operatingCentreJourney.totalCommunityAuthorisationField, SelectorType.XPATH, "5001");
        UIJourney.clickSaveAndContinue();
    }

    @Then("a maximum authorisation value error message should appear")
    public void aMaximumAuthorisationValueErrorMessageShouldAppear() {
        waitForTextToBePresent("There is a problem");
        assertTrue(isTextPresent("The input is not between '1' and '5000', inclusively"));
    }

    @Then("a maximum community authorisation value error message should appear")
    public void aMaximumCommunityAuthorisationValueErrorMessageShouldAppear() {
        waitForTextToBePresent("There is a problem");
        assertTrue(isTextPresent("The input is not between '0' and '5000', inclusively"));
    }

    @When("i enter an lgv authorisation and a higher community authorisation")
    public void iEnterAnLgvAuthorisationAndAHigherCommunityAuthorisation() {
        replaceText(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH, "5");
        replaceText(world.operatingCentreJourney.totalCommunityAuthorisationField, SelectorType.XPATH, "10");
        UIJourney.clickSaveAndContinue();
    }

    @Then("the community authorisation exceeding lgv authorisation error appears")
    public void theCommunityAuthorisationExceedingLgvAuthorisationErrorAppears() {
        assertTrue(isTextPresent("The number of UK licences for the community must not exceed the total number of vehicles"));
    }

    @When("i enter a combined hgv and lgv authorisation and a higher community authorisation")
    public void iEnterACombinedHgvAndLgvAuthorisationAndAHigherCommunityAuthorisation() {
        replaceText(world.operatingCentreJourney.totalHGVAuthorisationField, SelectorType.XPATH, "5");
        replaceText(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH, "5");
        replaceText(world.operatingCentreJourney.totalCommunityAuthorisationField, SelectorType.XPATH, "20");
        UIJourney.clickSaveAndContinue();
    }

    @When("I create an lgv only authorisation variation with {string}")
    public void iCreateAnLgvOnlyAuthorisationVariationWith(String newLGVTotalAuthority) {
        world.generalVariationJourney.signInAndBeginLicenceAuthorisationVariation();
        replaceText(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH, newLGVTotalAuthority);
        UIJourney.clickSaveAndReturn();
    }
}
