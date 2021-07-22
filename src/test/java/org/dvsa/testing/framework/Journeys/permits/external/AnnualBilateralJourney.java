package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;

import java.util.List;

public class AnnualBilateralJourney extends BasePermitJourney {

    private static volatile AnnualBilateralJourney instance = null;


    protected AnnualBilateralJourney(){
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static AnnualBilateralJourney getInstance(){
        if (instance == null) {
            synchronized (AnnualBilateralJourney.class){
                instance = new AnnualBilateralJourney();
            }
        }

        return instance;
    }

    public AnnualBilateralJourney countries(OperatorStore operatorStore) {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        CountrySelectionPage.randomCountries();
        RestrictedCountriesPage.saveAndContinue();
        licence.getEcmt().setRestrictedCountries(true);
        return this;
    }

    public AnnualBilateralJourney selectCountry(OperatorStore operatorStore, String country){
        String countryTitle = CountrySelectionPage.selectCountry(country);;
        operatorStore.setCountry(countryTitle);
        return this;
    }

    public AnnualBilateralJourney journeyType(World world, LicenceStore licenceStore){
        JourneyType journeyType = JourneyType.random();
        PermitUsagePage.journeyType(journeyType);
        RestrictedCountriesPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney checkYourAnswers(){
        CheckYourAnswerPage.untilOnPage();
        CheckYourAnswerPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney permitFee() {
        PermitFeePage.untilOnPage();
        PermitFeePage.submitAndPay();
        return this;
    }

}
