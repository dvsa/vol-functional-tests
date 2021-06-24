package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OverviewPageJourney extends BasePermitJourney {

    public static void clickOverviewSection(OverviewSection section) {
        OverviewPage.untilOnPage();
        OverviewPage.clickOverviewSection(section);
    }

    public static void hasPageHeading() {
        String heading = OverviewPage.getPageHeading();
        assertEquals("Application overview", heading);
    }

    public static void checkStatus(OverviewSection section, PermitStatus status) {
        PermitStatus sectionStatus = OverviewPage.getStatusOfSection(section);
        assertTrue(sectionStatus.toString().trim().toLowerCase().contains(status.toString().trim().toLowerCase()));
    }

}
