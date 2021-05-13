package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.ValidECMTRemovalPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SubmittedPage;
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
                    .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage()
                    .numberOfPermits()
                    .checkYourAnswers()
                    .declaration();
            EcmtApplicationJourney.getInstance()
                    .feeOverviewPage()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            SubmittedPage.untilElementIsPresent("//h1[@class='govuk-panel__title']", SelectorType.XPATH,10,TimeUnit.SECONDS);
            BaseApplicationSubmitPage.finish();

            String reference1 = String.valueOf(operatorStore.getCurrentLicenceNumber());

            HomePage.PermitsTab.untilPermitHasStatus(
                    operatorStore.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );
        });
        And("^I am viewing my issued ECMT removal permit on selfserve$", () -> {
            LicenceStore licence = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalStateException::new);
            HomePage.PermitsTab.select(licence.getLicenceNumber());
            ValidECMTRemovalPermitsPage.untilOnPage();
        });
        Then("^I am on the ECMT removal Permit list page$", ValidECMTRemovalPermitsPage::untilOnPage);
        And("^the licence number is displayed in ECMT removals list page$", () -> {
            String expectedReference = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            String actual = ValidECMTRemovalPermitsPage.reference();
            Assert.assertEquals(expectedReference, actual);
        });
        And("^the table of ECMT removal permits is as expected$", () -> {
            String message = "Expected all permits to have a status of 'VALID'";
            OperatorStore store = operatorStore;
            List<ValidECMTRemovalPermitsPage.Permit> permits = ValidECMTRemovalPermitsPage.permits();
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));
            IntStream.range(0, permits.size() - 1).forEach((idx) -> Assert.assertTrue(
                    permits.get(idx).getExpiryDate().isEqual(permits.get(idx).getStartDate().plusDays(364))));
        });

    }
}