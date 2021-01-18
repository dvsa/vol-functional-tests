package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.junit.Assert;

import java.net.MalformedURLException;

import static activesupport.driver.Browser.navigate;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;

public class SurrenderJourneySteps extends BasePage {

    private World world;
    private String discsToDestroy = "2";
    private String discsLost = "2";
    private String discsStolen = "1";
    private String updatedTown;

    public String getDiscsLost() { return discsLost; }

    public void setDiscsLost(String discsLost) { this.discsLost = discsLost; }

    public String getDiscsStolen() { return discsStolen; }

    public void setDiscsStolen(String discsStolen) { this.discsStolen = discsStolen; }

    public String getDiscsToDestroy() { return discsToDestroy; }

    public void setDiscsToDestroy(String discsToDestroy) { this.discsToDestroy = discsToDestroy; }

    public String getUpdatedTown() { return updatedTown; }

    public void setUpdatedTown(String updatedTown) { this.updatedTown = updatedTown; }

    public SurrenderJourneySteps(World world){
        this.world = world;
    }

    public void navigateToSurrendersStartPage() throws IllegalBrowserException, MalformedURLException {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "View");
        clickByLinkText("Apply to surrender licence");
    }

    public void startSurrender() throws IllegalBrowserException, MalformedURLException {
        click("//*[@id='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Review your contact information");
    }

    public void addOperatorLicenceDetails() throws IllegalBrowserException, MalformedURLException {
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='operatorLicenceDocument[lostContent][details]']", SelectorType.XPATH, "lost in the washing");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addCommunityLicenceDetails() throws IllegalBrowserException, MalformedURLException {
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='communityLicenceDocument[stolenContent][details]']", SelectorType.XPATH, "Stolen on the way here");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String getSurrenderAddressLine1() throws IllegalBrowserException, MalformedURLException {
        return getText("//dt[contains(text(),'Address')]//..//dd", SelectorType.XPATH);
    }

    public String getSurrenderTown() throws IllegalBrowserException, MalformedURLException {
        return getText("//dt[contains(text(),'Town/city')]//..//dd", SelectorType.XPATH);
    }

    public String getSurrenderCountry() throws IllegalBrowserException, MalformedURLException {
        return getText("//dt[contains(text(),'Country')]//..//dd", SelectorType.XPATH);
    }

    public String getSurrenderContactNumber() throws IllegalBrowserException, MalformedURLException {
        return getText("//*[@class='app-check-your-answers app-check-your-answers--long'][3]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
    }

    public void submitSurrender() throws MalformedURLException, IllegalBrowserException {
        submitSurrenderUntilChoiceOfVerification();
        if (navigate().getCurrentUrl().contains("qa")) {
            waitAndClick("//*[@id='sign']", SelectorType.XPATH);
            world.UIJourneySteps.signWithVerify();
            checkVerifyConfirmation();
        } else {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            world.UIJourneySteps.signManually();
        }
        assertEquals(getText("//*[@class='overview__status green']", SelectorType.XPATH), "SURRENDER UNDER CONSIDERATION");
    }

    public void submitSurrenderUntilChoiceOfVerification() throws IllegalBrowserException, MalformedURLException {
        submitSurrenderUntilReviewPage();
        acknowledgeDestroyPage();
    }

    public void submitSurrenderUntilReviewPage() throws IllegalBrowserException, MalformedURLException {
        navigateToSurrendersStartPage();
        startSurrender();
        waitAndClick("form-actions[submit]", SelectorType.ID);
        addDiscInformation();
        waitForTextToBePresent("In your possession");
        addOperatorLicenceDetails();
        if (world.createApplication.getLicenceType().equals("standard_international")) {
            assertTrue(navigate().getCurrentUrl().contains("community-licence"));
            addCommunityLicenceDetails();
        }
    }

    public void caseworkManageSurrender() throws MalformedURLException, IllegalBrowserException {
        world.APIJourneySteps.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewLicence();
        clickByLinkText("Surrender");
        waitForTextToBePresent("Surrender details");
        waitAndClick("//*[@for='checks[ecms]']", SelectorType.XPATH);
        // Refresh page
        javaScriptExecutor("location.reload(true)");
        waitAndClick("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);
    }

    public void checkVerifyConfirmation() throws IllegalBrowserException, MalformedURLException {
        waitForTextToBePresent("What happens next");
        Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        Assert.assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.applicationDetails.getLicenceNumber()), 10));
        Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy")), 20));
        assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk", 10));
        waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
    }

    public void acknowledgeDestroyPage() throws IllegalBrowserException, MalformedURLException {
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Securely destroy");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTitleToBePresent("Declaration");
    }

    public void addDiscInformation() throws IllegalBrowserException, MalformedURLException {
        assertTrue(getCurrentUrl().contains("current-discs"));
        click("//*[contains(text(),'In your possession')]", SelectorType.XPATH);
        waitForTextToBePresent("Number of discs you will destroy");
        waitAndEnterText("//*[@id='possessionSection[info][number]']", SelectorType.XPATH, getDiscsToDestroy());
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='lostSection[info][number]']", SelectorType.XPATH, getDiscsLost());
        waitAndEnterText("//*[@id='lostSection[info][details]']", SelectorType.XPATH, "lost");
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='stolenSection[info][number]']", SelectorType.XPATH, getDiscsStolen());
        waitAndEnterText("//*[@id='stolenSection[info][details]']", SelectorType.XPATH, "stolen");
        waitAndClick("//*[@id='submit']", SelectorType.XPATH);
    }

    public void removeDisc() throws IllegalBrowserException, MalformedURLException, ElementDidNotAppearWithinSpecifiedTimeException {
        waitAndClick("form-actions[submit]", SelectorType.ID);
        addDiscInformation();
        clickByLinkText("Home");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
        waitAndClick("//*[@value='Remove']", SelectorType.XPATH);
        untilElementPresent("//*[@id='modal-title']", SelectorType.XPATH);
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        javaScriptExecutor("location.reload(true)");
        waitForTextToBePresent("The selected discs have been voided. You must destroy the old discs");
    }
}
