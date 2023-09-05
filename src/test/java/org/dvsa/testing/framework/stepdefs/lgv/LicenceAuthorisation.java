package org.dvsa.testing.framework.stepdefs.lgv;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LicenceAuthorisation extends BasePage {

    World world;

    String lgvAuthorisationPageHintText = "These are vehicles that have a gross plated weight of over 2,500 Kilograms (kg) and up to and including 3,500 Kilograms (kg) including when combined with a trailer";
    String hgvTotalAuthorisationText = "Heavy goods vehicle authorisation";
    String lgvTotalAuthorisationText = "Light goods vehicle authorisation";
    String vehicleTotalAuthorisationText = "Vehicle authorisation";
    String hgvTableHeading = "//a[@class='sortable' and contains(text(),'Heavy goods vehicles')]";
    String vehicleTableHeading = "(//a[@class='govuk-link govuk-link--no-visited-state'])[2]";
    String noHGVAuthErrorText = "Enter a value for the field: \"How many heavy goods vehicles do you want to authorise on the licence?\"";
    String noLGVAuthErrorText = "Enter a value for the field: \"How many light goods vehicles do you want to authorise on the licence for international haulage? \"";
    String noTrailerAuthErrorText = "Enter a value for the field: \"How many trailers do you want to authorise on the licence?\"";

    public LicenceAuthorisation (World world) {
        this.world = world;
    }

    @Then("the only field and information is for LGVs only")
    public void theOnlyFieldAndInformationIsForLGVsOnly() {
        assertTrue(isTitlePresent("Licence authorisation", 10));
        assertFalse(isTitlePresent("Operating centres and authorisation", 10));

        assertFalse(isTextPresent(vehicleTotalAuthorisationText));
        assertFalse(isTextPresent("How many vehicles do you want to authorise on the licence?"));

        assertFalse(isTextPresent(hgvTotalAuthorisationText));
        assertFalse(isTextPresent("How many heavy goods vehicles do you want to authorise on the licence?"));
        assertFalse(isElementPresent(world.operatingCentreJourney.totalHGVAuthorisationField, SelectorType.XPATH));
        assertFalse(isElementPresent(world.operatingCentreJourney.vehicleAuthorisationHelpLink, SelectorType.XPATH));

        assertTrue(isTextPresent(lgvTotalAuthorisationText));
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
        assertTrue(isLinkPresent("The input is not between '1' and '5000', inclusively", 10));
    }

    @Then("a maximum community authorisation value error message should appear")
    public void aMaximumCommunityAuthorisationValueErrorMessageShouldAppear() {
        waitForTextToBePresent("There is a problem");
        assertTrue(isLinkPresent("The input is not between '0' and '5000', inclusively", 10));
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
        //waitForElementToBeClickable(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH);
        waitAndClick(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH);
        replaceText(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH, newLGVTotalAuthority);
        UIJourney.clickSaveAndReturn();
    }

    @Then("the lgv hint text is visible")
    public void theLgvHintTextIsVisible() {
        assertTrue(isTextPresent(lgvAuthorisationPageHintText));
    }

    @Then("the hgv and lgv authorisations text are not present")
    public void theHgvAndLgvAuthorisationsTextAreNotPresent() {
        assertFalse(isTextPresent(hgvTotalAuthorisationText));
        assertFalse(isTextPresent(lgvTotalAuthorisationText));
    }

    @Then("the hgv and lgv authorisations text are present")
    public void theHgvAndLgvAuthorisationsTextArePresent() {
        assertTrue(isTextPresent(hgvTotalAuthorisationText));
        assertTrue(isTextPresent(lgvTotalAuthorisationText));
    }

    @And("the vehicle authorisation text is present")
    public void theVehicleAuthorisationTextIsPresent() {
        assertTrue(isTextPresent(vehicleTotalAuthorisationText));
    }

    @And("the vehicle authorisation text is not present")
    public void theVehicleAuthorisationTextIsNotPresent() {
        assertFalse(isTextPresent(vehicleTotalAuthorisationText));
    }

    @Then("the operating centre table hgv text is present")
    public void theOperatingCentreTableHgvTextIsPresent() {
        assertTrue(isElementPresent(hgvTableHeading, SelectorType.XPATH));
    }

    @Then("the operating centre table vehicle text is not present")
    public void theOperatingCentreTableVehicleTextIsNotPresent() {
        assertTrue(isElementPresent(vehicleTableHeading, SelectorType.XPATH));
    }

    @When("i clear the authorisation fields and click save")
    public void iClearTheAuthorisationFieldsAndClickSave() {
        replaceText(world.operatingCentreJourney.totalHGVAuthorisationField, SelectorType.XPATH, "");
        replaceText(world.operatingCentreJourney.totalLGVAuthorisationField, SelectorType.XPATH, "");
        replaceText(world.operatingCentreJourney.totalTrailersAuthorisationField, SelectorType.XPATH, "");
        UIJourney.clickSaveAndReturn();
    }

    @Then("hgv, lgv and trailer missing authorisation value errors should display")
    public void hgvLgvAndTrailerMissingAuthorisationValueErrorsShouldDisplay() {
        assertTrue(isLinkPresent(noHGVAuthErrorText, 10));
        assertTrue(isLinkPresent(noLGVAuthErrorText, 10));
        assertTrue(isLinkPresent(noTrailerAuthErrorText, 10));
    }

    @When("i save a hgv authorisation greater that the overall number of vehicles across the licence")
    public void iSaveAHgvAuthorisationGreaterThatTheOverallNumberOfVehiclesAcrossTheLicence() {
        String totalNumberOfOCHgvs = getText("//tfoot//td[1]", SelectorType.XPATH);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(Integer.parseInt(totalNumberOfOCHgvs) + 1),
                String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority()),
                String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
    }

    @When("i save a hgv authorisation fewer that the overall number of vehicles across the licence")
    public void iSaveAHgvAuthorisationFewerThatTheOverallNumberOfVehiclesAcrossTheLicence() {
        String totalNumberOfOCTrailers = getText("//tfoot//td[2]", SelectorType.XPATH);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority("1",
                String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority()), totalNumberOfOCTrailers);
    }

    @When("i save a trailer authorisation greater that the overall number of vehicles across the licence")
    public void iSaveATrailerAuthorisationGreaterThatTheOverallNumberOfVehiclesAcrossTheLicence() {
        String totalNumberOfOCHgvs = getText("//tfoot//td[1]", SelectorType.XPATH);
        String totalNumberOfOCTrailers = getText("//tfoot//td[2]", SelectorType.XPATH);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(
                totalNumberOfOCHgvs,
                String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority()),
                String.valueOf(Integer.parseInt(totalNumberOfOCTrailers) + 1));
    }

    @When("i save a trailer authorisation fewer that the overall number of vehicles across the licence")
    public void iSaveAHgvAuthorisationLowerThatTheOverallNumberOfVehiclesAcrossTheLicence() {
        String totalNumberOfOCHgvs = getText("//tfoot//td[1]", SelectorType.XPATH);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(totalNumberOfOCHgvs,
                String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority()), "1");
    }

    @Then("i am prompted with the total hgv authorisation exceeds number of vehicles on licence error")
    public void iAmPromptedWithTheTotalHgvAuthorisationExceedsNumberOfVehiclesOnLicenceError() {
        assertTrue(isLinkPresent("You can't have more vehicles than the total across your operating centres", 10));
    }

    @Then("i am prompted with the total hgv authorisation is fewer than the largest OC on licence error")
    public void iAmPromptedWithTheTotalHgvAuthorisationIsFewerThanTheLargestOCOnLicenceError() {
        assertTrue(isLinkPresent("You can't have fewer vehicles than your largest operating centre", 10));
    }

    @Then("i am prompted with the total trailer authorisation exceeds number of vehicles on licence error")
    public void iAmPromptedWithTheTotalTrailerAuthorisationExceedsNumberOfVehiclesOnLicenceError() {
        assertTrue(isLinkPresent("You can't have more trailers than the total across your operating centres", 10));
    }

    @Then("i am prompted with the total trailer authorisation is fewer than the largest OC on licence error")
    public void iAmPromptedWithTheTotalTrailerAuthorisationIsFewerThanTheLargestOCOnLicenceError() {
        assertTrue(isLinkPresent("You can't have fewer trailers than your largest operating centre", 10));
    }
}
