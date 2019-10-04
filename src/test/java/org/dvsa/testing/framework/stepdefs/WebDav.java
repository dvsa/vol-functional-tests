package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.File;
import java.nio.file.Paths;

public class WebDav extends BasePage implements En {

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    private static final String templateName = "BUS_REG_CANCELLATION";

    public WebDav(World world) {
        And("^i make changes to the document with WebDav and save it$", () -> {
            String licenceNumber = world.createLicence.getLicenceNumber();
            String documentLink = Browser.getDriver().findElement(By.id("letter-link")).getText();

            world.UIJourneySteps.editDocumentWithWebDav();

            String fileName = getText("//table//tbody//tr//td", SelectorType.XPATH);
            world.genericUtils.writeLineToFile(
                    String.format("%s,%s,%s",licenceNumber, fileName, documentLink),
                    String.format("%s/Reports/WebDavRequiredStorage/%sWebDav.csv", Paths.get("").toAbsolutePath().toString(),env.toString())
            );
        });
        And("^the document should contain the changes$", () -> {

            Assert.assertTrue(isTextPresent(this.templateName,5));
            clickByLinkText(this.templateName);

            String templateRegex = String.format("(?:[\\d]){20}_%s_%s\\.rtf", world.createLicence.getLicenceNumber(), templateName);
            File file = null;
            do {
                file = GenericUtils.getDownloadedFile(templateRegex);
            }
            while (file == null);
            // We apologise sincerely, but we need a short delay to give the file a chance to fully download. Sorry.
            Thread.sleep(1000);

            Assert.assertTrue(GenericUtils.checkFileContainsText(file.getAbsolutePath(), "I would remind you that you must"));
        });
    }
}
