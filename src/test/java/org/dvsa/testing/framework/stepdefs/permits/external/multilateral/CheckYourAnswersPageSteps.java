package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import apiCalls.Utils.eupaBuilders.organisation.OrganisationModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.lib.pages.common.type.Permit;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.CheckYourAnswersPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;
import org.junit.Assert;

import java.util.Comparator;
import java.util.stream.Collectors;

public class CheckYourAnswersPageSteps implements En {
    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world) {
        Given("I am on the annual multilateral check your answers page", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication().permitType(PermitTypePage.PermitType.AnnualMultilateral, operatorStore)
                    .licencePage(operatorStore, world).overviewPage(OverviewPage.Section.NumberOfPaymentsRequired, operatorStore)
                    .numberOfPermitsPage(operatorStore);
        });
        Then("the annual bilateral check your answers page has an application reference displayed", () -> {
            String message = "Expected there to be a reference number displayed in the correct format but it wasn't";
            Assert.assertTrue(message, CheckYourAnswersPage.hasReference());
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
            Assert.assertEquals(operatorStore.getCurrentPermitType().get().toString(), CheckYourAnswersPage.getAnswer(CheckYourAnswersPage.Section.PermitType));

            // Checks licence selected
            Assert.assertEquals(expectedLicence, CheckYourAnswersPage.getAnswer(CheckYourAnswersPage.Section.Licence));

            // Check number of permits required
            Assert.assertEquals(
                    permit.getNumberOfPermits().stream().sorted(Comparator.comparing(Permit::getYear, Comparator.reverseOrder())).map(p -> String.format("%d permits in %s", p.getNumberOfPermits(), p.getYear())).collect(Collectors.joining("\n")),
                    CheckYourAnswersPage.getAnswer(CheckYourAnswersPage.Section.NumberOfPermits)
            );
        });
        Then("I am navigated to annual multilateral check your answers page", (StepdefBody.A0) CheckYourAnswersPage::untilOnPage);
    }
}
