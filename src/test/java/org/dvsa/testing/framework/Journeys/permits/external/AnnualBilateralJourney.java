package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;

import java.util.List;

import static org.junit.Assert.assertEquals;

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

        List<Country> name = CountrySelectionPage.randomCountries();
        RestrictedCountriesPage.saveAndContinue();
        licence.getEcmt().setRestrictedCountries(name);
        return this;
    }

    public AnnualBilateralJourney allCountries(OperatorStore operatorStore){
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        List<Country> name = CountrySelectionPage.allCountries();
        BasePermitPage.waitAndClick("//input[@id='Submit[SubmitButton]']", SelectorType.XPATH);
        licence.getEcmt().setRestrictedCountries(name);
        return this;
    }

    public AnnualBilateralJourney norway(OperatorStore operatorStore){
        String country = CountrySelectionPage.selectCountry("Norway");;
        operatorStore.setCountry(country);
        return this;
    }

    public AnnualBilateralJourney turkey(OperatorStore operatorStore) {
        String country = CountrySelectionPage.selectCountry("Turkey");
        operatorStore.setCountry(country);
        return this;
    }

    public AnnualBilateralJourney morocco(OperatorStore operatorStore) {
        String country = CountrySelectionPage.selectCountry("Morocco");
        operatorStore.setCountry(country);
        return this;
    }

    public AnnualBilateralJourney ukraine(OperatorStore operatorStore) {
        String country = CountrySelectionPage.selectCountry("Ukraine");
        operatorStore.setCountry(country);
        return this;
    }

    public AnnualBilateralJourney journeyType(World world, LicenceStore licenceStore){
        JourneyType journeyType = JourneyType.random();
        PermitUsagePage.journeyType(journeyType);
        RestrictedCountriesPage.saveAndContinue();
        licenceStore.getEcmt().setJourneyType(String.valueOf(journeyType));
        return this;
    }

    public AnnualBilateralJourney cabotageConfirmation(World world, LicenceStore licenceStore){
        String noCabotage  = BilateralJourneySteps.yesAndCabotagePermitConfirmation();
        RestrictedCountriesPage.saveAndContinue();
        licenceStore.getEcmt().setNoCabotage(String.valueOf(noCabotage));
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
