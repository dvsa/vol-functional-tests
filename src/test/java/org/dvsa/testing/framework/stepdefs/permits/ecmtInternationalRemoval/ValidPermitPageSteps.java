package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.ValidPermit.ValidECMTInternationalPermit;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.ValidPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidECMTInternationalPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
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