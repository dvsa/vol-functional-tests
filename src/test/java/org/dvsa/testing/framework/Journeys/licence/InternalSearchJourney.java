package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.SearchNavBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InternalSearchJourney extends BasePage {

    private final World world;

    public InternalSearchJourney(World world) {
        this.world = world;
    }

    public void searchAndViewApplication() {
        String applicationId = world.createApplication.getApplicationId();
        internalSearchUntilTextPresent(SearchType.Application, applicationId, applicationId);
        if (isLinkPresent("Interim", 3))
            clickByLinkText("Interim ");
    }

    public void searchAndViewLicence(String licenceNumber) {
        internalSearchUntilTextPresent(SearchType.Licence, licenceNumber, licenceNumber);
    }

    public void searchUser() {
        long kickOut = System.currentTimeMillis() + 200000;
        do {
            SearchNavBar.search(SearchType.Users, world.DataGenerator.getOperatorUser());
        } while (!isTextPresent(world.DataGenerator.getOperatorUser()) && System.currentTimeMillis() < kickOut);
    }

    public void searchAndViewCase() {
        String caseId = String.valueOf(world.updateLicence.getCaseId());
        internalSearchUntilTextPresent(SearchType.Case, caseId, caseId);
    }

    public void searchAndViewPSVDisc() {
        String licenceNo = world.applicationDetails.getLicenceNumber();
        internalSearchUntilTextPresent(SearchType.PsvDisc, world.updateLicence.getStartNumber(), licenceNo);
        clickByLinkText("Licence discs");
    }

    public void searchAndViewVehicleRegistration() {
        enterText("search", SelectorType.NAME, world.applicationDetails.getLicenceNumber());
        click("//*[@name='submit']", SelectorType.XPATH);
        searchAndViewLicence(world.applicationDetails.getLicenceNumber());
        clickByLinkText("Vehicles");
    }

    public void registeredVRMDisplay() {
        String vrm = BasePage.getElementValueByText("//*[@id='application_vehicle-safety_vehicle-psv']//table/tbody/tr[1]/td[1]/button", SelectorType.XPATH);
        selectValueFromDropDown("//*[@id='search-select']", SelectorType.XPATH, "Vehicle");
        replaceText("//*[@name='search']", SelectorType.XPATH, vrm);
        click("//*[@name='submit']", SelectorType.XPATH);
        refreshPageUntilElementAppears("//*[contains(text(),'1 Vehicle')]", SelectorType.XPATH);
        waitForTextToBePresent("1 Vehicle");
        assertTrue(isTextPresent(vrm));
    }

    public void searchAndViewAddress() {
        String address = world.formattedStrings.getFullCommaOperatingAddress();
        internalSearchUntilTextPresent(SearchType.Address, address, world.applicationDetails.getLicenceNumber());
        clickByLinkText("Addresses");
    }

    public void internalSearchUntilTextPresent(SearchType searchType, String searchString, String linkText) {
        boolean isLinkPresent;
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            isLinkPresent = isTextPresent(searchString);
            SearchNavBar.search(searchType, searchString);
        } while (!isLinkPresent && System.currentTimeMillis() < kickOut);
        if (!searchType.name().equals("Vehicle")) {
            waitForTextToBePresent(linkText);
            waitAndClick(linkText, SelectorType.LINKTEXT);
        }
    }

    public void searchForLicenceByName(String companyName) {
        SearchNavBar.search(SearchType.Licence, companyName);
    }
}