package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;

import java.util.concurrent.TimeUnit;

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
        assertTrue(isTextPresent("Your permit application is under consideration and will now be processed. We'll let you know the result of your permit application in the next 10 working days."));
        assertTrue(isTextPresent("You can check your application status on the next page."));
        assertTrue(isTextPresent("In the meantime, check if you have the"));
        assertTrue(isElementPresent("//a [@href='https://www.gov.uk/guidance/ecmt-international-road-haulage-permits#rules-for-using-the-permits']", SelectorType.XPATH));
    }
}
