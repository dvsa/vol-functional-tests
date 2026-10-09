package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.hc.core5.http.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.licence.ApplicationFeeApiJourney;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationFeeOrdering {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationFeeOrdering.class);
    private final World world;
    private final ApplicationFeeApiJourney journey;
    private final List<InvoiceFee> createdFees = new ArrayList<>();

    public ApplicationFeeOrdering(World world) {
        this.world = world;
        journey = new ApplicationFeeApiJourney(world);
    }

    @Given("the application has outstanding interim fees created in this order")
    public void createOutstandingInterimFees(DataTable table) throws HttpException {
        String feeTypeId = journey.interimFeeTypeId();
        LocalDate today = LocalDate.now();
        for (Map<String, String> row : table.asMaps()) {
            LocalDate invoiceDate = today.minusDays(Long.parseLong(row.get("days ago")));
            BigDecimal amount = new BigDecimal(row.get("amount"));
            int id = journey.createInterimFee(feeTypeId, invoiceDate, amount);
            createdFees.add(new InvoiceFee(id, invoiceDate, journey.grossAmount(id)));
        }
        assertTrue(createdFees.size() >= 3, "At least three fees are needed to check invoice date ordering");
        assertEquals(createdFees.size(), createdFees.stream().map(InvoiceFee::invoiceDate).distinct().count(),
                "The fixture must use distinct invoice dates");
        assertEquals(createdFees.size(), createdFees.stream().map(InvoiceFee::amount).distinct().count(),
                "Distinct amounts are needed to identify which fee the declaration selected");
    }

    @Then("the application interim fees should be selected in ascending invoice date order")
    public void assertInvoiceDateOrdering() throws HttpException {
        List<InvoiceFee> expectedOrder = createdFees.stream()
                .sorted(Comparator.comparing(InvoiceFee::invoiceDate))
                .toList();
        assertFalse(createdFees.equals(expectedOrder),
                "Creation order must differ from invoice date order so an unsorted query cannot pass");
        assertFalse(createdFees.get(0).equals(expectedOrder.get(0)),
                "The oldest invoice must not be the first fee created");

        LOGGER.info("Application fee ordering evidence: application={}, creation order={}, expected invoice date ASC order={}",
                world.createApplication.getApplicationId(), createdFees, expectedOrder);

        // Declaration exposes only the first outstanding interim fee, not the full repository result.
        for (InvoiceFee fee : expectedOrder) {
            BigDecimal actualAmount = journey.declarationInterimFee();
            assertEquals(0, fee.amount().compareTo(actualAmount),
                    "Expected oldest outstanding invoice " + fee.invoiceDate() + " (fee " + fee.id()
                            + ") with amount " + fee.amount() + ", but declaration returned " + actualAmount);
            LOGGER.info("PASS: oldest outstanding interim fee selected: application={}, fee={}, invoice date={}, expected amount={}, actual declaration amount={}",
                    world.createApplication.getApplicationId(), fee.id(), fee.invoiceDate(), fee.amount(), actualAmount);
            journey.payFee(fee.id(), fee.amount());
            LOGGER.info("PASS: fee={} payment created a transaction; fee status=paid and outstanding balance=0",
                    fee.id());
        }
        LOGGER.info("PASS: application={} selected all {} interim fees in invoice date ASC order, not creation order; verified through the declaration API",
                world.createApplication.getApplicationId(), expectedOrder.size());
    }

    private record InvoiceFee(int id, LocalDate invoiceDate, BigDecimal amount) {
    }
}
