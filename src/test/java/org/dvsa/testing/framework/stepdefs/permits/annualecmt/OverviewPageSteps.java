package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.CommonPatterns;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
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
            for (PermitSection section : PermitSection.values()){
                Assert.assertThat(OverviewPage.statusOfSection(section), isIn(PermitStatus.values()));
            }
        });
        When("^I select '([\\w ]+)'$", (String overviewSection) -> {
            PermitSection section = PermitSection.getEnum(overviewSection);
            OverviewPage.section(section);
        });
        Then("^the (check your answers|declaration) section should be disabled$", (String section) -> {
            PermitSection sectionEnum = PermitSection.getEnum(section);
            Assert.assertFalse(
                    sectionEnum.toString() + " should NOT be active but is",
                    OverviewPage.isActive(sectionEnum)
            );

            Assert.assertTrue( sectionEnum.toString() + " should have the label " + sectionEnum.toString(), OverviewPage.checkStatus(sectionEnum, PermitStatus.CANT_START_YET));
        });
        When("^I fill all steps preceding steps to check your answers$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
        });
        Then("^the (check your answers|declaration) section should be enabled$", (String section) -> {
            PermitSection sectionEnum = PermitSection.getEnum(section);
            Assert.assertTrue(OverviewPage.isActive(sectionEnum));
        });
        When("^I fill all steps preceding steps to declaration$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
            ECMTPermitApplicationSteps.saveAndContinue();
        });
        When("^the page heading is displayed correctly$", OverviewPage::overviewPageHeading);
        When("^I'm on the annual multilateral overview page$", () -> {
            EcmtApplicationJourney.getInstance().permitType(PermitTypePage.PermitType.AnnualMultilateral, operatorStore)
             .licencePage(operatorStore, world);
            org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.untilOnPage();
        });
        Then("^the application reference number should be on the annual multilateral overview page$", () -> {
            String reference = BasePermitPage.getReference();
            Assert.assertThat(reference, MatchesPattern.matchesPattern(CommonPatterns.REFERENCE_NUMBER));
        });
        When("^I select (Licence number|Number of permits required) from multilateral overview page$",
                (StepdefBody.A1<String>) org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage::select);
        Then("^I am navigated to the corresponding page for ([\\w\\s]+)$", (String section) -> {
            switch (section.toLowerCase()) {
                case "number of permits required":
                    org.dvsa.testing.lib.pages.external.permit.multilateral.NumberOfPermitsPage.untilOnPage();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported section: '" + section + "'");
            }
        });
        Then("^the default section statuses are as expected$", () -> {
            boolean numberOfPermits = BaseOverviewPage.checkStatus(
                    org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.NumberOfPaymentsRequired.toString(), PermitStatus.NOT_STARTED_YET);

            boolean answers = BaseOverviewPage.checkStatus(
                    org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.CheckYourAnswers.toString(), PermitStatus.CANT_START_YET);

            boolean declaration = BaseOverviewPage.checkStatus(
                    org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.Declaration.toString(),
                    PermitStatus.CANT_START_YET);
            Assert.assertTrue("Expected 'number of permits' section status to be 'Not Started Yet' but it wasn't",
                    numberOfPermits);
            Assert.assertTrue("Expected 'Check your answers' section status to be 'Can't Start Yet' but it wasn't",
                    answers);
            Assert.assertTrue("Expected 'Declaration' section status to be 'Can't Start Yet' but it wasn't",
                    declaration);
        });
        Then("^future sections beyond the next following step from currently completed section are disabled$", () -> {
            String error = "Expected not to find a link to the '%s' page but there was one";
            boolean answers =
                    org.dvsa.testing.lib.pages.external.permit.multilateral
                            .OverviewPage.hasActiveLink(org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.CheckYourAnswers);

            boolean declaration =
                    org.dvsa.testing.lib.pages.external.permit.multilateral
                            .OverviewPage.hasActiveLink(org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.Declaration);

            Assert.assertFalse(String.format(error, "Check your answers"), answers);
            Assert.assertFalse(String.format(error, "Declaration"), declaration);
        });

        When("^I click cancel link on the multilateral overview page$", org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage::cancel);
    }
}
