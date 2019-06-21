package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;

public class PageChecks extends BasePage implements En {

    public PageChecks(World world) {

        Then("^the \"([^\"]*)\" document is produced automatically$", (String documentName) -> {
            clickByLinkText("Docs & attachments");
            checkForPartialMatch(documentName);
        });
    }
}
