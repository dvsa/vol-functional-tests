package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
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
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        String country  = CountrySelectionPage.norwaySelection();
        operatorStore.setCountry(country);
        return this;

    }
    public AnnualBilateralJourney turkey(OperatorStore operatorStore) {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        String country = CountrySelectionPage.turkeySelection();
        operatorStore.setCountry(country);
        return this;
    }
    public AnnualBilateralJourney morocco(OperatorStore operatorStore) {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        String country = CountrySelectionPage.moroccoSelection();
        operatorStore.setCountry(country);
        return this;
    }

    public AnnualBilateralJourney ukraine(OperatorStore operatorStore) {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        String country = CountrySelectionPage.ukraineSelection();
        operatorStore.setCountry(country);
        return this;
    }
    public AnnualBilateralJourney permit(OperatorStore operatorStore){
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        String permitname  = NumberOfPermitsPage.permitLabel();
        operatorStore.setPermit(permitname);
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
    public AnnualBilateralJourney overview(OverviewPage.Section section, OperatorStore operatorStore) {
        String reference = BasePermitPage.getReference();
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
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        String country = CountrySelectionPage.norwaySelection();
        RestrictedCountriesPage.saveAndContinue();
        operatorStore.setCountry1(country);
        return this;
    }

    public AnnualBilateralJourney numberOfPermits(OperatorStore operatorStore){
        LicenceStore licenceStore =
                operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        NumberOfPermitsPage.untilOnPage();
        licenceStore.getEcmt().setPermitsPerCountry(NumberOfPermitsPage.quantity(1)); // To pass test
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney checkYourAnswers(){
        CheckYourAnswersPage.untilOnPage();
        CheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney declare(boolean declaration){
        DeclarationPage.untilOnPage();
        DeclarationPage.declare(declaration);
        DeclarationPage.acceptAndContinue();
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
