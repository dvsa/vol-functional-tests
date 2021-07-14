package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.PermitApplication;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.BasePage;
import org.junit.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;

public class PermitsDashboardPageSteps extends BasePage implements En {
    public PermitsDashboardPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I navigate to the Permits dashboard page from the Bilaterals Overview page$", () -> {
            world.selfServeNavigation.navigateToNavBarPage("home");
            HomePageJourney.selectPermitTab();
        });
        Then("^my Bilaterals permit should be under the ongoing permit application table with correct columns and values$", () -> {
            HomePage.untilOnPage();

            //Verifying that Application reference displayed on the Dashboard is the same as on the application overview page
            String referenceNumber = findElement("(//table)[count(//table)]//td[@data-heading='Application reference']", SelectorType.XPATH).getText();
            Assert.assertEquals(referenceNumber.substring(17), OverviewPage.getReferenceNumber());

            //Verifying that number of permits displayed on the dashboard is same as entered on number of permits page
            String dashboardNumberOfPermits = findElement("(//table)[count(//table)]//td[2][@data-heading='Number of permits']", SelectorType.XPATH).getText();
            Assert.assertEquals(dashboardNumberOfPermits, NumberOfPermitsPageJourney.getLabel());

            //Verifying that the permit type displayed on the dashboard is same as chosen while making an application
            String dashboardPermitType = findElement("(//table)[count(//table)]//td[3][@data-heading='Type']", SelectorType.XPATH).getText();
            Assert.assertEquals(dashboardPermitType, operatorStore.getCurrentPermitType().get().toString());

            //Verify the status on the ongoing permits table is NOT yet submitted
            String statusTagText = getText("//span[contains(text(),'Not yet submitted')]", SelectorType.XPATH);
            Assert.assertTrue(statusTagText.equalsIgnoreCase("NOT YET SUBMITTED"));

            List<PermitApplication> actualPermits = HomePage.PermitsTab.getOngoingPermitApplications();

        });
        Then("^I Submit my Annual bilateral partial application and navigate to the Permits dashboard$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourney.completeDeclaration();
            AnnualBilateralJourney.getInstance()
                    .permitFee();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();
            untilElementIsPresent("//h2[contains(text(),'Issued permits and certificates')]", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
        });
        Then("^my Bilaterals permit should be under the Issued permit applications table with correct columns and values$", () -> {
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.VALID);

            //Verifying that licence number displayed on the dashboard page is the one selected while making an application
            String expectedReference= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            String dashboardIssuedRef = findElement("//div[3]//table[1]//tbody[1]//tr[1]//td[1]//a[1]//span[1]", SelectorType.XPATH).getText();
            Assert.assertEquals(dashboardIssuedRef, expectedReference);

            //Verifying that number of permits displayed on the dashboard is same as entered on number of permits page
            String dashboardNumberOfIssuedPermits = findElement("//div[3]//table[1]//tbody[1]//tr[1]//td[2]", SelectorType.XPATH).getText();
            Assert.assertEquals(dashboardNumberOfIssuedPermits, NumberOfPermitsPageJourney.getLabel());

            //Verifying that the permit type displayed on the dashboard is same as chosen while making an application
            String dashboardIssuedType = findElement("//div[3]//table[1]//tbody[1]//tr[1]//td[3]", SelectorType.XPATH).getText();
            Assert.assertEquals(dashboardIssuedType, operatorStore.getCurrentPermitType().get().toString());

            //Verify the status on the issued permits table is VALID
            String permitsTableStatus = getText("//span[contains(text(),'Valid')]", SelectorType.XPATH);
            Assert.assertEquals(permitsTableStatus, "VALID");
        });
    }
}
