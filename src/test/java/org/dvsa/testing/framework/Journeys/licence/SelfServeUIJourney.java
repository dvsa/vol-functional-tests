package org.dvsa.testing.framework.Journeys.licence;

import activesupport.aws.s3.SecretsManager;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import apiCalls.enums.LicenceType;
import apiCalls.enums.VehicleType;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.SelfServeNavigation;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Set;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.returnNthNumberSequenceInString;
import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelfServeUIJourney extends BasePage {

    private FakerUtils faker = new FakerUtils();
    private final World world;
    private String VRMField = "//*[@name='data[vrm]']";
    private String weightField = "//*[@name='data[platedWeight]']";
    private String volOperatorName;

    public SelfServeUIJourney(World world) {
        this.world = world;
    }


    public void addNewOperator() {
        if (isElementPresent("//h1[text()='Create an account                ']", SelectorType.XPATH)) {
            waitAndEnterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
            waitAndEnterText("forename", SelectorType.ID, faker.generateFirstName());
            waitAndEnterText("familyName", SelectorType.ID, faker.generateLastName());
            waitAndEnterText("fields[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
            waitAndEnterText("fields[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
            findSelectAllRadioButtonsByValue("N");
            waitAndEnterText("fields[organisationName]", SelectorType.ID, faker.generateCompanyName());
            waitAndClick("//*[contains(text(),'Limited')]", SelectorType.XPATH);
            click("termsAgreed", SelectorType.ID);
            UniversalActions.clickSubmit();
        } else {
            operatorCreatesAccount();
        }
    }

    public void existingAppOrLicence() {
        findSelectAllRadioButtonsByValue("Y");
        waitAndEnterText("fields[licenceContent][licenceNumber]", SelectorType.ID, "ob1057273");
        UniversalActions.clickSubmit();
    }

    public void operatorCreatesAccount() {
        findSelectAllRadioButtonsByValue("N");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Are you acting on behalf of an operator?");
        findSelectAllRadioButtonsByValue("N");
        UniversalActions.clickSubmit();
        completeOperatorAccountDetails();
    }

    public void consultantCreatesAccounts() {
        findSelectAllRadioButtonsByValue("N");
        UniversalActions.clickSubmit();
        waitForTextToBePresent("Are you acting on behalf of an operator?");
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSubmit();
        completeOperatorAccountDetails();
        completeConsultantAccountDetails();
    }

    public void completeConsultantAccountDetails() {
        waitAndEnterText("username", SelectorType.ID, world.DataGenerator.getConsultantUser());
        waitAndEnterText("forename", SelectorType.ID, world.DataGenerator.getConsultantForeName());
        waitAndEnterText("familyName", SelectorType.ID, world.DataGenerator.getConsultantFamilyName());
        waitAndEnterText("fields[emailAddress]", SelectorType.ID, world.DataGenerator.getConsultantUserEmail());
        waitAndEnterText("fields[emailConfirm]", SelectorType.ID, world.DataGenerator.getConsultantUserEmail());
        click("termsAgreed", SelectorType.ID);
        UniversalActions.clickSubmit();
    }

    public void completeOperatorAccountDetails() {
       waitAndEnterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
        waitAndEnterText("forename", SelectorType.ID, world.DataGenerator.getOperatorForeName());
        waitAndEnterText("familyName", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
        waitAndEnterText("fields[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        waitAndEnterText("fields[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        waitAndEnterText("fields[organisationName]", SelectorType.ID, faker.generateCompanyName());
        waitAndClick("//*[contains(text(),'Limited')]", SelectorType.XPATH);
        if (isElementPresent("termsAgreed", SelectorType.ID)){
            click("termsAgreed", SelectorType.ID);
        }
        UniversalActions.clickSubmit();
    }

    public void resetSelfServePassword() {
        String passWord = SecretsManager.getSecretValue("adminPassword");
        waitAndEnterText("auth.reset-password.new-password", SelectorType.ID, passWord);
        waitAndEnterText("auth.reset-password.confirm-password", SelectorType.ID, passWord);
        click(nameAttribute("input", "submit"), SelectorType.CSS);
        assertTrue(isTextPresent("Your password was reset successfully"));
    }

    public void resettingExternalPassword() {
        if (Browser.isBrowserOpen()) {
            navigate().manage().deleteAllCookies();
        }
        world.selfServeNavigation.navigateToLoginPage();
        waitAndClickByLinkText("Forgotten your password?");
    }

    public void addUser() {
        waitAndClickByLinkText("Manage");
        click("//*[@id='addUser']", SelectorType.XPATH);
        waitAndEnterText("username", SelectorType.ID, world.DataGenerator.getOperatorUser());
        waitAndEnterText("forename", SelectorType.ID, world.DataGenerator.getOperatorForeName());
        waitAndEnterText("familyName", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
        waitAndEnterText("main[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        waitAndEnterText("main[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        UniversalActions.clickSubmit();
    }

    public void completeFinancialEvidencePage() {
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.FINANCIAL_EVIDENCE);
        clickByXPath("//*[@id='uploadLaterRadio']");
        UniversalActions.clickSaveAndReturn();
    }

    public void signDeclaration() {
        waitAndClick("//*[contains(text(),'Sign your declaration online')]", SelectorType.XPATH);
        if (isTitlePresent("Review and declarations", 10)) {
            click("//*[@name='declarationsAndUndertakings[signatureOptions]']", SelectorType.XPATH);
            waitAndClick("form-actions[sign]", SelectorType.NAME);
        } else if (isTitlePresent("Declaration", 10)) {
            UniversalActions.clickSubmit();
        }
    }

    public void signDeclarationForVariation() {
        if (isElementPresent("//tr[@class='govuk-table__row']", SelectorType.XPATH)) {
            world.selfServeNavigation.navigateToPage("variation", SelfServeSection.REVIEW_AND_DECLARATIONS);
        } else {
            waitAndClickByLinkText("Review and declarations");
        }
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        if (size("//*[@id='submitAndPay']", SelectorType.XPATH) != 0) {
            click("//*[@id='submitAndPay']", SelectorType.XPATH);
        } else if (size("//*[@id='submit']", SelectorType.XPATH) != 0)
            click("//*[@id='submit']", SelectorType.XPATH);
    }

    public void signManually() {
        String defaultWindow = navigate().getWindowHandle();
        Set<String> windows;
        waitForTextToBePresent("A business owner");
        do {
            waitAndClickByLinkText("Print");
            windows = navigate().getWindowHandles();
        } while (windows.size() == 1);
        String printWindow = windows.stream().reduce((first, second) -> second).get();
        navigate().switchTo().window(printWindow).close();
        navigate().switchTo().window(defaultWindow);
        click("//*[contains(@title,'return to home')]", SelectorType.XPATH);
    }

    public void changeLicenceForVariation() {
        waitForTextToBePresent(world.applicationDetails.getLicenceNumber());
        waitForElementToBeClickable("//*[contains(text(),'change your licence')]", SelectorType.XPATH);
        waitAndClickByLinkText("change your licence");
        waitForTextToBePresent("Applying to change a licence");
        UniversalActions.clickSubmit();
        waitForElementNotToBePresent("//*[@id=\"form-actions[submit]\"]");
        String url = navigate().getCurrentUrl();
        world.updateLicence.setVariationApplicationId(returnNthNumberSequenceInString(url, 1));
    }

    public void prepVariation() {
        waitAndClick("OF2054541", SelectorType.LINKTEXT);
        waitAndClick("Licence authorisation", SelectorType.LINKTEXT);
        waitAndClick("change your licence", SelectorType.LINKTEXT);
        UniversalActions.clickSubmit();
        waitAndEnterText("totAuthLgvVehicles", SelectorType.ID, "1");
        UniversalActions.clickSave();
    }

    public void removeFirstVehicleOnVehiclePage() {
        navigate().findElements(By.xpath("//tbody//input[@type='checkbox']")).stream().findFirst().get().click();
        navigate().findElements(By.xpath("//tbody//input[@type='submit'][@value='Remove']")).stream().findFirst().get().click();
        UniversalActions.clickSubmit();
    }

    public void inputLicenceAndVehicleType(String licenceType, String vehicleType, String lgvUndertaking) {
        if (isElementPresent("//input[@value='" + LicenceType.valueOf(licenceType.toUpperCase()).asString() + "']", SelectorType.XPATH)) {
            clickByXPath("//input[@value='" + LicenceType.valueOf(licenceType.toUpperCase()).asString() + "']");
        } else {
            clickByXPath("//label[@value='" + LicenceType.valueOf(licenceType.toUpperCase()).asString() + "']");
        }

        if (licenceType.equals("standard_international")) {
            if (!"no_selection".equals(vehicleType)) {
                clickByXPath("//input[@value='" + VehicleType.valueOf(vehicleType.toUpperCase()).asString() + "']");
                if (lgvUndertaking.equals("checked")) {
                    clickByXPath(world.typeOfLicenceJourney.lgvDeclarationCheckbox);
                }
            }
        }
        if (vehicleType.equals("lgv_only_fleet"))
            world.createApplication.setVehicleType(VehicleType.LGV_ONLY_FLEET.asString());
        else
            world.createApplication.setVehicleType(VehicleType.MIXED_FLEET.asString());
        UniversalActions.clickSaveAndReturn();
        if (!getCurrentUrl().contains("#validationSummary"))
            world.createApplication.setApplicationId(returnNthNumberSequenceInString(navigate().getCurrentUrl(), 1));
    }

    public void addNewAddressDetails(HashMap<String, String> address, String postcodeMatchingTrafficArea, String typeOfAddress) {
        address.forEach((key, value) ->
                replaceText(String.format("//*[contains(@name,'%s[%s]')]", typeOfAddress, key), SelectorType.XPATH, value)
        );
        if (ifUrlContains("preview")) {
            replaceText(String.format("//*[contains(@name,'%s[postcode]')]", typeOfAddress), SelectorType.XPATH, "LL55 1BN");
        }
        else {
        replaceText(String.format("//*[contains(@name,'%s[postcode]')]", typeOfAddress), SelectorType.XPATH, postcodeMatchingTrafficArea);}
    }

    public void checkAddressDetails(HashMap<String, String> address, String postcode, String typeOfAddress) {
        world.universalActions.checkValue(String.format("//*[@name='%s[addressLine1]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine1"));
        world.universalActions.checkValue(String.format("//*[@name='%s[addressLine2]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine2"));
        world.universalActions.checkValue(String.format("//*[@name='%s[addressLine3]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine3"));
        world.universalActions.checkValue(String.format("//*[@name='%s[addressLine4]']", typeOfAddress), SelectorType.XPATH, address.get("addressLine4"));
        world.universalActions.checkValue(String.format("//*[@name='%s[town]']", typeOfAddress), SelectorType.XPATH, address.get("town"));
        world.universalActions.checkValue(String.format("//*[@name='%s[postcode]']", typeOfAddress), SelectorType.XPATH, postcode);
    }

    public void addAVehicle(String licenceNumber) {
        findSelectAllRadioButtonsByValue("add");
        waitAndClick("next", SelectorType.ID);
        waitAndEnterText("vehicle-search[search-value]", SelectorType.ID, licenceNumber);
        waitAndClick("vehicle-search[submit]", SelectorType.ID);
        if(isTextPresent(String.format("A vehicle has been found with registration"))){
            waitAndClick("confirm", SelectorType.ID);
        }
    }

    public void addAVehicleToAnApplication(String vrm, String weight){
        click("add", SelectorType.ID);
        waitForTitleToBePresent("Add vehicle");
        waitAndEnterText(VRMField, SelectorType.XPATH, vrm);
        waitAndEnterText(weightField, SelectorType.XPATH, weight);
        UniversalActions.clickSubmit();
    }

    public void removeVehicle() {
        findSelectAllRadioButtonsByValue("remove");
        waitAndClick("next", SelectorType.ID);
    }

    public void vehicleRemovalConfirmationPage() {
        removeVehicle();
        waitAndClick("//*[@name='table[id][]'][1]", SelectorType.XPATH);
        waitAndClick("action-button", SelectorType.ID);
    }

    public void noteOperatorNameOnDashboardPage() {
        volOperatorName = getText("/html/body/div[3]/ul/li/b", SelectorType.XPATH);
        System.out.println("Noted operator name: " + volOperatorName);
    }

    public String getVolOperatorName() {
        return volOperatorName;
    }

    public void verifyOperatorNameOnOperatorReportsPage() {
        String topsOperatorName = getText("//*[@id=\"operator_name\"]", SelectorType.XPATH);;
        assertEquals(topsOperatorName, volOperatorName);
    }
}
