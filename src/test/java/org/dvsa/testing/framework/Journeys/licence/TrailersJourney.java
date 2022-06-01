package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class TrailersJourney extends BasePage {

    private World world;

    public TrailersJourney(World world) {
        this.world = world;
    }


    public void addTrailerToLicence(String number, String iLST) {
        click("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add trailer");
        waitAndEnterText("data[trailerNo]", SelectorType.NAME, number);
        String Selector = "//input[@name='data[longerSemiTrailer][isLongerSemiTrailer]']";
        String longerSemiTrailerRadioButton = (iLST.equals("Yes") ? Selector + "[@value='Y']" : Selector + "[@value='N']");
        clickByXPath(longerSemiTrailerRadioButton);
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }
}
