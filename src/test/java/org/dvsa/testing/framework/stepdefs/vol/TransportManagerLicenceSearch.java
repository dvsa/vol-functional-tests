package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransportManagerLicenceSearch extends BasePage {

    private final World world;
    private String winningTransportManagerId;

    public TransportManagerLicenceSearch(World world) {
        this.world = world;
    }

    @When("I identify the transport manager that holds both licences")
    public void iIdentifyTheTransportManagerThatHoldsBothLicences() throws HttpException {
        String tmOne = world.internalNavigation.transportManagerIdOne;
        String tmTwo = world.internalNavigation.transportManagerIdTwo;

        world.transportManagerLicenceApiJourney.searchTransportManagerLicences(null, tmTwo);
        if (world.transportManagerLicenceApiJourney.returnedRowCount() >= 2) {
            winningTransportManagerId = tmTwo;
        } else {
            world.transportManagerLicenceApiJourney.searchTransportManagerLicences(null, tmOne);
            if (world.transportManagerLicenceApiJourney.returnedRowCount() >= 2) {
                winningTransportManagerId = tmOne;
            }
        }

        assertNotNull(winningTransportManagerId,
                "Neither TM " + tmOne + " nor TM " + tmTwo + " holds both licences after the merge,"
                        + " so the combined-filter check cannot be made meaningful.");
    }

    @Then("searching transport manager licences by that transport manager alone should return both licences")
    public void searchingByTransportManagerAloneShouldReturnBothLicences() throws HttpException {
        world.transportManagerLicenceApiJourney.searchTransportManagerLicences(null, winningTransportManagerId);

        List<String> licenceIds = world.transportManagerLicenceApiJourney.returnedLicenceIds();
        assertTrue(licenceIds.contains(world.internalNavigation.licenceIdOne),
                "Transport manager only search should include licence " + world.internalNavigation.licenceIdOne
                        + ", returned: " + licenceIds);
        assertTrue(licenceIds.contains(world.internalNavigation.licenceIdTwo),
                "Transport manager only search should include licence " + world.internalNavigation.licenceIdTwo
                        + ", returned: " + licenceIds);
    }

    @Then("searching transport manager licences by the first licence alone should return only that licence")
    public void searchingByLicenceAloneShouldReturnOnlyThatLicence() throws HttpException {
        String licenceId = world.internalNavigation.licenceIdOne;
        world.transportManagerLicenceApiJourney.searchTransportManagerLicences(licenceId, null);

        List<String> licenceIds = world.transportManagerLicenceApiJourney.returnedLicenceIds();
        assertTrue(licenceIds.stream().allMatch(licenceId::equals),
                "Licence only search should return rows for licence " + licenceId + " exclusively,"
                        + " returned: " + licenceIds);
        assertTrue(licenceIds.contains(licenceId),
                "Licence only search should return at least one row for licence " + licenceId);
    }

    @Then("searching transport manager licences by both the first licence and that transport manager should return only the matching record")
    public void searchingByBothFiltersShouldReturnOnlyTheMatchingRecord() throws HttpException {
        String licenceId = world.internalNavigation.licenceIdOne;
        world.transportManagerLicenceApiJourney.searchTransportManagerLicences(licenceId, winningTransportManagerId);

        List<String> licenceIds = world.transportManagerLicenceApiJourney.returnedLicenceIds();
        List<String> transportManagerIds = world.transportManagerLicenceApiJourney.returnedTransportManagerIds();

        assertEquals(1, licenceIds.size(),
                "Filtering by licence " + licenceId + " and transport manager " + winningTransportManagerId
                        + " should return exactly one record. Returned " + licenceIds.size()
                        + " rows for licences " + licenceIds + "."
                        + " More than one row means the licence filter was dropped"
                        + " (where() overwriting instead of andWhere()).");
        assertEquals(licenceId, licenceIds.get(0),
                "The single returned record should belong to licence " + licenceId);
        assertEquals(winningTransportManagerId, transportManagerIds.get(0),
                "The single returned record should belong to transport manager " + winningTransportManagerId);
        assertEquals(1, world.transportManagerLicenceApiJourney.reportedCount(),
                "The reported count should also respect both filters, not just the transport manager filter.");
    }
}
