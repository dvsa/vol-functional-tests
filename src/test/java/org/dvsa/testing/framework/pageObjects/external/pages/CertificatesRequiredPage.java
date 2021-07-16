package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class CertificatesRequiredPage extends BasePermitPage {

    private static String certificateRequiredConfirmation = "//input[@id='qaElement']";

    public static void untilOnPage() {
        untilElementIsPresent("//h1[contains(text(), 'Mandatory certificates')]", SelectorType.XPATH, 6L, TimeUnit.SECONDS);
    }

    public static void completePage() {
        confirmCertificateRequired();
        saveAndContinue();
    }

    public static void confirmCertificateRequired() {
        if (checkboxNotConfirmed())
            scrollAndClick(certificateRequiredConfirmation, SelectorType.XPATH);
    }

    public static boolean checkboxNotConfirmed() {
        return !isElementPresent( certificateRequiredConfirmation+ "/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
    }

    public static String  getAdvisoryText() {
        return getText("//p[@class='govuk-body']", SelectorType.XPATH);
    }

    public static boolean isCheckboxTextPresent() {
        return isTextPresent("I understand that I must obtain and carry the appropriate Certificate of Compliance and Certificate of Roadworthiness for each vehicle and trailer I intend to use with this permit.");
    }

    public static boolean isComplianceAndRoadworthinessFontIsBold() {
        return isElementPresent("//b[contains(text(),'Certificate of Compliance')]", SelectorType.XPATH)
        && isElementPresent("//b[contains(text(),'Certificate of Roadworthiness')]", SelectorType.XPATH);
    }
}
