package org.dvsa.testing.framework.pageObjects.internal;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.jetbrains.annotations.NotNull;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class SearchNavBar extends NavigationBar {

    public static void search(@NotNull SearchType searchType, @NotNull String search) {
        selectValueFromDropDown("#search-select", SelectorType.CSS, searchType.toString());
        String SEARCH = "//input[@name='search']";
        String SEARCH_BUTTON = "//*[@name='submit']";
        if(findElement(SEARCH,SelectorType.XPATH).getAttribute("value").isEmpty()) {
           enterText(SEARCH, SelectorType.XPATH, search);
       }
        waitAndClick(SEARCH_BUTTON,SelectorType.XPATH);
    }
}