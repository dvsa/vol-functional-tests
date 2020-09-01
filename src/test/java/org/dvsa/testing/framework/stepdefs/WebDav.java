package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import autoitx4java.AutoItX;
import cucumber.api.java8.En;
import org.apache.commons.lang.StringUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.File;
import java.nio.file.Paths;

import static activesupport.autoITX.AutoITX.initiateAutoItX;
import static activesupport.file.Files.checkFileContainsText;
import static activesupport.file.Files.getDownloadedFile;

public class WebDav extends BasePage implements En {

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    private static final String templateName = "BUS_REG_CANCELLATION";
    AutoItX autoIt;

    public WebDav(World world) {
        And("^i make changes to the document with WebDav and save it$", () -> {
            String licenceNumber = world.createLicence.getLicenceNumber();
            String documentLink = Browser.navigate().findElement(By.id("letter-link")).getText();

            world.UIJourneySteps.editDocumentWithWebDav();

            String fileName = getText("//table//tbody//tr//td", SelectorType.XPATH);
            world.genericUtils.writeLineToFile(
                    String.format("%s,%s,%s", licenceNumber, fileName, documentLink),
                    String.format("%s/Reports/WebDavRequiredStorage/%sWebDav.csv", Paths.get("").toAbsolutePath().toString(), env.toString())
            );
        });
        And("^the document should contain the changes$", () -> {

            Assert.assertTrue(isTextPresent(templateName,30));
            clickByLinkText(templateName);

            String templateRegex = String.format("(?:[\\d]){20}_%s_%s\\.rtf", world.createLicence.getLicenceNumber(), templateName);

            File file = getDownloadedFile("downloadDirectory", templateRegex);

            Assert.assertTrue(checkFileContainsText(file.getAbsolutePath(), "WebDav Change!"));
        });
        And("^i open the document in word for the first time$", () -> {
            String window = "Olcs - ".concat(world.createLicence.getLicenceNumber()).concat(" - Google Chrome");
            Thread.sleep(1000);
            clickByLinkText("BUS");

            this.autoIt = initiateAutoItX("jacob-1.16", "lib/jacob-1.16");
            this.autoIt.winWaitActive(window, "Chrome Legacy Window");
            Thread.sleep(1000);
            this.autoIt.mouseClick("left", 1200, 195, 2, 20);
            Thread.sleep(5000);
        });
        Then("^i should be prompted to login$", () -> {
            String wordLoginWindow = StringUtils.removeEnd(URL.build(ApplicationType.INTERNAL, env).toString(), "/");
            Assert.assertTrue(this.autoIt.winExists(wordLoginWindow, ""));
        });
        When("^i update my operating system on internal to \"([^\"]*)\"$", (String operatingSystem) -> {
            world.internalNavigation.urlSearchAndViewInternalUserAccount(world.updateLicence.getAdminUserId());
            waitForTextToBePresent("Operating System");
            selectValueFromDropDown("//*[@id='osType']", SelectorType.XPATH, operatingSystem);
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        });
        Then("^the operating system should be updated to \"([^\"]*)\"$", (String operatingSystem) -> {
            world.internalNavigation.urlSearchAndViewInternalUserAccount(world.updateLicence.getAdminUserId());
            Assert.assertEquals(getText("//*[@id='osType']//*[@selected='selected']", SelectorType.XPATH), operatingSystem);
        });
        And("^upload a document$", () -> {
            world.UIJourneySteps.uploadDocument(String.format("%s/%s",System.getProperty("user.dir"),"src/test/resources/testBusTemplate.rtf"));
        });
    }
}
