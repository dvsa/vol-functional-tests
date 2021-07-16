package org.dvsa.testing.framework.pageObjects.internal.admin.permits;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.ScoringStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;

import java.util.concurrent.TimeUnit;

public class Scoring extends BasePage {

    public static void run(long duration, TimeUnit timeUnit){
        String selector = "//a[@id='runButton']";
        untilElementIsPresent(selector, SelectorType.XPATH, duration, timeUnit);
        clickUntilModelIsPresent(selector, Duration.CENTURY);
    }

    public static void accept(long duration, TimeUnit timeUnit) {
        String selector = "//a[@id='acceptButton']";
        untilVisible(selector, SelectorType.XPATH, duration, timeUnit);
        clickUntilModelIsPresent(selector, Duration.CENTURY);
    }

    private static void clickUntilModelIsPresent(String selector, int seconds){
        int count = seconds * 2; // There is a 500 millisecond wait per iteration, meaning that 2 counts = 1 second
        do {
            try {
                scrollAndClick(selector, SelectorType.XPATH);
                Model.untilModalIsPresent(500, TimeUnit.MICROSECONDS);
                count--;
            } catch (Exception e) {}
        } while (!Model.hasModel() && count > 0);
    }

    public static void untilScoringStatusIs(ScoringStatus status, long duration, TimeUnit timeUnit) {
        untilElementIsPresent(String.format("//h2[contains(text(), '%s')]", status.toString()), SelectorType.XPATH, duration, timeUnit);
    }

    public static class Model extends BaseModel {
        public static void continueButton(){
            String selector = "(//button[contains(text(), 'Continue')])[2]";
            untilElementIsPresent(selector, SelectorType.XPATH, Duration.SHORT, TimeUnit.MINUTES);

            int count = Duration.LONG;
            do {
                scrollAndClick(selector, SelectorType.XPATH);
                count--;
                try {
                    untilModalIsGone();
                } catch (Exception e) {}
            } while (hasModel() && count > 0);
        }
    }

}
