package org.dvsa.testing.framework.stepdefs.permits.common;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.hamcrest.core.StringContains;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
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
