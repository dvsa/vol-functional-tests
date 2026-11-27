package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.Injectors.World;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InternalUndertakingsAndConditionsJourney extends BasePage {
    private World world;

    public InternalUndertakingsAndConditionsJourney(World world) {
        this.world = world;
    }

    public void verifyUndertakingsPageDisplays() {
        // Check for page navigation or breadcrumb showing we're on conditions/undertakings
        waitForElementToBePresent("//li[contains(@class, 'govuk-breadcrumbs__list-item') and contains(text(), 'Conditions and undertakings')]");
    }

    public void verifyUndertakingsTableExists() {
        waitForElementToBePresent("//form[@id='lva-conditions-undertakings']");
        String fieldsetXpath = "//form[@id='lva-conditions-undertakings']//*[@data-group='table']";
        assertTrue(findElements(fieldsetXpath, SelectorType.XPATH).size() > 0,
                "Conditions and undertakings form should be present");
    }

    public void verifyConditionsTableExists() {
        waitForElementToBePresent("//form[@id='lva-conditions-undertakings']");
        String fieldsetXpath = "//form[@id='lva-conditions-undertakings']//*[@data-group='table']";
        assertTrue(findElements(fieldsetXpath, SelectorType.XPATH).size() > 0,
                "Conditions and undertakings section should contain content");
    }

    public void verifyUndertakingTextIsPresent(String undertakingText) {
        waitForElementToBePresent("//form[@id='lva-conditions-undertakings']");
        String undertakingXpath = "//form[@id='lva-conditions-undertakings']//*[@data-group='table']//tr[contains(., '" + undertakingText + "')]";
        assertTrue(findElements(undertakingXpath, SelectorType.XPATH).size() > 0,
                "Undertaking text '" + undertakingText + "' should be present in form");
    }
}
