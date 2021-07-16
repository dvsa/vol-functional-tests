package org.dvsa.testing.framework.pageObjects.external.pages.baseClasses;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.concurrent.TimeUnit;

public class BaseCertificateOfRoadWorthiness extends BasePermitPage{

    public static void untilOnPage() {
        untilElementIsPresent(PAGE_HEADING, SelectorType.XPATH,10L, TimeUnit.SECONDS);
    }

    public static void enterTextIntoField(String complianceNumber) {
        waitAndEnterText("//input[@type ='text']", SelectorType.XPATH, complianceNumber);
    }
}