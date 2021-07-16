package org.dvsa.testing.framework.pageObjects.external.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.enums.Sector;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class SectorPage extends BasePermitPage {

    private static String SECTOR_TITLE = ".govuk-label";

    public static void selectSectionAndContinue(Sector sector) {
        scrollAndClick(String.format("//label[contains(text(), '%s')]/../input", sector.toString()), SelectorType.XPATH);
        saveAndContinue();
    }

    public static List<String> getSectorsOnPage() {
        List<WebElement> sectors = findAll(SECTOR_TITLE, SelectorType.CSS);
        return sectors.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}