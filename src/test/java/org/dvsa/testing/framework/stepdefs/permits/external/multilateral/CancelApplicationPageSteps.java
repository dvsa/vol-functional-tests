package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import activesupport.IllegalBrowserException;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CancelConfirmationPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.net.MalformedURLException;

public class CancelApplicationPageSteps extends BasePage implements En {
    public CancelApplicationPageSteps(OperatorStore operator, World world) {
        And("^I am on the cancel application page for Annual Multilateral$", () -> {
            AnnualMultilateralJourney.INSTANCE
                    .signin(operator, world);

            operator.getLicences().forEach((licence) -> {
                try {
                    AnnualMultilateralJourney.INSTANCE
                            .beginApplication()
                            .permitType(PermitTypePage.PermitType.AnnualMultilateral, operator)
                            .licencePage(operator, world)
                            .overviewPage(OverviewPage.Section.NumberOfPaymentsRequired, operator)
                            .numberOfPermitsPage(operator);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IllegalBrowserException e) {
                    e.printStackTrace();
                }

                NumberOfPermitsPage.overview();
                OverviewPage.cancel();
                CancelApplicationPage.untilOnPage();
            });

        });
        Then("^the annual multilateral Cancel Application page has a reference number$", () -> {
            String expectedReference = operator.getCurrentLicence().get().getReferenceNumber();
            Assert.assertEquals(expectedReference, CancelApplicationPage.reference());
        });
        Then("^the annual multilateral Cancel Application page has expected text$", () -> {
            String expectedAdvisory = "By cancelling you understand that:" +
                    "this application will be cancelled permanently" +
                    "the information you have entered will be deleted";

            Assert.assertEquals("Cancel application", CancelApplicationPage.title());
            Assert.assertEquals(expectedAdvisory, CancelApplicationPage.advisoryText());
            Assert.assertEquals("I confirm that I would like to cancel my application.", CancelApplicationPage.checkboxLabel());
        });
        Then("^the annual multilateral Cancel Application confirmation checkbox is unselected by default$", () -> {
            String message = "Expected checkbox to be unselected but it was selected";
            Assert.assertFalse(message, CancelApplicationPage.confirmationCheckboxStatus());
        });
        When("^I cancel my Annual Multilateral application without confirming$", CancelApplicationPage::cancel);
        Then("^I should get the expected error message for annual multilateral Cancel Application page$", () -> {
            Assert.assertEquals("You must select the checkbox to continue", CancelApplicationPage.errorMessage());
        });
        When("^I confirm and cancel my annual multilateral permit$", () -> {
            CancelApplicationPage.confirm(true);
            CancelApplicationPage.cancel();
        });
        Then("^I am taken to the application cancelled page$", CancelConfirmationPage::untilOnPage);
        And("^there are no fees for the permit$", () -> {
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());
            Assert.assertFalse(HomePage.FeesTab.hasOutstandingFees());
        });
    }
}