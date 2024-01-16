package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class EssentialInformationPage extends BasePermitPage {

    public static void untilOnPage() {
        untilElementIsPresent("//h1[contains(text(),'Essential information')]", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
    }

    public static boolean isTurkeyPageContentPresent() {
        return isTextPresent("Turkey only offers single journey permits that allow one outward and return journey.") &&
        isTextPresent("Each permit costs £8.") &&
        isTextPresent("These permits are valid from the day they are issued until 31 January the following year.");
    }

    public static boolean isUkrainePageContentPresent() {
        return isTextPresent("Ukraine only offers single journey permits that allow one outward and return journey. These permits are valid from the day they are issued until 31 January the following year.") &&
        isTextPresent("Each permit costs £8.") &&
        isTextPresent("The following euro emissions conditions apply:") &&
        isTextPresent("You do not need permits for this country if you use Euro 5, Euro 6 or a higher emission standard vehicle") &&
        isTextPresent("Euro 3 and 4 vehicles need a permit to enter this country") &&
        isTextPresent("Euro 2 or a lower emission standard vehicle cannot be used");
    }
}
