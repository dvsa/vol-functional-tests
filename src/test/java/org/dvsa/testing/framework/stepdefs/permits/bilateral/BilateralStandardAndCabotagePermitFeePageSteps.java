package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.dvsa.testing.lib.pages.external.permit.multilateral.FeeOverviewPage;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.untilOnPage;


public class BilateralStandardAndCabotagePermitFeePageSteps implements En
{
    public BilateralStandardAndCabotagePermitFeePageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore)
    {
        And("^I'm on the annual bilateral standard and cabotage permit fee page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickNoToCabotage();
            BilateralJourneySteps.saveAndContinue();
            NumberOfPermitsPage.numberOfPermitsNew();
            NumberOfPermitsPage.setCabotageValue(NumberOfPermitsPage.getCabotageValue());
            NumberOfPermitsPage.setStandardValue(NumberOfPermitsPage.getStandardValue());
            NumberOfPermitsPage.setFieldCount(NumberOfPermitsPage.getFieldCount());
            NumberOfPermitsPage.setLabel(NumberOfPermitsPage.permitLabel());
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            AnnualBilateralJourney.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true);
        });
        And("^I'm on the annual bilateral standard and cabotage permit fee page with Cabotage selected on Cabotage page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickYesToCabotage();
            BilateralJourneySteps.yesAndCabotagePermitConfirmation();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermitsNew();
            NumberOfPermitsPage.setCabotageValue(NumberOfPermitsPage.getCabotageValue());
            NumberOfPermitsPage.setStandardValue(NumberOfPermitsPage.getStandardValue());
            NumberOfPermitsPage.setFieldCount(NumberOfPermitsPage.getFieldCount());
            NumberOfPermitsPage.setLabel(NumberOfPermitsPage.permitLabel());
            NumberOfPermitsPage.setStandardLabel(NumberOfPermitsPage.getStandardLabel());
            NumberOfPermitsPage.setCabotageLabel(NumberOfPermitsPage.getCabotageLabel());
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true);
        });
        And("^I'm on the annual bilateral standard permit no cabotage fee page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            PermitUsagePage.saveAndContinue();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermitsNew();
            NumberOfPermitsPage.setFieldCount(NumberOfPermitsPage.getFieldCount());
            NumberOfPermitsPage.setLabel(NumberOfPermitsPage.permitLabel());
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true);
        });
        Then("^the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table$", () ->
        {
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
            int numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPage.permitValue));

            // if the journey is Standard Multiple then total fee is number of permits * 50
            if(NumberOfPermitsPage.getLabel().contentEquals("Standard multiple journey permit"))
            {
                int expectedTotal = numberOfPermits * 50;
                Assert.assertEquals(actualTotal, expectedTotal);

                //Fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH),operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH),NumberOfPermitsPage.getPermitValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
            }
            // if the journey is any other than Standard Multiple then total fee is number of permits * 8
            else{
                int expectedTotal = numberOfPermits * 8;
                Assert.assertEquals(actualTotal, expectedTotal);
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH),operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH),NumberOfPermitsPage.getPermitValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
                }
        });
        Then("^the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table when cabotage is selected$", () ->
        {
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



            // if the journey is Standard Multiple then total fee is number of permits * 50 and if it's Cabotage multiple then number of permits * 8
            if(NumberOfPermitsPage.getFieldCount() >1) {
                int standardValue = Integer.parseInt(NumberOfPermitsPage.getStandardValue());
                int expectedTotalStandardValue = standardValue * 50;
                int cabotageValue = Integer.parseInt(NumberOfPermitsPage.getCabotageValue());
                int expectedTotalCabotageValue = cabotageValue * 8;
                int expectedTotalFee = expectedTotalStandardValue + expectedTotalCabotageValue;

                // Total fee to be paid check
                int actualTotal = Integer.parseInt(Str.find("[\\d,]+", FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));
                Assert.assertEquals(actualTotal, expectedTotalFee);

                // Number of permits required check
                int totalpermits = standardValue + cabotageValue;
                String actualNumberOfPermits = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.PermitsRequired);
                Assert.assertEquals(String.valueOf(totalpermits), String.valueOf(actualNumberOfPermits));

                //Fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr[1]/td[2][@data-heading='Type']", SelectorType.XPATH).contains(NumberOfPermitsPage.getStandardLabel())), true);
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[1][@data-heading='Country']", SelectorType.XPATH), operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[3][@data-heading='Number of permits']", SelectorType.XPATH), NumberOfPermitsPage.getStandardValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[4][@data-heading='Total fee']", SelectorType.XPATH), "£" + expectedTotalStandardValue);
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr[2]/td[2][@data-heading='Type']", SelectorType.XPATH).contains(NumberOfPermitsPage.getCabotageLabel())), true);
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[1][@data-heading='Country']", SelectorType.XPATH), operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[3][@data-heading='Number of permits']", SelectorType.XPATH), NumberOfPermitsPage.getCabotageValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[4][@data-heading='Total fee']", SelectorType.XPATH), "£" + expectedTotalCabotageValue);
            }
            // if the journey is any other than Standard Multiple then total fee is number of permits * 8
            else{
                int actualTotal = Integer.parseInt(Str.find("[\\d,]+", FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));

                // Total fee to be paid check
                int numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPage.permitValue));
                int expectedTotal = numberOfPermits * 8;
                Assert.assertEquals(actualTotal, expectedTotal);

                // Number of permits required check
                String actualNumberOfPermits = FeeOverviewPage.getSectionValue(FeeOverviewPage.FeeSection.PermitsRequired);
                String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPage.permitValue);
                Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

               // fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH),operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH),NumberOfPermitsPage.getPermitValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
            }
        });
    }
}
