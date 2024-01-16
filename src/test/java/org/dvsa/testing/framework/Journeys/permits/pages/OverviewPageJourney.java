package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OverviewPageJourney extends BasePermitJourney {

    public OverviewPageJourney(World world){
        super(world);
    }
    public static void clickOverviewSection(OverviewSection section) {
        OverviewPage.untilOnPage();
        OverviewPage.clickOverviewSection(section);
    }

    public static void clickBilateralOverviewSection(OverviewSection section) {
        OverviewPage.untilOnPage();
        OverviewPage.clickBilateralOverviewSection(section);
    }

    public static void hasPageHeading() {
        String heading = OverviewPage.getPageHeading();
        assertEquals("Application overview", heading);
    }

    public static void checkStatus(OverviewSection section, PermitStatus status) {
        PermitStatus sectionStatus = OverviewPage.getStatusOfSection(section);
        assertTrue(sectionStatus.toString().trim().toLowerCase().contains(status.toString().trim().toLowerCase()));
    }

    public static void checkBilateralStatus(OverviewSection section, PermitStatus status) {
        PermitStatus sectionStatus = OverviewPage.getBilateralStatusOfSection(section);
        assertTrue(sectionStatus.toString().trim().toLowerCase().contains(status.toString().trim().toLowerCase()));
    }
}