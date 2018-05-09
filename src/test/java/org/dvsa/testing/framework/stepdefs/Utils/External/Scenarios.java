package org.dvsa.testing.framework.stepdefs.Utils.External;

import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.S3;
import activesupport.string.Str;
import activesupport.system.Properties;

import org.dvsa.testing.framework.stepdefs.Utils.Internal.GenericUtils;
import org.dvsa.testing.framework.stepdefs.Utils.Internal.GrantApplicationAPI;
import org.dvsa.testing.lib.Environment;
import org.dvsa.testing.lib.Login;
import org.dvsa.testing.lib.URI;
import org.dvsa.testing.lib.browser.Browser;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.utils.ApplicationType;
import org.dvsa.testing.lib.utils.EnvironmentType;

import java.io.*;
import java.util.zip.ZipInputStream;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.stepdefs.Utils.Internal.GenericUtils.payPsvFees;
import static org.dvsa.testing.framework.stepdefs.Utils.Internal.GenericUtils.zipFolder;

public class Scenarios extends BasePage {

    private static String env = System.getProperty("env");
    private static String zipFilePath = "/src/test/resources/ESBR.zip";

    public InputStream stream() {
        File initialFile = new File(zipFilePath);
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(initialFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipInputStream;
    }

    public static void generateAndGrantPsvApplicationPerTrafficArea(CreateInterimPsvLicenceAPI psvApp, GrantApplicationAPI grantApp, String trafficArea) {
        psvApp.setTrafficArea(trafficArea);
        psvApp.createAndSubmitPsvApp();
        payPsvFees(grantApp, psvApp);
        grantApp.payGrantFees(psvApp.getOrganisationId(), psvApp.getApplicationNumber());
    }

    public static void uploadAndSubmitESBR(GenericUtils genericUtils, CreateInterimPsvLicenceAPI psvApp, String state, int interval) throws MissingRequiredArgument {
        // for the date state the options are ['current','past','future'] and depending on your choice the months you want to add/remove
        genericUtils.modifyXML(psvApp, state, interval);
        zipFolder();

        EnvironmentType env = Environment.enumType(Properties.get("env", true));
        String URL = URI.build(ApplicationType.EXTERNAL, env);

        if (Browser.isInitialised()) {
            //Quit Browser and open a new window
            Browser.quit();
        }
        Browser.go(URL);
        String password = S3.getTempPassword(psvApp.getEmailAddress());

        if (isTextPresent("Username", 60))
            Login.signIn(psvApp.getLoginId(), password);
        if (isTextPresent("Current password", 60)) {
            enterField(nameAttribute("input", "oldPassword"), password);
            enterField(nameAttribute("input", "newPassword"), "Password1");
            enterField(nameAttribute("input", "confirmPassword"), "Password1");
            click(nameAttribute("input", "submit"));
        }

        clickByLinkText("Bus");
        waitAndClick("//*[@id='main']/div[2]/ul/li[2]/a", SelectorType.XPATH);
        click(nameAttribute("button", "action"));
        String workingDir = System.getProperty("user.dir");
        uploadFile("//*[@id='fields[files][file]']", workingDir + zipFilePath, "document.getElementById('fields[files][file]').style.left = 0", SelectorType.XPATH);
        waitAndClick("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }

    public static void internalSiteAddBusNewReg(int day, int month, int year) {
        waitForTextToBePresent("Service details");
        assertTrue(isTextPresent("Service No. & type", 5));
        enterText("serviceNo", "123");
        enterText("startPoint", Str.randomWord(9));
        enterText("finishPoint", Str.randomWord(11));
        enterText("via", Str.randomWord(5));
        selectServiceType("//ul[@class='chosen-choices']", "//*[@id=\"busServiceTypes_chosen\"]/div/ul/li[1]", SelectorType.XPATH);
        enterDate(getCurrentDayOfMonth(), getCurrentMonth(), getCurrentYear());
        enterText("effectiveDate_day", String.valueOf(day));
        enterText("effectiveDate_month", String.valueOf(month));
        enterText("effectiveDate_year", String.valueOf(year));
        click(nameAttribute("button", "form-actions[submit]"));
    }

    public static void enterDate(int day, int month, int year) {
        enterText("receivedDate_day", String.valueOf(day));
        enterText("receivedDate_month", String.valueOf(month));
        enterText("receivedDate_year", String.valueOf(year));
    }
}