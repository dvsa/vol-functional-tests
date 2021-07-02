package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.common.CommonPatterns;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;

import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertTrue;

public class OverviewPageSteps extends BasePage implements En {

    public OverviewPageSteps(World world, OperatorStore operatorStore) {
        Then("^I should be on the Annual ECMT overview page$", () -> {
            isPath("/permits/application/\\d+");
        });
        And("^I am on the application overview page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);

        });
        Then("^only the expected status labels are displayed$", () -> {
            for (OverviewSection section : OverviewSection.values()){
                Assert.assertThat(OverviewPage.getStatusOfSection(section), isIn(PermitStatus.values()));
            }
        });
        When("^I select '([\\w ]+)'$", (String overviewSection) -> {
            OverviewSection section = OverviewSection.toEnum(overviewSection);
            OverviewPageJourney.clickOverviewSection(section);;
        });
        Then("^the (check your answers|declaration) section should be disabled$", (String section) -> {
            OverviewSection sectionEnum = OverviewSection.toEnum(section);
            Assert.assertFalse(
                    sectionEnum.toString() + " should NOT be active but is",
                    OverviewPage.isSectionActive(sectionEnum)
            );

            OverviewPageJourney.checkStatus(sectionEnum, PermitStatus.CANT_START_YET);
        });
        When("^I fill all steps preceding steps to check your answers$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
        });
        Then("^the (check your answers|declaration) section should be enabled$", (String section) -> {
            OverviewSection sectionEnum = OverviewSection.toEnum(section);
            assertTrue(OverviewPage.isSectionActive(sectionEnum));
        });
        When("^I fill all steps preceding steps to declaration$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
            ECMTPermitApplicationSteps.saveAndContinue();
        });
        When("^the overview page heading is displayed correctly$", OverviewPageJourney::hasPageHeading);
        When("^I'm on the annual multilateral overview page$", () -> {
            EcmtApplicationJourney.getInstance().permitType(PermitType.ANNUAL_MULTILATERAL, operatorStore)
             .licencePage(operatorStore, world);
            OverviewPage.untilOnPage();
        });
        Then("^the application reference number should be on the annual multilateral overview page$", () -> {
            String reference = BasePermitPage.getReferenceFromPage();
            Assert.assertThat(reference, MatchesPattern.matchesPattern(CommonPatterns.REFERENCE_NUMBER));
        });
        When("^I select (Licence number|Number of permits required) from multilateral overview page$", (String section) -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.toEnum(section));
        });
        Then("^I am navigated to the corresponding page for ([\\w\\s]+)$", (String section) -> {
            switch (section.toLowerCase()) {
                case "number of permits required":
                    NumberOfPermitsPageJourney.setNumberOfPermitsAndSetRespectiveValues();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported section: '" + section + "'");
            }
        });
        Then("^the default section statuses are as expected$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.NumberOfPaymentsRequired, PermitStatus.NOT_STARTED_YET);

            OverviewPageJourney.checkStatus(OverviewSection.CheckYourAnswers, PermitStatus.CANT_START_YET);

            OverviewPageJourney.checkStatus(OverviewSection.Declaration, PermitStatus.CANT_START_YET);
        });
        Then("^future sections beyond the next following step from currently completed section are disabled$", () -> {
            assertTrue(OverviewPage.isActiveLinkPresent(OverviewSection.CheckYourAnswers));
            assertTrue(OverviewPage.isActiveLinkPresent(OverviewSection.Declaration));
        });
    }
}
