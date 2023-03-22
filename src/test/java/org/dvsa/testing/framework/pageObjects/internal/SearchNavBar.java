package org.dvsa.testing.framework.pageObjects.internal;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.jetbrains.annotations.NotNull;

public class SearchNavBar extends NavigationBar {

    public static void search(@NotNull SearchType searchType, @NotNull String search) {
        selectValueFromDropDown("#search-select", SelectorType.CSS, searchType.toString());
        String SEARCH = "//input[@name='search']";
        if(findElement(SEARCH,SelectorType.XPATH).getAttribute("value").isEmpty()) {
           enterText(SEARCH, SelectorType.XPATH, search);
       }
        String SEARCH_BUTTON = "//input[@name='submit']";
        waitAndClick(SEARCH_BUTTON,SelectorType.XPATH);
    }
}