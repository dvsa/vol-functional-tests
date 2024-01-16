package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class PermitsDeclinedPage extends BasePermitPage {

    public static void untilOnPage() {
          untilUrlMatches("/permits/application/\\d+/decline/confirmation", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static boolean isAdvisoryTextPresent() {
        return isTextPresent("What happens now") &&
                        isTextPresent("You have declined your permits and you will no longer be able to view or access them.") &&
                        isTextPresent("You can still apply for ") &&
                        isTextPresent("International Road Haulage Permits");
    }

}