package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.enums.ApplicationInfo;
import org.dvsa.testing.lib.pages.external.permit.enums.RestrictedCountry;
import org.hamcrest.core.StringContains;
import org.junit.Assert;

import java.lang.reflect.Field;

import static org.dvsa.testing.lib.pages.BasePage.getURL;
import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.external.permit.enums.ApplicationInfo.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckYourAnswersPageSteps implements En {

    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world) {
        And("^I have completed all steps prior to check your answers page$", () -> {
            ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage(world, operatorStore);
        });
        Then("^the information I inserted during the application is displayed$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().get();
            String licence = CheckYourAnswersPage.getAnswer(Licence);
            String euro6 = CheckYourAnswersPage.getAnswer(Euro6);
            String cabotage = CheckYourAnswersPage.getAnswer(Cabotage);
            String restrictedCountries = CheckYourAnswersPage.getAnswer(RestrictedCountries);
            assertThat(licence, StringContains.containsString(operatorStore.getCurrentLicence().get().getLicenceNumber()));
            Assert.assertEquals("I confirm that I will only use my ECMT permits with vehicles that meet the minimum euro emissions standards allowed.",euro6);
            Assert.assertEquals("I confirm that I will not undertake cabotage journeys using an ECMT permit.",cabotage);
            Assert.assertEquals("No",restrictedCountries);
        });
        When("^I confirm and continue$", CheckYourAnswersPage::saveAndContinue);
        When("^I change the (.+)$", (String section) ->
            CheckYourAnswersPage.edit(ApplicationInfo.valueOf(section))
        );
        Then("^I should be taken to the (.+) page permits$", (String section) -> {
            Class<? extends BasePage> pageClass = sectionPageClass(ApplicationInfo.valueOf(section));
            Field resourceField = pageClass.getDeclaredField("RESOURCE");
            String resource = (String) resourceField.get(null);
            Assert.assertTrue("The current page does not contain the expected URL resource", isPath(resource));
        });
        When("^I edit (.+) and apply the changes$", (String section) -> {
            ApplicationInfo sectionEnum = ApplicationInfo.valueOf(section);

            CheckYourAnswersPage.edit(sectionEnum);
            updateSectionWithValidRandomAnswer(sectionEnum, world, operatorStore);
        });
    }

    private void updateSectionWithValidRandomAnswer(ApplicationInfo section, World world, OperatorStore operatorStore) {
        LicenceStore licenceStore = operatorStore.getLatestLicence().get();
        operatorStore.withLicences(licenceStore);
        CommonSteps.origin.put("origin", getURL());
        switch (section) {
            case Euro6:
                EcmtApplicationJourney.getInstance().euro6Page(world, licenceStore);
                break;
            case Cabotage:
                EcmtApplicationJourney.getInstance().cabotagePage(world, licenceStore);
                break;
            case RestrictedCountries:
                RestrictedCountry restrictedCountry = RestrictedCountry.random();

                RestrictedCountriesPage.countries(restrictedCountry);
                BasePermitPage.saveAndContinue();

                break;
            case NumberOfPermits:
                EcmtApplicationJourney.getInstance().numberOfPermitsPage(operatorStore);
        }
    }

    private static Class<? extends BasePage> sectionPageClass(ApplicationInfo section) {
        Class<? extends BasePage> pageObject;

        switch (section) {
            case Euro6:
                pageObject = VehicleStandardPage.class;
                break;
            case Cabotage:
                pageObject = CabotagePage.class;
                break;
            case RestrictedCountries:
                pageObject = RestrictedCountriesPage.class;
                break;
            case NumberOfPermits:
                pageObject = NumberOfPermitsPage.class;
                break;
            default:
                throw new IllegalArgumentException();
        }

        return pageObject;
    }

}
