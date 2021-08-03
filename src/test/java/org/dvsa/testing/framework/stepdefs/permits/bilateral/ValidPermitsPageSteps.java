package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenWindowModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualBilateralPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.hasItem;

public class ValidPermitsPageSteps implements En {

    public ValidPermitsPageSteps(World world) {
        And("^The content and information on valid permits is correct$", () -> {

            // the table of annual bilateral permits is as expected
            OpenByCountryModel stock = IrhpPermitWindowAPI.openByCountry();
            String message =  "Expected all permits to have a status of 'VALID' but one or more DIDN'T!!!";
            List<ValidAnnualBilateralPermit> permits = ValidPermitsPage.annualBilateralPermits();

            List<OpenWindowModel> windows = stock.openWindowsFor(permits.stream().map(p -> p.getCountry().toString()).toArray(String[]::new));

            // Verify status is VALID
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));

            // Verify that Type is displayed is always Standard single journey for Turkey
            Assert.assertEquals("Standard single journey", ValidPermitsPage.getType());

            IntStream.range(0, permits.size() - 1).forEach((idx) -> {
                List<LocalDate> expiryDates = stock.openWindowsFor(permits.get(idx).getCountry().toString())
                        .stream().map(win -> win.getIrhpPermitStockModel().getValidTo()).collect(Collectors.toList());

                // Check Country order
                Assert.assertTrue(permits.get(idx).getCountry().compareTo(permits.get(idx + 1).getCountry()) <= 0);
                // Check expiry date is in ascending order
                Assert.assertTrue(
                        permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                                permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
                );
                // Check expiry date matches that of stock window
                Assert.assertThat(expiryDates, hasItem(permits.get(idx).getExpiryDate()));
            });
        });
    }
}