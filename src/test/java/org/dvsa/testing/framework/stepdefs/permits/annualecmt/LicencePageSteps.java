package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.external.pages.SelectALicencePage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LicencePageSteps {
    private final World world;

    public LicencePageSteps(World world) {
        this.world = world;
    }

    @Then("I should see the type of licence next to each licence")
    public void iShouldSeeTheTypeOfLicence() {
        List<LicenceModel> expectedLicences = OrganisationAPI.dashboard(world.userDetails.getOrganisationId()).getDashboard().getLicences();

        if (!(SelectALicencePage.numberOfLicences() > 1)) {
            assertTrue(SelectALicencePage.getLicenceNumberWithType().contains("Standard International"));
        } else {
            List<String> actualLicences = IntStream.rangeClosed(1, SelectALicencePage.numberOfLicences()).mapToObj(SelectALicencePage::getLicenceNumberWithType).collect(Collectors.toList());
            expectedLicences.forEach((licence) -> {
                boolean matchFound = actualLicences.stream().anyMatch(actualLicence -> actualLicence.contains(world.applicationDetails.getLicenceNumber()) && actualLicence.contains(licence.getLicenceType().getDescription()));

                assertTrue(matchFound);
            });
        }
    }
}