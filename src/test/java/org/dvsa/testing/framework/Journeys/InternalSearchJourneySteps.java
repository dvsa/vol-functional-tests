package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.internal.SearchNavBar;
import org.dvsa.testing.lib.newPages.internal.enums.SearchType;
import org.dvsa.testing.lib.pages.BasePage;

import java.sql.SQLException;

import static activesupport.driver.Browser.navigate;
import static junit.framework.TestCase.assertTrue;

public class InternalSearchJourneySteps extends BasePage {

    private World world;

    public InternalSearchJourneySteps(World world) {
        this.world = world;
    }

    public void searchAndViewApplication()  {
        String variationApplicationNumber = world.updateLicence.getVariationApplicationId();
        long kickOut = System.currentTimeMillis() + 120000;
        if (variationApplicationNumber != null) {
            do {
                SearchNavBar.search(SearchType.Application, variationApplicationNumber);
            } while (!isLinkPresent(variationApplicationNumber, 60) && System.currentTimeMillis() < kickOut);
            clickByLinkText(variationApplicationNumber);
            assertTrue(Boolean.parseBoolean(String.valueOf(navigate().getCurrentUrl().contains("variation"))));
        } else {
            do {
                SearchNavBar.search(SearchType.Application, world.createApplication.getApplicationId());
            } while (!isLinkPresent(world.createApplication.getApplicationId(), 200) && System.currentTimeMillis() < kickOut);
            clickByLinkText(world.createApplication.getApplicationId());
            if (isLinkPresent("Interim", 60))
                clickByLinkText("Interim ");
        }
    }

    public void searchAndViewLicence()  {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(SearchType.Licence, world.applicationDetails.getLicenceNumber());
        } while (!isLinkPresent(world.applicationDetails.getLicenceNumber(), 200) && System.currentTimeMillis() < kickOut);
        clickByLinkText(world.applicationDetails.getLicenceNumber());
    }

    public void searchAndViewCase()  {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(SearchType.Case, String.valueOf(world.updateLicence.getCaseId()));
        } while (!isLinkPresent(String.valueOf(world.updateLicence.getCaseId()), 200) && System.currentTimeMillis() < kickOut);
        clickByLinkText(String.valueOf(world.updateLicence.getCaseId()));
    }

    public void searchAndViewPSVDisc() throws UnsupportedDatabaseDriverException, SQLException {
        int psvDiscNumber = world.DBUtils.getFirstPsvDiscNumber(world.createApplication.getLicenceId(), world.configuration);
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            // - 5 is required because the set start number is for all licences that need printing, not just this licence, and so taking the end number and scaling back makes more sense.
            SearchNavBar.search(SearchType.PsvDisc, String.valueOf(psvDiscNumber));
        } while (!isTextPresent(String.valueOf(psvDiscNumber)) && System.currentTimeMillis() < kickOut);
        waitForElementToBeClickable(String.format("//a[contains(text(),%s)]", world.applicationDetails.getLicenceNumber()), SelectorType.XPATH);
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
    }

    public void searchAndViewAddress()  {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(SearchType.Address, String.format("%s, %s, %s, %s, %s",
                    world.createApplication.getOperatingCentreAddressLine1(),
                    world.createApplication.getOperatingCentreAddressLine2(),
                    world.createApplication.getOperatingCentreAddressLine3(),
                    world.createApplication.getOperatingCentreAddressLine4(),
                    world.createApplication.getOperatingCentreTown()));
        } while (!isTextPresent(String.format("%s, %s, %s, %s, %s",
                world.createApplication.getOperatingCentreAddressLine1(),
                world.createApplication.getOperatingCentreAddressLine2(),
                world.createApplication.getOperatingCentreAddressLine3(),
                world.createApplication.getOperatingCentreAddressLine4(),
                world.createApplication.getOperatingCentreTown()))
                && System.currentTimeMillis() < kickOut);
        waitForElementToBeClickable(String.format("//a[contains(text(),%s)]", world.applicationDetails.getLicenceNumber()), SelectorType.XPATH);
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Addresses");
    }
}