package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.common.CommonPatterns;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OverviewPageSteps extends BasePage implements En {

    String[] ECMTAnnualSections = {
            "Check if you need ECMT permits",
            "Cabotage",
            "Certificates required",
            "Countries with limited permits",
            "Number of permits required",
            "Euro emission standards",
            "Check your answers",
            "Declaration"
    };

    public OverviewPageSteps(World world, OperatorStore operatorStore) {
        Then("^I should be on the Annual ECMT overview page$", () -> {
            isPath("/permits/application/\\d+");
        });
        And("^I am on the application overview page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);

        });
        Then("^only the expected status labels are displayed$", () -> {
            List<OverviewSection> overviewSections = new LinkedList<>();
            for (String section : ECMTAnnualSections) {
                overviewSections.add(OverviewSection.toEnum(section));
            }
            for (OverviewSection section : overviewSections) {
                assertThat(OverviewPage.getStatusOfSection(section), isIn(PermitStatus.values()));
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
    }
}
