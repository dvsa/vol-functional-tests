package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class DocumentsJourney extends BasePage {

    World world;

    public DocumentsJourney(World world) {
        this.world = world;
    }
    public void noteDocId() {
        clickByLinkText("Docs & attachments");
        waitForPageLoad();
        String docId = getAttribute("//a[@class='govuk-link']", SelectorType.XPATH, "govuk-link" );

    }
}
