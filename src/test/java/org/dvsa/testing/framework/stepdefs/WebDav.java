package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Paths;

public class WebDav extends BasePage implements En {

    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    public WebDav(World world) {
        And("^I download and check the change has been made to the document$", () -> {
            String fileLine = GenericUtils.readLastLineFromFile(String.format("%s/target/%sWebDav.txt", Paths.get("").toAbsolutePath().toString(), env.toString()));
            String[] fileArray = fileLine.split(", ");
            String licenceNo = fileArray[0];
            String fileName = fileArray[1];
            Assert.assertTrue(isTextPresent(fileName,5));
            clickByLinkText(fileName);

            File directory = new File("/Users/whitehousea/Downloads");
            File[] files = directory.listFiles((FileFilter) new RegexFileFilter(String.format("(?:[\\d]){20}_%s_BUS_REG_CANCELLATION\\.rtf", licenceNo)));

            Assert.assertTrue(GenericUtils.checkFileContainsText(files[0].getAbsolutePath(),"I would remind you that you must"));
        });
    }
}
