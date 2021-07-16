package org.dvsa.testing.framework.pageObjects.internal.details;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.ApplicationSection;

import java.time.temporal.ChronoUnit;

public class DocumentsPage extends BasePage {

    public static final String RESOURCE = "/file/\\d+";

    public static void untilOnPage() {
        untilUrlMatches(RESOURCE, Duration.LONG, ChronoUnit.SECONDS);
    }

    public static String getSectionBody(ApplicationSection section) {
        String selector = String.format("//dt[contains(text(), '%s')]/../dd", section.toString());
        return getText(selector, SelectorType.XPATH);
    }

    public static String getSubHeading() {
        return getText("h2").trim();
    }

    public static String getAddress() {
        return getText("//div[@class='printable__address']/p[last()]", SelectorType.XPATH);
    }
}
