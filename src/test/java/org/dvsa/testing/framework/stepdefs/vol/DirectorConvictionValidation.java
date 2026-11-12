package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;

public class DirectorConvictionValidation {
    private final World world;

    public DirectorConvictionValidation(World world) {
        this.world = world;
    }

    @When("I reach the new director conviction declaration")
    public void reachDirectorConvictionDeclaration() {
        world.directorJourney.beginDirectorConvictionDeclaration();
    }

    @When("I submit the director conviction declaration without an answer")
    public void submitEmptyDirectorConvictionDeclaration() {
        world.directorJourney.submitEmptyDirectorConvictionDeclaration();
    }

    @Then("the director conviction declaration shows {string} in the summary and inline")
    public void verifyDirectorConvictionDeclarationRequired(String expectedError) {
        world.directorJourney.verifyDirectorConvictionDeclarationRequired(expectedError);
    }

    @When("I submit an empty previous conviction for the director")
    public void submitEmptyDirectorConviction() {
        world.convictionsAndPenaltiesJourney.submitEmptyConviction();
    }

    @Then("the director conviction required fields show errors in the summary and inline")
    public void verifyDirectorConvictionRequiredFields(DataTable errors) {
        world.convictionsAndPenaltiesJourney.verifyConvictionRequiredFields(
                "director-change", errors.asMap(String.class, String.class));
    }
}
