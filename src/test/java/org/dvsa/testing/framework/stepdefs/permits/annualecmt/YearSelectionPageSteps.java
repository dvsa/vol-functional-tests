package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YearSelectionPageSteps extends BasePage {
    private final World world;

    public YearSelectionPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the Year Selection Page")
    public void iAmOnTHeYearSelectionPage() {
        world.ecmtApplicationJourney.beginApplicationToYearSelectionPage();
    }

    @And("the user is navigated to the permit type page")
    public void theUserIsNavigatedToThePermitTypePage() {
        String pageHeading = PermitTypePage.getPageHeading();
        assertEquals("Select a permit type or certificate to apply for", pageHeading);
    }

    @And("the page heading on Annual Ecmt Year selection page is displayed correctly")
    public void thePageHeadingOnAnnualECMTYearSelectionPage() {
        if (world.yearSelectionPage.isYearChoicePresent()) {
            assertEquals(YearSelectionPage.getPageHeading(), "Select which year you want permits for");
        } else {
            assertEquals(YearSelectionPage.getPageHeading(), "Permits requested will be valid for 2021");
        }
    }

    @And("the validity error message is displayed")
    public void theValidityErrorMessageIsDisplayed() {
        BasePermitPage.saveAndContinue();
        String errorText = YearSelectionPage.getErrorText();
        assertEquals("You must select one year to continue", errorText);
    }

    @When("I confirm  the year selection")
    public void iConfirmTheYearSelection() {
        if (world.yearSelectionPage.isYearChoicePresent()) {
            world.yearSelectionPage.selectECMTValidityPeriod();
        } else {
            BasePermitPage.saveAndContinue();
        }
    }

    @When("the user is navigated to licence selection page")
    public void theUserIsNavigatedToLicenceSelectionPage() {
        isPath("/permits/type/\\d+/licence/");
    }
}