package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import java.time.Year;
import java.util.Random;

public class YearSelectionPage extends BasePermitPage {

    World world;

    public YearSelectionPage(World world) {
        this.world = world;
    }

    public void selectShortTermValidityPeriod() {
        if (isElementPresent("//input[@type='radio']", SelectorType.XPATH)) {
            String selector = String.format("//input[@id='year']|//input[@value='2021']");
            scrollAndClick(selector, SelectorType.XPATH);
        }
        saveAndContinue();
    }

    public void selectECMTValidityPeriod() {
        if (isYearChoicePresent()) {
            int currentYear = Year.now().getValue();
            int nextYear = currentYear + 1;
            Random random = new Random();
            String year = random.nextBoolean() ? String.valueOf(currentYear) : String.valueOf(nextYear);
            String selector = String.format("//label[contains(text(),'%s')]", year);
            scrollAndClick(selector, SelectorType.XPATH);
            world.basePermitJourney.setYearChoice(year);
        }
        saveAndContinue();
    }


    public  boolean isYearChoicePresent() {
        return isElementPresent("//input[@type='radio']", SelectorType.XPATH);
    }
}
