package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.util.LinkedList;
import java.util.List;

public class GrantedPermitRestrictionsPage extends BasePermitPage {

    public static List<String> getTableRowHeadings() {
        refreshPage();
        List<String> headings = new LinkedList<>();
        headings.add(getText("//th[1]", SelectorType.XPATH));
        headings.add(getText("//th[2]", SelectorType.XPATH));
        headings.add(getText("//th[3]", SelectorType.XPATH));
        return headings;
    }

   public static void returnToFeeSummaryPage() {
        waitAndClick("//a[@class='return-overview']", SelectorType.XPATH);
   }

}
