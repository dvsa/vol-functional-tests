package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TmVerifyDifferentOperator extends BasePage implements En {
    private final World world;

    public TmVerifyDifferentOperator(World world) {this.world=world;}

    @And("I am the operator and not the transport manager")
    public void iAmTheOperatorAndNotTheTransportManager() {
        world.createApplication.setIsOwner("N");
    }

    @When("i add an existing person as a transport manager who is not the operator on {string}")
    public void iAddAnExistingPersonAsATransportManagerWhoIsNotTheOperatorOn(String applicationType) {
        boolean applicationOrNot = applicationType.equals("application");
        world.TMJourney.generateAndAddOperatorUser();
        world.TMJourney.addAndCompleteOperatorUserAsTransportManager("N", applicationOrNot);
    }

    @And("i sign the declaration")
    public void iSignTheDeclaration() {
        world.UIJourney.signDeclaration();
    }

    @When("i add an operator as a transport manager")
    public void iAddAnOperatorAsATransportManager() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.TMJourney.nominateOperatorUserAsTransportManager(String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()),true);
        click("form-actions[submit]", SelectorType.ID);
        waitForPageLoad();
    }

    @Then("the {string} post signature page is displayed")
    public void theReviewAndDeclarationsPostSignaturePageIsDisplayed() {
        Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        Assert.assertTrue(isTextPresent("Review and declarations"));
        if (Integer.parseInt(getCurrentDate("dd/MMM/yyyy").split("/")[0])<10) {
            Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy"))));
        } else if (Integer.parseInt(getCurrentDate("dd/MMM/yyyy").split("/")[0])>=10){
            Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("dd MMM yyyy"))));
        }
    }

    @And("the operator countersigns digitally")
    public void theOperatorCountersignsDigitally() {
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
    }

    @And("the operator countersigns by print and sign")
    public void theOperatorCountersignsByPrintAndSign() {
        waitForTextToBePresent("What happens next?");
        clickByLinkText("Sign out");
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", "Transport Managers");
        clickByLinkText(String.format("%s %s", world.TMJourney.getOperatorForeName(), world.TMJourney.getOperatorFamilyName()));
        click("form-actions[submit]", SelectorType.ID);
        click("//*[contains(text(),'Print')]",SelectorType.XPATH);
        click("//*[@name='form-actions[submit]']", SelectorType.XPATH);
    }

    @When("i add new person as a transport manager and they fill out their details")
    public void iAddNewPersonAsATransportManagerAndTheyFillOutTheirDetails() {
        world.TMJourney.generateOperatorValues();
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.TMJourney.addNewPersonAsTransportManager("application");
        world.selfServeNavigation.navigateToLogin(world.TMJourney.getOperatorUser(), world.TMJourney.getOperatorUserEmail());
        clickByLinkText("Provide details");
        world.TMJourney.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
    }

    @When("the operator rejects the transport managers details")
    public void theOperatorRejectsTheTransportManagersDetails() {
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
    }

    @And("the TM has got the reset link email")
    public void theTMHasGotTheResetLinkEmail() {
        // Needs writing. Since maildev is soon to be implemented, makes no sense to add s3 method yet.
    }

    @And("the TM should see the incomplete label and provide details link")
    public void theTMShouldSeeTheIncompleteLabelAndProvideDetailsLink() {
        world.selfServeNavigation.navigateToLogin(world.TMJourney.getOperatorUser(), world.TMJourney.getOperatorUserEmail());
        world.TMJourney.assertTMDetailsIncomplete();
    }

    @When("create a user and add them as a tm with a future DOB")
    public void createAUserAndAddThemAsATmWithAFutureDOB() {
        world.TMJourney.generateAndAddOperatorUser();
        HashMap<String, String> dob = world.globalMethods.date.getDateHashMap(1, 0, 0);
        world.TMJourney.addOperatorUserAsTransportManager(dob, true);
    }

    @Then("two TM DOB errors should display")
    public void twoTMDOBErrorsShouldDisplay() {
        assertTrue(isElementPresent("//*[@class='validation-summary']//a[contains(text(),'This date is not allowed to be in the future')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//*[@class='validation-wrapper']//p[contains(text(),'This date is not allowed to be in the future')]", SelectorType.XPATH));
    }

    @When("i add an operator as a transport manager with a future DOB")
    public void iAddAnOperatorAsATransportManagerWithAFutureDOB() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.TMJourney.nominateOperatorUserAsTransportManager(String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()),true);
        HashMap<String, String> dob = world.globalMethods.date.getDateHashMap(1, 0, 0);
        replaceDateFieldsByPartialId("dob", dob);
        click("form-actions[submit]", SelectorType.ID);
        waitForPageLoad();
    }

    @When("i add an operator as a transport manager with a no hours worked")
    public void iAddAnOperatorAsATransportManagerWithANoHoursWorked() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.TMJourney.nominateOperatorUserAsTransportManager(String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()),true);
        click("form-actions[submit]", SelectorType.ID);
        waitForPageLoad();
    }

    @Then("two worked hours errors should display")
    public void twoWorkedHoursErrorsShouldDisplay() {
        assertTrue(isElementPresent("//*[@class='validation-summary']//a[contains(text(),'You must enter the hours per week you will spend on your duties')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//*[@class='validation-wrapper']//p[contains(text(),'You must enter the hours per week you will spend on your duties')]", SelectorType.XPATH));
    }

    @When("i add a new transport manager")
    public void iAddANewTransportManager() {
        world.selfServeNavigation.navigateToPage("licence", "Transport Managers");
        world.UIJourney.changeLicenceForVariation();
        world.TMJourney.generateOperatorValues();
        world.TMJourney.addNewPersonAsTransportManager("variation");
    }

    @Then("a transport manager has been created banner is displayed")
    public void aTransportManagerHasBeenCreatedBannerIsDisplayed() {
        findElement("//p[@role]",SelectorType.XPATH,10).getText().contains("The transport manager's user account has been created and a link sent to them");
    }

    @Then("the download TM{int} for should not be displayed on the details page")
    public void theDownloadTMForShouldNotBeDisplayedOnTheDetailsPage(int arg0) {
        waitAndClick(String.format("//a[contains(text(),'%s %s')]", world.TMJourney.getOperatorForeName(), world.TMJourney.getOperatorFamilyName()), SelectorType.XPATH);
        waitForTitleToBePresent("Details not submitted");
        assertFalse(isTextPresent("Alternatively they can download a TM1 form (PDF 150KB)."));
        assertFalse(isLinkPresent("download a TM1 form (PDF 150KB).", 30));
    }

    @Then("the correct information is displayed on the declaration page")
    public void theCorrectInformationIsDisplayedOnTheDeclarationPage() throws IOException, URISyntaxException {
        Path fileToRead = getPath(world);
        String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
        assertTrue(isTextPresent(data));
        assertTrue(isTextPresent(world.registerUser.getForeName() + " " + world.registerUser.getFamilyName()));
        assertTrue(isTextPresent("How would you like to sign the declaration?"));
        assertTrue(isTextPresent("Sign your declaration online"));
        assertTrue(isTextPresent("Print, sign and return"));
    }

    @And("the user chooses to print and sign")
    public void theUserChoosesToPrintAndSign() {
        click("//*[@id=\"content[isDigitallySigned]\"]", SelectorType.XPATH);
    }

    @Then("the declaration text and verify button are not displayed")
    public void theDeclarationTextAndVerifyButtonAreNotDisplayed() throws IOException, URISyntaxException {
        Path fileToRead = Paths.get(getClass().getClassLoader()
                .getResource("operator-GB-declaration.txt").toURI());
        String data = world.genericUtils.readFileAsString(String.valueOf(fileToRead));
        assertTrue(isTextPresent("I agree - continue"));
        assertFalse(isTextPresent(data));
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
