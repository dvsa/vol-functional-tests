package org.dvsa.testing.framework.stepdefs;

import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

public class PublicationsRelatedSteps extends BasePage implements En {
    public PublicationsRelatedSteps() {
        And("^i navigate to the admin publications page$", () -> {
            click("//*[contains(text(),'Admin')]",SelectorType.XPATH);
            click("//*[@id='menu-admin-dashboard/admin-publication']",SelectorType.XPATH);
        });
        And("^i generate and publish all \"([^\"]*)\" publications$", (String cap) -> {
            for (int i=1; i<= Integer.parseInt(cap); i++) {

            }
        });
    }
}
