package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class DownloadPermissions extends BasePage {

    private final World world;

    public DownloadPermissions(World world) {this.world = world;}
    @And("i print a licence document")
    public void iPrintALicenceDocument() {
        world.internalUIJourney.printLicence();
    }

    @And("i note the document id")
    public void iNoteTheDocumentId() {
        world.documentsJourney.noteDocId();
    }
}
