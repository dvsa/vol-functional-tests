package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CheckYourAnswersPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.Declaration;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.NumberofPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.enums.ECMTRemovalsInfo;
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
                    .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                     .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage()
                    .numberOfPermits();

        });
        Then("^ECMT Removals permit check your answers page has correct heading label$", () -> CheckYourAnswersPage.hasPageHeading());
        And("^the ECMT Removals check your answers page has reference number$", CheckYourAnswersPage::reference);
        And("^the ECMT Removals application answers are displayed on the check your answers page$", () -> {
            String licence = org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CheckYourAnswersPage.getAnswer(ECMTRemovalsInfo.Licence);
            assertThat(licence,StringContains.containsString(operatorStore.getCurrentLicence().get().getLicenceNumber()));
            String permitType = org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CheckYourAnswersPage.getAnswer(ECMTRemovalsInfo.PermitType);
            System.out.println(permitType);
            Assert.assertEquals(permitType,"ECMT International Removal");
            String RemovalsEligibility = org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CheckYourAnswersPage.getAnswer(ECMTRemovalsInfo.RemovalsEligibility);
            Assert.assertEquals(RemovalsEligibility,CheckYourAnswersPage.permitEligibility());
            String Cabotage = org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CheckYourAnswersPage.getAnswer(ECMTRemovalsInfo.Cabotage);
            Assert.assertEquals(Cabotage,CheckYourAnswersPage.cabotage());
        });
        Then("^I am on the ECMT removals permits overview page with check your answers section marked as complete$", () -> OverviewPage.checkStatus("Check your answers",COMPLETED));
        And("^I click the ECMT Removals Check your answers link on the overview page again$", () -> OverviewPage.select(OverviewPage.Section.Checkyouranswers));
        Then("^I am navigated to the ECMT Removals check your answers page$", () ->{
                CheckYourAnswersPage.checkAnswersPageLoad();
                CheckYourAnswersPage.hasPageHeading();
        });
        Then("^I am on ECMT Removal Declaration page$", Declaration::heading);
        //Then("^I choose to change the ECMT Removals Permits Licence section$", () -> CheckYourAnswersPage.change(ECMTRemovalsInfo.Licence));
        Then("^I choose to change the ECMT Removals Permits Eligibility  section$", () -> CheckYourAnswersPage.change(ECMTRemovalsInfo.RemovalsEligibility));
        Then("^I choose to change the ECMT Removals Permits Cabotage section$", () -> CheckYourAnswersPage.change(ECMTRemovalsInfo.Cabotage));
        Then("^I choose to change the ECMT Removals Permits Number of permits section$", () -> CheckYourAnswersPage.change(ECMTRemovalsInfo.NumberOfPermits));
        Then("^I should be on the ECMT international removal number of permits page$", () -> { NumberofPermitsPage.hasheading(); });
    }
}
