package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.FeeSection;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.PermitFeePage;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.dvsa.testing.lib.pages.BasePage.waitAndClick;

public class FeePageSteps implements En {

    public FeePageSteps(OperatorStore operator, World world) {
        And("I am on the annual multilateral fee page", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            AnnualMultilateralJourney.INSTANCE
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator);
            NumberOfPermitsPageJourney.completeMultilateralPage();
            AnnualMultilateralJourney.INSTANCE
                    .checkYourAnswers();
            DeclarationPageJourney.completeDeclaration();

            PermitFeePage.untilOnPage();
        });
        Then("^the application details are displayed in fee page table$", () -> {
            AnnualMultilateralStore permit = operator.getCurrentLicence()
                    .orElseThrow(IllegalStateException::new).getLatestAnnualMultilateral()
                    .orElseThrow(IllegalStateException::new);


            // Application reference check
            String actualReference = PermitFeePage.getTableSectionValue(FeeSection.ApplicationReference);
            Assert.assertEquals(permit.getReference(), actualReference);

            // Application date check
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            LocalDateTime expectedDateTime = NumberOfPermitsPageJourney.getApplicationDate();
            String actualDate = PermitFeePage.getTableSectionValue(FeeSection.ApplicationDate);
            String expectedDate = expectedDateTime.format(format);

            Assert.assertEquals(expectedDate, actualDate);

            // Permit type check
            String actualPermitType = PermitFeePage.getTableSectionValue(FeeSection.PermitType);
            String expectedPermitType = PermitType.ANNUAL_MULTILATERAL.toString();

            Assert.assertEquals(expectedPermitType, actualPermitType);

            // Number of permits required check
            String actualNumberOfPermits = PermitFeePage.getTableSectionValue(FeeSection.PermitsRequired);
            String expectedNumberOfPermits = String.valueOf(permit.totalNumberOfPermits());

            Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);

            // Total fee to be paid check
            int actualTotal = Integer.parseInt(Str.find("[\\d,]+", PermitFeePage.getTableSectionValue(FeeSection.TotalApplicationFeeToBePaid)).get().replaceAll(",", ""));
            int  numberOfPermits = Integer.parseInt(String.valueOf(permit.totalNumberOfPermits()));
            String feePerPermit= getElementValueByText("//tbody/tr/td[@data-heading='Fee per permit']", SelectorType.XPATH).toString();
            int feePerPermit1= Integer.parseInt(feePerPermit.substring(1));
            int expectedTotal = numberOfPermits * feePerPermit1;
            Assert.assertEquals(actualTotal,expectedTotal);
        });

        Then("^the fees are to be updated to reflect changes$", () -> {
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);
            List<FeesPage.Fee> actualFees = FeesPage.fees();
            List<FeesPage.Fee> expectedFees = operator.getCurrentLicence().get().getFees();
            Assert.assertNotEquals(expectedFees, actualFees);
        });
        Then("^I save and return to overview on multilateral fee page$", () -> {
            waitAndClick("//a[@class='govuk-link govuk-body']",SelectorType.XPATH);
        });
    }
}
