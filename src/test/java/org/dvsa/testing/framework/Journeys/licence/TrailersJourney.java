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
        //waitForTitleToBePresent("Trailers");

        click("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add trailer");
        waitAndEnterText("data[trailerNo]", SelectorType.NAME, number);
        /*
        waitForTitleToBePresent("Review your contact information");
        assertTrue(isTextPresent("Overview"));
        if (!isLinkPresent("Transport", 60) && isTextPresent("Granted")) {
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            tmCount = returnTableRows("//*[@id='lva-transport-managers']/fieldset/div/div[2]/table/tbody/tr", SelectorType.XPATH);
        }
        clickByLinkText("Transport");
        assertTrue(isTextPresent("Transport Managers"));
        click("//*[@value='Remove']", SelectorType.XPATH);
        */

    }
}
