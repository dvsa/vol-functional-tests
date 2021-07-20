package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.EssentialInformationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertTrue;

public class BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps implements En {
    public BilateralStandardPermitNoCabotageCheckYourAnswersPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I am on the Bilateral Standard permits no Cabotage check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().selectCountry(operatorStore, "Norway");
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
        });

        Then("^I see three sections displayed on the table correctly$", () -> {
            assertTrue(BilateralJourneySteps.areSectionsPresent(false));
        });

        Then("^For bilateral standard permits no cabotage permit type,the value of how many permits you need, will be as per the ones saved on the number of permits page$", () -> {
            String permitLabel = NumberOfPermitsPageJourney.getLabel();
            String permitValue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getCabotageValue(), permitValue + " " + permitLabel + "s");
        });
    }
}

