package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.Section;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.jetbrains.annotations.NotNull;

public class CheckYourAnswerPage extends BasePermitPage {

    private static final String ANSWER_SECTION_ELEMENTS = "//dt[contains(text(), '%s')]/..//dd";

    public static void untilOnPage() {
        waitForElementToBePresent("//h1[contains(text(),'Check your answers')]");
    } // Another method has been deleted for this. If this doesn't work for those tests, delete this and use a wait for the title instead.

    public static String getAnswer(@NotNull Section section) {
        String sectionTitle =  section.toString();
        String answerText = ANSWER_SECTION_ELEMENTS + "[1]";
        String selector = String.format(answerText, sectionTitle);

        return getText(selector, SelectorType.XPATH);
    }

    public static void clickChangeAnswer(@NotNull Section section) {
        String sectionTitle =  section.toString();
        String answerChangeLink = ANSWER_SECTION_ELEMENTS + "[2]/a";
        String selector = String.format(answerChangeLink, sectionTitle);
        scrollAndClick(selector, SelectorType.XPATH);
    }

    public static void clickConfirmAndReturnToOverview() {
        click("//*[@value='Confirm and return to overview']", SelectorType.XPATH);
    }
}
