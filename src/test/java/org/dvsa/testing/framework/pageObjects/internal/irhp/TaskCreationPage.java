package org.dvsa.testing.framework.pageObjects.internal.irhp;

import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.concurrent.TimeUnit;

public class TaskCreationPage extends BasePermitPage {

    public static void navigateToTaskCreated() {
      waitAndClick("//select[@id='assignedToTeam']",SelectorType.XPATH);
      selectValueFromDropDown("//select[@id='assignedToTeam']",SelectorType.XPATH,"IRHPO"  );
      waitAndClick("//select[@id='category']",SelectorType.XPATH);
      selectValueFromDropDown("//select[@id='category']",SelectorType.XPATH,"Permits"  );
      waitAndClick("//select[@id='assignedToUser']",SelectorType.XPATH);
      selectValueFromDropDown("//select[@id='assignedToUser']",SelectorType.XPATH,"All"  );
    }

    public static void viewTaskCreated() {
      String applicationLink = "//tbody/tr/td/a";
      untilElementIsPresent(applicationLink, SelectorType.XPATH, Duration.CENTURY, TimeUnit.SECONDS);
      waitAndClick(applicationLink,SelectorType.XPATH);
    }
}
