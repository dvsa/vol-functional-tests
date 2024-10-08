package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubmittedPageJourney extends BasePermitJourney {

    public SubmittedPageJourney(World world){
        super(world);
    }
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

    public static void goToPermitsDashboard() {
        SubmittedPage.untilOnPage();
        SubmittedPage.goToPermitsDashboard();
    }
}
