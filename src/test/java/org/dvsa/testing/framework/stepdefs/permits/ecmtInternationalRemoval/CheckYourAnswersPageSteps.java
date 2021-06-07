package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.NumberofPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.sections.ECMTRemovalsSection;
import org.hamcrest.core.StringContains;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.enums.PermitStatus.COMPLETED;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckYourAnswersPageSteps implements En {

    public CheckYourAnswersPageSteps(World world, OperatorStore operatorStore) {
        When("^I am on ECMT Removal check your answers page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewSection.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                     .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage()
                    .numberOfPermits();

        });
        Then("^ECMT Removals permit check your answers page has correct heading label$", () -> CheckYourAnswerPage.hasPageHeading());
        And("^the ECMT Removals check your answers page has reference number$", BasePermitPage::getReferenceFromPage);
        And("^the ECMT Removals application answers are displayed on the check your answers page$", () -> {
            String licence = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.Licence);
            assertThat(licence,StringContains.containsString(operatorStore.getCurrentLicence().get().getLicenceNumber()));
            String permitType = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.PermitType);
            Assert.assertEquals(permitType,"ECMT International Removal");
            String RemovalsEligibility = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.RemovalsEligibility);
            Assert.assertEquals(RemovalsEligibility, "I confirm that I will only use an ECMT international removal permit to move household goods or business possessions and that I will use specialised equipment and staff for removal operations.')]");
            String Cabotage = CheckYourAnswerPage.getAnswer(ECMTRemovalsSection.Cabotage);
            Assert.assertEquals(Cabotage, "I confirm that I will not undertake cabotage journeys using an ECMT international removal permit.");
        });
        Then("^I am on the ECMT removals permits overview page with check your answers section marked as complete$", () -> OverviewPage.checkStatus(OverviewSection.CheckYourAnswers, COMPLETED));
        And("^I click the ECMT Removals Check your answers link on the overview page again$", () -> OverviewPage.clickOverviewSection(OverviewSection.CheckYourAnswers));
        Then("^I am navigated to the ECMT Removals check your answers page$", () ->{
                CheckYourAnswerPage.untilOnPage();
                CheckYourAnswerPage.hasPageHeading();
        });
        Then("^I am on ECMT Removal Declaration page$", DeclarationPage::untilOnPage);
        Then("^I choose to change the ECMT Removals Permits Eligibility  section$", () -> {
            CheckYourAnswerPage.clickChangeAnswer(ECMTRemovalsSection.RemovalsEligibility);
        });
        Then("^I choose to change the ECMT Removals Permits Cabotage section$", () -> CheckYourAnswerPage.clickChangeAnswer(ECMTRemovalsSection.Cabotage));
        Then("^I choose to change the ECMT Removals Permits Number of permits section$", () -> CheckYourAnswerPage.clickChangeAnswer(ECMTRemovalsSection.NumberOfPermits));
        Then("^I should be on the ECMT international removal number of permits page$", () -> { NumberofPermitsPage.hasheading(); });
    }
}
