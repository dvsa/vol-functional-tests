package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocumentsJourney extends BasePage {

    World world;

    public DocumentsJourney(World world) {
        this.world = world;
    }

    private String trimmedUrl;

    public void noteDocId() {
        String docId = getAttribute("//a[contains(@href, '/file/')]", SelectorType.XPATH, "href");
        this.trimmedUrl = docId.substring(docId.lastIndexOf("/file") + 1);
    }

    public void noErrorOnDownload() throws InterruptedException {
        String myURL = String.valueOf(URL.build(ApplicationType.EXTERNAL, world.configuration.env, trimmedUrl));
        DriverUtils.get(myURL);
        wait(10000);
        assertFalse(isTextPresent("We can't find that page"));
    }

    public void errorOnDownload() throws InterruptedException {
        String myURL = String.valueOf(URL.build(ApplicationType.EXTERNAL, world.configuration.env, trimmedUrl));
        DriverUtils.get(myURL);
        wait(10000);
        assertTrue(isTextPresent("We can't find that page"));
    }
}
