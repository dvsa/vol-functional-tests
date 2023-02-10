package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.aws.s3.SecretsManager;
import activesupport.driver.Browser;
import apiCalls.enums.OperatorType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.WebElement;
import scanner.AXEScanner;
import scanner.ReportGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SubmitSelfServeApplication extends BasePage {

    World world;
    Initialisation initialisation;
    static AXEScanner scanner = new AXEScanner();
    static ReportGenerator reportGenerator = new ReportGenerator();
    private static final Logger LOGGER = LogManager.getLogger(ManagerUsersPage.class);

    public static String selfServeUser;

    public SubmitSelfServeApplication(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @And("i submit and pay for a {string} licence application")
    public void iStartANewLicenceApplication(String licenceType){
       world.submitApplicationJourney.startANewLicenceApplication(licenceType);
       world.submitApplicationJourney.submitAndPayForApplication();
    }

    @Given("i have a self serve account")
    public void iHaveASelfServeAccount() {
      world.userRegistrationJourney.navigateAndLogIntoSelfServiceWithExistingUser();
    }

    @And("i have no existing applications")
    public void iHaveNoExistingApplications() {
        world.submitApplicationJourney.cancelAndWithdrawExistingApplications();
    }

    public static void accessibilityScanner() throws IllegalBrowserException, IOException {
        scanner.scan(false);
        if (scanner.getTotalViolationsCount() != 0) {
            LOGGER.info("ERROR: Violation found");
            reportGenerator.urlScannedReportSection(Browser.navigate().getCurrentUrl());
            reportGenerator.violationsReportSectionHTML(Browser.navigate().getCurrentUrl(), scanner);
        }
    }

    private void chooseLicenceType(String licenceType) {
        waitForTitleToBePresent("Type of licence");
        waitAndClick("//*[contains(text(),'Great Britain')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'" + licenceType + "')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Standard National')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Save')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Business type')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Limited Company')]", SelectorType.XPATH);
    }
}