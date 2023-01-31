package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval.SubmissionPageSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TurkeySubmissionPageSteps  {

    private final World world;

    public TurkeySubmissionPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the Annual Bilateral application submitted page with correct information and content")
    public void iAmOnTheAnnualBilateralApplicationSubmittedPageWithCorrectInformationAndContent() {
        SubmittedPage.untilOnPage();

        // the page heading on the submission page is displayed correctly
        SubmissionPageSteps.assertHeadingPresentInSubmissionPanel();

        // the application reference number is displayed correctly;
        SubmissionPageSteps.assertReferenceNumberPresentInPanelBody(world);

        // the texts on the submission page are displayed correctly
        assertTrue(SubmittedPage.isBilateralAdvisoryTextPresent());

        //the view receipt of Annual Bilateral  hyperlink opens in a new window
        SubmittedPage.openReceipt();
    }

    @And("I click 'go to permits' dashboard on the submitted page")
    public void iClickGoToPermitsDashboardOnTheSubmittedPage() {
        SubmittedPage.goToPermitsDashboard();
    }
}