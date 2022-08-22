package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.enums.RestrictedCountry;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.ApplicationSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.RestrictedCountriesPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.hamcrest.core.StringContains;
import org.junit.Assert;

import static org.dvsa.testing.framework.pageObjects.external.enums.sections.ApplicationSection.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckYourAnswersPageSteps extends BasePage implements En {
    
    String licence;
    String euro6;
    String cabotage;
    String restrictedCountries;
    

    public CheckYourAnswersPageSteps(World world) {
        And("^I have completed all steps prior to check your answers page$", EcmtApplicationJourney::completeUntilCheckYourAnswersPage);
        Then("^the information I inserted during the application is displayed$", () -> {
            licence = CheckYourAnswerPage.getAnswer(Licence);
            euro6 = CheckYourAnswerPage.getAnswer(Euro6);
            cabotage = CheckYourAnswerPage.getAnswer(Cabotage);
            restrictedCountries = CheckYourAnswerPage.getAnswer(RestrictedCountries);
            assertThat(licence, StringContains.containsString(world.applicationDetails.getLicenceNumber()));
            Assert.assertEquals("I confirm that I will only use my ECMT permits with vehicles that meet the minimum euro emissions standards allowed.",euro6);
            Assert.assertEquals("I confirm that I will not undertake cabotage journeys using an ECMT permit.",cabotage);
            Assert.assertEquals("No",restrictedCountries);
        });
        When("^I change the (.+)$", (String section) ->
            CheckYourAnswerPage.clickChangeAnswer(ApplicationSection.valueOf(section))
        );
        When("^I edit (.+) and apply the changes$", (String section) -> {
            ApplicationSection sectionEnum = ApplicationSection.valueOf(section);

            CheckYourAnswerPage.clickChangeAnswer(sectionEnum);
            updateSectionWithValidRandomAnswer(sectionEnum);
        });
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
