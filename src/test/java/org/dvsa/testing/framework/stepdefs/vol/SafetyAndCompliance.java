package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SafetyAndCompliance extends BasePage {

    World world;

    public SafetyAndCompliance (World world) {
        this.world = world;
    }

    @When("I add an internal safety inspector")
    public void iAddAnInternalSafetyInspector() {
        world.safetyComplianceJourney.addSafetyInspector(false);
    }

    @When("I add an external safety inspection provider")
    public void iAddAnExternalSafetyInspectionProvider() {
        world.safetyComplianceJourney.addSafetyInspector(true);
    }

    @Then("the safety inspector is listed with external status {string}")
    public void theSafetyInspectorIsListedWithExternalStatus(String externalStatus) {
        world.safetyComplianceJourney.verifySafetyInspectorIsListed(externalStatus.equals("Yes"));
    }

    @When("I edit the safety inspector")
    public void iEditTheSafetyInspector() {
        world.safetyComplianceJourney.editSafetyInspector();
    }

    @Then("the edited safety inspector is listed")
    public void theEditedSafetyInspectorIsListed() {
        world.safetyComplianceJourney.verifySafetyInspectorWasEdited();
    }

    @When("I remove the safety inspector")
    public void iRemoveTheSafetyInspector() {
        world.safetyComplianceJourney.removeSafetyInspector();
    }

    @Then("the safety inspector is no longer listed")
    public void theSafetyInspectorIsNoLongerListed() {
        world.safetyComplianceJourney.verifySafetyInspectorWasRemoved();
    }

    @When("I submit invalid safety inspection settings")
    public void iSubmitInvalidSafetyInspectionSettings() {
        world.safetyComplianceJourney.submitInvalidSafetySettings();
    }

    @Then("the invalid safety inspection settings are rejected")
    public void theInvalidSafetyInspectionSettingsAreRejected() {
        world.safetyComplianceJourney.verifyInvalidSafetySettingsAreRejected();
    }

    @When("I correct and save the safety inspection settings")
    public void iCorrectAndSaveTheSafetyInspectionSettings() {
        world.safetyComplianceJourney.saveValidSafetySettings();
    }

    @Then("the valid safety inspection settings are saved")
    public void theValidSafetyInspectionSettingsAreSaved() {
        world.safetyComplianceJourney.verifyValidSafetySettingsWereSaved();
    }

    @When("I view the safety and compliance section")
    public void iViewTheSafetyAndComplianceSection() {
        world.safetyComplianceJourney.navigateToSafetyCompliancePage();
    }

    @Then("the PSV safety section does not include trailer inspection fields")
    public void thePsvSafetySectionDoesNotIncludeTrailerInspectionFields() {
        world.safetyComplianceJourney.verifyPsvSafetySection();
    }

    @Then("there is no reference of trailers on the safety and compliance page")
    public void thereIsNoReferenceOfTrailersOnTheSafetyAndCompliancePage() {
        assertTrue(isTextPresent("Maximum number of weeks before safety inspections on your vehicles"));
        assertTrue(isElementPresent("licence[safetyInsVehicles]", SelectorType.ID));

        assertFalse(isTextPresent("Maximum number of weeks before safety inspections on your trailers"));
        assertFalse(isElementPresent("licence[safetyInsTrailers]", SelectorType.ID));

        assertTrue(isTextPresent("Are some of your vehicles inspected more often than this"));
        assertTrue(isTextPresent("This can sometimes be the case with older vehicles"));
        assertFalse(isTextPresent("Are some of your vehicles or trailers inspected more often than this"));
        List<String> actualRadioButtons = findAll("licence[safetyInsVaries]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        String[] expectedRadioButtons = new String[] {"Y", "N"};
        for (int i = 0; i < actualRadioButtons.size(); i++) {
            assertTrue(actualRadioButtons.get(i).equals(expectedRadioButtons[i]));
        }
        assertTrue(isTextPresent("Yes"));

        assertTrue(isTextPresent("How do you analyse the information from your digital tachographs"));
        actualRadioButtons = findAll("licence[tachographIns]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        expectedRadioButtons = new String[] {"tach_internal", "tach_external", "tach_na"};
        for (int i=0; i < actualRadioButtons.size(); i++) {
            assertTrue(actualRadioButtons.get(i).equals(expectedRadioButtons[i]));
        }
        assertTrue(isTextPresent("In-house, using software on your own PC"));
        assertTrue(isTextPresent("By sending to an external analysis bureau/company"));
        assertTrue(isTextPresent("Not applicable"));
    }

    @Then("there is trailer related information on the safety and compliance page")
    public void thereIsTrailerRelatedInformationOnTheSafetyAndCompliancePage() {
        assertTrue(isTextPresent("Maximum number of weeks before safety inspections on your vehicles"));
        assertTrue(isElementPresent("licence[safetyInsVehicles]", SelectorType.ID));

        assertTrue(isTextPresent("Maximum number of weeks before safety inspections on your trailers"));
        assertTrue(isElementPresent("licence[safetyInsTrailers]", SelectorType.ID));

        assertTrue(isTextPresent("Are some of your vehicles or trailers inspected more often than this"));
        assertTrue(isTextPresent("This can sometimes be the case with older vehicles"));
        assertFalse(isTextPresent("Are some of your vehicles inspected more often than this"));
        List<String> actualRadioButtons = findAll("licence[safetyInsVaries]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        String[] expectedRadioButtons = new String[] {"Y", "N"};
        for (int i = 0; i < actualRadioButtons.size(); i++) {
            assertEquals(actualRadioButtons.get(i), expectedRadioButtons[i]);
        }
        assertTrue(isTextPresent("Yes"));

        assertTrue(isTextPresent("How do you analyse the information from your digital tachographs"));
        actualRadioButtons = findAll("licence[tachographIns]", SelectorType.NAME).stream().map(x -> x.getAttribute("value")).collect(Collectors.toList());
        expectedRadioButtons = new String[] {"tach_internal", "tach_external", "tach_na"};
        for (int i=0; i < actualRadioButtons.size(); i++) {
            assertEquals(actualRadioButtons.get(i), expectedRadioButtons[i]);
        }
        assertTrue(isTextPresent("In-house, using software on your own PC"));
        assertTrue(isTextPresent("By sending to an external analysis bureau/company"));
        assertTrue(isTextPresent("Not applicable"));
    }
}