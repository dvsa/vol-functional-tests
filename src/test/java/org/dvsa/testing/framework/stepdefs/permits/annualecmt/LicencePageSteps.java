package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.external.pages.SelectALicencePage;
import org.hamcrest.text.IsEqualIgnoringCase;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.external.pages.SelectALicencePage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LicencePageSteps implements En {

    public LicencePageSteps(World world) {
        Then("^I should see the type of licence next to each licence$", () -> {
            List<LicenceModel> expectedLicences = OrganisationAPI.dashboard(world.userDetails.getOrganisationId()).getDashboard().getLicences();

            if (!(SelectALicencePage.numberOfLicences() > 1)){
                Assert.assertTrue(SelectALicencePage.getLicenceNumberWithType().contains("Standard International"));
            } else {
                List<String> actualLicences = IntStream.rangeClosed(1, SelectALicencePage.numberOfLicences()).mapToObj(SelectALicencePage::getLicenceNumberWithType).collect(Collectors.toList());
                expectedLicences.forEach((licence) -> {
                    boolean matchFound = actualLicences.stream().anyMatch(actualLicence -> actualLicence.contains(world.applicationDetails.getLicenceNumber()) && actualLicence.contains(licence.getLicenceType().getDescription()));

                    Assert.assertTrue(matchFound);
                });
            }
        });
    }

}
