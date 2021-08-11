package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
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

    public OverviewPageSteps(World world) {
        Then("^I should be on the Annual ECMT overview page$", () -> {
            isPath("/permits/application/\\d+");
        });
        And("^I am on the application overview page$", () -> {
            EcmtApplicationJourney.beginApplication(world);
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

        Then("^the (check your answers|declaration) section should be disabled$", (String section) -> {
            OverviewSection sectionEnum = OverviewSection.toEnum(section);
            Assert.assertFalse(
                    sectionEnum.toString() + " should NOT be active but is",
                    OverviewPage.isSectionActive(sectionEnum)
            );

            OverviewPageJourney.checkStatus(sectionEnum, PermitStatus.CANT_START_YET);
        });
        Then("^the (check your answers|declaration) section should be enabled$", (String section) -> {
            OverviewSection sectionEnum = OverviewSection.toEnum(section);
            assertTrue(OverviewPage.isSectionActive(sectionEnum));
        });
        When("^I fill all steps preceding steps to declaration$", () -> {
            EcmtApplicationJourney.completeUntilCheckYourAnswersPage();
            ECMTPermitApplicationSteps.saveAndContinue();
        });
        When("^the overview page heading is displayed correctly$", OverviewPageJourney::hasPageHeading);
    }
}
