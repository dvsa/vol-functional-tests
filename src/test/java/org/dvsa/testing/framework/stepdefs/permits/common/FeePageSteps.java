package org.dvsa.testing.framework.stepdefs.permits.common;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.FeeSection;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitFeePage;
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
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.selectECMTValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
            ECMTPermitApplicationSteps.saveAndContinue();
            DeclarationPageJourneySteps.completeDeclaration();
        });
        When("^I submit and pay$", PermitFeePage::saveAndContinue);
        When("^I save and return to overview from fee page$", PermitFeePage::returnToOverview);
        Then("^I expect the reference number to match$", () -> {
            String actualReference = PermitFeePage.getTableSectionValue(FeeSection.ApplicationReference);
            String expectedReference = ECMTPermitApplicationSteps.applicationReference.get("application.reference");
            assertThat(actualReference, is(expectedReference));
        });
        Then("^the number of permits on the fee overview should match$", () -> {
            String expectedNumberOfPermits = String.valueOf(operatorStore.getLatestLicence().get().getEcmt().getNumberOfPermits());
        });
        Then("^the price per permit is as expected$", () -> {
                    String feePerPermit = PermitFeePage.getTableSectionValue(FeeSection.ApplicationFeePerPermit);
                    assertThat(feePerPermit, is("£" + "10"));
                });

        Then("^the total application fee is calculated correctly$", () -> {
           assertThat(PermitFeePage.totalFee(), is(String.valueOf(operatorStore.getLatestLicence().get().getEcmt().getNumberOfPermits() * 10)));
        });
        Then("^I am taken to CPMS payment page$", () -> {
            Assert.assertThat(getURL().getHost(), StringContains.containsString("e-paycapita"));
        });
        Then("^the page heading and alert message on the fee page is displayed correctly$", () -> {
            PermitFeePage.pageHeading();
            PermitFeePage.hasAlertMessage();
            PermitFeePage.subHeading();
        });
        Then("^the table contents matches as per AC$", () -> {
            PermitFeePage.tableCheck();
            String expectedDateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
            Assert.assertEquals(expectedDateTime,actualDate);
        });
    }


}
