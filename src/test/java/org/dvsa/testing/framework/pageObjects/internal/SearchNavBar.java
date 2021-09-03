package org.dvsa.testing.framework.pageObjects.internal;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.NavigationBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.jetbrains.annotations.NotNull;

public class SearchNavBar extends NavigationBar {
    private static String SEARCH = nameAttribute("input", "search");
    private static String SEARCH_BUTTON = nameAttribute("input", "submit");

    public static void search(@NotNull SearchType searchType, @NotNull String search) {
        selectValueFromDropDown("#search-select", SelectorType.CSS, searchType.toString());
        scrollAndEnterField(SEARCH, search);
        scrollAndClick(SEARCH_BUTTON);
    }

}
