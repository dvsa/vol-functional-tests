package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitTypePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SelectALicencePage;

import java.util.Optional;

public class BasePermitJourney extends BaseJourney {
    protected static volatile BasePermitJourney instance = null;
    public static PermitType permitType;
    public static String yearChoice;
    public static String fullReferenceNumber;

    protected BasePermitJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    private void setPermitType(PermitType permitType) {
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

    public static BasePermitJourney getInstance() {
        if (instance == null) {
            synchronized (BasePermitJourney.class) {
                instance = new BasePermitJourney();
            }
        }

        return instance;
    }

    public BasePermitJourney permitType(OperatorStore operatorStore) {
        return permitType(PermitType.ECMT_ANNUAL, operatorStore);
    }

    public BasePermitJourney permitType() {
        return permitType(PermitType.ECMT_ANNUAL, new OperatorStore());
    }

    public BasePermitJourney permitType(PermitType type, OperatorStore operator) {
        Optional<LicenceStore> potentialLicence = operator.getLatestLicence();
        LicenceStore licence = potentialLicence.orElseGet(LicenceStore::new);
        operator.withLicences(licence);

        setPermitType(type);
        // TODO: Remove next line below and update code dependent on it
        licence.getEcmt().setType(type);
        operator.setCurrentPermitType(type);
        operator.withLicences(licence);

        PermitTypePage.selectType(type);
        PermitTypePage.clickContinue();
        return this;
    }

    public BasePermitJourney shortTermType(PeriodType shortTermType, OperatorStore operator) {
        Optional<LicenceStore> potentialLicence = operator.getLatestLicence();
        LicenceStore licence = potentialLicence.orElseGet(LicenceStore::new);
        operator.withLicences(licence);
        licence.getEcmt().setShortTermType(shortTermType);
        operator.setCurrentShortTermType(shortTermType);
        operator.withLicences(licence);

        PeriodSelectionPage.selectShortTermType(shortTermType);
        PeriodSelectionPage.saveAndContinue();
        return this;
    }
    public BasePermitJourney bilateralPeriodType (PeriodType bilateralPeriodType, OperatorStore operator) {
        Optional<LicenceStore> potentialLicence = operator.getLatestLicence();
        LicenceStore licence = potentialLicence.orElseGet(LicenceStore::new);
        operator.withLicences(licence);
        operator.setCurrentBilateralPeriodType(bilateralPeriodType);

        operator.withLicences(licence);
        PeriodSelectionPage.bilateralPeriodType(bilateralPeriodType);
        //PeriodSelectionPage.continueButton();
        return this;
    }

    public BasePermitJourney licencePage(OperatorStore operator, World world) {

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

        fullReferenceNumber = OverviewPage.getReferenceFromPage();

        BasePermitJourney.setReferenceNumber(OverviewPage.getReferenceFromPage());
        return this;
    }


}
