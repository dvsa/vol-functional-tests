package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.RestrictedCountriesPageJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.RestrictedCountriesPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestrictedCountriesPageSteps extends BasePage {
    private final World world;

    public RestrictedCountriesPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the restricted countries page")
    public void iAmOnTheRestrictedCountriesPage() {
        EcmtApplicationJourney.completeUntilRestrictedCountriesPage(world);
    }

    @Given("I (do |don't )?plan on delivering to a restricted country")
    public void iPlanOnDeliveringToARestrictedCountry(String deliverToRestrictedCountries) {
        boolean deliverToRestricted = deliverToRestrictedCountries.equals("do ");
        RestrictedCountriesPage.deliverToRestrictedCountry(deliverToRestricted);
    }

    @Then("the Advisory text on Annual ECMT countries with limited countries page is Shown Correctly")
    public void theAdvisoryTextOnAnnualECMTCountries() {
        String advisoryText = RestrictedCountriesPage.getAdvisoryText();
        assertEquals("There is a very small number of permits available for these countries.\n" +
                "We cannot guarantee if you receive a permit that it will allow you to travel to these countries.\n" +
                "Annual ECMT Euro 5 permits are not valid for journeys to or through Austria.", advisoryText);
    }

    @Then("the page heading on Annual ECMT countries with limited countries page is Shown Correctly")
    public void thePageHeadingOnAnnualECMTCountries() {
        RestrictedCountriesPageJourney.hasPageHeading();
    }

    @Then("the application reference number is shown correctly")
    public void theApplicationReferenceNumberIsShownCorrectly() {
        String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
        assertThat(actualReferenceNumber, containsString(world.applicationDetails.getLicenceNumber()));
    }
}