package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.ValidAnnualMultilateralPermitsPage;
import org.junit.Assert;

import java.util.List;
import java.util.stream.IntStream;

public class ValidPermitPageSteps implements En {
    public ValidPermitPageSteps(OperatorStore operator, World world) {
        Then("^the user is in the annual multilateral list page$", ValidAnnualMultilateralPermitsPage::untilOnPage);
        And("^I am viewing an issued annual multilateral permit on self-serve$", () -> {
            LicenceStore licence = operator.getCurrentLicence()
                    .orElseThrow(IllegalStateException::new);
            HomePage.PermitsTab.select(licence.getLicenceNumber());
            ValidAnnualMultilateralPermitsPage.untilOnPage();
        });
        Then("^the Multilateral permit list page table should display all relevant fields$", () -> {
            String message = "Expected all permits to have a status of 'pending'";
            OperatorStore store = operator;
            List<ValidAnnualMultilateralPermitsPage.Permit> permits = ValidAnnualMultilateralPermitsPage.permits();
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));
            IntStream.range(0, permits.size() - 1).forEach((idx) -> Assert.assertTrue(
                    permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                            permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
            ));
        });
    }
}

