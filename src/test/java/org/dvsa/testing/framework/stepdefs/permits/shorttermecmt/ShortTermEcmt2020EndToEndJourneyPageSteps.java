package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;


import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;

import static org.junit.Assert.assertEquals;

public class ShortTermEcmt2020EndToEndJourneyPageSteps extends BasePage implements En {
    public ShortTermEcmt2020EndToEndJourneyPageSteps(World world)  {
        Then("^I select year on the select year page$", YearSelectionPage::selectShortTermValidityPeriod);

        Then("^I select any licence number for short term permit$", () -> {
            ShorttermECMTJourney.getInstance().licencePage(world);
        });
        Then("^I complete the Check if you need ECMT permits section and click save and continue$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourney.completePage();
        });
        Then("^I complete Certificates required page section and click save and continue$", () -> {
            String heading = CertificatesRequiredPage.getPageHeading();
            assertEquals("Mandatory certificates for vehicles and trailers you intend to use", heading);
            CertificatesRequiredPage.completePage();
        });
        Then("^I complete Countries with limited permits section and click save and continue$", () -> {
            CountriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
            BasePermitPage.saveAndContinue();
        });
        Then("^I complete Number of permits required section and click save and continue$", () -> {
            NumberOfPermitsPageJourney.hasPageHeading();
            NumberOfPermitsPageJourney.completeECMTPage();
        });
        Then("^I complete Euro emissions standard page section and click save and continue$", () -> {
            EmissionStandardsPageJourney.hasPageHeading();
            EmissionStandardsPageJourney.completePage();
        });
        Then("^I click confirm and continue on the Check your answers page$", CheckYourAnswerPage::saveAndContinue);
        Then("^I click on Accept and continue on the Declaration page$", DeclarationPageJourney::completeDeclaration);
        Then("^I click on Submit and Pay button on the Permit fee page and complete the payment", () -> {
            PermitFeePage.submitAndPay();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
        });
        Then("^I click on the Finish button on the Application submitted page", () -> {
            SubmittedPage.untilOnPage();
            SubmittedPageJourney.hasPageHeading();
            waitForElementToBePresent("//a[contains(text(),'Go to permits dashboard')]");
            BilateralJourneySteps.clickFinishButton();
        });;
        Then("^I am navigated back to the permits dashboard page with my application status shown as Under Consideration", () -> {
            HomePage.PermitsTab.selectFirstOngoingApplication();
            assertEquals(getElementValueByText("//span[@class='status orange']",SelectorType.XPATH),"UNDER CONSIDERATION");
        });
    }
}
