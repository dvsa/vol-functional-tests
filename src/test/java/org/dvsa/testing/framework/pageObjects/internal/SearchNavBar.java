package org.dvsa.testing.framework.pageObjects.internal;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SearchNavBar extends NavigationBar {

    public static void search(@NotNull SearchType searchType, @NotNull String search) {
        selectValueFromDropDown("#search-select", SelectorType.CSS, searchType.toString());
        String SEARCH = "//input[@name='search']";
        String SEARCH_BUTTON = "//*[@name='submit']";
        findElement(SEARCH,SelectorType.XPATH).clear();
        if(Objects.requireNonNull(findElement(SEARCH, SelectorType.XPATH).getAttribute("value")).isEmpty()) {
           enterText(SEARCH, SelectorType.XPATH, search);
       }
        waitAndClick(SEARCH_BUTTON,SelectorType.XPATH);
    }
}