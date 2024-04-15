package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
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

    @Then("i should be able to download the file")
    public void iShouldBeAbleToDownloadTheFile() throws InterruptedException {
        world.documentsJourney.noErrorOnDownload();
    }

    @Then("i should not be able to download the file")
    public void iShouldNotBeAbleToDownloadTheFile() throws InterruptedException {
        world.documentsJourney.errorOnDownload();
    }

    @And("i change the operator correspondence to Post")
    public void iChangeTheOperatorCorrespondenceToPost() {
        world.internalUIJourney.changeToPostOnOperatorProfile();
        world.internalUIJourney.navigateToLicenceFromOperatorProfile();
    }
}
