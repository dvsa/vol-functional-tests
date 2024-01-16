package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.pages.WithdrawApplicationPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.ApplicationDetailsPage;
import org.dvsa.testing.framework.pageObjects.enums.ApplicationDetail;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationDetailsPageSteps {
    private final World world;

    public ApplicationDetailsPageSteps(World world) {
        this.world = world;
    }

    @And("I am viewing an application")
    public void iAmViewingOnApplication() {
        HomePage.PermitsTab.selectFirstOngoingApplication();
    }

    @Then("all the information should match that which was entered during the application process")
    public void allTheInformationShouldMatchThatWhichWasEntered() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date();
        String expectedDate = dateFormat.format(date);
        assertThat(ApplicationDetailsPage.details(ApplicationDetail.Status).toLowerCase(), is("under consideration"));
        assertEquals(ApplicationDetailsPage.details(ApplicationDetail.PermitType), "Annual ECMT");
        assertTrue(ApplicationDetailsPage.details(ApplicationDetail.ReferenceNumber).contains(world.applicationDetails.getLicenceNumber()));
        assertThat(ApplicationDetailsPage.details(ApplicationDetail.ApplicationDate), is(expectedDate));
    }

    @When("I select return to permits dashboard hyperlink")
    public void iSelectReturnToPermitsDashboard() {
        ApplicationDetailsPage.returnToPermitsDashboard();
    }

    @And("the advisory text on ECMT under consideration page is displayed correctly")
    public void theAdvisoryTextOnECMTUnderConsiderationPage() {
        assertEquals("We will contact you within 10 working days after the application submission date.", ApplicationDetailsPage.getAdvisoryText());
    }

    @And("I select the withdraw application button")
    public void iSelectTheWithdrawApplicationButton() {
        ApplicationDetailsPage.withdraw();
    }

    @And("I should be navigated to the withdraw application page")
    public void iShouldBeNavigatedToTheWithdraw() {
        WithdrawApplicationPageJourney.hasPageHeading();
    }
}