package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.enums.RestrictedCountry;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.ApplicationSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.RestrictedCountriesPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.hamcrest.core.StringContains;

import static org.dvsa.testing.framework.pageObjects.external.enums.sections.ApplicationSection.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckYourAnswersPageSteps extends BasePage{
    private final World world;
    String licence;
    String euro6;
    String cabotage;
    String restrictedCountries;


    public CheckYourAnswersPageSteps(World world) {
        this.world = world;
    }

    @And("I have completed all steps prior to check your answers page")
    public void iHaveCompletedAllStepsPriorToCheck() {
        EcmtApplicationJourney.completeUntilCheckYourAnswersPage();
    }

    @Then("the information I inserted during the application is displayed")
    public void theInformationIInsertedDuringTheApplication() {
        licence = CheckYourAnswerPage.getAnswer(Licence);
        euro6 = CheckYourAnswerPage.getAnswer(Euro6);
        cabotage = CheckYourAnswerPage.getAnswer(Cabotage);
        restrictedCountries = CheckYourAnswerPage.getAnswer(RestrictedCountries);
        assertThat(licence, StringContains.containsString(world.applicationDetails.getLicenceNumber()));
        assertEquals("I confirm that I will only use my ECMT permits with vehicles that meet the minimum euro emissions standards allowed.", euro6);
        assertEquals("I confirm that I will not undertake cabotage journeys using an ECMT permit.", cabotage);
        assertEquals("No", restrictedCountries);
    }

    @When("I change the {string}")
    public void iChangeThe(String section) {
        CheckYourAnswerPage.clickChangeAnswer(ApplicationSection.valueOf(section));
    }

    @When("I edit {string} and apply the changes")
    public void iEditAndApplyTheChanges(String section) {
        ApplicationSection sectionEnum = ApplicationSection.valueOf(section);

        CheckYourAnswerPage.clickChangeAnswer(sectionEnum);
        updateSectionWithValidRandomAnswer(sectionEnum);
    }

    private void updateSectionWithValidRandomAnswer(ApplicationSection section) {
        switch (section) {
            case Cabotage:
                CabotagePage.confirmWontUndertakeCabotage();
                CabotagePage.saveAndContinue();
                break;
            case RestrictedCountries:
                RestrictedCountry restrictedCountry = RestrictedCountry.random();
                RestrictedCountriesPage.countries(restrictedCountry);
                BasePermitPage.saveAndContinue();
                break;
        }
    }
}
