package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.WithdrawApplicationPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.permits.pages.WithdrawApplicationPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.ApplicationDetailsPage;
import org.junit.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.dvsa.testing.lib.pages.external.permit.ApplicationDetailsPage.ApplicationDetail.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class ApplicationDetailsPageSteps implements En {

    private World world;

    public ApplicationDetailsPageSteps(OperatorStore operatorStore, World world) {
        And("^I am viewing an application$", () -> {
            LicenceStore licence = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalStateException::new);
            HomePage.PermitsTab.selectOngoing(licence.getLicenceNumber());
        });
        Then("^all the information should match that which was entered during the application process$", () -> {
            LicenceStore licence = operatorStore.getLatestLicence().orElseThrow(IllegalStateException::new);
             DateFormat dateFormat= new SimpleDateFormat("dd MMMM yyyy");
             Date date = new Date();
             String expectedDate= dateFormat.format(date);
            Assert.assertThat(ApplicationDetailsPage.details(Status).toLowerCase(), is("under consideration"));
            Assert.assertThat(ApplicationDetailsPage.details(PermitType), is(licence.getEcmt().getType().get().toString()));
            Assert.assertThat(ApplicationDetailsPage.details(ReferenceNumber), is(licence.getReferenceNumber()));
            Assert.assertThat(ApplicationDetailsPage.details(ApplicationDate), is(expectedDate));
        });
        When("^I select return to permits dashboard hyperlink$", ApplicationDetailsPage::returnToPermitsDashboard);
        And("^the advisory text on ECMT under consideration page is displayed correctly$", ApplicationDetailsPage::advisoryText);
        And("^I select the withdraw application button$", ApplicationDetailsPage::withdraw);
        And("^I should be navigated to the withdraw application page$", WithdrawApplicationPageJourney::hasPageHeading);
    }

}
