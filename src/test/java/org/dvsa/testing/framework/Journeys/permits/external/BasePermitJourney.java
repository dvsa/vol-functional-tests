package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SelectALicencePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class BasePermitJourney extends BasePermitPage {
    protected static volatile BasePermitJourney instance = null;
    public static PermitType permitType;
    public static String yearChoice;
    public static String fullReferenceNumber;
    public static Boolean countriesWithLimitedPermitsChoice;

    protected BasePermitJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static void setPermitType(PermitType permitType) {
        BasePermitJourney.permitType = permitType;
    }

    public static PermitType getPermitType() {
        return permitType;
    }

    public static void setYearChoice(String yearChoice) {
        BasePermitJourney.yearChoice = yearChoice;
    }

    public static String getYearChoice() {
        return yearChoice;
    }

    public static void setFullReferenceNumber(String fullReferenceNumber) {
        BasePermitJourney.fullReferenceNumber = fullReferenceNumber;
    }

    public static String getFullReferenceNumber() {
        return fullReferenceNumber;
    }

    public static void setCountriesWithLimitedPermitsChoice(boolean choice) {
        BasePermitJourney.countriesWithLimitedPermitsChoice = choice;
    }

    public static boolean getCountriesWithLimitedPermitsChoice() {
        return BasePermitJourney.countriesWithLimitedPermitsChoice;
    }

    public static void permitType(PermitType type) {
        BasePermitJourney.setPermitType(type);
        PermitTypePage.selectType(type);
        PermitTypePage.clickContinue();
    }

    public static void licencePage(World world) {

        String licenceNumber;
        if (SelectALicencePage.numberOfLicences() > 1) {
//            SelectALicencePage.clickLicence(selectedLicence.getLicenceNumber());
//            licenceNumber = selectedLicence.getLicenceNumber();
        } // Need way of clicking licence when multiple are available. See if test breaks first.

        else {
            licenceNumber = world.applicationDetails.getLicenceNumber();
            SelectALicencePage.clickLicence(licenceNumber);
        }

        SelectALicencePage.saveAndContinue();

        setFullReferenceNumber(OverviewPage.getReferenceFromPage());

        BasePermitJourney.setReferenceNumber(OverviewPage.getReferenceFromPage());
    }


}
