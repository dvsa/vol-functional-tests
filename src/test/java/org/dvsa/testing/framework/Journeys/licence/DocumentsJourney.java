package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocumentsJourney extends BasePage {

    World world;

    public DocumentsJourney(World world) {
        this.world = world;
    }

    public void noErrorOnDownload() {

        assertFalse(isTextPresent("We can't find that page"));
    }

    public void errorOnDownload() {

        assertTrue(isTextPresent("We can't find that page"));
    }
}
