package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import activesupport.file.TestResourceReader;
import activesupport.system.Properties;
import apiCalls.enums.LicenceType;
import com.typesafe.config.Config;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import static activesupport.driver.Browser.navigate;

public class ContinuationJourney extends BasePage {

    private World world;
    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    public ContinuationJourney(World world) {
        this.world = world;
    }

    public void generateContinuationOnInternal(String licenceNo, String licenceTrafficArea, String month)  {
        waitAndClick("//*[contains(text(),'Admin')]", SelectorType.XPATH);
        waitAndClick("menu-admin-dashboard/continuations", SelectorType.ID);
        waitForElementToBePresent("//*[@id='generate-continuation-type']");
        selectValueFromDropDownByIndex("details[date][month]", SelectorType.NAME, Integer.parseInt(month) - 1); // Minus one in the month because of indexing.
        selectValueFromDropDown("generate-continuation-trafficArea", SelectorType.ID, licenceTrafficArea);
        waitAndClick("form-actions[generate]", SelectorType.ID);
        waitAndEnterText("filters[licenceNo]",  SelectorType.ID, licenceNo);
        waitAndClick("main", SelectorType.ID);
        waitForTextToBePresent("1 licence(s)");
        waitAndClick("id[]", SelectorType.NAME);
        waitAndClick("generate", SelectorType.ID);
        world.UIJourney.clickSubmit();
        waitForTextToBePresent("The selected licence(s) have been queued");
    }

    public void continueLicenceWithVerifyAndPay()  {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        clickContinueLicenceOnSelfServe();
        click("submit", SelectorType.ID);
        completeContinuationsReviewPage();
        completeContinuationConditionsAndUndertakingsPage();
        completeContinuationFinancesPage();
        completeContinuationsSignPage();
        completeContinuationPayOrSubmit();
    }

    public void clickContinueLicenceOnSelfServe()  {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        refreshPageUntilElementAppears("//*[@class='info-box info-box--pink']", SelectorType.XPATH);
        click("//a[contains(text(),'Continue licence')]", SelectorType.XPATH);
    }

    public void completeContinuationFinancesPage()  {
        if (!(world.licenceCreation.isPSVLicence() && world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString()))) {
            String necessaryIncome = Browser.navigate().findElement(By.xpath("//strong[contains(text(),'£')]")).getText().replaceAll("[£,]","");
            enterText("averageBalance", SelectorType.ID, necessaryIncome);
            findSelectAllRadioButtonsByValue("N");
            click("submit", SelectorType.ID);
        }
    }

    public void viewContinuationSnapshotOnInternal()  {
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
        clickByLinkText("Docs & attachments");
        refreshPageUntilElementAppears("//*[contains(text(), 'Digital continuation snapshot')]", SelectorType.XPATH);
        Assert.assertTrue(isTextPresent("Digital continuation snapshot"));
        clickByLinkText("Digital continuation snapshot");
        waitForTabsToLoad(2, 60);
        ArrayList<String> tabs = new ArrayList<String> (getWindowHandles());
        switchToWindow(tabs.get(1));
    }

    public void replaceContinuationAndReviewDates(LinkedHashMap<String, String> continuationDates, LinkedHashMap<String, String> reviewDates)  {
        waitForTextToBePresent("Continuation date");
        enterDateFieldsByPartialId("details[continuationDate]", continuationDates);
        enterDateFieldsByPartialId("details[reviewDate]", reviewDates);
        waitForElementToBeClickable("form-actions[submit]", SelectorType.ID);
        world.UIJourney.clickSubmit();
    }

    public void completeContinuationPayOrSubmit()  {
        if (world.licenceCreation.isGoodsLicence() || world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString())) {
           waitAndClick("submitAndPay", SelectorType.ID);
            world.UIJourney.clickPay();
            world.feeAndPaymentJourney.customerPaymentModule();
        } else {
            click("submit", SelectorType.ID);
        }
        waitForTextToBePresent("Your licence has been continued");
    }

    public void completeContinuationsReviewPage() {
        clickAllCheckboxes();
        findSelectAllRadioButtonsByValue("Y");
        click("licenceChecklistConfirmation[yesContent][submit]", SelectorType.ID);
    }

    public void completeContinuationsSignPage()  {
        if (Objects.equals(world.configuration.env.toString(), "qa") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            click("content[signatureOptions]", SelectorType.ID);
            waitAndClick("sign", SelectorType.ID);
            world.govSignInJourney.navigateToGovUkSignIn();
            world.govSignInJourney.signInGovAccount();
        } else {
            waitAndClick("//*[contains(text(),'Print, sign and return')]", SelectorType.XPATH);
        }
    }

    public void completeContinuationConditionsAndUndertakingsPage() {
        if (!world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString())) {
            if (world.licenceCreation.isPSVLicence() &&
                    (world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString()) || !world.createApplication.getPsvVehicleSize().equals("psvvs_medium_large"))) {
                waitForTextToBePresent("You must review and comply with any conditions and undertakings.");
                clickAllCheckboxes();
                click("submit", SelectorType.ID);
            }
        }
    }

    public void checkPSVRestrictedConditionsAndUndertakingsText() throws FileNotFoundException {
        Config testFile = new TestResourceReader("testResources/PSV/PSVRestrictedConditionsAndUndertakings.properties").getFile();
        Assert.assertTrue(isTextPresent(testFile.getString("conditionsOne")));
        Assert.assertTrue(isTextPresent(testFile.getString("conditionsTwo")));
        Assert.assertTrue(isTextPresent(testFile.getString("conditionsThree")));
        Assert.assertTrue(isTextPresent(testFile.getString("undertakingsOne")));
        Assert.assertTrue(isTextPresent(testFile.getString("undertakingsTwo")));
    }

    public void checkContinuationReviewSections() {
        Assert.assertTrue(isTextPresent("Type of licence"));
        Assert.assertTrue(isTextPresent("Business type"));
        Assert.assertTrue(isTextPresent("Business details"));
        Assert.assertTrue(isTextPresent("Addresses"));
        Assert.assertTrue(isTextPresent("Directors"));
        if (!world.createApplication.getLicenceType().equals(LicenceType.SPECIAL_RESTRICTED.asString())){
            Assert.assertTrue(isTextPresent("Operating centres and authorisation"));
            Assert.assertTrue(isTextPresent("Safety and compliance"));
            if (!world.createApplication.getLicenceType().equals(LicenceType.RESTRICTED.asString())) {
                Assert.assertTrue(isTextPresent("Transport managers"));
            }
        }
        if (world.licenceCreation.isGoodsLicence()) {
            // 'Vehicle' targeting is fine on snapshot.
            if (isTextPresent("Print this page")) {
                Assert.assertTrue(isTextPresent("Vehicles"));
            } else {
                // Selenium is struggling to target Vehicle title on Self Serve.
                Assert.assertTrue(isElementPresent("vehiclesCheckbox", SelectorType.ID));
            }
        }
        Assert.assertTrue(isTextPresent("User access"));
    }
}
