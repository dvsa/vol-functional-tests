package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import apiCalls.eupaActions.OrganisationAPI;
import io.cucumber.java8.En;
import edu.emory.mathcs.backport.java.util.Collections;
import io.cucumber.java8.PendingException;
import io.cucumber.java8.StepDefinitionBody;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.common.type.Permit;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.CheckYourAnswersPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.InternalAnnualBilateralPermitApplicationPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.Section.NumberOfPaymentsRequired;

public class NumberOfPermitPageSteps implements En {

    public NumberOfPermitPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the number of permits required page$", () -> {
            AnnualMultilateralJourney
                    .INSTANCE
                    .signin(operatorStore, world)
                    .beginApplication()
                    .permitType(PermitTypePage.PermitType.AnnualMultilateral, operatorStore)
                    .licencePage(operatorStore, world)
                    .overviewPage(NumberOfPaymentsRequired, operatorStore);
        });
        Then("^the reference number and heading are displayed correct", () -> {
            String expectedLicenceNumber = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalAccessError::new)
                    .getReferenceNumber();
            String actualReferenceNumber = NumberOfPermitsPage.reference();
            Assert.assertEquals(expectedLicenceNumber, actualReferenceNumber);
            Assert.assertEquals("How many permits do you require for this licence?",OverviewPage.pageHeading().trim());
        });
        Then("^should see the text box for each year for Annual Multilateral permit stock with an open window$", () -> {
            //need to add steps later
            throw new PendingException();
        });
        When("^I choose to change the number of permits section$", () -> {
            CheckYourAnswersPage.untilOnPage();
            CheckYourAnswersPage.change(CheckYourAnswersPage.Section.NumberOfPermits);
        });
        And("^my previously selected values are remembered", () -> {
           List<Permit> actualPermits = NumberOfPermitsPage.currentPermits();
           List<Permit> expectedPermits = operatorStore.getCurrentLicence()
                   .orElseThrow(IllegalStateException::new)
                   .getLatestAnnualMultilateral().get().getNumberOfPermits();

           actualPermits.stream().forEach(p -> {
               Assert.assertTrue(expectedPermits.stream().anyMatch(p::equals));
           });
        });
        When("^I specify my number of multilateral permits$", () -> {
            AnnualMultilateralJourney.INSTANCE.numberOfPermitsPage(operatorStore);
        });
        Then("^the number of permits section on the annual multilateral overview page is complete$", () -> {
            boolean isComplete = org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage.checkStatus(OverviewPage.Section.NumberOfPaymentsRequired, PermitStatus.COMPLETED);
            Assert.assertTrue("The 'Number of Permits Required' section status is not complete", isComplete);
        });
        Then("^the user is on annual multilateral check your answers page$", (StepDefinitionBody.A0) CheckYourAnswersPage::untilOnPage);
        When("^the case worker is viewing current fees$", () -> {
            LicenceStore licence = operatorStore.getCurrentLicence().get();
            BaseInternalJourney.getInstance().openLicence(
                    OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0).getLicenceId()
            ).signin();

            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);

            licence.setFees(FeesPage.fees());
        });
        Then("^the relevant error message is displayed$", () -> {
            Assert.assertEquals("You need to apply for at least 1 permit",NumberOfPermitsPage.errorText());
        });
        And("^I enter zero as value in the number of permits fields$", () -> {
            int numberOfFields = NumberOfPermitsPage.numberOfFields();
            List<String> numPermits = new ArrayList<String>(Collections.nCopies(numberOfFields, "0"));
            NumberOfPermitsPage.quantity(numPermits.toArray(new String[numberOfFields]));
        });
        And("^I specify the number of permits I require for my multilateral permit$", () -> {
            AnnualMultilateralJourney.INSTANCE.numberOfPermitsPage(operatorStore);
        });
        When("^I update the number of permits for my multilateral permit$", () -> {
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.viewApplication();
            InternalAnnualBilateralPermitApplicationPage.numPermits(1, 1);
            InternalAnnualBilateralPermitApplicationPage.save();
        });
    }
}
