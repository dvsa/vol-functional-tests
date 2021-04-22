package org.dvsa.testing.framework.Journeys.permits.external;

import activesupport.IllegalBrowserException;
import activesupport.number.Int;
import activesupport.string.Str;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.FeeOverviewPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PeriodSelectionPageOne;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ECMTShortTermJourney extends BasePermitJourney implements PaymentJourney {

    private static volatile ECMTShortTermJourney instance = null;

    protected ECMTShortTermJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static ECMTShortTermJourney getInstance() {
        if (instance == null) {
            synchronized (ECMTShortTermJourney.class) {
                instance = new ECMTShortTermJourney();
            }
        }

        return instance;
    }

    public ECMTShortTermJourney overviewPage(PermitSection section) throws MalformedURLException, IllegalBrowserException {
        OverviewPage.untilOnPage();
        OverviewPage.section(section);
        return this;
    }

    public ECMTShortTermJourney euro6Page(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        boolean euro6 = true;

        VehicleStandardPage.isEuro6Compliant(euro6);
        VehicleStandardPage.saveAndContinue();
        world.put("euro6", euro6);
        licenceStore.getEcmt().setEuro6(euro6);
        return this;
    }

    public ECMTShortTermJourney cabotagePage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        boolean cabotage = true;

        CabotagePage.wontCarryCabotage(cabotage);
        CabotagePage.saveAndContinue();
        world.put("cabotage", cabotage);
        licenceStore.getEcmt().setCabotage(cabotage);
        return this;
    }

    public ECMTShortTermJourney certificateRequired(boolean certificateRequired) throws MalformedURLException, IllegalBrowserException {
        CertificatesRequiredPage.certificatesRequired(certificateRequired);
        CertificatesRequiredPage.saveAndContinue();
        return this;
    }

    public ECMTShortTermJourney restrictedCountriesPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        boolean restrictedCountries = false;

        RestrictedCountriesPage.deliverToRestrictedCountry(restrictedCountries);
        RestrictedCountriesPage.saveAndContinue();
        world.put("restricted.countries", restrictedCountries);
        licenceStore.getEcmt().setRestrictedCountries(restrictedCountries);

        return this;
    }

    public ECMTShortTermJourney numberOfPermitsPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        int authorisedVehicles = licenceStore.getEcmt().getNumberOfPermits();

        int numOfPermits = Int.random(1, authorisedVehicles);

        NumberOfPermitsPage.quantity(numOfPermits);
        NumberOfPermitsPage.saveAndContinue();
        world.put("numberOfAuthorisedVehicles", numOfPermits);
        licenceStore.getEcmt().setNumberOfPermits(numOfPermits);

        return this;
    }

    public ECMTShortTermJourney numberOfTripsPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        int numberOfTrips = Integer.parseInt(Str.randomNumbers(5));
        int numberOfPermits = licenceStore.getNumberOfAuthorisedVehicles();

        NumberOfTripsPage.quantity(numberOfTrips);
        NumberOfTripsPage.saveAndContinue();

        if (numberOfTrips / numberOfPermits > 100) {
            NumberOfTripsPage.saveAndContinue();
        }

        world.put("number.of.trips", numberOfTrips);
        licenceStore.getEcmt().setNumberOfTrips(numberOfTrips);

        return this;
    }

    public ECMTShortTermJourney internationalBusinessPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        JourneyProportion journeyProportion = JourneyProportion.random();
        PercentageOfInternationalJourneysPage.proportion(journeyProportion);
        PercentageOfInternationalJourneysPage.saveAndContinue();

        if (journeyProportion == JourneyProportion.MoreThan90Percent) {
            PercentageOfInternationalJourneysPage.saveAndContinue();
        }

        world.put("journey.proportion", journeyProportion);
        licenceStore.getEcmt().setInternationalBusiness(journeyProportion);

        return this;
    }

    public ECMTShortTermJourney sectorPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        Sector sector = Sector.random();

        SectorPage.sector(sector);
        SectorPage.saveAndContinue();

        world.put("sector", sector);
        licenceStore.getEcmt().setSector(sector);

        return this;
    }

    public ECMTShortTermJourney checkYourAnswersPage() throws MalformedURLException, IllegalBrowserException {
        CheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public ECMTShortTermJourney declaration(boolean declaration) throws MalformedURLException, IllegalBrowserException {
        DeclarationPage.declare(declaration);
        DeclarationPage.saveAndContinue();
        return this;
    }

    public ECMTShortTermJourney feeOverviewPage() throws MalformedURLException, IllegalBrowserException {
        FeeOverviewPage.saveAndContinue();
        return this;
    }

    @Override
    public ECMTShortTermJourney beginApplication() throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.beginApplication();
    }

    @Override
    public ECMTShortTermJourney permitType(OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.permitType(operatorStore);
    }

    @Override
    public ECMTShortTermJourney permitType() throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.permitType();
    }

    @Override

    public ECMTShortTermJourney yearSelection() throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.yearSelection();
    }

    @Override
    public ECMTShortTermJourney permitType(PermitTypePage.PermitType type, OperatorStore operator) throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.permitType(type, operator);
    }

    @Override
    public ECMTShortTermJourney yearSelection(YearSelectionPage.YearSelection yearSelection, OperatorStore operator) throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.yearSelection(yearSelection, operator);
    }

    @Override
    public ECMTShortTermJourney licencePage(OperatorStore operator, World world) throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.licencePage(operator, world);
    }

    @Override
    public ECMTShortTermJourney shortTermType(PeriodSelectionPageOne.ShortTermType shortTermType, OperatorStore operator) throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) super.shortTermType(shortTermType, operator);
    }


    @Override
    public ECMTShortTermJourney cardDetailsPage() throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) PaymentJourney.super.cardDetailsPage();
    }

    @Override
    public ECMTShortTermJourney cardHolderDetailsPage() throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) PaymentJourney.super.cardHolderDetailsPage();
    }

    @Override
    public ECMTShortTermJourney confirmAndPay() throws MalformedURLException, IllegalBrowserException {
        return (ECMTShortTermJourney) PaymentJourney.super.confirmAndPay();
    }

    public ECMTShortTermJourney submitApplication(LicenceStore licenceStore, World world) throws MalformedURLException, IllegalBrowserException {
        licenceStore.setReferenceNumber(ApplicationSubmitPage.getReferenceNumber());
        world.put("referenceNumber", licenceStore.getReferenceNumber());
        ApplicationSubmitPage.finish();

        LocalDateTime date = LocalDateTime.now();
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));

        licenceStore.getEcmt().setSubmitDate(date);
        world.put("application.date", date);
        world.put("application.date.formatted", dateFormatted);

        return this;
    }

}