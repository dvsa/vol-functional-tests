package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.external.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

import static org.junit.Assert.assertTrue;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world) {
        Then("^I should get the number of permits page error message$", () ->{
            assertTrue(NumberOfPermitsPage.isEnterNumberOfPermitsErrorTextPresent());
        });
        Then("^I should get the maximum number of permits exceeded page error message$", () ->{
            assertTrue(NumberOfPermitsPage.isMaximumNumberOfPermitsExceededErrorTextPresent());
        });
        Then("^I should get the number of permits page error message on short term$", () ->{
            assertTrue(NumberOfPermitsPage.isShortTermEnterNumberOfPermitsErrorTextPresent());
            assertTrue(NumberOfPermitsPage.isShortTermECMTEmissionErrorTextPresent());
        });

        Then("^I enter the valid number of short term permits required$", NumberOfPermitsPage::enterEuro5OrEuro6permitsValue);
    }
}
