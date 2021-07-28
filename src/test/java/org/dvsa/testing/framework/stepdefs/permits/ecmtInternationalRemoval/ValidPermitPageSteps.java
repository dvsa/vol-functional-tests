package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
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
import org.junit.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class ValidPermitPageSteps implements En {

    public ValidPermitPageSteps(World world, OperatorStore operatorStore) {
        And("^I have a valid ECMT removal permit$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
            EcmtInternationalRemovalJourney.getInstance()
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage();
            NumberOfPermitsPageJourney.completePage();
            EcmtInternationalRemovalJourney.getInstance()
                    .checkYourAnswers();
            DeclarationPageJourney.completeDeclaration();
            EcmtApplicationJourney.getInstance()
                    .feeOverviewPage();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();

            HomePage.PermitsTab.untilPermitHasStatus(
                    world.applicationDetails.getLicenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );
        });
        And("^I am viewing my issued ECMT removal permit on selfserve$", () -> {
            HomePage.PermitsTab.selectFirstValidPermit();
            ValidPermitsPage.untilOnPage();
        });
        Then("^I am on the ECMT removal Permit list page$", ValidPermitsPage::untilOnPage);
        And("^the licence number is displayed in ECMT removals list page$", () -> {
            String expectedReference = world.applicationDetails.getLicenceNumber();
            String actual = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(expectedReference, actual);
        });
        And("^the table of ECMT removal permits is as expected$", () -> {
            String message = "Expected all permits to have a status of 'VALID'";
            List<ValidECMTInternationalPermit> permits = ValidPermitsPage.annualECMTPermits();
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));
            IntStream.range(0, permits.size() - 1).forEach((idx) -> Assert.assertTrue(
                    permits.get(idx).getExpiryDate().isEqual(permits.get(idx).getStartDate().plusDays(364))));
        });

    }
}