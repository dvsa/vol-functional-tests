package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import edu.emory.mathcs.backport.java.util.Collections;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.enums.sections.BilateralSection;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class CheckYourAnswersSteps implements En {
    public CheckYourAnswersSteps(OperatorStore operatorStore, World world) {
        And("^I'm on the annual bilateral check your answers page$", ()-> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance()
                    .overview(OverviewSection.Countries)
                    .countries(operatorStore);
            NumberOfPermitsPageJourneySteps.completeBilateralPage();

            CheckYourAnswerPage.untilOnPage();
        });
        Then("^I am able to see the application reference number on the annual bilateral check your answers page$", () -> {
            CheckYourAnswerPage.untilOnPage();
            String actualReference =  BasePermitPage.getReferenceFromPage();
            String expectedReference = operatorStore.getCurrentLicence().get().getReferenceNumber();
            Assert.assertEquals(expectedReference, actualReference);
        });
        Then("^the bilateral check your answers page heading should be correct$", CheckYourAnswerPage::untilOnPage);
        Then("^all of the answers displayed match the answers I gave$", () -> {
            LicenceStore licenceStore = operatorStore.getCurrentLicence().get();
            Collections.reverse(NumberOfPermitsPageJourneySteps.getPermitsPerCountry());

            String expectedPermitType = licenceStore.getEcmt().getType().get().toString();
            LicenceModel licenceModel = OrganisationAPI.dashboard(operatorStore.getOrganisationId())
                    .getDashboard().getLicence(licenceStore.getLicenceNumber()).get();
            String expectedLicence = licenceModel.getLicNo().concat("\n" + licenceModel.getTrafficArea().getName());

            List<String> deliveryCountries =
                    NumberOfPermitsPageJourneySteps.getPermitsPerCountry().stream().map(permit -> permit.getCountry().toString()).collect(Collectors.toList());
            Collections.reverse(deliveryCountries);
            String expectedDeliveryCountry = String.join(", ", deliveryCountries);

            Assert.assertEquals(expectedPermitType, CheckYourAnswerPage.getAnswer(BilateralSection.PermitType));
            Assert.assertEquals(expectedLicence, CheckYourAnswerPage.getAnswer(BilateralSection.Licence));
            Assert.assertEquals(expectedDeliveryCountry, CheckYourAnswerPage.getAnswer(BilateralSection.Country));

            NumberOfPermitsPageJourneySteps.getPermitsPerCountry().sort((o1, o2) -> {
                int result = o1.getCountry().toString().substring(0, 1).compareTo(o2.getCountry().toString().substring(0, 1));

                if (result == 0 && Integer.parseInt(o1.getYear()) < Integer.parseInt(o2.getYear())) {
                    result = -1;
                } else if (result == 0 && Integer.parseInt(o1.getYear()) >Integer.parseInt(o2.getYear())) {
                    result = 1;
                }

                return result;
            });

            String expectedPermits = NumberOfPermitsPageJourneySteps.getPermitsPerCountry().stream().map(permits -> String.format("%d permits for %s in %s", permits.getQuantity(), permits.getCountry(), permits.getYear())).collect(Collectors.joining("\n"));

            Assert.assertEquals(expectedPermits, CheckYourAnswerPage.getAnswer(BilateralSection.Permits));
        });
    }
}
