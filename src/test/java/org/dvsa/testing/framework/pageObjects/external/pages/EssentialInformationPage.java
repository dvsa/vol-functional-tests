package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class EssentialInformationPage extends BasePermitPage {

    public static void untilOnPage() {
        untilElementIsPresent("//h1[contains(text(),'Essential information')]", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
    }

    public static boolean isPageContentPresent() {
        return isTextPresent("Hauliers visiting Norway between 1 January 2021 and 30 September 2021 will only require permits if carrying out cabotage (transporting goods between 2 points within Norway).") &&
        isTextPresent("Hauliers visiting Norway from 1 October 2021 onwards will require a standard permit to enter the country and a cabotage permit in addition if you wish to carry out cabotage.") &&
        isTextPresent("Permits of all types are valid from the year they issued until 31 January the following year.") &&
        isTextPresent("Applications can be made for either multi journey permits which allow unlimited journeys for the period the permit is valid or for single journey permits. Which types of permit are best for you depends on the number of journeys you are to take.") &&
        isTextPresent("The fees for applications and for granting of permits is as follows:") &&
        isTextPresent("Please be aware that any cabotage permits applied for may arrive separately after any standard permits.");
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
