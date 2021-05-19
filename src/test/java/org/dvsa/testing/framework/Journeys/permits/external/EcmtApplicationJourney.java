package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.FeeOverviewPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;

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

    public EcmtApplicationJourney overviewPage(PermitSection section) {
       // OverviewPage.untilOnPage();
        OverviewPage.section(section);
        return this;
    }

    public EcmtApplicationJourney euro6Page(LicenceStore licenceStore) {
        boolean euro6 = true;

        VehicleStandardPage.isEuro6Compliant(euro6);
        VehicleStandardPage.saveAndContinue();
        licenceStore.getEcmt().setEuro6(euro6);
        return this;
    }

    public EcmtApplicationJourney cabotagePage(LicenceStore licenceStore) {
        boolean cabotage = true;

        CabotagePage.wontCarryCabotage(cabotage);
        CabotagePage.saveAndContinue();
        licenceStore.getEcmt().setCabotage(cabotage);
        return this;
    }

    public EcmtApplicationJourney numberOfPermitsPage(int maxNumberOfPermits, OperatorStore operatorStore) {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        licence.getEcmt().setNumberOfPermits(NumberOfPermitsPage.quantity(maxNumberOfPermits));
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney numberOfPermitsPage(OperatorStore operatorStore) {
        int authVehicles = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).getNumberOfAuthorisedVehicles();
        numberOfPermitsPage(operatorStore);
        numberOfPermitsPage(authVehicles,operatorStore);
        operatorStore.getCurrentLicence().get().getEcmt().setSubmitDate(LocalDateTime.now());
        return this;
    }

    public EcmtApplicationJourney checkYourAnswersPage() {
        EcmtApplicationJourney.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney declaration(boolean declaration) {
        DeclarationPage.declare(declaration);
        DeclarationPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney feeOverviewPage() {
        FeeOverviewPage.saveAndContinue();
        return this;
    }

    @Override
    public EcmtApplicationJourney beginApplication() {
        return (EcmtApplicationJourney) super.beginApplication();
    }

    @Override
    public EcmtApplicationJourney permitType(OperatorStore operatorStore) {
        return (EcmtApplicationJourney) super.permitType(operatorStore);
    }

    @Override
    public EcmtApplicationJourney permitType() {
        return (EcmtApplicationJourney) super.permitType();
    }

    @Override

    public EcmtApplicationJourney yearSelection() {
        return (EcmtApplicationJourney) super.yearSelection();
    }

    @Override
    public EcmtApplicationJourney permitType(PermitTypePage.PermitType type, OperatorStore operator) {
        return (EcmtApplicationJourney) super.permitType(type, operator);
    }

    @Override
    public EcmtApplicationJourney yearSelection(YearSelectionPage.YearSelection yearSelection, OperatorStore operator) {
        return (EcmtApplicationJourney) super.yearSelection(yearSelection, operator);
    }

    @Override
    public EcmtApplicationJourney licencePage(OperatorStore operator, World world) {
        return (EcmtApplicationJourney) super.licencePage(operator, world);
    }

    @Override
    public EcmtApplicationJourney cardDetailsPage() {
        return (EcmtApplicationJourney) PaymentJourney.super.cardDetailsPage();
    }

    @Override
    public EcmtApplicationJourney cardHolderDetailsPage() {
        return (EcmtApplicationJourney) PaymentJourney.super.cardHolderDetailsPage();
    }

    @Override
    public EcmtApplicationJourney confirmAndPay() {
        return (EcmtApplicationJourney) PaymentJourney.super.confirmAndPay();
    }

    @Override
    public EcmtApplicationJourney passwordAuthorisation() {
        return (EcmtApplicationJourney) PaymentJourney.super.passwordAuthorisation();
    }

    public EcmtApplicationJourney submitApplication(LicenceStore licenceStore, World world) {
        licenceStore.setReferenceNumber(ApplicationSubmitPage.getReferenceNumber().substring(22,40));
        ApplicationSubmitPage.finish();

        LocalDateTime date = LocalDateTime.now();
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));

        licenceStore.getEcmt().setSubmitDate(date);

        return this;
    }

}