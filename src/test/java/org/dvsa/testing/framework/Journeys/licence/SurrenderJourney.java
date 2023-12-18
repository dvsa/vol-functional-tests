package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.LicenceType;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication;
import scanner.AXEScanner;

import java.io.IOException;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SurrenderJourney extends BasePage {

    private World world;
    private String discsToDestroy = "2";
    private String discsLost = "2";
    private String discsStolen = "1";
    private String updatedTown;

    AXEScanner axeScanner = SubmitSelfServeApplication.scanner;


    public String getDiscsLost() {
        return discsLost;
    }

    public void setDiscsLost(String discsLost) {
        this.discsLost = discsLost;
    }

    public String getDiscsStolen() {
        return discsStolen;
    }

    public void setDiscsStolen(String discsStolen) {
        this.discsStolen = discsStolen;
    }

    public String getDiscsToDestroy() {
        return discsToDestroy;
    }

    public void setDiscsToDestroy(String discsToDestroy) {
        this.discsToDestroy = discsToDestroy;
    }

    public String getUpdatedTown() {
        return updatedTown;
    }

    public void setUpdatedTown(String updatedTown) {
        this.updatedTown = updatedTown;
    }

    public SurrenderJourney(World world) {
        this.world = world;
    }

    public void navigateToSurrendersStartPage(boolean scanOrNot) throws IllegalBrowserException, IOException {
        refreshPageWithJavascript();
        if (!getDriver().getCurrentUrl().contains("ssweb")) {
            if (!isTextPresent("Current licences")) {
                world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            }
        }
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("Apply to surrender licence");
    }

    public void startSurrender(boolean scanOrNot) throws IllegalBrowserException, IOException {
        if (scanOrNot) {
            axeScanner.scan(true);
        }
        click("//*[@id='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Review your contact information");
        if (scanOrNot) {
            axeScanner.scan(true);
        }
    }

    public void addOperatorLicenceDetails(boolean scanOrNot) throws IllegalBrowserException, IOException {
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='operatorLicenceDocument[lostContent][details]']", SelectorType.XPATH, "lost in the washing");
        world.UIJourney.clickSubmit();
        if (scanOrNot) {
            axeScanner.scan(true);
        }
    }

    public void addCommunityLicenceDetails() {
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='communityLicenceDocument[stolenContent][details]']", SelectorType.XPATH, "Stolen on the way here");
        world.UIJourney.clickSubmit();
    }

    public String getSurrenderAddressLine1() {
        return getText("//dt[contains(text(),'Address')]//..//dd", SelectorType.XPATH);
    }

    public String getSurrenderTown() {
        return getText("//dt[contains(text(),'Town/city')]//..//dd", SelectorType.XPATH);
    }

    public String getSurrenderCountry() {
        return getText("//dt[contains(text(),'Country')]//..//dd", SelectorType.XPATH);
    }

    public void submitSurrender(boolean scanOrNot) throws IOException, InterruptedException, IllegalBrowserException {
        submitSurrenderUntilChoiceOfVerification(true);
        waitAndClick("//*[@id='sign']", SelectorType.XPATH);
        world.govSignInJourney.navigateToGovUkSignIn();
        world.govSignInJourney.signInGovAccount();
        world.govSignInJourney.changeProtocolForSignInToWorkOnLocal();
        checkSignInConfirmation();
        refreshPageWithJavascript();
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag')]", SelectorType.XPATH), "SURRENDER UNDER CONSIDERATION");
        if (scanOrNot) {
            axeScanner.scan(true);
        }
    }

    public void submitSurrenderUntilChoiceOfVerification(boolean scanOrNot) throws IllegalBrowserException, IOException {
        submitSurrenderUntilReviewPage(true);
        if (scanOrNot) {
            axeScanner.scan(true);
        }
        acknowledgeDestroyPage(true);
        if (scanOrNot) {
            axeScanner.scan(true);
        }
    }

    public void submitSurrenderUntilReviewPage(boolean scanOrNot) throws IllegalBrowserException, IOException {
        navigateToSurrendersStartPage(false);
        startSurrender(false);
        world.UIJourney.clickSubmit();
        addDiscInformation(false);
        waitForTextToBePresent("In your possession");
        addOperatorLicenceDetails(false);
        if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
            assertTrue(navigate().getCurrentUrl().contains("community-licence"));
            addCommunityLicenceDetails();
        }
    }

    public void caseworkManageSurrender() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("Surrender");
        waitForTextToBePresent("Surrender details");
        waitAndClick("//*[@for='checks[ecms]']", SelectorType.XPATH);
        world.UIJourney.closeAlert();
        // Refresh page
        refreshPageWithJavascript();
        waitAndClick("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);
    }

    public void checkVerifyConfirmation() {
        waitForTextToBePresent("What happens next");
        assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.applicationDetails.getLicenceNumber())));
        assertTrue(isTextPresent(String.format("Signed by Kenneth Decerqueira on %s", getCurrentDate("d MMM yyyy"))));
        assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.applicationDetails.getLicenceNumber())));
        waitForTextToBePresent("Application to surrender licence");
        assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk"));
        waitAndClick("//*[contains(text(),'Return to home')]", SelectorType.XPATH);
    }

    public void checkSignInConfirmation() {
        waitForTextToBePresent("What happens next");
        assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.applicationDetails.getLicenceNumber())));
        assertTrue(isTextPresent(String.format("Signed by KENNETH DECERQUEIRA on %s", getCurrentDate("d MMM yyyy"))));
        assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk"));
        waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
    }

    public void acknowledgeDestroyPage(boolean scanOrNot) throws IllegalBrowserException, IOException {
        world.UIJourney.clickSubmit();
        if (scanOrNot) {
            axeScanner.scan(true);
        }
        waitForTextToBePresent("Securely destroy");
        world.UIJourney.clickSubmit();
        waitForTitleToBePresent("Declaration");
    }

    public void addDiscInformation(boolean scanOrNot) throws IllegalBrowserException, IOException {
        assertTrue(getCurrentUrl().contains("current-discs"));
        clickById("stolenSection[stolen]");
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        click("//*[contains(text(),'In your possession')]", SelectorType.XPATH);
        waitForTextToBePresent("Number of discs stolen");
        waitAndEnterText("//*[@id='possessionSection[info][number]']", SelectorType.XPATH, getDiscsToDestroy());
        waitAndEnterText("//*[@id='lostSection[info][number]']", SelectorType.XPATH, getDiscsLost());
        waitAndEnterText("//*[@id='lostSection[info][details]']", SelectorType.XPATH, "lost");
        waitAndEnterText("//*[@id='stolenSection[info][number]']", SelectorType.XPATH, getDiscsStolen());
        waitAndEnterText("//*[@id='stolenSection[info][details]']", SelectorType.XPATH, "stolen");
        if (scanOrNot) {
            axeScanner.scan(true);
        }
        waitAndClick("//*[@id='submit']", SelectorType.XPATH);
        if (scanOrNot) {
            axeScanner.scan(true);
        }
    }

    public void removeDisc() throws IllegalBrowserException, IOException {
        world.UIJourney.clickSubmit();
        addDiscInformation(false);
        clickByLinkText("Home");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
        waitAndClick("//*[@value='Remove']", SelectorType.XPATH);
        waitForElementToBePresent("//*[@id='modal-title']");
        world.UIJourney.clickSubmit();
        refreshPageWithJavascript();
        waitForTextToBePresent("The selected discs have been voided. You must destroy the old discs");
    }
}