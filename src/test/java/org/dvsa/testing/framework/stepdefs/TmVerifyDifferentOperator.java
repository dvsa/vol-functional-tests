package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.dates.Dates;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import activesupport.string.Str;
import activesupport.system.Properties;
import com.amazonaws.services.s3.model.S3Object;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.Color;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Scanner;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TmVerifyDifferentOperator extends BasePage implements En {
    private String forename = "Transport";
    private String familyName = "Manager";

    public TmVerifyDifferentOperator(World world) {
        Then("^the 'Awaiting operator review' post signature page is displayed showing the correct information$", () -> {
            String name = world.createLicence.getForeName() + " " + world.createLicence.getFamilyName();
            assertTrue(isTextPresent(name,30));
            assertTrue(isTextPresent("What happens next",30));
            assertTrue(isTextPresent("Awaiting operator review",30));
            assertTrue(isTextPresent("Declaration signed through GOV.UK Verify",30));
            assertTrue(isTextPresent("You've submitted your details to the operator. We'll let you know once they've been reviewed.",30));
            assertTrue(isElementPresent("//button[@class='govuk-button']", SelectorType.XPATH));
        });
        And("^the confirmation panel is displaying the correct assets$", () -> {
            Assert.assertEquals("#fff", Color.fromString(world.genericUtils.confirmationPanel("//div[@class='govuk-panel govuk-panel--confirmation']", "color")).asHex());
            Assert.assertEquals("#28a197", Color.fromString(world.genericUtils.confirmationPanel("//div[@class='govuk-panel govuk-panel--confirmation']", "background-color")).asHex());
        });
        When("^the user has been redirected to the awaiting confirmation page$", () -> {
            EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
            String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();
            String url = Browser.navigate().getCurrentUrl();
            assertEquals(myURL, url);
        });

        Then("^the correct information is displayed on the declaration page$", () -> {
            Path fileToRead = getPath(world);
            String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
            assertTrue(isTextPresent(data,30));
            assertTrue(isTextPresent(world.createLicence.getForeName() + " " + world.createLicence.getFamilyName(),30));
            assertTrue(isTextPresent("How would you like to sign the declaration?",30));
            assertTrue(isTextPresent("Sign your declaration online",30));
            assertTrue(isTextPresent("Print, sign and return",30));
        });
        When("^the user confirms details on the TM 'Review and submit' page$", () -> {
            Path fileToRead = getPath(world);
            String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
            String dataOnPage = Browser.navigate().findElement(By.xpath("//*[@id='conditional-tm-verfy-declaration-1']/p[2]")).getText();
            assertEquals(data, dataOnPage);
        });
        And("^the users chooses to sign with verify$", () -> {
            click("//*[@id=\"content[isDigitallySigned]\"]", SelectorType.XPATH);
        });
        Then("^the declaration text and verify button are displayed$", () -> {
            Path fileToRead = Paths.get(getClass().getClassLoader()
                    .getResource("verify-declaration.txt").toURI());
            String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
            assertTrue(isTextPresent(data,30));
            assertTrue(isTextPresent("I agree - continue",30));
        });
        Then("^the declaration text and verify button are not displayed$", () -> {
            Path fileToRead = Paths.get(getClass().getClassLoader()
                    .getResource("operator-GB-declaration.txt").toURI());
            String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
            assertTrue(isTextPresent("I agree - continue",30));
            assertFalse(isTextPresent(data,30));
        });
        When("^i add a new transport manager$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
            world.UIJourneySteps.changeLicenceForVariation();
            String emailAddress = "tme".concat(Str.randomWord(2)).concat("externalTM@vol.gov");
            world.TMJourneySteps.addNewPersonAsTransportManager(forename, familyName, emailAddress);
        });
        Then("^a transport manager has been created banner is displayed$", () -> {
            findElement("//p[@role]",SelectorType.XPATH,10).getText().contains("The transport manager's user account has been created and a link sent to them");
        });
        Then("^the download TM(\\d+) for should not be displayed on the details page$", (Integer arg0) -> {
            waitAndClick("//a[contains(text(),'" + forename + " " + familyName + "')]", SelectorType.XPATH);
            waitForTextToBePresent("Details not submitted");
            assertFalse(isTextPresent("Alternatively they can download a TM1 form (PDF 150KB).",30));
            assertFalse(isLinkPresent("download a TM1 form (PDF 150KB).", 30));
        });
        And("^the user chooses to print and sign$", () -> {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        });
        And("^I am the operator and not the transport manager$", () -> {
            world.createLicence.setIsOwner("N");
        });
        And("^i add an existing person as a transport manager who is not the operator on \"([^\"]*)\"$", (String applicationType) -> {
            boolean applicationOrNot = applicationType.equals("application");
            world.TMJourneySteps.generateAndOperatorUser();
            world.TMJourneySteps.addAndCompleteOperatorUserAsTransportManager("N", applicationOrNot);
        });
        And("^the operator countersigns digitally$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Sign out");
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            if(Browser.navigate().findElements(By.partialLinkText(world.createLicence.getApplicationNumber())).size()!=0) {
                world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            } else if (Browser.navigate().findElements(By.partialLinkText(world.updateLicence.getVariationApplicationNumber())).size()!=0) {
                world.selfServeNavigation.navigateToPage("variation", "Transport Managers");
            }
            clickByLinkText(world.TMJourneySteps.getOperatorForeName() + " " + world.TMJourneySteps.getOperatorFamilyName());
            click("form-actions[submit]", SelectorType.ID);
            world.UIJourneySteps.signDeclaration();
            world.UIJourneySteps.signWithVerify();
        });
        Then("^the 'Review and declarations' post signature page is displayed$", () -> {
            waitForTextToBePresent("Review and declarations");
            Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            Assert.assertTrue(isTextPresent("Review and declarations",30));
            if (Integer.parseInt(getCurrentDate("dd/MMM/yyyy").split("/")[0])<10) {
                Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy")),30));
            } else if (Integer.parseInt(getCurrentDate("dd/MMM/yyyy").split("/")[0])>=10){
                Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("dd MMM yyyy")),30));
            }
        });
        When("^i add an operator as a transport manager$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.TMJourneySteps.addOperatorAdminAsTransportManager();
        });
        And("^i sign the declaration$", () -> {
            world.UIJourneySteps.signDeclaration();
        });
        And("^the operator countersigns by print and sign$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Sign out");
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            clickByLinkText(String.format("%s %s", world.TMJourneySteps.getOperatorForeName(), world.TMJourneySteps.getOperatorFamilyName()));
            click("form-actions[submit]", SelectorType.ID);
            click("//*[contains(text(),'Print')]",SelectorType.XPATH);
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        });
        When("^create a user and add them as a tm with a future DOB$", () -> {
            world.TMJourneySteps.generateAndOperatorUser();
            HashMap<String, Integer> dob = world.globalMethods.date.getDate(1, 0, 0);
            world.TMJourneySteps.addOperatorUserAsTransportManager(dob, true);
        });
        Then("^two TM DOB errors should display$", () -> {
            assertTrue(isElementPresent("//*[@class='validation-summary']//a[contains(text(),'This date is not allowed to be in the future')]", SelectorType.XPATH));
            assertTrue(isElementPresent("//*[@class='validation-wrapper']//p[contains(text(),'This date is not allowed to be in the future')]", SelectorType.XPATH));
        });
        When("^i add an operator as a transport manager with a future DOB$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.TMJourneySteps.nominateOperatorUserAsTransportManager(String.format("%s %s", world.createLicence.getForeName(), world.createLicence.getFamilyName()),true);
            HashMap<String, Integer> dob = world.globalMethods.date.getDate(1, 0, 0);
            replaceDateById("dob", dob);
            click("form-actions[submit]", SelectorType.ID);
            waitForPageLoad();
        });
        When("^i add an operator as a transport manager with a no hours worked$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.TMJourneySteps.nominateOperatorUserAsTransportManager(String.format("%s %s", world.createLicence.getForeName(), world.createLicence.getFamilyName()),true);
            click("form-actions[submit]", SelectorType.ID);
            waitForPageLoad();
        });
        Then("^two worked hours errors should display$", () -> {
            assertTrue(isElementPresent("//*[@class='validation-summary']//a[contains(text(),'You must enter the hours per week you will spend on your duties')]", SelectorType.XPATH));
            assertTrue(isElementPresent("//*[@class='validation-wrapper']//p[contains(text(),'You must enter the hours per week you will spend on your duties')]", SelectorType.XPATH));
        });
        When("^i add new person as a transport manager$", () -> {
            world.TMJourneySteps.generateOperatorValues();
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.TMJourneySteps.nominateNewPersonAsTransportManager();
            world.selfServeNavigation.navigateToLogin(world.TMJourneySteps.getOperatorUser(), world.TMJourneySteps.getOperatorUserEmail());
            clickByLinkText("Provide details");
            world.TMJourneySteps.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
        });
        And("^the operator rejects the transport managers details$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Home");
            world.TMJourneySteps.assertTMDetailsWithOperator();
            clickByLinkText("Sign out");
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            clickByLinkText(String.format("%s %s", world.TMJourneySteps.getOperatorForeName(), world.TMJourneySteps.getOperatorFamilyName()));
            click("//span[@class='govuk-details__summary-text']", SelectorType.XPATH);
            waitForElementToBePresent("//*[@id='emailAddress']");
            click("submit", SelectorType.ID);
            waitForTextToBePresent("The link has been e-mailed");
        });
        And("^the TM has got the reset link email$", () -> {
//            Needs writing. Since maildev is soon to be implemented, makes no sense to add s3 method yet.
        });
        And("^the TM should see the incomplete label and provide details link$", () -> {
            world.selfServeNavigation.navigateToLogin(world.TMJourneySteps.getOperatorUser(), world.TMJourneySteps.getOperatorUserEmail());
            world.TMJourneySteps.assertTMDetailsIncomplete();
        });
    }

    private Path getPath(World world) throws URISyntaxException {
        String declarationText;
        if (world.createLicence.getNiFlag().equals("N")) {
            declarationText = "operator-GB-declaration.txt";
        } else {
            declarationText = "operator-NI-declaration.txt";
        }
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(declarationText)).toURI());
    }
}