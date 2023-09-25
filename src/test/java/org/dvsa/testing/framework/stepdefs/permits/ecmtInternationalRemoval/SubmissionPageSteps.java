package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.SelfServeNavBar;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.stepdefs.vol.Initialisation;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionPageSteps extends BasePermitPage{
    World world;

    public SubmissionPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the ECMT International removal submission page")
    public void iAmOnTheECMTInternationalRemovalSubmissionPage() {
        world.ecmtInternationalRemovalJourney.completeAndSubmitApplication();
    }

    @Then("the page heading on the submission page is displayed correctly")
    public void thePageHeadingOnTheSubmissionPageIsDisplayedCorrectly() {
        world.basePermitJourney.assertHeadingPresentInSubmissionPanel();
    }

    @And("the application reference number is displayed correctly")
    public void theApplicationReferenceNumberIsDisplayedCorrectly() {
        world.basePermitJourney.assertReferenceNumberPresentInPanelBody();
    }

    @And("the texts on the submission page are displayed correctly")
    public void theTextsOnTheSubmissionPageAreDisplayedCorrectly() {
        String expectedHeading = SubmittedPage.getSubHeading();
        String expectedAdvisoryText1 = getText("//p[contains(text(),'We will now post your paper permit within the next')]", SelectorType.XPATH);
        String expectedAdvisoryText2 = getText("//p[contains(text(),'Your valid permits will be grouped together under')]", SelectorType.XPATH);
        String expectedWarningMessage = getText("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH);

        assertEquals("What happens next", expectedHeading);
        assertEquals("We will now post your paper permit within the next 3 working days.", expectedAdvisoryText1);
        assertEquals("Your valid permits will be grouped together under the same licence number that you applied with.", expectedAdvisoryText2);
        assertEquals("Warning" + "\n" + "Make sure your correspondence address is correct on all your operator licences and your email address is up-to-date on your account.", expectedWarningMessage);
    }

    @Then("the view receipt of ECMT International hyperlink opens in a new window")
    public void theViewReceiptOfECMTInternationalHyperlinkOpensOInANewWindow() {
        WebDriver driver = getDriver();
        String[] windows = driver.getWindowHandles().toArray(new String[0]);
        SubmittedPage.openReceipt();
        driver.switchTo().window(windows[0]);
    }

    @And("I have partial ECMT international removal application")
    public void iHavePartialECMTInternationalRemovalApplication() {
        world.ecmtInternationalRemovalJourney.completeUntilDeclarationPage();
    }

    @And("the application is under issued permits table with status as valid")
    public void theApplicationIsUnderIssuedPermitsTableWithStatusAsValid() {
        refreshPage();
        world.basePermitJourney.waitUntilPermitHasStatus();
    }

    @And("I navigate to permit dashboard page")
    public void iNavigateToPermitDashboardPage() {
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.HOME);
    }

    @And("I'm on the ECMT international submitted page for my active application")
    public void iMOnTheECMTInternationalSubmittedPageForMyActiveApplication() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        HomePageJourney.selectPermitTab();
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
        DeclarationPageJourney.completeDeclaration();
    }

    @And("I proceed with the application")
    public void iProceedWithTheApplication() {
        HomePageJourney.selectPermitTab();
        HomePage.PermitsTab.selectFirstOngoingApplication();
        OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
        DeclarationPageJourney.completeDeclaration();
    }

    @Given("I have a submission")
    public void iHaveASubmission() {
        world.internalNavigation.getLicence();
        world.submissionsJourney.createAndSubmitSubmission();
    }

    @And("I edit that submission")
    public void iEditThatSubmission() {
        clickByLinkText("Edit submission");
        selectRandomValueFromDropDown("fields[submissionSections][submissionType]", SelectorType.NAME);
        world.UIJourney.clickSubmit();
    }

    @Then("The change should be displayed on the Submission detail page")
    public void theChangeShouldBeDisplayedOnTheSubmissionDetailPage() {
        waitForElementToBePresent("//div[@class='read-only__header']//h3[1]");
        String submissionBanner = getText("//div[@class='read-only__header']//h3[1]", SelectorType.XPATH);
        assertTrue(isTextPresent(submissionBanner));
    }
}