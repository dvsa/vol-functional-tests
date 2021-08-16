package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.system.Properties;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
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
import java.util.Objects;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TmVerifyDifferentOperator extends BasePage implements En {

    public TmVerifyDifferentOperator(World world) {
        Then("^the 'Awaiting operator review' post signature page is displayed showing the correct information$", () -> {
            String name = world.registerUser.getForeName() + " " + world.registerUser.getFamilyName();
            assertTrue(isTextPresent(name));
            assertTrue(isTextPresent("What happens next"));
            assertTrue(isTextPresent("Awaiting operator review"));
            assertTrue(isTextPresent("Declaration signed through GOV.UK Verify"));
            assertTrue(isTextPresent("You've submitted your details to the operator. We'll let you know once they've been reviewed."));
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
            assertTrue(isTextPresent(data));
            assertTrue(isTextPresent(world.registerUser.getForeName() + " " + world.registerUser.getFamilyName()));
            assertTrue(isTextPresent("How would you like to sign the declaration?"));
            assertTrue(isTextPresent("Sign your declaration online"));
            assertTrue(isTextPresent("Print, sign and return"));
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
            assertTrue(isTextPresent(data));
            assertTrue(isTextPresent("I agree - continue"));
        });
        Then("^the declaration text and verify button are not displayed$", () -> {
            Path fileToRead = Paths.get(getClass().getClassLoader()
                    .getResource("operator-GB-declaration.txt").toURI());
            String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
            assertTrue(isTextPresent("I agree - continue"));
            assertFalse(isTextPresent(data));
        });
        When("^i add a new transport manager$", () -> {
            world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
            world.UIJourney.changeLicenceForVariation();
            world.TMJourney.generateOperatorValues();
            world.TMJourney.addNewPersonAsTransportManager("variation");
        });
        Then("^a transport manager has been created banner is displayed$", () -> {
            findElement("//p[@role]",SelectorType.XPATH,10).getText().contains("The transport manager's user account has been created and a link sent to them");
        });
        Then("^the download TM(\\d+) for should not be displayed on the details page$", (Integer arg0) -> {
            waitAndClick(String.format("//a[contains(text(),'%s %s')]", world.TMJourney.getOperatorForeName(), world.TMJourney.getOperatorFamilyName()), SelectorType.XPATH);
            waitForTitleToBePresent("Details not submitted");
            assertFalse(isTextPresent("Alternatively they can download a TM1 form (PDF 150KB)."));
            assertFalse(isLinkPresent("download a TM1 form (PDF 150KB).", 30));
        });
        And("^the user chooses to print and sign$", () -> {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        });
        And("^I am the operator and not the transport manager$", () -> {
            world.createApplication.setIsOwner("N");
        });
        And("^i add an existing person as a transport manager who is not the operator on \"([^\"]*)\"$", (String applicationType) -> {
            boolean applicationOrNot = applicationType.equals("application");
            world.TMJourney.generateAndAddOperatorUser();
            world.TMJourney.addAndCompleteOperatorUserAsTransportManager("N", applicationOrNot);
        });
        And("^the operator countersigns digitally$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Sign out");
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            if(Browser.navigate().findElements(By.partialLinkText(world.createApplication.getApplicationId())).size()!=0) {
                world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            } else if (Browser.navigate().findElements(By.partialLinkText(world.updateLicence.getVariationApplicationId())).size()!=0) {
                world.selfServeNavigation.navigateToPage("variation", "Transport Managers");
            }
            clickByLinkText(world.TMJourney.getOperatorForeName() + " " + world.TMJourney.getOperatorFamilyName());
            click("form-actions[submit]", SelectorType.ID);
            world.UIJourney.signDeclaration();
            world.UIJourney.signWithVerify();
        });
        Then("^the 'Review and declarations' post signature page is displayed$", () -> {
            waitForTextToBePresent("Review and declarations");
            Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            Assert.assertTrue(isTextPresent("Review and declarations"));
            if (Integer.parseInt(getCurrentDate("dd/MMM/yyyy").split("/")[0])<10) {
                Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy"))));
            } else if (Integer.parseInt(getCurrentDate("dd/MMM/yyyy").split("/")[0])>=10){
                Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("dd MMM yyyy"))));
            }
        });
        When("^i add an operator as a transport manager$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.TMJourney.addOperatorAdminAsTransportManager();
        });
        And("^i sign the declaration$", () -> {
            world.UIJourney.signDeclaration();
        });
        And("^the operator countersigns by print and sign$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Sign out");
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            clickByLinkText(String.format("%s %s", world.TMJourney.getOperatorForeName(), world.TMJourney.getOperatorFamilyName()));
            click("form-actions[submit]", SelectorType.ID);
            click("//*[contains(text(),'Print')]",SelectorType.XPATH);
            click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
        });
        When("^create a user and add them as a tm with a future DOB$", () -> {
            world.TMJourney.generateAndAddOperatorUser();
            HashMap<String, String> dob = world.globalMethods.date.getDateHashMap(1, 0, 0);
            world.TMJourney.addOperatorUserAsTransportManager(dob, true);
        });
        Then("^two TM DOB errors should display$", () -> {
            assertTrue(isElementPresent("//*[@class='validation-summary']//a[contains(text(),'This date is not allowed to be in the future')]", SelectorType.XPATH));
            assertTrue(isElementPresent("//*[@class='validation-wrapper']//p[contains(text(),'This date is not allowed to be in the future')]", SelectorType.XPATH));
        });
        When("^i add an operator as a transport manager with a future DOB$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.TMJourney.nominateOperatorUserAsTransportManager(String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()),true);
            HashMap<String, String> dob = world.globalMethods.date.getDateHashMap(1, 0, 0);
            replaceDateFieldsByPartialId("dob", dob);
            click("form-actions[submit]", SelectorType.ID);
            waitForPageLoad();
        });
        When("^i add an operator as a transport manager with a no hours worked$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.TMJourney.nominateOperatorUserAsTransportManager(String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()),true);
            click("form-actions[submit]", SelectorType.ID);
            waitForPageLoad();
        });
        Then("^two worked hours errors should display$", () -> {
            assertTrue(isElementPresent("//*[@class='validation-summary']//a[contains(text(),'You must enter the hours per week you will spend on your duties')]", SelectorType.XPATH));
            assertTrue(isElementPresent("//*[@class='validation-wrapper']//p[contains(text(),'You must enter the hours per week you will spend on your duties')]", SelectorType.XPATH));
        });
        When("^i add new person as a transport manager and they fill out their details$", () -> {
            world.TMJourney.generateOperatorValues();
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.TMJourney.addNewPersonAsTransportManager("application");
            world.selfServeNavigation.navigateToLogin(world.TMJourney.getOperatorUser(), world.TMJourney.getOperatorUserEmail());
            clickByLinkText("Provide details");
            world.TMJourney.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
        });
        And("^the operator rejects the transport managers details$", () -> {
            waitForTextToBePresent("What happens next?");
            clickByLinkText("Home");
            world.TMJourney.assertTMDetailsWithOperator();
            clickByLinkText("Sign out");
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            clickByLinkText(String.format("%s %s", world.TMJourney.getOperatorForeName(), world.TMJourney.getOperatorFamilyName()));
            click("//span[@class='govuk-details__summary-text']", SelectorType.XPATH);
            waitForElementToBePresent("//*[@id='emailAddress']");
            click("submit", SelectorType.ID);
            waitForTextToBePresent("The link has been e-mailed");
        });
        And("^the TM has got the reset link email$", () -> {
//            Needs writing. Since maildev is soon to be implemented, makes no sense to add s3 method yet.
        });
        And("^the TM should see the incomplete label and provide details link$", () -> {
            world.selfServeNavigation.navigateToLogin(world.TMJourney.getOperatorUser(), world.TMJourney.getOperatorUserEmail());
            world.TMJourney.assertTMDetailsIncomplete();
        });
    }

    private Path getPath(World world) throws URISyntaxException {
        String declarationText;
        if (world.createApplication.getNiFlag().equals("N")) {
            declarationText = "operator-GB-declaration.txt";
        } else {
            declarationText = "operator-NI-declaration.txt";
        }
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(declarationText)).toURI());
    }
}