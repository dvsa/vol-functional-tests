package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.permit.*;

public class AnnualTripsAbroadPageSteps extends BasePage implements En {

    private World world;

    public AnnualTripsAbroadPageSteps(World world, OperatorStore store) {
        And("^I am on the annual trips abroad page$", () -> {
            LicenceStore licenceStore = store.getCurrentLicence().orElseGet(LicenceStore::new);
            store.withLicences(licenceStore);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourneySteps.completePage();
            CabotagePage.confirmWontUndertakeCabotage();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPageJourneySteps.completeECMTPage();
            EmissionStandardsPageJourneySteps.completePage();

        });
        Given("^I specify a valid amount of annual trips$", NumberOfTripsPage::enterNumberOfTripsValue);
        Given("^I specify an invalid input$", () -> {
            scrollAndEnterField("//input[@type ='text']", SelectorType.XPATH, "1000000");
        });

        Given("^I specify an invalid ([\\w\\-]+) of annual trips$", (StepdefBody.A1<String>) NumberOfTripsPage::quantity);
    }

}
