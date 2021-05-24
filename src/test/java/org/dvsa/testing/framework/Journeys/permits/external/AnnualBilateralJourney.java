package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.CountrySelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.BaseDeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;

import org.junit.Assert;

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

        List<Country> name = RestrictedCountriesPage.randomCountries();
        RestrictedCountriesPage.saveAndContinue();
        licence.getEcmt().setRestrictedCountries(name);
        return this;
    }

    public AnnualBilateralJourney allCountries(OperatorStore operatorStore){
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        List<Country> name = RestrictedCountriesPage.allCountries();
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
    public AnnualBilateralJourney overview(OverviewSection section, OperatorStore operatorStore) {
        String reference = BasePermitPage.getReferenceFromPage();
        OverviewPage.untilOnOverviewPage();
        Assert.assertTrue(operatorStore.hasLicence(reference));
        operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).setReferenceNumber(reference);
        OverviewPage.select(section);
        return this;
    }

    public AnnualBilateralJourney overviewNorway(OperatorStore operatorStore) {
        operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        OverviewPage.sectionNorway();
        return this;
    }

    public AnnualBilateralJourney country(World world, OperatorStore operatorStore) {
        String country = CountrySelectionPage.selectCountry("Norway");
        RestrictedCountriesPage.saveAndContinue();
        operatorStore.setCountry1(country);
        return this;
    }

    public AnnualBilateralJourney numberOfPermits(OperatorStore operatorStore){
        LicenceStore licenceStore =
                operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        licenceStore.getEcmt().setPermitsPerCountry(org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.quantity(1, PermitType.ANNUAL_BILATERAL)); // To pass test
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney checkYourAnswers(){
        CheckYourAnswerPage.untilOnPage();
        CheckYourAnswerPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney declare(boolean declaration){
        DeclarationPage.untilOnPage();
        BaseDeclarationPage.declare(declaration);
        DeclarationPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney declare(){
        return declare(true);
    }

    public AnnualBilateralJourney permitFee() {
        PermitFeePage.untilOnPage();
        PermitFeePage.submitAndPay();
        return this;
    }

}
