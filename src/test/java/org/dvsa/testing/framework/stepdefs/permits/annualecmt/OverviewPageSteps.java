package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.junit.jupiter.api.Assertions.*;

public class OverviewPageSteps extends BasePage {
    private final World world;
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
        this.world = world;
    }

    @Then("I should be on the Annual ECMT overview page")
    public void iShouldBeOnTheAnnualECMTOverviewPage() {
        isPath("/permits/application/\\d+");
    }

    @And("I am on the application overview page")
    public void iAmOnTheApplicationOverviewPage() {
        world.ecmtApplicationJourney.beginApplication();
    }

    @Then("only the expected status labels are displayed")
    public void onlyTheExpectedStatusLabelsDisplayed() {
        List<OverviewSection> overviewSections = new LinkedList<>();
        for (String section : ECMTAnnualSections) {
            overviewSections.add(OverviewSection.toEnum(section));
        }
        for (OverviewSection section : overviewSections) {
            assertThat(OverviewPage.getStatusOfSection(section),
                    isIn(PermitStatus.values()));
        }
    }

    @Then("the (check your answers|declaration) section should be disabled")
    public void theSectionShouldBeDisabled(String section) {
        OverviewSection sectionEnum = OverviewSection.toEnum(section);
        assertFalse(
                OverviewPage.isSectionActive(sectionEnum)
        );
        OverviewPageJourney.checkStatus(sectionEnum, PermitStatus.CANT_START_YET);
    }

    @Then("the (check your answers|declaration) section should be enabled")
    public void theSectionShouldBeEnabled(String section) {
        OverviewSection sectionEnum = OverviewSection.toEnum(section);
        assertTrue(OverviewPage.isSectionActive(sectionEnum));
    }

    @When("I fill all steps preceding steps to declaration")
    public void iFillAllStepsPrecedingSteps() {
        world.ecmtApplicationJourney.completeUntilCheckYourAnswersPage();
        ECMTPermitApplicationSteps.saveAndContinue();
    }

    @When("the overview page heading is displayed correctly")
    public void theOverviewPageHeadingIsDisplayedCorrectly() {
        OverviewPageJourney.hasPageHeading();
    }
}