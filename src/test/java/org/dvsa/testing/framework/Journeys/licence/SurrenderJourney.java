package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import activesupport.aws.s3.SecretsManager;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.LicenceType;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.AccessibilitySteps;
import scanner.AXEScanner;

import java.io.IOException;
import java.util.Objects;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SurrenderJourney extends BasePage {

    private final World world;
    private String discsToDestroy = "2";
    private String discsLost = "2";
    private String discsStolen = "1";
    private String updatedTown;

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

    public void navigateToSurrendersStartPage() {
        world.selfServeNavigation.navigateToLoginPage();
        if (Objects.requireNonNull(getDriver().getCurrentUrl()).contains("ssweb")) {
            if (!isTextPresent("Current licences")) {
                world.globalMethods.signIn(world.registerUser.getUserName(), SecretsManager.getSecretValue("internalNewPassword"));
            }
        }
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("Apply to surrender licence");
    }

    public void startSurrender() throws IllegalBrowserException, IOException {
        click("//*[@id='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Review your contact information");
    }


    public void addOperatorLicenceDetails() throws IllegalBrowserException, IOException {
        waitAndClick("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='operatorLicenceDocument[lostContent][details]']", SelectorType.XPATH, "lost in the washing");
        UniversalActions.clickSubmit();
    }


    public void addCommunityLicenceDetails() {
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='communityLicenceDocument[stolenContent][details]']", SelectorType.XPATH, "Stolen on the way here");
        UniversalActions.clickSubmit();
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

    public void submitSurrender() throws IOException, InterruptedException, IllegalBrowserException {
        submitSurrenderUntilChoiceOfVerification();
        waitAndClick("//*[@id='sign']", SelectorType.XPATH);
        world.govSignInJourney.navigateToGovUkSignIn();
        world.govSignInJourney.signInGovAccount();
        world.govSignInJourney.changeProtocolForSignInToWorkOnLocal();
        checkSignInConfirmation();
        refreshPageWithJavascript();
        assertTrue(getElementValueByText("//*[contains(@class,'govuk-tag govuk-tag')]", SelectorType.XPATH).equalsIgnoreCase("SURRENDER UNDER CONSIDERATION"));
    }

    public void submitSurrenderUntilChoiceOfVerification() throws IllegalBrowserException, IOException {
        submitSurrenderUntilReviewPage();
        acknowledgeDestroyPage();
    }


    public void submitSurrenderUntilReviewPage() throws IllegalBrowserException, IOException {
        navigateToSurrendersStartPage();
        startSurrender();
        UniversalActions.clickSubmit();
        addDiscInformation();
        waitForTextToBePresent("In your possession");
        addOperatorLicenceDetails();
        if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
            addCommunityLicenceDetails();
        }
    }

    public void caseworkManageSurrender() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("Surrender");
        waitForTextToBePresent("Surrender details");
        waitAndClick("//*[@for='checks[ecms]']", SelectorType.XPATH);
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

    public void acknowledgeDestroyPage() throws IllegalBrowserException, IOException {
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Securely destroy");
        UniversalActions.clickSubmit();
        waitForTitleToBePresent("Declaration");
    }

    public void addDiscInformation() throws IllegalBrowserException, IOException {
        waitForPageLoad();
        clickById("stolenSection[stolen]");
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        click("//*[contains(text(),'In your possession')]", SelectorType.XPATH);
        waitForTextToBePresent("Number of discs stolen");
        waitAndEnterText("//*[@id='possessionSection[info][number]']", SelectorType.XPATH, getDiscsToDestroy());
        waitAndEnterText("//*[@id='lostSection[info][number]']", SelectorType.XPATH, getDiscsLost());
        waitAndEnterText("//*[@id='lostSection[info][details]']", SelectorType.XPATH, "lost");
        waitAndEnterText("//*[@id='stolenSection[info][number]']", SelectorType.XPATH, getDiscsStolen());
        waitAndEnterText("//*[@id='stolenSection[info][details]']", SelectorType.XPATH, "stolen");
        waitAndClick("//*[@id='submit']", SelectorType.XPATH);
    }

    public void removeDisc() throws IllegalBrowserException, IOException {
        UniversalActions.clickSubmit();
        addDiscInformation();
          UniversalActions.clickHome();
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
        waitAndClick("//*[@value='Remove']", SelectorType.XPATH);
        waitForElementToBePresent("//*[@id='modal-title']");
        UniversalActions.clickSubmit();
        refreshPageWithJavascript();
        waitForTextToBePresent("The selected discs have been voided. You must destroy the old discs");
    }
}