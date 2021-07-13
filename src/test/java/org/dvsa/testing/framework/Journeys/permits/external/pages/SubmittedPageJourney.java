package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmittedPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        assertEquals("Application submitted", SubmittedPage.getPanelHeading());
    }

    public static void hasSubHeading() {
        assertEquals("What happens next", SubmittedPage.getSubHeading());
    }

    public static void hasShortTermECMTAdvisoryText() {
        assertTrue(isTextPresent("You will receive an email to confirm that we have received your application."));
        assertTrue(isTextPresent("Your permit application is under consideration and will now be processed. We may contact you to verify your application. We will let you know the result of your application within 10 working days."));
        assertTrue(isTextPresent("You can check your application status on the next page."));
    }
}
