package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationConfirmationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

import static org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage.clickCancelCheckbox;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AnnualBilateralCancelPageSteps extends BasePage {

    World world;
    public AnnualBilateralCancelPageSteps(World world){
        this.world = world;
    }
    @When("the checkbox is selected")
    public void theCheckboxIsSelected() {
        clickCancelCheckbox();
    }

    @Then("I should be taken to cancel confirmation page")
    public void iShouldBeTakenToCancelConfirmation() {
        CancellationConfirmationPage.untilOnPage();
        assertEquals("Application cancelled", CancellationConfirmationPage.getPanelHeading());
        assertEquals("What happens now", CancellationConfirmationPage.getAdvisoryHeadingPresent());
        assertEquals("You have cancelled your application and you will no longer be able to view or access it.", CancellationConfirmationPage.getAdvisoryTextPresent());
    }

    @And("I select cancel application button")
    public void iSelectCancelApplicationButton() {
        CancellationPage.clickCancelButton();
    }

    //Guidance link no more displayed on the page,changed the assertion
    @Then("I select finish button")
    public void iSelectTheFinishButton() {
        SubmittedPage.goToPermitsDashboard();
    }
}