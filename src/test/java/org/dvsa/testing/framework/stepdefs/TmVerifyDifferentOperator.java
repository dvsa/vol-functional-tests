package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.string.Str;
import activesupport.system.Properties;
import cucumber.api.PendingException;
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
import java.util.Objects;

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
            assertTrue(isTextPresent(name, 30));
            assertTrue(isTextPresent("What happens next", 30));
            assertTrue(isTextPresent("Awaiting operator review", 30));
            assertTrue(isTextPresent("Declaration signed through GOV.UK Verify", 30));
            assertTrue(isTextPresent("You've submitted your details to the operator. We'll let you know once they've been reviewed.", 30));
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
            assertTrue(isTextPresent(data, 10));
            assertTrue(isTextPresent(world.createLicence.getForeName() + " " + world.createLicence.getFamilyName(), 10));
            assertTrue(isTextPresent("How would you like to sign the declaration?", 10));
            assertTrue(isTextPresent("Sign your declaration online", 10));
            assertTrue(isTextPresent("Print, sign and return", 10));
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
            assertTrue(isTextPresent(data, 20));
            assertTrue(isTextPresent("I agree - continue", 20));
        });
        Then("^the declaration text and verify button are not displayed$", () -> {
            Path fileToRead = Paths.get(getClass().getClassLoader()
                    .getResource("operator-GB-declaration.txt").toURI());
            String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
            assertTrue(isTextPresent("I agree - continue", 20));
            assertFalse(isTextPresent(data, 20));
        });
        When("^i add a new transport manager$", () -> {
            clickByLinkText(world.createLicence.getLicenceNumber());
            clickByLinkText("Transport");
            waitForTextToBePresent("Transport Managers");
            if (isTextPresent("To add a transport manager", 10)) {
                clickByLinkText("change");
                waitForTextToBePresent("Applying to change a licence");
                click("form-actions[submit]", SelectorType.ID);
            }
            String emailAddress = "tme".concat(Str.randomWord(2)).concat("externalTM@vol.gov");
            world.UIJourneySteps.addNewPersonAsTransportManager(forename, familyName, emailAddress);
        });
        Then("^a transport manager has been created banner is displayed$", () -> {
            findElement("//p[@role]",SelectorType.XPATH,10).getText().contains("The transport manager's user account has been created and a link sent to them");
        });
        Then("^the download TM(\\d+) for should not be displayed on the details page$", (Integer arg0) -> {
            waitAndClick("//a[contains(text(),'" + forename + " " + familyName + "')]", SelectorType.XPATH);
            waitForTextToBePresent("Details not submitted");
            assertFalse(isTextPresent("Alternatively they can download a TM1 form (PDF 150KB).", 30));
            assertFalse(isLinkPresent("download a TM1 form (PDF 150KB).", 30));
        });
        And("^the user chooses to print and sign$", () -> {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        });
        And("^I am the operator and not the transport manager$", () -> {
            world.createLicence.setIsOwner("N");
        });
        And("^i add an existing person as a transport manager who is not the operator$", () -> {
            world.UIJourneySteps.addInternalAdmin();
            world.UIJourneySteps.addOperatorUserAsTransportManager(1, "N");
        });
        And("^the operator countersigns digitally$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Sign out");
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText(world.updateLicence.getVariationApplicationNumber());
            if(Browser.getDriver().getCurrentUrl().contains("application")) {
                waitForTextToBePresent("Apply for a new licence");
            } else if (Browser.getDriver().getCurrentUrl().contains("variation")) {
                waitForTextToBePresent("Apply to change a licence");
            }
            clickByLinkText("Transport");
            clickByLinkText(world.UIJourneySteps.getOperatorForeName() + " " + world.UIJourneySteps.getOperatorFamilyName());
            click("form-actions[submit]", SelectorType.ID);
            world.UIJourneySteps.signDeclaration();
            world.UIJourneySteps.signWithVerify("pavlov", "Password1");
        });
        Then("^the 'Review and declarations' post signature page is displayed$", () -> {
            waitForTextToBePresent("Review and declarations");
            Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            Assert.assertTrue(isTextPresent("Review and declarations", 10));
            Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("dd MMM yyyy")), 20));
        });
        When("^i add an operator as a transport manager$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.UIJourneySteps.addOperatorAdminAsTransportManager(1);
        });
        And("^i sign the declaration$", () -> {
            world.UIJourneySteps.signDeclaration();
        });
        And("^the operator countersigns by print and sign$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Sign out");
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getApplicationNumber());
            waitForTextToBePresent("Apply for a new licence");
            clickByLinkText("Transport");
            clickByLinkText(world.UIJourneySteps.getOperatorForeName() + " " + world.UIJourneySteps.getOperatorFamilyName());
            click("form-actions[submit]", SelectorType.ID);
            click("//*[contains(text(),'Print')]",SelectorType.XPATH);
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
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