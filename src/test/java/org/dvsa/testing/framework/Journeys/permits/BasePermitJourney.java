package org.dvsa.testing.framework.Journeys.permits;

import Injectors.World;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SelectALicencePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class BasePermitJourney extends BasePermitPage {
    public static PermitType permitType;
    public static String yearChoice;
    public static String fullReferenceNumber;
    public static Boolean countriesWithLimitedPermitsChoice;

    protected BasePermitJourney() { }

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
        // Note not ready for MLH but not needed.
        SelectALicencePage.clickLicence(world.applicationDetails.getLicenceNumber());
        SelectALicencePage.saveAndContinue();
        setFullReferenceNumber(OverviewPage.getReferenceFromPage());
    }


}
