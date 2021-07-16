package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertTrue;

public class CabotagePageSteps extends BasePermitPage implements En {

    private int numberOfCountries;

    private void annualBilateralOverviewPageUntilPeriodSelectionPage() {
        OverviewPage.untilOnPage();
        OverviewPage.clickCountrySection(Country.Norway);
        EssentialInformationPage.untilOnPage();
        saveAndContinue();
        PeriodSelectionPage.untilOnPage();
    } // Could look a another method where it does the journey with differences in if statements
} // TODO REPETITION.
