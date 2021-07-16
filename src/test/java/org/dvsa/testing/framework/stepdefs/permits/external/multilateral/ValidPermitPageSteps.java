package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.ValidPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualMultilateralPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;
import org.junit.Assert;

import java.util.List;
import java.util.stream.IntStream;

public class ValidPermitPageSteps implements En {
    public ValidPermitPageSteps(OperatorStore operator, World world) {
        Then("^the user is in the annual multilateral list page$", () -> {
            ValidPermitsPage.untilOnPage();
            ValidPermitsPageJourney.hasMultilateralHeading();
        });
        And("^I am viewing an issued annual multilateral permit on self-serve$", () -> {
            HomePage.PermitsTab.selectFirstValidPermit();
            ValidPermitsPage.untilOnPage();
            ValidPermitsPageJourney.hasMultilateralHeading();
        });
        Then("^the Multilateral permit list page table should display all relevant fields$", () -> {
            String message = "Expected all permits to have a status of 'pending'";
            OperatorStore store = operator;
            List<ValidAnnualMultilateralPermit> permits = ValidPermitsPage.annualMultilateralPermits();
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));
            IntStream.range(0, permits.size() - 1).forEach((idx) -> Assert.assertTrue(
                    permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                            permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
            ));
        });
    }
}

