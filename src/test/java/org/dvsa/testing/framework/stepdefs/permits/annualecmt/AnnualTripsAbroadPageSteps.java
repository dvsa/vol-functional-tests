package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.EuroEmissioStandardsPage;

public class AnnualTripsAbroadPageSteps implements En {

    public AnnualTripsAbroadPageSteps(World world, OperatorStore store) {
        And("^I am on the annual trips abroad page$", () -> {
            LicenceStore licenceStore = store.getCurrentLicence().orElseGet(LicenceStore::new);
            store.withLicences(licenceStore);
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
            CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.certificatesRequired(true);
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();

        });
        Given("^I specify a valid amount of annual trips$", NumberOfTripsPage::numberOfTripsValue);
        Given("^I specify an invalid input$", NumberOfTripsPage::inValidValue);

        Given("^I specify an invalid ([\\w\\-]+) of annual trips$", (StepdefBody.A1<String>) NumberOfTripsPage::quantity);
    }

}
