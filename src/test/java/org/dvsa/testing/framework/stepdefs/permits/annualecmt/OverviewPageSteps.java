package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.CommonPatterns;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.hamcrest.Matchers.isIn;

public class OverviewPageSteps implements En {

    public OverviewPageSteps(World world, OperatorStore operatorStore) {
        Then("^I should be on the Annual ECMT overview page$", () -> {
            isPath("/permits/application/\\d+");
        });
        And("^I am on the application overview page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);

        });
        Then("^only the expected status labels are displayed$", () -> {
            for (OverviewSection section : OverviewSection.values()){
                Assert.assertThat(org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.statusOfSection(section), isIn(PermitStatus.values()));
            }
        });
        When("^I select '([\\w ]+)'$", (String overviewSection) -> {
            OverviewSection section = OverviewSection.toEnum(overviewSection);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(section);
        });
        Then("^the (check your answers|declaration) section should be disabled$", (String section) -> {
            OverviewSection sectionEnum = OverviewSection.toEnum(section);
            Assert.assertFalse(
                    sectionEnum.toString() + " should NOT be active but is",
                    org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.isActive(sectionEnum)
            );

            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(sectionEnum, PermitStatus.CANT_START_YET);
        });
        When("^I fill all steps preceding steps to check your answers$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
        });
        Then("^the (check your answers|declaration) section should be enabled$", (String section) -> {
            OverviewSection sectionEnum = OverviewSection.toEnum(section);
            Assert.assertTrue(OverviewPage.isActive(sectionEnum));
        });
        When("^I fill all steps preceding steps to declaration$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
            ECMTPermitApplicationSteps.saveAndContinue();
        });
        When("^the page heading is displayed correctly$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::hasPageHeading);
        When("^I'm on the annual multilateral overview page$", () -> {
            EcmtApplicationJourney.getInstance().permitType(PermitType.ANNUAL_MULTILATERAL, operatorStore)
             .licencePage(operatorStore, world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
        });
        Then("^the application reference number should be on the annual multilateral overview page$", () -> {
            String reference = BasePermitPage.getReferenceFromPage();
            Assert.assertThat(reference, MatchesPattern.matchesPattern(CommonPatterns.REFERENCE_NUMBER));
        });
        When("^I select (Licence number|Number of permits required) from multilateral overview page$", (String section) -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.toEnum(section));
        });
        Then("^I am navigated to the corresponding page for ([\\w\\s]+)$", (String section) -> {
            switch (section.toLowerCase()) {
                case "number of permits required":
                    NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported section: '" + section + "'");
            }
        });
        Then("^the default section statuses are as expected$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(
                    OverviewSection.NumberOfPaymentsRequired, PermitStatus.NOT_STARTED_YET);

            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(
                    OverviewSection.CheckYourAnswers, PermitStatus.CANT_START_YET);

            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(
                    OverviewSection.Declaration,
                    PermitStatus.CANT_START_YET);
        });
        Then("^future sections beyond the next following step from currently completed section are disabled$", () -> {
            String error = "Expected not to find a link to the '%s' page but there was one";
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.hasActiveLink(OverviewSection.CheckYourAnswers);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.hasActiveLink(OverviewSection.Declaration);
        });

        When("^I click cancel link on the multilateral overview page$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::clickCancelApplication);
    }
}
