package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.temporal.ChronoUnit;

public class DeclarationPage extends BasePermitPage {

    private static String DeclarationCheckBox = "//input[@id='declaration']";

    public static void untilOnPage() {
        untilUrlMatches("/permits/application/\\d+/declaration/", Duration.LONG, ChronoUnit.SECONDS);
    }

    public static boolean hasReference() {
        return getReferenceFromPage().matches("\\w{2}\\d{7} / \\d+");
    }

    public static boolean isBilateralAdvisoryTextPresent() {
        return isTextPresent("By continuing, you confirm that you:") &&
                isTextPresent("will comply fully with the conditions of use under which the permit may be used") &&
                isTextPresent("will carry the permit for the entire outbound and return journey and present it to any competent authority or inspectors") &&
                isTextPresent("You also confirm that you understand that a permit is required for all countries you are travelling to and transiting through.") &&
                isTextPresent("I confirm that I understand the conditions and that the information I have provided is true and correct to the best of my knowledge.");
    }

    public static boolean isMultilateralAdvisoryTextPresent() {
        return isElementPresent("//a[contains(text(),'guidance notes')]",SelectorType.XPATH) &&
        isTextPresent("You have read the") &&
        isTextPresent("You will comply fully with the conditions of use and terms and conditions under which the permits may be used.") &&
        isTextPresent("Your driver will carry the permit for the entire outbound and return journey and present it to any competent authority or inspectors.") &&
        isTextPresent("You understand that one permit is required for each vehicle you intend to use to transport goods to and from any EU or EEA country.");
    }

    public static boolean isECMTAdvisoryTextPresent() {
        return isTextPresent("By continuing you confirm that you:") &&
        isTextPresent("have read the guidance notes on how to use ECMT international removal permits.") &&
        isTextPresent("will comply fully with the conditions of use under which the permits may be used.") &&
        isTextPresent("understand you must obtain a Certificate of Compliance and a Certificate of Roadworthiness for each vehicle and trailer you intend to use.") &&
        isTextPresent("driver will carry the permit and the certificates for the entire outbound and return journey and present it to any competent authority or inspectors.") &&
        isTextPresent("company uses specialised equipment and staff to undertake removal operations.") &&
        isTextPresent("understand that this permit can only be used for international removals between ECMT member countries.") &&
        isTextPresent("understand that, when transiting, you can only use it to deliver removal goods and business possessions to other ECMT countries.");
    }

    public static boolean isShortTermECMTAdvisoryTextPresent() {
        return isTextPresent("You will comply fully with the conditions of use under which the permits may be used.") &&
        isTextPresent("You understand that this permit is for transiting through EU member states to non-EU countries who are ECMT members, including Switzerland.") &&
        isTextPresent("You understand that this permit is for cross-trade for the haulage of goods for hire or reward between two EU countries by a vehicle registered in a different EU country.") &&
        isTextPresent("You understand that Short-term permits are not valid for Austria.") &&
        isTextPresent("You will only use the appropriate Euro emission standard vehicle that the permit allows.") &&
        isTextPresent("You understand you must obtain a Certificate of Compliance and a Certificate of Roadworthiness for each vehicle and trailer you intend to use.") &&
        isTextPresent("Your driver must carry the permit and corresponding logbook, and the certificates for the entire outbound and return journey and present it to any competent authority or inspectors.") &&
        isTextPresent("You understand that Short-term ECMT permits can only be used for 30 consecutive days during the calendar year they have been issued.") &&
        isTextPresent("You will complete the paper permit with the start and end date before starting your journey.") &&
        isTextPresent("You understand the logbook must be returned to DVSA within 2 weeks after returning to the UK.");
    }

    public static boolean isTrailersCertificateAdvisoryMessagePresent() {
        return isTextPresent("By continuing, you confirm that you:")
                && isTextPresent("understand your vehicle must carry warning triangles when travelling in Europe")
                && isTextPresent("understand your drivers must carry the Certificate of Roadworthiness and the Certificate of Compliance for the entire outbound and return journey and present it to any competent authority or inspectors")
                && isTextPresent("are aware that Certificate of Roadworthiness will expire on the same day as the MOT certificate")
                && isTextPresent("I confirm that I understand the conditions and that the information I have provided is true and correct to the best of my knowledge.");
    }

    public static void confirmDeclaration() {
        if (declarationIsNotConfirmed()) {
            scrollAndClick(DeclarationCheckBox, SelectorType.XPATH);
        }
    }

    public static boolean declarationIsNotConfirmed() {
        return !isElementPresent(DeclarationCheckBox + "/ancestor::label[contains(@class, 'selected')]", SelectorType.XPATH);
    }

    public static String getCheckboxText() {
        return getText("//label[@class='form-control form-control--checkbox form-control--advanced']", SelectorType.XPATH);
    }

    public static boolean isWarningTextPresent() {
        return isElementPresent("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH);
    }

    public static boolean isGuidanceNotesLinkPresent() {
        return isElementPresent("//a [@href='https://www.gov.uk/guidance/international-road-haulage-removal-permits", SelectorType.XPATH);
    }
}
