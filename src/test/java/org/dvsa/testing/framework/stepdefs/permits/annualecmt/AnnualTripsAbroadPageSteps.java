package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.permits.pages.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.EmissionStandardsPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfTripsPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;

public class AnnualTripsAbroadPageSteps extends BasePage implements En {

    private World world;

    public AnnualTripsAbroadPageSteps(World world, OperatorStore store) {
        And("^I am on the annual trips abroad page$", () -> {
            LicenceStore licenceStore = store.getCurrentLicence().orElseGet(LicenceStore::new);
            store.withLicences(licenceStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.confirmCertificateRequired();
            CertificatesRequiredPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.permitsValue();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPage.confirmCheckbox();
            EmissionStandardsPage.saveAndContinue();

        });
        Given("^I specify a valid amount of annual trips$", NumberOfTripsPage::enterNumberOfTripsValue);
        Given("^I specify an invalid input$", () -> {
            scrollAndEnterField("//input[@type ='text']", SelectorType.XPATH, "1000000");
        });

        Given("^I specify an invalid ([\\w\\-]+) of annual trips$", (StepdefBody.A1<String>) org.dvsa.testing.lib.newPages.permits.pages.NumberOfTripsPage::quantity);
    }

}
