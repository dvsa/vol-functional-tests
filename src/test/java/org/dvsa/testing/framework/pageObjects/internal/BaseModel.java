package org.dvsa.testing.framework.pageObjects.internal;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.concurrent.TimeUnit;

public class BaseModel extends BasePage {

    private static String MODAL_CONTAINER = "//*[@class='modal' or @class='modal--alert']";

    public static void untilModalIsPresent(long duration, TimeUnit timeUnit){
        untilElementIsPresent(MODAL_CONTAINER, SelectorType.XPATH, duration, timeUnit);
    }

    public static void untilModalIsGone(){
        untilElementIsNotPresent(MODAL_CONTAINER, SelectorType.XPATH);
    }
}
