package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationConfirmationPage;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

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
                OverviewPage.cancel();
                CancellationPage.untilOnPage();
            });

        });
        Then("^the annual multilateral Cancel Application page has a reference number$", () -> {
            String expectedReference = operator.getCurrentLicence().get().getReferenceNumber();
            Assert.assertEquals(expectedReference, BasePermitPage.getReferenceFromPage());
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
        When("^I cancel my Annual Multilateral application without confirming$", CancellationPage::clickCancelButton);
        Then("^I should get the expected error message for annual multilateral Cancel Application page$", () -> {
            Assert.assertEquals("You must select the checkbox to continue", CancelApplicationPage.errorMessage());
        });
        When("^I confirm and cancel my annual multilateral permit$", () -> {
            CancellationPage.clickCancelCheckbox();
            CancellationPage.clickCancelButton();
        });
        Then("^I am taken to the application cancelled page$", CancellationConfirmationPage::untilOnPage);
        And("^there are no fees for the permit$", () -> {
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());
            Assert.assertFalse(HomePage.FeesTab.hasOutstandingFees());
        });
    }
}