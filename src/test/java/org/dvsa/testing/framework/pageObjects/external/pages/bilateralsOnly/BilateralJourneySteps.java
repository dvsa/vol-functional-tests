package org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class BilateralJourneySteps extends BasePermitPage {

    public static void clickNoToCabotage() {
        waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
    }

    public static void clickYesToCabotage() {
        waitAndClick("//label[contains(text(),'Yes')]", SelectorType.XPATH);
    }

    public static boolean isBilateralCabotageAdvisoryTextPresent() {
        return isTextPresent("Between 1 January 2021 and 30 September 2021 you can continue transporting goods to Norway using a UK Licence for the Community.") &&
        isTextPresent("If you intend to carry out cabotage, you will need a Norway special permit for cabotage. This must be used together with the UK Licence for the Community.");
    }

    public static boolean isNonCabotageConfirmationAdvisoryTextPresent() {
        return isTextPresent("You do not need permits for Norway. We will remove it from your application when you click on 'Save and continue'.") &&
        isTextPresent("If this is the only country you are applying for, you can cancel this application on the next page.");
    }

    public static boolean isStandardAndCabotagePermitsAdvisoryTextPresent() {
        return isTextPresent("Cabotage is the haulage and moving of goods for hire or reward between two points in the same country, carried out by a vehicle that is not registered in that country.") &&
        isTextPresent("From 1 October 2021, if you intend to carry out cabotage, you will need a Norway special permit for cabotage. To be valid it must be used together with a Standard permit.");
    }

    public static String yesAndCabotagePermitConfirmation() {
        List<WebElement> options = findAll("//*[starts-with(text(), 'I')]",SelectorType.XPATH);
        Random random = new Random();
        int index = random.nextInt(options.size());
        options.get(index).click();
        return String.valueOf(random);
    }

    public static void clickFinishButton() {
        waitAndClick("//a[contains(text(),'Go to permits dashboard')]",SelectorType.XPATH);
    }

    public static void bilateralCancel() {
        scrollAndClick("//input[@id='Submit[CancelButton]']", SelectorType.XPATH);
    }

    public static boolean areSectionsPresent(boolean hasCabotage) {
        boolean areSectionsPresent = isTextPresent("//dt[contains(text(),'Validity period')]") &&
        isTextPresent("//dt[contains(text(),'Permit usage')]") &&
        isTextPresent("//dt[contains(text(),'Number of permits')]");
        if (hasCabotage) {
            areSectionsPresent = areSectionsPresent && isTextPresent("//dt[contains(text(),'Cabotage needed')]");
        }
        return areSectionsPresent;
    }

    public static String getPeriodText() {
        return getText("//*[@id='main-content']//dl/div[1]/dd[1]", SelectorType.XPATH);
    }

    public static String getJourney() {
        return getText("//*[@id='main-content']/div/div/dl/div[2]/dd[1]", SelectorType.XPATH);
    }

    public static String getCabotageValue() {
        return getText("//*[@id='main-content']//dl/div[3]/dd[1]", SelectorType.XPATH);
    }

    public static String getPermitValue() {
        return getText("//*[@id='main-content']//dl/div[4]/dd[1]", SelectorType.XPATH);
    }

    public static String getPermitValueMultiple() {
        return getText("//div[4]//dd[1]", SelectorType.XPATH);
    }
}
