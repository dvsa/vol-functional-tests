package org.dvsa.testing.framework.stepdefs.permits.common;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.FeeOverviewPage;
import org.hamcrest.core.StringContains;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getURL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FeePageSteps implements En {

    public FeePageSteps(World world, OperatorStore operatorStore) {

        And("^I am on the fee page$", () -> {
            clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtAnnual, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
            CheckYourAnswersPage.saveAndContinue();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
          //  FeeOverviewPage.untilOnPage(Duration.MEDIUM, ChronoUnit.SECONDS);
        });
        When("^I submit and pay$", FeeOverviewPage::saveAndContinue);
        When("^I save and return to overview from fee page$", BaseFeeOverviewPage::returnToOverview);
        Then("^I expect the reference number to match$", () -> {
            String actualReference = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.ApplicationReference);
            String expectedReference = ECMTPermitApplicationSteps.applicationReference.get("application.reference");
            assertThat(actualReference, is(expectedReference));
        });
        Then("^the number of permits on the fee overview should match$", () -> {
            String expectedNumberOfPermits = String.valueOf(operatorStore.getLatestLicence().get().getEcmt().getNumberOfPermits());
        });
        Then("^the price per permit is as expected$", () -> {
                    String feePerPermit = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.ApplicationFeePerPermit);
                    assertThat(feePerPermit, is("£" + "10"));
                });

        Then("^the total application fee is calculated correctly$", () -> {
            String numberOfPermits = BaseFeeOverviewPage.numberOfPermits("Number of permits");
           assertThat(FeeOverviewPage.totalFee(), is(String.valueOf(operatorStore.getLatestLicence().get().getEcmt().getNumberOfPermits() * 10)));
        });
        Then("^I am taken to CPMS payment page$", () -> {
            Assert.assertThat(getURL().getHost(), StringContains.containsString("e-paycapita"));
        });
        Then("^the page heading and alert message on the fee page is displayed correctly$", () -> {
            FeeOverviewPage.pageHeading();
            FeeOverviewPage.alertMessage();
            FeeOverviewPage.subHeading();
        });
        //Then("^the issuing fee per permit link opens in a new window$", FeeOverviewPage::issuingfeeperpermitlink);
        Then("^the table contents matches as per AC$", () -> {
            FeeOverviewPage.tableCheck();
            String expectedDateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            String actualDate = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.ApplicationDate);
            Assert.assertEquals(expectedDateTime,actualDate);
        });
    }


}
