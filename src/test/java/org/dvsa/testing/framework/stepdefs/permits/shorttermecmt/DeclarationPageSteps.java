package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.external.enums.JourneyProportion;
import org.dvsa.testing.lib.newPages.external.enums.Sector;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertTrue;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps (World world) {

        Then("^I should see the correct heading on the declaration page$", DeclarationPageJourney::hasPageHeading);
        Then("^the declaration page has a reference number", DeclarationPage::hasReference);

    }
}
