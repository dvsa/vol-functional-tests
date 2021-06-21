package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.BilateralDashboardPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.ApplicationSubmitPage;
import org.junit.Assert;

import java.util.List;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.lib.pages.Driver.DriverUtils.refreshPage;

public class PermitsDashboardPageSteps implements En {
    public PermitsDashboardPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I navigate to the Permits dashboard page from the Bilaterals Overview page$", () -> {
            OverviewPage.overviewToHome();
            HomePage.selectTab(Tab.PERMITS);
        });
        Then("^my Bilaterals permit should be under the ongoing permit application table with correct columns and values$", () -> {
            HomePage.untilOnPage();
            LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

            //Verifying that Application reference displayed on the Dashboard is the same as on the application overview page
            Assert.assertEquals(BilateralDashboardPage.dashboardReferenceNumber().substring(17),OverviewPage.getReferenceNumber());

            //Verifying that number of permits displayed on the dashboard is same as entered on number of permits page
            Assert.assertEquals(BilateralDashboardPage.dashboardNumberofPermits(),operatorStore.getPermit());

            //Verifying that the permit type displayed on the dashboard is same as chosen while making an application
            Assert.assertEquals(BilateralDashboardPage.dashboardPermitType(),operatorStore.getCurrentPermitType().get().toString());

            //Verify the status on the ongoing permits table is NOT yet submitted
            BilateralDashboardPage.dashboardPermitStatus();

            List<PermitApplication> actualPermits = HomePage.PermitsTab.ongoingPermitApplications();

        });
        Then("^I Submit my Annual bilateral partial application and navigate to the Permits dashboard$", () -> {
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true)
                    .permitFee();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
            ApplicationSubmitPage.finish();
            BilateralDashboardPage.untilOnBilateralsDashboardPage();
        });
        Then("^my Bilaterals permit should be under the Issued permit applications table with correct columns and values$", () -> {
            //HomePage.untilPresentWithRefresh()
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.VALID);

            //Verifying that licence number displayed on the dashboard page is the one selected while making an application
            String expectedReference= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            Assert.assertEquals(BilateralDashboardPage.dashboardRefIssued(),expectedReference);

            //Verifying that number of permits displayed on the dashboard is same as entered on number of permits page
            Assert.assertEquals(BilateralDashboardPage.dashboardNumberofPermitsIssued(),operatorStore.getPermit());

            //Verifying that the permit type displayed on the dashboard is same as chosen while making an application
            Assert.assertEquals(BilateralDashboardPage.dashboardTypeIssued(),operatorStore.getCurrentPermitType().get().toString());

            //Verify the status on the issued permits table is VALID
            BilateralDashboardPage.dashboardTypeStatus();
        });
    }
}
