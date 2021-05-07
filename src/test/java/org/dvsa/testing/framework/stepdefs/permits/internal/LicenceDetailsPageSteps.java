package org.dvsa.testing.framework.stepdefs.permits.internal;

import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.junit.Assert;

public class LicenceDetailsPageSteps implements En {

    public LicenceDetailsPageSteps(OperatorStore operator, World world, LicenceStore licenceStore) {
        Given("^I am viewing a good operating licence on internal$", () -> {
            BaseInternalJourney.getInstance().openLicence(
                    OrganisationAPI.dashboard(operator.getOrganisationId()).getDashboard().getLicences().get(0).getLicenceId()
            ).signin();

//Existing code commented out
            //IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.DocsAndAttachments);
           // LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
           // operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            //BaseInternalJourney.getInstance().openLicence(
                    //licence.getLicenceId()
            //).signin();
        });
        Then("^I should see the IRHP permits tab$", () -> {
            Assert.assertTrue("The IRHP Permits tab is not displayed on the current page", LicenceDetailsPage.Tab.hasTab(LicenceDetailsPage.DetailsTab.IrhpPermits));
        });
        Then("^I should not see the IRHP permits tab$", () -> {
            Assert.assertTrue("The IRHP Permits tab is displayed on the current page", !LicenceDetailsPage.Tab.hasTab(LicenceDetailsPage.DetailsTab.IrhpPermits));
        });
    }

}
