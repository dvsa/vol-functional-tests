package org.dvsa.testing.framework.pageObjects.internal.admin.permits;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.FeatureToggleStatus;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.Features;

import java.util.concurrent.TimeUnit;

public class FeatureTogglesPage extends BasePage {
    private static final String ROW_TEMPLATE = "//a[contains(text(), '%s')]/../../";

    public static void toggle(Features feature, FeatureToggleStatus status){
        if (!status(feature, status)) {
            scrollAndClick(String.format(ROW_TEMPLATE, feature.toString()) + "td[4]//input", SelectorType.XPATH);
            scrollAndClick("//*[contains(text(), 'Edit')]", SelectorType.XPATH);
            Model.toggle(status);
            Model.save();
        }
    }

    private static boolean status(Features feature, FeatureToggleStatus active) {
        String selector = String.format(ROW_TEMPLATE.concat("td[3]/span[text()='%s']"), feature.toString(), active.name());
        Model.untilModalIsGone();
        return isElementPresent(selector, SelectorType.XPATH);
    }

    public static class Model extends BaseModel {
        public static void toggle(FeatureToggleStatus status){
            untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            scrollAndClick(String.format("//*[contains(text(), '%s')]/../input", status.toString()), SelectorType.XPATH);
        }
        public static void save(){
            scrollAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
        }
    }

}
