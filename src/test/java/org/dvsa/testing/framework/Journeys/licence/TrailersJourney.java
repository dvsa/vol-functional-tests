package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class TrailersJourney extends BasePage {

    private World world;

    public TrailersJourney(World world) {
        this.world = world;
    }

    public void addTrailerToLicence(String number) {
        waitForTextToBePresent("Add trailer");
        waitAndEnterText("data[trailerNo]", SelectorType.NAME, number);
    }

    public void isLongerSemiTrailer(String iLST) {
        waitForTextToBePresent("Is this a longer semi-trailer");
        String Selector = "//input[@name='data[longerSemiTrailer][isLongerSemiTrailer]']";
        String longerSemiTrailerRadioButton = (iLST.equals("Yes") ? Selector + "[@value='Y']" : Selector + "[@value='N']");
        clickByXPath(longerSemiTrailerRadioButton);
    }
}
