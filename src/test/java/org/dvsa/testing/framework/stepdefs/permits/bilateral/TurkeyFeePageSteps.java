package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TurkeyFeePageSteps extends BasePermitPage implements En {
    public TurkeyFeePageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {

        When("^I am on the permit fee page for annual permit.bilateral turkey application with correct information and content$", () -> {
            PermitFeePage.untilOnPage();

            // Checking Fee Summary section contents are displayed correctly
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
            String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.PermitsRequired);
            String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPageJourney.permitValue);
            Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

            // Total fee to be paid check
            int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalFee)).get().replaceAll(",", ""));
            int numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPageJourney.permitValue));
            int expectedTotal = numberOfPermits * 8 ;
            Assert.assertEquals(actualTotal, expectedTotal);

            //Fee breakdown check
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Type']",SelectorType.XPATH),"Standard single journey");
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH), operatorStore.getCountry());
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']",SelectorType.XPATH), NumberOfPermitsPageJourney.getPermitValue());
            Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£"+expectedTotal);
        });

        When("^I submit and pay the Bilateral fee$", () -> {
            EcmtApplicationJourney.getInstance()
                    .feeOverviewPage();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
        });

    }
}

