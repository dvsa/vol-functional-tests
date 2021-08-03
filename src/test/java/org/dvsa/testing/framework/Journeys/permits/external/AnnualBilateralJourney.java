package org.dvsa.testing.framework.Journeys.permits.external;

import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.CountrySelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.RestrictedCountriesPage;

public class AnnualBilateralJourney extends BasePermitJourney {

    private static volatile AnnualBilateralJourney instance = null;
    private String bilateralCountry;
    private static PeriodType periodType;


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

    public void setBilateralCountry(String bilateralCountry) {
        this.bilateralCountry = bilateralCountry;
    }

    public String getBilateralCountry() {
        return bilateralCountry;
    }

    public static void setPeriodType(PeriodType periodType) {
        AnnualBilateralJourney.periodType = periodType;
    }

    public static PeriodType getPeriodType() {
        return periodType;
    }

    public AnnualBilateralJourney selectCountry(String country){
        String countryTitle = CountrySelectionPage.selectCountry(country);;
        setBilateralCountry(countryTitle);
        return this;
    }

    public AnnualBilateralJourney journeyType(){
        JourneyType journeyType = JourneyType.random();
        PermitUsagePage.journeyType(journeyType);
        RestrictedCountriesPage.saveAndContinue();
        return this;
    }

    public AnnualBilateralJourney permitFee() {
        PermitFeePage.untilOnPage();
        PermitFeePage.submitAndPay();
        return this;
    }

}
