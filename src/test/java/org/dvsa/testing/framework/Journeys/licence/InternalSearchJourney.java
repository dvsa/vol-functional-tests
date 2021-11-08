package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.SearchNavBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;

import java.sql.SQLException;

public class InternalSearchJourney extends BasePage {

    private World world;

    public InternalSearchJourney(World world) {
        this.world = world;
    }

    public void searchAndViewApplication() {
        String applicationId = world.createApplication.getApplicationId();
        internalSearchUntilTextPresent(SearchType.Application, applicationId, applicationId);
        if (isLinkPresent("Interim", 60))
            clickByLinkText("Interim ");
    }

    public void searchAndViewLicence()  {
        String licenceNo = world.applicationDetails.getLicenceNumber();
        internalSearchUntilTextPresent(SearchType.Licence, licenceNo, licenceNo);
    }

    public void searchUser() {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(SearchType.Users, world.DataGenerator.getOperatorUserEmail());
        } while (!isTextPresent(world.DataGenerator.getOperatorUserEmail()));
    }

    public void searchLicense() {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(SearchType.Licence, world.applicationDetails.getLicenceNumber());
        } while (!isTextPresent(world.applicationDetails.getLicenceNumber()));
        clickByLinkText(String.valueOf(world.applicationDetails.getLicenceNumber()));
    }


    public void searchAndViewCase()  {
        String caseId = String.valueOf(world.updateLicence.getCaseId());
        internalSearchUntilTextPresent(SearchType.Case, caseId, caseId);
    }

    public void searchAndViewPSVDisc() throws UnsupportedDatabaseDriverException, SQLException {
        int psvDiscNumber = world.DBUtils.getFirstPsvDiscNumber(world.createApplication.getLicenceId(), world.configuration);
        internalSearchUntilTextPresent(SearchType.PsvDisc, String.valueOf(psvDiscNumber), world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
    }

    public void searchAndViewAddress()  {
        String address = String.format("%s, %s, %s, %s, %s",
                world.createApplication.getOperatingCentreAddressLine1(),
                world.createApplication.getOperatingCentreAddressLine2(),
                world.createApplication.getOperatingCentreAddressLine3(),
                world.createApplication.getOperatingCentreAddressLine4(),
                world.createApplication.getOperatingCentreTown());
        internalSearchUntilTextPresent(SearchType.Address, address, world.applicationDetails.getLicenceNumber());
        clickByLinkText("Addresses");
    }

    public void internalSearchUntilTextPresent(SearchType searchType, String searchString, String linkText) {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(searchType, searchString);
        } while (!isTextPresent(searchString) && System.currentTimeMillis() < kickOut);
        waitForElementToBeClickable(String.format("//a[contains(text(),%s)]", linkText), SelectorType.XPATH);
        clickByLinkText(linkText);
    }
}