package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

public class CancelApplicationPageSteps extends BasePage implements En {
    public CancelApplicationPageSteps(OperatorStore operator, World world) {
        And("^I am on the cancel application page for Annual Multilateral$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());

            operator.getLicences().forEach((licence) -> {
                AnnualMultilateralJourney.INSTANCE
                            .beginApplication()
                            .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                            .licencePage(operator, world)
                            .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator)
                            .numberOfPermitsPage(operator);

                NumberOfPermitsPage.overview();
                OverviewPage.clickCancelApplication();
                CancellationPage.untilOnPage();
            });

        });
        Then("^the annual multilateral Cancel Application page has a reference number$", () -> {
            String expectedReference = operator.getCurrentLicence().get().getReferenceNumber();
            assertEquals(expectedReference, BasePermitPage.getReferenceFromPage());
        });
        Then("^the annual multilateral Cancel Application confirmation checkbox is unselected by default$", () -> {
            String message = "Expected checkbox to be unselected but it was selected";
            Assert.assertFalse(message, CancellationPage.isConfirmed());
        });
        When("^I cancel my Annual Multilateral application without confirming$", CancellationPage::clickCancelButton);
        When("^I confirm and cancel my annual multilateral permit$", () -> {
            CancellationPage.clickCancelCheckbox();
            CancellationPage.clickCancelButton();
        });
        And("^there are no fees for the permit$", () -> {
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());
            Assert.assertFalse(HomePage.FeesTab.hasOutstandingFees());
        });
    }
}