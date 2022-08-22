package org.dvsa.testing.framework.pageObjects.internal.irhp;

import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;

public class IrhpPermitsPage extends BaseDetailsPage {

    public static class Model extends BaseModel {

        private static String submitButton = "form-actions[submit]";

        public static void permitType(PermitType type) {
            selectValueFromDropDown("#permitType", SelectorType.CSS, type.toString());
        }

        public static void continueButton() {
            waitAndClick(submitButton, SelectorType.ID);
        }

        public static void waitForContinueToBeClickable() {
            waitForElementToBeClickable(submitButton, SelectorType.ID);
        }
    }

}
