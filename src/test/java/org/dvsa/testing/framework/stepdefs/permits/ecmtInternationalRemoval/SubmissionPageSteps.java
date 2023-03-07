package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.SelfServeNavBar;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionPageSteps extends BasePermitPage{
    World world;
    private String username;
    private String userEmailAddress;
    private String applicationDetails;

    public SubmissionPageSteps(World world) {
        this.world = world;
        setUserEmailAddress(world.registerUser.getEmailAddress());
        setUsername(world.registerUser.getUserName());
        setApplicationDetails(world.applicationDetails.getLicenceNumber());
    }

    @And("I am on the ECMT International removal submission page")
    public void iAmOnTheECMTInternationalRemovalSubmissionPage() {
        EcmtInternationalRemovalJourney.completeAndSubmitApplication(world);
    }

    @Then("the page heading on the submission page is displayed correctly")
    public void thePageHeadingOnTheSubmissionPageIsDisplayedCorrectly() {
        SubmissionPageSteps.assertHeadingPresentInSubmissionPanel();
    }

    @And("the application reference number is displayed correctly")
    public void theApplicationReferenceNumberIsDisplayedCorrectly() {
        SubmissionPageSteps.assertReferenceNumberPresentInPanelBody(world);
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
        EcmtInternationalRemovalJourney.completeUntilDeclarationPage(world);
    }

    @And("the application is under issued permits table with status as valid")
    public void theApplicationIsUnderIssuedPermitsTableWithStatusAsValid() {
        refreshPage();
        CommonSteps.waitUntilPermitHasStatus(world);
    }

    @And("I navigate to permit dashboard page")
    public void iNavigateToPermitDashboardPage() {
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.HOME);
    }

    @And("I'm on the ECMT international submitted page for my active application")
    public void iMOnTheECMTInternationalSubmittedPageForMyActiveApplication() {
        world.selfServeNavigation.navigateToLogin(getUsername(), getUserEmailAddress());
        HomePageJourney.selectPermitTab();
        clickByLinkText(getApplicationDetails());
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

    public static void assertHeadingPresentInSubmissionPanel() {
        assertEquals(BasePage.getElementValueByText("//h1[@class='govuk-panel__title']", SelectorType.XPATH),"Application submitted");
    }

    public static void assertReferenceNumberPresentInPanelBody(World world) {
        String referenceNumber = BasePage.getElementValueByText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
        assertTrue(referenceNumber.contains("Your reference number"));
        String actualReferenceNumber = BasePage.getElementValueByText("//div/strong", SelectorType.XPATH);
        assertTrue(actualReferenceNumber.contains(world.applicationDetails.getLicenceNumber()));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getApplicationDetails() {
        return applicationDetails;
    }

    public void setApplicationDetails(String applicationDetails) {
        this.applicationDetails = applicationDetails;
    }
}