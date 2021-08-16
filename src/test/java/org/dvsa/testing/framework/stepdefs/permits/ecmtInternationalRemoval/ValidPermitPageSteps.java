package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidECMTInternationalPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.junit.Assert;

import java.util.List;
import java.util.stream.IntStream;

public class ValidPermitPageSteps implements En {

    public ValidPermitPageSteps(World world) {
        And("^I have a valid ECMT removal permit$", () -> {
            EcmtInternationalRemovalJourney.completeAndSubmitApplication(world);
            SubmittedPage.goToPermitsDashboard();
            CommonSteps.waitUntilPermitHasStatus(world);
        });
        And("^I am viewing my issued ECMT removal permit on selfserve$", () -> {
            HomePage.PermitsTab.selectFirstValidPermit();
            ValidPermitsPage.untilOnPage();
        });
        Then("^I am on the ECMT removal Permit list page$", ValidPermitsPage::untilOnPage);
        And("^the table of ECMT removal permits is as expected$", () -> {
            String message = "Expected all permits to have a status of 'VALID'";
            List<ValidECMTInternationalPermit> permits = ValidPermitsPage.ECMTInternationalRemovalPermits();
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));
            IntStream.range(0, permits.size() - 1).forEach((idx) -> Assert.assertTrue(
                    permits.get(idx).getExpiryDate().isEqual(permits.get(idx).getStartDate().plusDays(364))));
        });

    }
}