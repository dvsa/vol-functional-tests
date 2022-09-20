package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.ApplicationDetail;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;

public class ApplicationDetailsPage extends BaseDetailsPage {

    public static String details(ApplicationDetail detail) {
        String selector = String.format(
                "//dt[contains(text(), '%s')]/../dd//span | //dt[contains(text(), '%s')]/../dd",
                detail.toString(),
                detail.toString()
        );

        selector += (detail == ApplicationDetail.Status) ? "/strong" : "";

        return getText(selector, SelectorType.XPATH);
    }

    public static void withdraw() {
        scrollAndClick("//a[contains(text(), 'Withdraw application')]", SelectorType.XPATH);
    }

    public static void returnToPermitsDashboard() {
        scrollAndClick("//*[contains(text(), 'Return to permits dashboard')]", SelectorType.XPATH);
    }

    public static String getAdvisoryText() {
        return getText("//div[@class='govuk-inset-text']", SelectorType.XPATH);
    }

}
