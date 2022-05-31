package org.dvsa.testing.framework.Journeys.licence;

        import Injectors.World;
        import activesupport.IllegalBrowserException;
        import activesupport.dates.Dates;
        import org.dvsa.testing.framework.enums.SelfServeSection;
        import org.dvsa.testing.framework.pageObjects.BasePage;
        import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
        import org.joda.time.LocalDate;
        import org.junit.Assert;

        import java.io.IOException;
        import java.util.HashMap;
        import java.util.LinkedHashMap;

        import static junit.framework.TestCase.assertTrue;
        import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
        import static org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication.accessibilityScanner;

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
