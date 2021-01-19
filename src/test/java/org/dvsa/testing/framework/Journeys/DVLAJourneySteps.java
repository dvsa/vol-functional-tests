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

    public void navigateToManageVehiclesPage(String licenceStatus) throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToPage(licenceStatus, "Vehicle");
        String URL = Browser.navigate().getCurrentUrl();
        String newURL = URL.substring(0, URL.length()-2);
        Browser.navigate().get(newURL);
    }

    public void navigateToAddVehiclePage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='add-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Add a vehicle");
    }

    public void navigateToRemoveVehiclePage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='remove-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Remove a vehicle");
    }

    public void navigateToReprintVehicleDiscPage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='reprint-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Reprint vehicle disc");
    }

    public void navigateToTransferVehiclePage() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
    }
}
