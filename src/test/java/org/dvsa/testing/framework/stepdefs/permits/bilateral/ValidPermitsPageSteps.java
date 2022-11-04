package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenWindowModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualBilateralPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidPermitsPageSteps implements En {

    public ValidPermitsPageSteps(World world) {
        And("^The content and information on valid permits is correct$", () -> {

            // the table of annual bilateral permits is as expected
            OpenByCountryModel stock = IrhpPermitWindowAPI.openByCountry();
            String message =  "Expected all permits to have a status of 'VALID' but one or more DIDN'T!!!";
            List<ValidAnnualBilateralPermit> permits = ValidPermitsPage.annualBilateralPermits();

            List<OpenWindowModel> windows = stock.openWindowsFor(permits.stream().map(p -> p.getCountry().toString()).toArray(String[]::new));

            // Verify status is VALID
            assertTrue(permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID), message);

            // Verify that Type is displayed is always Standard single journey for Turkey
            assertEquals("Standard single journey", ValidPermitsPage.getType());

            IntStream.range(0, permits.size() - 1).forEach((idx) -> {
                List<LocalDate> expiryDates = stock.openWindowsFor(permits.get(idx).getCountry().toString())
                        .stream().map(win -> win.getIrhpPermitStockModel().getValidTo()).collect(Collectors.toList());

                // Check Country order
                assertTrue(permits.get(idx).getCountry().compareTo(permits.get(idx + 1).getCountry()) <= 0);
                // Check expiry date is in ascending order
                assertTrue(
                        permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                                permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
                );
                // Check expiry date matches that of stock window
                assertThat(expiryDates, hasItem(permits.get(idx).getExpiryDate()));
            });
        });
    }
}