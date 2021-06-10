package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.permits.pages.LicencePage;
import org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.YearSelectionPage;

import java.util.Optional;
import java.util.stream.Collectors;

public class BasePermitJourney extends BaseJourney {
    protected static volatile BasePermitJourney instance = null;

    protected BasePermitJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static BasePermitJourney getInstance() {
        if (instance == null) {
            synchronized (BasePermitJourney.class) {
                instance = new BasePermitJourney();
            }
        }

        return instance;
    }

    public BasePermitJourney beginApplication()  {
        HomePage.selectTab(Tab.PERMITS);
        HomePage.applyForLicenceButton();
        return this;
    }

    public BasePermitJourney permitType(OperatorStore operatorStore) {
        return permitType(PermitType.ECMT_ANNUAL, operatorStore);
    }

    public BasePermitJourney permitType() {
        return permitType(PermitType.ECMT_ANNUAL, new OperatorStore());
    }

    public BasePermitJourney yearSelection() {
        return yearSelection(YearSelectionPage.YearSelection.YEAR_2019, new OperatorStore());
    }

    public BasePermitJourney permitType(PermitType type, OperatorStore operator) {
        Optional<LicenceStore> potentialLicence = operator.getLatestLicence();
        LicenceStore licence = potentialLicence.orElseGet(LicenceStore::new);
        operator.withLicences(licence);

        // TODO: Remove next line below and update code dependent on it
        licence.getEcmt().setType(type);
        operator.setCurrentPermitType(type);
        operator.withLicences(licence);

        PermitTypePage.type(type);
        PermitTypePage.continueButton();
        return this;
    }

    public BasePermitJourney shortTermType(PeriodType shortTermType, OperatorStore operator) {
        Optional<LicenceStore> potentialLicence = operator.getLatestLicence();
        LicenceStore licence = potentialLicence.orElseGet(LicenceStore::new);
        operator.withLicences(licence);
        licence.getEcmt().setShortTermType(shortTermType);
        operator.setCurrentShortTermType(shortTermType);
        operator.withLicences(licence);

        PeriodSelectionPage.shortTermType(shortTermType);
        PeriodSelectionPage.saveAndContinue();
        return this;
    }
    public BasePermitJourney bilateralPeriodType (PeriodType bilateralPeriodType, OperatorStore operator) {
        Optional<LicenceStore> potentialLicence = operator.getLatestLicence();
        LicenceStore licence = potentialLicence.orElseGet(LicenceStore::new);
        operator.withLicences(licence);
        operator.setCurrentBilateralPeriodType(bilateralPeriodType);

        operator.withLicences(licence);
        org.dvsa.testing.lib.newPages.permits.pages.PeriodSelectionPage.bilateralPeriodType(bilateralPeriodType);
        //PeriodSelectionPage.continueButton();
        return this;
    }
    public BasePermitJourney yearSelection(YearSelectionPage.YearSelection yearSelection, OperatorStore operator){
        Optional<LicenceStore> potentialLicence = operator.getLatestLicence();
        LicenceStore licence = potentialLicence.orElseGet(LicenceStore::new);
        operator.withLicences(licence);
        licence.getEcmt().setYear(yearSelection);
        operator.setCurrentYearSelection(yearSelection);
        operator.withLicences(licence);

        YearSelectionPage.type(yearSelection);
        YearSelectionPage.continueButton();
        return this;
    }

    public BasePermitJourney licencePage(OperatorStore operator, World world) {
        LicenceStore selectedLicence;
        String licenceNumber;

        if (operator.getCurrentLicenceNumber().isPresent()) {
            selectedLicence = operator.getLicences().stream().filter(l -> !l.getLicenceNumber().equals(operator.getCurrentLicenceNumber())).collect(Collectors.toList()).get(0);
        } else {
            selectedLicence = operator.randomLicence();
        }

        if (selectedLicence.getEcmt().hasType(PermitType.ANNUAL_BILATERAL) ||
                operator.hasCurrentPermitType(PermitType.ANNUAL_MULTILATERAL) ||
                operator.hasCurrentPermitType(PermitType.SHORT_TERM_ECMT) ||
                operator.hasCurrentPermitType(PermitType.ECMT_INTERNATIONAL_REMOVAL)) {
            LicencePage.licence(world.applicationDetails.getLicenceNumber());
            licenceNumber = selectedLicence.getLicenceNumber();
        }

        else if (LicencePage.numberOfLicences() > 1) {
            LicencePage.licence(selectedLicence.getLicenceNumber());
            licenceNumber = selectedLicence.getLicenceNumber();
        }

        else {
            licenceNumber = world.applicationDetails.getLicenceNumber();
            LicencePage.licence(licenceNumber);
        }

        operator.setCurrentLicenceNumber(licenceNumber);

        LicencePage.saveAndContinue();

        return this;
    }


}
