package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import apiCalls.enums.LicenceType;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

import static activesupport.driver.Browser.navigate;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;

public class SurrenderJourney extends BasePage {

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

    public SurrenderJourney(World world){
        this.world = world;
    }

    public void navigateToSurrendersStartPage()  {
        refreshPageWithJavascript();
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("Apply to surrender licence");
    }

    public void startSurrender()  {
        click("//*[@id='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Review your contact information");
    }

    public void addOperatorLicenceDetails()  {
        click("//*[contains(text(),'Lost')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='operatorLicenceDocument[lostContent][details]']", SelectorType.XPATH, "lost in the washing");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void addCommunityLicenceDetails()  {
        click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);
        waitAndEnterText("//*[@id='communityLicenceDocument[stolenContent][details]']", SelectorType.XPATH, "Stolen on the way here");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public String getSurrenderAddressLine1()  {
        return getText("//dt[contains(text(),'Address')]//..//dd", SelectorType.XPATH);
    }

    public String getSurrenderTown()  {
        return getText("//dt[contains(text(),'Town/city')]//..//dd", SelectorType.XPATH);
    }

    public String getSurrenderCountry()  {
        return getText("//dt[contains(text(),'Country')]//..//dd", SelectorType.XPATH);
    }

    public void submitSurrender() {
        submitSurrenderUntilChoiceOfVerification();
        if (navigate().getCurrentUrl().contains("qa")) {
            waitAndClick("//*[@id='sign']", SelectorType.XPATH);
            world.UIJourney.signWithVerify();
            checkVerifyConfirmation();
        } else {
            waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
            world.UIJourney.signManually();
        }
        assertEquals(getText("//*[@class='overview__status green']", SelectorType.XPATH), "SURRENDER UNDER CONSIDERATION");
    }


    public void submitSurrenderUntilChoiceOfVerification()  {
        submitSurrenderUntilReviewPage();
        acknowledgeDestroyPage();
    }

    public void submitSurrenderUntilReviewPage()  {
        navigateToSurrendersStartPage();
        startSurrender();
        waitAndClick("form-actions[submit]", SelectorType.ID);
        addDiscInformation();
        waitForTextToBePresent("In your possession");
        addOperatorLicenceDetails();
        if (world.createApplication.getLicenceType().equals(LicenceType.STANDARD_INTERNATIONAL.asString())) {
            assertTrue(navigate().getCurrentUrl().contains("community-licence"));
            addCommunityLicenceDetails();
        }
    }

    public void caseworkManageSurrender() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getLicence();
        clickByLinkText("Surrender");
        waitForTextToBePresent("Surrender details");
        waitAndClick("//*[@for='checks[ecms]']", SelectorType.XPATH);
        // Refresh page
        refreshPageWithJavascript();
        waitAndClick("//*[contains(text(),'Digital signature')]", SelectorType.XPATH);
    }

    public void checkVerifyConfirmation()  {
        waitForTextToBePresent("What happens next");
        Assert.assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        Assert.assertTrue(isTextPresent(String.format("Application to surrender licence %s", world.applicationDetails.getLicenceNumber())));
        Assert.assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy"))));
        assertTrue(isTextPresent("notifications@vehicle-operator-licensing.service.gov.uk"));
        waitAndClick("//*[contains(text(),'home')]", SelectorType.XPATH);
    }

    public void acknowledgeDestroyPage()  {
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("Securely destroy");
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTitleToBePresent("Declaration");
    }

    public void addDiscInformation()  {
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
        waitAndClick("//*[@id='submit']", SelectorType.XPATH);
    }

    public void removeDisc() {
        waitAndClick("form-actions[submit]", SelectorType.ID);
        addDiscInformation();
        clickByLinkText("Home");
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
        waitAndClick("//*[@value='Remove']", SelectorType.XPATH);
        waitForElementToBePresent("//*[@id='modal-title']");
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        refreshPageWithJavascript();
        waitForTextToBePresent("The selected discs have been voided. You must destroy the old discs");
    }
}