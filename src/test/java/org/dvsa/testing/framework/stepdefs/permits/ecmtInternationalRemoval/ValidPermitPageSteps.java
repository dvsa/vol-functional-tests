package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidECMTInternationalPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;

import java.util.List;
import java.util.stream.IntStream;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidPermitPageSteps {
    private final World world;

    public ValidPermitPageSteps(World world) {
        this.world = world;
    }

    @And("I have a valid ECMT removal permit")
    public void iHaveAValidECMTRemoval() {
        refreshPageWithJavascript();
        EcmtInternationalRemovalJourney.completeAndSubmitApplication(world);
        SubmittedPage.goToPermitsDashboard();
        CommonSteps.waitUntilPermitHasStatus(world);
    }

    @And("I am viewing my issued ECMT removal permit on selfserve")
    public void iAmViewingMyIssuedECMT() {
        HomePage.PermitsTab.selectFirstValidPermit();
        ValidPermitsPage.untilOnPage();
    }

    @Then("I am on the ECMT removal Permit list page")
    public void iAmOnTheECMTRemovalPermit() {
        ValidPermitsPage.untilOnPage();
    }
    @And("the table of ECMT removal permits is as expected")
    public void theTableOfECMTRemovalPermitsIsAsExpected() {
        String message = "Expected all permits to have a status of 'VALID'";
        List<ValidECMTInternationalPermit> permits = ValidPermitsPage.ECMTInternationalRemovalPermits();
        assertTrue(permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID),message);
        IntStream.range(0, permits.size() - 1).forEach((idx) -> assertTrue(
                permits.get(idx).getExpiryDate().isEqual(permits.get(idx).getStartDate().plusDays(365))));
    }
}