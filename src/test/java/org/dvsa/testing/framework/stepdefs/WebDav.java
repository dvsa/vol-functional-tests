package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;

import java.io.File;

public class WebDav extends BasePage implements En {

    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    private String templateName = "BUS_REG_CANCELLATION";

    public WebDav(World world) {
        And("^I download and check the change has been made to the document$", () -> {

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
