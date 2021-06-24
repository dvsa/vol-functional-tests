package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.OrganisationModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.common.type.Permit;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.pages.external.permit.enums.sections.MultilateralSection;
import org.dvsa.testing.lib.util.CommonPatterns;
import org.junit.Assert;

import java.util.Comparator;
import java.util.stream.Collectors;

import static org.dvsa.testing.lib.pages.external.permit.BasePermitPage.getReferenceFromPage;

public class CheckYourAnswersPageSteps implements En {
    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world) {
        Given("I am on the annual multilateral check your answers page", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication().permitType(PermitType.ANNUAL_MULTILATERAL, operatorStore)
                    .licencePage(operatorStore, world).overviewPage(OverviewSection.NumberOfPaymentsRequired, operatorStore);
            NumberOfPermitsPageJourney.completeMultilateralPage();
        });
        Then("the annual bilateral check your answers page has an application reference displayed", () -> {
            String message = "Expected there to be a reference number displayed in the correct format but it wasn't";
            Assert.assertTrue(message, getReferenceFromPage().matches(CommonPatterns.REFERENCE_NUMBER));
        });
        Then("^Annual multilateral application answers are displayed on the check your answers page$", () -> {
            LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
            AnnualMultilateralStore permit = licence.getLatestAnnualMultilateral().get();
            OrganisationModel organisation = OrganisationAPI.dashboard(operatorStore.getOrganisationId());
            String expectedLicence = String.format(
                    "%s\n%s",
                    organisation.getDashboard().getLicence(licence.getLicenceNumber()).get().getLicNo(),
                    organisation.getDashboard().getLicence(licence.getLicenceNumber()).get().getTrafficArea().getName()
            );

            // Checks permit type
            Assert.assertEquals(operatorStore.getCurrentPermitType().get().toString(), CheckYourAnswerPage.getAnswer(MultilateralSection.PermitType));

            // Checks licence selected
            Assert.assertEquals(expectedLicence, CheckYourAnswerPage.getAnswer(MultilateralSection.Licence));

            // Check number of permits required
            Assert.assertEquals(
                    permit.getNumberOfPermits().stream().sorted(Comparator.comparing(Permit::getYear, Comparator.reverseOrder())).map(p -> String.format("%d permits in %s", p.getNumberOfPermits(), p.getYear())).collect(Collectors.joining("\n")),
                    CheckYourAnswerPage.getAnswer(MultilateralSection.NumberOfPermits)
            );
        });
        Then("I am navigated to annual multilateral check your answers page", CheckYourAnswerPage::untilOnPage);
    }
}
