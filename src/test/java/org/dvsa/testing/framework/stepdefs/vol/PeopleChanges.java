package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.enums.SelfServeSection;

public class PeopleChanges {
    private World world;

    public PeopleChanges(World world) {
        this.world = world;
    }

    @When("i navigate to the people page")
    public void iNavigateToThePeoplePage() {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.PEOPLE);
    }

    @When("i add a partner with name {string}, other names {string}, and date of birth {string}")
    public void iAddAPartnerWithNameOtherNamesAndDateOfBirth(String fullName, String otherNames, String dateOfBirth) {
        String[] names = fullName.split(" ");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";
        String title = "Mr"; // default; could be parameterized
        world.peopleJourney.addPartnerWithMinimalDetails(title, firstName, lastName, dateOfBirth);
    }

    @When("i add a partner named {string} with date of birth {string}")
    public void iAddAPartnerNamedWithDateOfBirth(String fullName, String dateOfBirth) {
        String[] names = fullName.split(" ");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";
        String title = "Mr";
        world.peopleJourney.addPartnerWithMinimalDetails(title, firstName, lastName, dateOfBirth);
    }

    @When("i remove the partner named {string}")
    public void iRemoveThePartnerNamed(String partnerName) {
        world.peopleJourney.removePartner(partnerName);
    }

    @Then("the partner named {string} should be present in the table")
    public void thePartnerNamedShouldBePresentInTheTable(String partnerName) {
        world.peopleJourney.verifyPartnerIsPresent(partnerName);
    }

    @Then("the partner named {string} should not be present in the table")
    public void thePartnerNamedShouldNotBePresentInTheTable(String partnerName) {
        world.peopleJourney.verifyPartnerIsNotPresent(partnerName);
    }
}
