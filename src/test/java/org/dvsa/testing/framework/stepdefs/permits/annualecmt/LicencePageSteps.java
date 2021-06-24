package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.Utils.eupaBuilders.organisation.OrganisationModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.external.pages.SelectALicencePage;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LicencePageSteps implements En {

    public LicencePageSteps(OperatorStore operatorStore, World world) {
        Then("^the licence number should be selected$", () -> {
            OrganisationModel organisation = OrganisationAPI.dashboard(operatorStore.getOrganisationId());

            String expectedLicenceNumber = organisation.getDashboard().getLicences().get(0).getLicNo();
            String actualLicenceNumber = SelectALicencePage.getLicenceNumber();

            Assert.assertThat(actualLicenceNumber, new IsEqualIgnoringCase(expectedLicenceNumber));
        });
        When("^I select any licence number$", () -> EcmtApplicationJourney.getInstance().licencePage(operatorStore, world));
        Then("^I should see the type of licence next to each licence$", () -> {
            List<LicenceModel> expectedLicences = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences();

            if (!(SelectALicencePage.numberOfLicences() > 1)){
                Assert.assertTrue(SelectALicencePage.getLicenceNumberWithType().contains(expectedLicences.get(0).getLicenceType().getDescription()));
            } else {
                List<String> actualLicences = IntStream.rangeClosed(1, SelectALicencePage.numberOfLicences()).mapToObj(SelectALicencePage::getLicenceNumberWithType).collect(Collectors.toList());
                expectedLicences.forEach((licence) -> {
                    boolean matchFound = actualLicences.stream().anyMatch(actualLicence -> actualLicence.contains(licence.getLicNo()) && actualLicence.contains(licence.getLicenceType().getDescription()));

                    Assert.assertTrue(matchFound);
                });
            }
        });
    }

}
