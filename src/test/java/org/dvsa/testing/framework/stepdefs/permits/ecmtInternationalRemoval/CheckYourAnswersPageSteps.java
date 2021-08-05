package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.ECMTRemovalsSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.hamcrest.core.StringContains;
import org.junit.Assert;

import static org.dvsa.testing.framework.enums.PermitStatus.COMPLETED;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckYourAnswersPageSteps implements En {

    public CheckYourAnswersPageSteps(World world) {
        When("^I am on ECMT Removal check your answers page", () -> {
            EcmtInternationalRemovalJourney.completeUntilCheckYourAnswersPage(world);

        });
        And("^the ECMT Removals check your answers page has reference number$", BasePermitPage::getReferenceFromPage);
        And("^the ECMT Removals application answers are displayed on the check your answers page$", () -> {
            String licence = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.Licence);
            assertThat(licence,StringContains.containsString(world.applicationDetails.getLicenceNumber()));
            String permitType = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.PermitType);
            Assert.assertEquals(permitType,"ECMT International Removal");
            String RemovalsEligibility = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.RemovalsEligibility);
            Assert.assertEquals(RemovalsEligibility, "I confirm that I will only use an ECMT international removal permit to move household goods or business possessions and that I will use specialised equipment and staff for removal operations.");
            String Cabotage = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.Cabotage);
            Assert.assertEquals(Cabotage, "I confirm that I will not undertake cabotage journeys using an ECMT international removal permit.");
        });
        Then("^I am on the ECMT removals permits overview page with check your answers section marked as complete$", () -> OverviewPageJourney.checkStatus(OverviewSection.CheckYourAnswers, COMPLETED));
        And("^I click the ECMT Removals Check your answers link on the overview page again$", () -> OverviewPageJourney.clickOverviewSection(OverviewSection.CheckYourAnswers));
        Then("^I should be on the declaration page$", DeclarationPage::untilOnPage);
        Then("^I choose to change the ECMT Removals Permits Eligibility  section$", () -> {
            CheckYourAnswerPage.clickChangeAnswer(ECMTRemovalsSection.RemovalsEligibility);
        });
        Then("^I choose to change the ECMT Removals Permits Cabotage section$", () -> CheckYourAnswerPage.clickChangeAnswer(ECMTRemovalsSection.Cabotage));
        Then("^I choose to change the ECMT Removals Permits Number of permits section$", () -> CheckYourAnswerPage.clickChangeAnswer(ECMTRemovalsSection.NumberOfPermits));
        Then("^I should be on the ECMT international removal number of permits page$", () -> {
            NumberOfPermitsPage.untilOnPage();
            NumberOfPermitsPageJourney.hasECMTPageHeading();
        });
    }
}
