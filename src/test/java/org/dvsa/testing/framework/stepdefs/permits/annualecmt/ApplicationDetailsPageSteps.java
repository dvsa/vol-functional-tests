package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.pages.WithdrawApplicationPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.ApplicationDetailsPage;
import org.dvsa.testing.framework.pageObjects.enums.ApplicationDetail;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.junit.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class ApplicationDetailsPageSteps implements En {

    private World world;

    public ApplicationDetailsPageSteps(World world) {
        And("^I am viewing an application$", HomePage.PermitsTab::selectFirstOngoingApplication);
        Then("^all the information should match that which was entered during the application process$", () -> {
             DateFormat dateFormat= new SimpleDateFormat("dd MMMM yyyy");
             Date date = new Date();
             String expectedDate= dateFormat.format(date);
            Assert.assertThat(ApplicationDetailsPage.details(ApplicationDetail.Status).toLowerCase(), is("under consideration"));
            Assert.assertEquals(ApplicationDetailsPage.details(ApplicationDetail.PermitType),"Annual ECMT");
            Assert.assertTrue(ApplicationDetailsPage.details(ApplicationDetail.ReferenceNumber).contains(world.applicationDetails.getLicenceNumber()));
            Assert.assertThat(ApplicationDetailsPage.details(ApplicationDetail.ApplicationDate), is(expectedDate));
        });
        When("^I select return to permits dashboard hyperlink$", ApplicationDetailsPage::returnToPermitsDashboard);
        And("^the advisory text on ECMT under consideration page is displayed correctly$", () -> {
            assertEquals("We will contact you within 10 working days after the application submission date.", ApplicationDetailsPage.getAdvisoryText());
        });
        And("^I select the withdraw application button$", ApplicationDetailsPage::withdraw);
        And("^I should be navigated to the withdraw application page$", WithdrawApplicationPageJourney::hasPageHeading);
    }

}
