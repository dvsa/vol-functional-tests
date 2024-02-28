package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.commons.lang3.StringUtils;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import autoitx4java.AutoItX;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import static activesupport.autoITX.AutoITX.initiateAutoItX;
import static activesupport.file.Files.checkFileContainsText;
import static activesupport.file.Files.getDownloadedFile;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebDav extends BasePage{
    private final World world;
    Initialisation initialisation;
    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    private static final String templateName = "BUS_REG_CANCELLATION";
    AutoItX autoIt;

    public WebDav(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }


    @When("i update my operating system on internal to {string}")
    public void iUpdateMyOperatingSystemOnInternalTo(String operatingSystem) {
        world.internalNavigation.getEditUserAccount(world.updateLicence.getInternalUserId());
        waitForTextToBePresent("User type");
        selectValueFromDropDown("//*[@id='osType']", SelectorType.XPATH, operatingSystem);
        UniversalActions.clickSubmit();
    }

    @And("i open the document in word for the first time")
    public void iOpenTheDocumentInWordForTheFirstTime() throws MalformedURLException, InterruptedException {
        String window = "Olcs - ".concat(world.applicationDetails.getLicenceNumber()).concat(" - Google Chrome");
        Thread.sleep(1000);
        clickByLinkText("BUS");
        this.autoIt = initiateAutoItX("jacob-1.16", "lib/jacob-1.16");
        this.autoIt.winWaitActive(window, "Chrome Legacy Window");
        Thread.sleep(1000);
        this.autoIt.mouseClick("left", 1200, 195, 2, 20);
        Thread.sleep(5000);
    }

    @Then("i should be prompted to login")
    public void iShouldBePromptedToLogin() {
        String wordLoginWindow = StringUtils.removeEnd(URL.build(ApplicationType.INTERNAL, env).toString(), "/");
        assertTrue(this.autoIt.winExists(wordLoginWindow, ""));
    }

    @And("i make changes to the document with WebDav and save it")
    public void iMakeChangesToTheDocumentWithWebDavAndSaveIt() throws IOException, InterruptedException {
        String licenceNumber = world.applicationDetails.getLicenceNumber();
        String documentLink = Browser.navigate().findElement(By.id("letter-link")).getText();

        world.internalUIJourney.editDocumentWithWebDav();

        String fileName = getText("//table//tbody//tr//td", SelectorType.XPATH);
        world.genericUtils.writeLineToFile(
                String.format("%s,%s,%s", licenceNumber, fileName, documentLink),
                String.format("%s/Reports/WebDavRequiredStorage/%sWebDav.csv", Paths.get("").toAbsolutePath(), env.toString()));
    }

    @Then("the document should contain the changes")
    public void theDocumentShouldContainTheChanges() throws IOException {
        assertTrue(isTextPresent(templateName));
        clickByLinkText(templateName);

        String templateRegex = String.format("(?:[\\d]){20}_%s_%s\\.rtf", world.applicationDetails.getLicenceNumber(), templateName);

        File file = getDownloadedFile("downloadDirectory", templateRegex);

        assertTrue(checkFileContainsText(file.getAbsolutePath(), "WebDav Change!"));
    }

    @And("upload a document")
    public void uploadADocument() {
        world.internalUIJourney.uploadDocument(String.format("%s/%s",System.getProperty("user.dir"), "src/test/resources/testBusTemplate.rtf"));
    }

    @Then("the operating system should be updated to {string}")
    public void theOperatingSystemShouldBeUpdatedTo(String operatingSystem) {
        world.internalNavigation.getEditUserAccount(world.updateLicence.getInternalUserId());
        waitForTextToBePresent("User type");
        selectValueFromDropDown("//*[@id='osType']", SelectorType.XPATH, operatingSystem);
        UniversalActions.clickSubmit();
    }
}
