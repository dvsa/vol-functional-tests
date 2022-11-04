package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import activesupport.string.Str;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeePageSteps extends BasePermitPage implements En {
    public FeePageSteps(World world) {

        When("^I am on the permit fee page for annual bilateral application with correct information and content$", () -> {
            PermitFeePage.untilOnPage();

            // Checking Fee Summary section contents are displayed correctly
            assertTrue(true,BasePermitPage.getElementValueByText("//h2[contains(text(),'Fee summary')]",SelectorType.XPATH));

            // Application reference check
            String actualReference = PermitFeePage.getTableSectionValue(FeeSection.ApplicationReference);
            assertTrue(actualReference.contains(world.applicationDetails.getLicenceNumber()));
            // Application date check
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            LocalDateTime expectedDateTime = LocalDateTime.now();
            String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
            String expectedDate = expectedDateTime.format(format);
            assertEquals(expectedDate, actualDate);

            // Permit type check
            String actualPermitType = PermitFeePage.getTableSectionValue(FeeSection.PermitType);
            String expectedPermitType = PermitType.ANNUAL_BILATERAL.toString();
            assertEquals(expectedPermitType, actualPermitType);

            // Number of permits required check
            String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.NumberOfPermits);
            String expectedNumberOfPermits = String.valueOf(NumberOfPermitsPageJourney.permitValue);
            assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

            // Total fee to be paid check
            int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalFeeToBePaid)).get().replaceAll(",", ""));
            int  numberOfPermits = Integer.parseInt(String.valueOf(NumberOfPermitsPageJourney.permitValue));
            int expectedTotal= numberOfPermits * 8 ;
            assertEquals(actualTotal,expectedTotal);

            //Fee breakdown check
            assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Type']", SelectorType.XPATH),"Standard single journey");
            assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Number of permits']", SelectorType.XPATH), NumberOfPermitsPageJourney.getPermitValue());
            assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Total fee']", SelectorType.XPATH),"£" + expectedTotal);
        });

        When("^I submit and pay the Bilateral fee$", () -> {
            PermitFeePage.saveAndContinue();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
        });

    }
}

