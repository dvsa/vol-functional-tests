package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import java.net.MalformedURLException;

public class DVLAJourneySteps extends BasePage {

    private World world;

    public DVLAJourneySteps(World world){ this.world = world; }

    public void navigateToTransferVehiclePage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
    }

    public void navigateToManageVehiclesPage() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage("licence", "Vehicles");
        String URL = Browser.navigate().getCurrentUrl();
        String newURL = URL.substring(0, URL.length()-2);
        Browser.navigate().get(newURL);
    }
}
