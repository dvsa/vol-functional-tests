package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.SearchNavBar;
import org.dvsa.testing.framework.pageObjects.internal.enums.SearchType;
import org.openqa.selenium.WebElement;

import java.util.stream.Stream;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class InternalSearchJourney extends BasePage {

    private World world;

    public InternalSearchJourney(World world) {
        this.world = world;
    }

    public void searchAndViewApplication() {
        String applicationId = world.createApplication.getApplicationId();
        internalSearchUntilTextPresent(SearchType.Application, applicationId, applicationId);
        if (isLinkPresent("Interim", 3))
            clickByLinkText("Interim ");
    }

    public void searchAndViewLicence() {
        String licenceNo = world.applicationDetails.getLicenceNumber();
        internalSearchUntilTextPresent(SearchType.Licence, licenceNo, licenceNo);
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
        internalSearchUntilTextPresent(SearchType.PsvDisc, world.updateLicence.getStartNumber(), world.applicationDetails.getLicenceNumber());
        clickByLinkText("Licence discs");
    }

    public void searchAndViewAddress() {
        String address = world.formattedStrings.getFullCommaOperatingAddress();
        internalSearchUntilTextPresent(SearchType.Address, address, world.applicationDetails.getLicenceNumber());
        clickByLinkText("Addresses");
    }

    public void internalSearchUntilTextPresent(SearchType searchType, String searchString, String linkText) {
        boolean linkIsThere;
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            SearchNavBar.search(searchType, searchString);
            linkIsThere = findElements("//tbody", SelectorType.XPATH).stream().filter(x -> x.getText().contains(linkText)).isParallel();
        } while (!linkIsThere && System.currentTimeMillis() < kickOut);
        waitAndClick(linkText, SelectorType.PARTIALLINKTEXT);
    }

    public void searchForLicenceByName() {
        String companyName = "Company Name";
        SearchNavBar.search(SearchType.Licence, companyName);
    }
}