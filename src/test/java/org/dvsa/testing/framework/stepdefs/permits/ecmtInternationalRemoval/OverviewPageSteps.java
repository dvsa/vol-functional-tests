package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;


import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class OverviewPageSteps extends BasePage {
    private final World world;

    public OverviewPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the ECMT International Removal overview page")
    public void iAmOnTheECMTInternationalRemovalPage() {
        world.ecmtInternationalRemovalJourney.beginApplication();
    }

    @And("I click cancel application link on the International removal overview page")
    public void iClickCancelApplicationLinkOnTheInternational() {
        world.basePermitJourney.setFullReferenceNumber(BasePermitPage.getReferenceFromPage());
        OverviewPage.clickCancelApplication();
    }

    @And("the licence number is displayed correctly")
    public void theLicenceNumberIsDisplayedCorrectly() {
        String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
        assertTrue(actualReferenceNumber.contains(world.applicationDetails.getLicenceNumber()));
    }

    @And("future sections beyond the current step are disabled")
    public void futureSectionsBeyondTheCurrentStepAreDisabled() {
        assertFalse(OverviewPage.isActiveLinkPresent(OverviewSection.Cabotage));
    }
}