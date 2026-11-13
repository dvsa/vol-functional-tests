package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;

public class ApplicationConvictions {
    private final World world;

    public ApplicationConvictions(World world) {
        this.world = world;
    }

    @When("I open the {string} convictions and penalties declarations")
    public void openDeclarations(String type) {
        world.convictionsAndPenaltiesJourney.navigateToDeclarations(type);
    }

    @When("I add a conviction to the declarations")
    public void addConviction(DataTable details) {
        world.convictionsAndPenaltiesJourney.addApplicationConviction(details.asMap(String.class, String.class));
    }

    @When("I submit an empty application conviction")
    public void submitEmptyApplicationConviction() {
        world.convictionsAndPenaltiesJourney.submitEmptyConviction();
    }

    @Then("the application conviction required fields show errors in the summary and inline")
    public void verifyApplicationConvictionRequiredFields(DataTable errors) {
        world.convictionsAndPenaltiesJourney.verifyConvictionRequiredFields(
                "application", errors.asMap(String.class, String.class));
    }

    @When("I cancel the conviction entry")
    public void cancelConvictionEntry() {
        world.convictionsAndPenaltiesJourney.cancelConvictionEntry();
    }

    @Then("no conviction has been added")
    public void verifyNoConvictionWasAdded() {
        world.convictionsAndPenaltiesJourney.verifyNoConvictionWasAdded();
    }

    @When("I save the convictions and penalties declarations")
    public void saveDeclarations() {
        world.convictionsAndPenaltiesJourney.saveDeclarations();
    }

    @Then("the {string} declarations contain the saved conviction")
    public void verifyConviction(String type, DataTable details) {
        world.convictionsAndPenaltiesJourney.verifySavedConviction(type, details.asMap(String.class, String.class));
    }

    @When("I change the declared conviction for {string} to offence {string}")
    public void editConviction(String name, String offence) {
        world.convictionsAndPenaltiesJourney.editApplicationConviction(name, offence);
    }

    @When("I remove the declared conviction for {string}")
    public void removeConviction(String name) {
        world.convictionsAndPenaltiesJourney.removeApplicationConviction(name);
    }

    @Then("the declared conviction for {string} is removed")
    public void verifyConvictionRemoved(String name) {
        world.convictionsAndPenaltiesJourney.verifyApplicationConvictionRemoved(name);
    }

    @When("I declare no convictions")
    public void declareNoConvictions() {
        world.convictionsAndPenaltiesJourney.declareNoConvictions();
    }

    @Then("offence entry is hidden")
    public void verifyOffenceEntryHidden() {
        world.convictionsAndPenaltiesJourney.verifyOffenceEntryHidden();
    }

    @Then("the {string} declarations retain no convictions without offence entry")
    public void verifyNoConvictions(String type) {
        world.convictionsAndPenaltiesJourney.verifyNoConvictionsSaved(type);
    }
}
