package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps extends BasePermitPage {
    private final World world;

    public BilateralStandardAndCabotagePermitsCheckYourAnswersPageSteps(World world) {
        this.world = world;
    }

    @Then("I am on the Bilateral Standard and Cabotage permits check your answers page")
    public void iAmOnTheBilateralStandardAndCabotage() {
        world.annualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(PeriodType.BilateralsStandardAndCabotagePermits, Country.Norway, true);
    }

    @Then("Value of do you need to carry out cabotage, will always be as per the value selected on the cabotage pag")
    public void valueOfDoYouNeedToCarryOutCabotage() {
        int count = NumberOfPermitsPageJourney.getFieldCount();
        String value = getText("//*[@id='main-content']//dl/div[3]/dd[1]", SelectorType.XPATH);
        if (count == 1) {
            assertEquals("Yes\nI only need permits for cabotage", value);
        } else {
            assertEquals("Yes\nI need standard and cabotage permits", value);
        }
    }

    @Then("For Bilateral Standard and cabotage permits ,the Value of How many permits you need, will be as per the ones saved on the number of permits page")
    public void forBilateralStandardAndCabotagePermitsTheValueOfHowMany() {
        int count = NumberOfPermitsPageJourney.getFieldCount();
        String permitLabel = NumberOfPermitsPageJourney.getLabel();
        String permitStandardLabel = String.valueOf(NumberOfPermitsPage.getStandardLabel());
        String permitCabotageLabel = String.valueOf(NumberOfPermitsPage.getCabotageLabel());
        String permitCabotage = String.valueOf(NumberOfPermitsPageJourney.getCabotageValue());
        String permitStandard = String.valueOf(NumberOfPermitsPageJourney.getStandardValue());
        String permitValue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());

        if (count == 1) {
            assertEquals(BilateralJourneySteps.getPermitValueMultiple(), permitValue + " " + permitLabel + "s");
        } else {
            assertEquals(BilateralJourneySteps.getPermitValueMultiple(), permitStandard + " " + permitStandardLabel + "s" + '\n' + permitCabotage + " " + permitCabotageLabel + "s");
        }
    }
}