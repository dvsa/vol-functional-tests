package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.string.Str;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.lib.pages.common.type.Permit;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.multilateral.FeeOverviewPage;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.untilOnPage;


public class PermitFeePageSteps implements En {
    public PermitFeePageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        And("^I'm on the annual bilateral cabotage only permit fee page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            CabotagePage.yesButton();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true);
        });
        And("^I select the submit and pay link from overview page$", OverviewPage::submitAndPay);
        Then("^I select return to overview link on the permit fee page$", PermitFeePage::returnToOverview);
        Then("^I should be on the permit fee page$", PermitFeePage::untilOnPage);
        Then("^the page heading is displayed correctly on the fee page$", PermitFeePage::untilOnPage);
        Then("^the Fee-breakdown sub-heading can be seen below the fee summary table$", PermitFeePage::feeBreakDownHeading);
        Then("^the application details on the fee page is displayed correctly in fee page table$", () -> {
            Assert.assertTrue(BasePermitPage.getElementValueByText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH),true);
            // Application reference check
            String actualReference = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.ApplicationReference);
            String licence1 = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            Assert.assertEquals(actualReference.contains(licence1),true);
            Assert.assertTrue(actualReference.contains(licence1));
            // Application date check
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            LocalDateTime expectedDateTime = LocalDateTime.now();
            String actualDate = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.ApplicationDate);
            String expectedDate = expectedDateTime.format(format);
            Assert.assertEquals(expectedDate, actualDate);

            // Permit type check
            String actualPermitType = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.PermitType);
            String expectedPermitType = PermitTypePage.PermitType.AnnualBilateral.toString();
            Assert.assertEquals(expectedPermitType, actualPermitType);

            // Number of permits required check
            String actualNumberOfPermits = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.PermitsRequired);
            String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPage.permitValue);
            Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

            // Total fee to be paid check
            int actualTotal = Integer.parseInt(Str.find("[\\d,]+", FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));
            int  numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPage.permitValue));
            int expectedTotal= numberOfPermits *8 ;
            Assert.assertEquals(actualTotal,expectedTotal);

            //Fee breakdown check
            Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH),operatorStore.getCountry());
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH),NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+expectedTotal);
        });
        Then("^my fee should be tiered as expected$", () -> {
            List<PermitFeePage.Fee> feeBreakdowns = PermitFeePage.Fees();

            AnnualMultilateralStore permit = operatorStore.getCurrentLicence().get().getLatestAnnualMultilateral().get();
            feeBreakdowns.stream().forEach(fb -> {
                Assert.assertTrue(permit.getNumberOfPermits().stream().map(Permit::getYear).anyMatch(py -> py.equalsIgnoreCase(fb.getYear())));
                Permit existingPermit = permit.getNumberOfPermits().stream().filter(ep -> ep.getYear().equals(fb.getYear()))
                        .findFirst().get();
                Assert.assertEquals(existingPermit.getFeePerPermit().get().intValue(), fb.getFeePerPermit());
            });

        });
    }
}
