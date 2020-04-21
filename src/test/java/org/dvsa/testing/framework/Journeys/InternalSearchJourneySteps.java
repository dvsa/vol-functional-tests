package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.internal.SearchNavBar;

import java.net.MalformedURLException;

import static activesupport.driver.Browser.navigate;
import static junit.framework.TestCase.assertTrue;

public class InternalSearchJourneySteps extends BasePage {

    private World world;

    public InternalSearchJourneySteps(World world) {
        this.world = world;
    }

    public void searchAndViewApplication() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("search-select", SelectorType.ID, "Applications");

        String variationApplicationNumber = world.updateLicence.getVariationApplicationNumber();
        long kickOut = System.currentTimeMillis() + 120000;
        if (variationApplicationNumber != null) {
            do {
                SearchNavBar.search(variationApplicationNumber);
            } while (!isLinkPresent(variationApplicationNumber, 60) && System.currentTimeMillis() < kickOut);
            clickByLinkText(variationApplicationNumber);
            assertTrue(Boolean.parseBoolean(String.valueOf(navigate().getCurrentUrl().contains("variation"))));
        } else {
            do {
                SearchNavBar.search(String.valueOf(world.createLicence.getApplicationNumber()));
            } while (!isLinkPresent(world.createLicence.getApplicationNumber(), 200) && System.currentTimeMillis() < kickOut);
            clickByLinkText(world.createLicence.getApplicationNumber());
            if (isLinkPresent("Interim", 60))
                clickByLinkText("Interim ");
        }
    }

    public void searchAndViewLicence() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("search-select", SelectorType.ID, "Licence");
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(String.valueOf(world.createLicence.getLicenceNumber()));
        } while (!isLinkPresent(world.createLicence.getLicenceNumber(), 200) && System.currentTimeMillis() < kickOut);
        clickByLinkText(world.createLicence.getLicenceNumber());
    }

    public void searchAndViewCase() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("search-select", SelectorType.ID, "Case");
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(String.valueOf(world.updateLicence.getCaseId()));
        } while (!isLinkPresent(String.valueOf(world.updateLicence.getCaseId()), 200) && System.currentTimeMillis() < kickOut);
        clickByLinkText(String.valueOf(world.updateLicence.getCaseId()));
    }

    public void searchAndViewPSVDisc() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("search-select", SelectorType.ID, "Psv Disc");
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            // - 5 is required because the set start number is for all licences that need printing, not just this licence, and so taking the end number and scaling back makes more sense.
            SearchNavBar.search(String.valueOf(Integer.parseInt(world.updateLicence.getEndNumber()) - 5));
        } while (!isTextPresent(String.valueOf(Integer.parseInt(world.updateLicence.getEndNumber()) - 5), 200) && System.currentTimeMillis() < kickOut);
        waitForElementToBeClickable(String.format("//a[contains(text(),%s)]",world.createLicence.getLicenceNumber()), SelectorType.XPATH);
        clickByLinkText(world.createLicence.getLicenceNumber());
        clickByLinkText("Licence discs");
    }

    public void searchAndViewAddress() throws IllegalBrowserException, MalformedURLException {
        selectValueFromDropDown("search-select", SelectorType.ID, "Address");
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(String.format("%s, %s, %s, %s, %s",
                    world.createLicence.getOperatingCentreAddressLine1(),
                    world.createLicence.getOperatingCentreAddressLine2(),
                    world.createLicence.getOperatingCentreAddressLine3(),
                    world.createLicence.getOperatingCentreAddressLine4(),
                    world.createLicence.getOperatingCentreTown()));
        } while (!isTextPresent(String.format("%s, %s, %s, %s, %s",
                world.createLicence.getOperatingCentreAddressLine1(),
                world.createLicence.getOperatingCentreAddressLine2(),
                world.createLicence.getOperatingCentreAddressLine3(),
                world.createLicence.getOperatingCentreAddressLine4(),
                world.createLicence.getOperatingCentreTown()), 200)
                && System.currentTimeMillis() < kickOut);
        waitForElementToBeClickable(String.format("//a[contains(text(),%s)]", world.createLicence.getLicenceNumber()), SelectorType.XPATH);
        clickByLinkText(world.createLicence.getLicenceNumber());
        clickByLinkText("Addresses");
    }
}