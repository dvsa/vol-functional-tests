package org.dvsa.testing.framework.pageObjects.internal.details.sections;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;

import java.util.concurrent.TimeUnit;

public class Decisions extends BasePage {

    private static final String BUTTON_TEMPLATE = "//a[contains(text(), '%s')]";

    public static class Model extends BaseModel {

        public static void affectNow(boolean immediate){
            String value = immediate ? "Y" : "N";
            String selector = String.format("//legend[text()='Affect now']/../descendant::input[@value='%s']", value);

            BaseDetailsPage.scrollAndClick(selector, SelectorType.XPATH);
        }

        public static void clickCurtailLegislation() {
            String selector = "//li[text()='Art. 8(2)']/following-sibling::li[text() = 'Curtail']";
            activateLegislationOptions();
            scrollAndClick(selector, SelectorType.XPATH);
            org.dvsa.testing.framework.pageObjects.internal.details.sections.Decisions.untilNotVisible(selector, SelectorType.XPATH, Duration.SHORT, TimeUnit.SECONDS);
        }

        private static void activateLegislationOptions() {
            if (!hasLegislationOptionsActive()) {
                org.dvsa.testing.framework.pageObjects.internal.details.sections.Decisions.scrollAndClick("fieldset[data-group='licence-decision-legislation'] .chosen-choices");
            }
        }

        private static boolean hasLegislationOptionsActive() {
            return org.dvsa.testing.framework.pageObjects.internal.details.sections.Decisions.isElementPresent("fieldset[data-group='licence-decision-legislation'] div.chosen-container-active", SelectorType.CSS);
        }

        public static void affectNow() {
            String selector = "//button[text()='Affect now']";
            org.dvsa.testing.framework.pageObjects.internal.details.sections.Decisions.untilVisible(selector, SelectorType.XPATH,Duration.MEDIUM, TimeUnit.SECONDS);
            scrollAndClick(selector, SelectorType.XPATH);
        }
    }

}
