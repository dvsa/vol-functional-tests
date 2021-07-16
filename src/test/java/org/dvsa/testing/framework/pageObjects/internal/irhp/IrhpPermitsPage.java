package org.dvsa.testing.framework.pageObjects.internal.irhp;

import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.enums.YearSelection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.BaseDetailsPage;

public class IrhpPermitsPage extends BaseDetailsPage {

    public static class Model extends BaseModel {

        public static void permitType(PermitType type) {
            selectValueFromDropDown("#permitType", SelectorType.CSS, type.toString());
        }

        public static void yearSelection(YearSelection type) {
            selectValueFromDropDown("#yearList", SelectorType.CSS, type.toString());
        }

        public static void continueButton() {
            org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsPage.scrollAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
        }
    }

}
