package org.dvsa.testing.framework.stepdefs.permits.common;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.FeeSection;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitFeePage;
import org.hamcrest.core.StringContains;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeePageSteps extends BasePage implements En {

    public FeePageSteps(World world, OperatorStore operatorStore) {

        And("^I am on the fee page$", () -> {
            clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.selectECMTValidityPeriod();
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
            ECMTPermitApplicationSteps.saveAndContinue();
            DeclarationPageJourney.completeDeclaration();
        });
        When("^I submit and pay$", PermitFeePage::saveAndContinue);
        When("^I save and return to overview from fee page$", PermitFeePage::clickReturnToOverview);
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
            assertEquals("Permit fee", PermitFeePage.getPageHeading());
            assertTrue(PermitFeePage.isAlertMessagePresent());
            assertEquals("Fee summary", PermitFeePage.getSubHeading());
        });
        Then("^the table contents matches as per AC$", () -> {
            PermitFeePage.tableCheck();
            String expectedDateTime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
            String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
            assertEquals(expectedDateTime, actualDate);
        });
    }


}
