package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.*;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.EssentialInformationPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitFeePage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;


public class BilateralStandardAndCabotagePermitFeePageSteps implements En
{
    public BilateralStandardAndCabotagePermitFeePageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore)
    {
        And("^I'm on the annual bilateral standard and cabotage permit fee page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickNoToCabotage();
            BilateralJourneySteps.saveAndContinue();
            NumberOfPermitsPageJourneySteps.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourneySteps.completeDeclaration();
        });
        And("^I'm on the annual bilateral standard and cabotage permit fee page with Cabotage selected on Cabotage page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickYesToCabotage();
            BilateralJourneySteps.yesAndCabotagePermitConfirmation();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourneySteps.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourneySteps.completeDeclaration();
        });
        And("^I'm on the annual bilateral standard permit no cabotage fee page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            PermitUsagePage.saveAndContinue();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourneySteps.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourneySteps.completeDeclaration();
        });
        Then("^the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table$", () ->
        {
            Assert.assertTrue(BasePermitPage.getElementValueByText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH),true);

            // Application reference check
            String actualReference = PermitFeePage.getTableSectionValue(FeeSection.ApplicationReference);
            String licence1 = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            Assert.assertEquals(actualReference.contains(licence1),true);
            Assert.assertTrue(actualReference.contains(licence1));

            // Application date check
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            LocalDateTime expectedDateTime = LocalDateTime.now();
            String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
            String expectedDate = expectedDateTime.format(format);
            Assert.assertEquals(expectedDate, actualDate);

            // Permit type check
            String actualPermitType = PermitFeePage.getTableSectionValue(FeeSection.PermitType);
            String expectedPermitType = PermitType.ANNUAL_BILATERAL.toString();
            Assert.assertEquals(expectedPermitType, actualPermitType);

            // Number of permits required check
            String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.PermitsRequired);
            String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPageJourneySteps.permitValue);
            Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

            // Total fee to be paid check
            int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));
            int numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPageJourneySteps.permitValue));

            // if the journey is Standard Multiple then total fee is number of permits * 50
            if(NumberOfPermitsPageJourneySteps.getLabel().contentEquals("Standard multiple journey permit"))
            {
                int expectedTotal = numberOfPermits * 50;
                Assert.assertEquals(actualTotal, expectedTotal);

                //Fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH),operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH), NumberOfPermitsPageJourneySteps.getPermitValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
            }
            // if the journey is any other than Standard Multiple then total fee is number of permits * 8
            else{
                int expectedTotal = numberOfPermits * 8;
                Assert.assertEquals(actualTotal, expectedTotal);
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH),operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH), NumberOfPermitsPageJourneySteps.getPermitValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
                }
        });
        Then("^the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table when cabotage is selected$", () ->
        {
            Assert.assertTrue(BasePermitPage.getElementValueByText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH),true);

            // Application reference check
            String actualReference = PermitFeePage.getTableSectionValue(FeeSection.ApplicationReference);
            String licence1 = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            Assert.assertEquals(actualReference.contains(licence1),true);
            Assert.assertTrue(actualReference.contains(licence1));

            // Application date check
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            LocalDateTime expectedDateTime = LocalDateTime.now();
            String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
            String expectedDate = expectedDateTime.format(format);
            Assert.assertEquals(expectedDate, actualDate);

            // Permit type check
            String actualPermitType = PermitFeePage.getTableSectionValue(FeeSection.PermitType);
            String expectedPermitType = PermitType.ANNUAL_BILATERAL.toString();
            Assert.assertEquals(expectedPermitType, actualPermitType);



            // if the journey is Standard Multiple then total fee is number of permits * 50 and if it's Cabotage multiple then number of permits * 8
            if(NumberOfPermitsPageJourneySteps.getFieldCount() >1) {
                int standardValue = Integer.parseInt(NumberOfPermitsPageJourneySteps.getStandardValue());
                int expectedTotalStandardValue = standardValue * 50;
                int cabotageValue = Integer.parseInt(NumberOfPermitsPageJourneySteps.getCabotageValue());
                int expectedTotalCabotageValue = cabotageValue * 8;
                int expectedTotalFee = expectedTotalStandardValue + expectedTotalCabotageValue;

                // Total fee to be paid check
                int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));
                Assert.assertEquals(actualTotal, expectedTotalFee);

                // Number of permits required check
                int totalpermits = standardValue + cabotageValue;
                String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.PermitsRequired);
                Assert.assertEquals(String.valueOf(totalpermits), String.valueOf(actualNumberOfPermits));

                //Fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr[1]/td[2][@data-heading='Type']", SelectorType.XPATH).contains(NumberOfPermitsPage.getStandardLabel())), true);
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[1][@data-heading='Country']", SelectorType.XPATH), operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[3][@data-heading='Number of permits']", SelectorType.XPATH), NumberOfPermitsPageJourneySteps.getStandardValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[4][@data-heading='Total fee']", SelectorType.XPATH), "£" + expectedTotalStandardValue);
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr[2]/td[2][@data-heading='Type']", SelectorType.XPATH).contains(NumberOfPermitsPage.getCabotageLabel())), true);
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[1][@data-heading='Country']", SelectorType.XPATH), operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[3][@data-heading='Number of permits']", SelectorType.XPATH), NumberOfPermitsPageJourneySteps.getCabotageValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[4][@data-heading='Total fee']", SelectorType.XPATH), "£" + expectedTotalCabotageValue);
            }
            // if the journey is any other than Standard Multiple then total fee is number of permits * 8
            else{
                int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));

                // Total fee to be paid check
                int numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPageJourneySteps.permitValue));
                int expectedTotal = numberOfPermits * 8;
                Assert.assertEquals(actualTotal, expectedTotal);

                // Number of permits required check
                String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.PermitsRequired);
                String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPageJourneySteps.permitValue);
                Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

               // fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH),operatorStore.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH), NumberOfPermitsPageJourneySteps.getPermitValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
            }
        });
    } //TODO: Check page for repetition. Seems to be a fair bit.
}
