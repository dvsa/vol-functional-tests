package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import activesupport.string.Str;
<<<<<<< HEAD
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.*;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.external.enums.JourneyType;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BilateralStandardAndCabotagePermitFeePageSteps extends BasePage implements En {

    public BilateralStandardAndCabotagePermitFeePageSteps(World world) {
        And("^I'm on the annual bilateral standard and cabotage permit fee page$", () -> {
            AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilPermitFeePage(world, PeriodType.BilateralsStandardAndCabotagePermits, Country.Norway, false);
        });
        And("^I'm on the annual bilateral standard and cabotage permit fee page with Cabotage selected on Cabotage page$", () -> {
            AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilPermitFeePage(world, PeriodType.BilateralsStandardAndCabotagePermits, Country.Norway, true);
        });
        And("^I'm on the annual bilateral standard permit no cabotage fee page$", () -> {
            AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilPermitFeePage(world, PeriodType.BilateralsStandardPermitsNoCabotage, Country.Norway, null);
        });
        Then("^the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table$", () -> {
            Assert.assertTrue(BasePermitPage.getElementValueByText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH),true);

            // Application reference check
            String actualReference = PermitFeePage.getTableSectionValue(FeeSection.ApplicationReference);
            Assert.assertTrue(actualReference.contains(world.applicationDetails.getLicenceNumber()));

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
            String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.NumberOfPermits);
            String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPageJourney.permitValue);
            Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

            // Total fee to be paid check
            int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));
            int numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPageJourney.permitValue));

            // Standard Multiple total fee is number of permits * 50 but any other type is number of permits * 8
            boolean isStandardMultipleJourneyPermit = NumberOfPermitsPageJourney.getLabel().contentEquals("Standard multiple journey permit");
            int expectedTotal = isStandardMultipleJourneyPermit ? numberOfPermits * 50 : numberOfPermits * 8;
            Assert.assertEquals(actualTotal, expectedTotal);
            Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH), AnnualBilateralJourney.getCountry());
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH), NumberOfPermitsPageJourney.getPermitValue());
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
        });
        Then("^the application details on the bilateral standard and cabotage fee page are displayed correctly in fee page table when cabotage is selected$", () -> {
            Assert.assertTrue(BasePermitPage.getElementValueByText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH),true);

            // Application reference check
            String actualReference = PermitFeePage.getTableSectionValue(FeeSection.ApplicationReference);
            Assert.assertTrue(actualReference.contains(world.applicationDetails.getLicenceNumber()));

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
            if(NumberOfPermitsPageJourney.getFieldCount() >1) {
                int standardValue = Integer.parseInt(NumberOfPermitsPageJourney.getStandardValue());
                int expectedTotalStandardValue = standardValue * 50;
                int cabotageValue = Integer.parseInt(NumberOfPermitsPageJourney.getCabotageValue());
                int expectedTotalCabotageValue = cabotageValue * 8;
                int expectedTotalFee = expectedTotalStandardValue + expectedTotalCabotageValue;

                // Total fee to be paid check
                int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));
                Assert.assertEquals(actualTotal, expectedTotalFee);

                // Number of permits required check
                int totalpermits = standardValue + cabotageValue;
                String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.NumberOfPermits);
                Assert.assertEquals(String.valueOf(totalpermits), String.valueOf(actualNumberOfPermits));

                // Fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr[1]/td[2][@data-heading='Type']", SelectorType.XPATH).contains(NumberOfPermitsPage.getStandardLabel())), true);
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[1][@data-heading='Country']", SelectorType.XPATH), AnnualBilateralJourney.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[3][@data-heading='Number of permits']", SelectorType.XPATH), NumberOfPermitsPageJourney.getStandardValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr[1]/td[4][@data-heading='Total fee']", SelectorType.XPATH), "£" + expectedTotalStandardValue);
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr[2]/td[2][@data-heading='Type']", SelectorType.XPATH).contains(NumberOfPermitsPage.getCabotageLabel())), true);
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[1][@data-heading='Country']", SelectorType.XPATH), AnnualBilateralJourney.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[3][@data-heading='Number of permits']", SelectorType.XPATH), NumberOfPermitsPageJourney.getCabotageValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr[2]/td[4][@data-heading='Total fee']", SelectorType.XPATH), "£" + expectedTotalCabotageValue);
            }
            // if the journey is any other than Standard Multiple then total fee is number of permits * 8
            else{
                int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));

                // Total fee to be paid check
                int numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPageJourney.permitValue));
                int expectedTotal = numberOfPermits * 8;
                Assert.assertEquals(actualTotal, expectedTotal);

                // Number of permits required check
                String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.NumberOfPermits);
                String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPageJourney.permitValue);
                Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

               // fee breakdown check
                Assert.assertTrue(String.valueOf(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH).contains(PermitUsagePage.getJourney())),true);
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH), AnnualBilateralJourney.getCountry());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH), NumberOfPermitsPageJourney.getPermitValue());
                Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+ expectedTotal);
            }
        });
    }
}
