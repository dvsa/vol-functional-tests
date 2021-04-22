package org.dvsa.testing.framework.Journeys.permits.external;

import activesupport.IllegalBrowserException;
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

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EcmtApplicationJourney extends BasePermitJourney implements PaymentJourney {

    private static volatile EcmtApplicationJourney instance = null;

    protected EcmtApplicationJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static EcmtApplicationJourney getInstance() {
        if (instance == null) {
            synchronized (EcmtApplicationJourney.class) {
                instance = new EcmtApplicationJourney();
            }
        }

        return instance;
    }

    public EcmtApplicationJourney overviewPage(PermitSection section) throws MalformedURLException, IllegalBrowserException {
       // OverviewPage.untilOnPage();
        OverviewPage.section(section);
        return this;
    }

    public EcmtApplicationJourney euro6Page(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        boolean euro6 = true;

        VehicleStandardPage.isEuro6Compliant(euro6);
        VehicleStandardPage.saveAndContinue();
        world.put("euro6", euro6);
        licenceStore.getEcmt().setEuro6(euro6);
        return this;
    }

    public EcmtApplicationJourney cabotagePage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        boolean cabotage = true;

        CabotagePage.wontCarryCabotage(cabotage);
        CabotagePage.saveAndContinue();
        world.put("cabotage", cabotage);
        licenceStore.getEcmt().setCabotage(cabotage);
        return this;
    }

    public EcmtApplicationJourney certificateRequired(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        boolean certificatesRequired = true;
        CertificatesRequiredPage.certificatesRequired(certificatesRequired);
        CertificatesRequiredPage.saveAndContinue();
        world.put("certificates.required",certificatesRequired);
        licenceStore.getEcmt().setCertificatesRequired(certificatesRequired);
        return this;
    }

    public EcmtApplicationJourney restrictedCountriesPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        boolean restrictedCountries = false;

        RestrictedCountriesPage.deliverToRestrictedCountry(restrictedCountries);
        RestrictedCountriesPage.saveAndContinue();
        world.put("restricted.countries", restrictedCountries);
        licenceStore.getEcmt().setRestrictedCountries(restrictedCountries);

        return this;
    }

    public EcmtApplicationJourney numberOfPermitsPage(int maxNumberOfPermits, OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        licence.getEcmt().setNumberOfPermits(NumberOfPermitsPage.quantity(maxNumberOfPermits));
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney numberOfPermitsPage(OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        int authVehicles = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).getNumberOfAuthorisedVehicles();
        numberOfPermitsPage(operatorStore);
        numberOfPermitsPage(authVehicles,operatorStore);
        operatorStore.getCurrentLicence().get().getEcmt().setSubmitDate(LocalDateTime.now());
        return this;
    }


    public EcmtApplicationJourney numberOfTripsPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        int numberOfTrips = Integer.parseInt(Str.randomNumbers(5));
        int numberOfPermits = licenceStore.getNumberOfAuthorisedVehicles();

        NumberOfTripsPage.quantity(numberOfTrips);
        NumberOfTripsPage.saveAndContinue();

        if (numberOfTrips / numberOfPermits > 100) {
            NumberOfTripsPage.hasIntensityMessage();
            NumberOfTripsPage.saveAndContinue();
        }

        world.put("number.of.trips", numberOfTrips);
        licenceStore.getEcmt().setNumberOfTrips(numberOfTrips);

        return this;
    }

    public EcmtApplicationJourney internationalBusinessPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
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

    public EcmtApplicationJourney sectorPage(World world, LicenceStore licenceStore) throws MalformedURLException, IllegalBrowserException {
        Sector sector = Sector.random();

        SectorPage.sector(sector);
      //  SectorPage.saveAndContinue();

        world.put("sector", sector);
        licenceStore.getEcmt().setSector(sector);

        return this;
    }

    public EcmtApplicationJourney checkYourAnswersPage() throws MalformedURLException, IllegalBrowserException {
        CheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney declaration(boolean declaration) throws MalformedURLException, IllegalBrowserException {
        DeclarationPage.declare(declaration);
        DeclarationPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney feeOverviewPage() throws MalformedURLException, IllegalBrowserException {
        FeeOverviewPage.saveAndContinue();
        return this;
    }

    @Override
    public EcmtApplicationJourney beginApplication() throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) super.beginApplication();
    }

    @Override
    public EcmtApplicationJourney permitType(OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) super.permitType(operatorStore);
    }

    @Override
    public EcmtApplicationJourney permitType() throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) super.permitType();
    }

    @Override

    public EcmtApplicationJourney yearSelection() throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) super.yearSelection();
    }

    @Override
    public EcmtApplicationJourney permitType(PermitTypePage.PermitType type, OperatorStore operator) throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) super.permitType(type, operator);
    }

    @Override
    public EcmtApplicationJourney yearSelection(YearSelectionPage.YearSelection yearSelection, OperatorStore operator) throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) super.yearSelection(yearSelection, operator);
    }

    @Override
    public EcmtApplicationJourney licencePage(OperatorStore operator, World world) throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) super.licencePage(operator, world);
    }

    @Override
    public EcmtApplicationJourney cardDetailsPage() throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) PaymentJourney.super.cardDetailsPage();
    }

    @Override
    public EcmtApplicationJourney cardHolderDetailsPage() throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) PaymentJourney.super.cardHolderDetailsPage();
    }

    @Override
    public EcmtApplicationJourney confirmAndPay() throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) PaymentJourney.super.confirmAndPay();
    }

    @Override
    public EcmtApplicationJourney passwordAuthorisation() throws MalformedURLException, IllegalBrowserException {
        return (EcmtApplicationJourney) PaymentJourney.super.passwordAuthorisation();
    }

    public EcmtApplicationJourney submitApplication(LicenceStore licenceStore, World world) throws MalformedURLException, IllegalBrowserException {
        licenceStore.setReferenceNumber(ApplicationSubmitPage.getReferenceNumber().substring(22,40));
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