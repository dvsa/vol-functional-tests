package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitFeePage;
import org.dvsa.testing.lib.newPages.permits.pages.RestrictedCountriesPage;
import org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CheckYourAnswersPage;

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

    public ECMTShortTermJourney overviewPage(OverviewSection section) {
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(section);
        return this;
    }

    public ECMTShortTermJourney cabotagePage(World world, LicenceStore licenceStore) {
        boolean cabotage = true;

        CabotagePage.wontCarryCabotage(cabotage);
        CabotagePage.saveAndContinue();
        licenceStore.getEcmt().setCabotage(cabotage);
        return this;
    }

    public ECMTShortTermJourney certificateRequired(boolean certificateRequired) {
        CertificatesRequiredPage.confirmCertificateRequired();
        CertificatesRequiredPage.saveAndContinue();
        return this;
    }

    public ECMTShortTermJourney restrictedCountriesPage(World world, LicenceStore licenceStore) {
        boolean restrictedCountries = false;

        RestrictedCountriesPage.deliverToRestrictedCountry(restrictedCountries);
        RestrictedCountriesPage.saveAndContinue();
        licenceStore.getEcmt().setRestrictedCountries(restrictedCountries);

        return this;
    }

    public ECMTShortTermJourney checkYourAnswersPage() {
        CheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public ECMTShortTermJourney declaration(boolean declaration) {
        DeclarationPage.declare(declaration);
        DeclarationPage.saveAndContinue();
        return this;
    }

    public ECMTShortTermJourney feeOverviewPage() {
        PermitFeePage.saveAndContinue();
        return this;
    }

    @Override
    public ECMTShortTermJourney beginApplication() {
        return (ECMTShortTermJourney) super.beginApplication();
    }

    @Override
    public ECMTShortTermJourney permitType(OperatorStore operatorStore) {
        return (ECMTShortTermJourney) super.permitType(operatorStore);
    }

    @Override
    public ECMTShortTermJourney permitType() {
        return (ECMTShortTermJourney) super.permitType();
    }

    @Override

    public ECMTShortTermJourney yearSelection() {
        return (ECMTShortTermJourney) super.yearSelection();
    }

    @Override
    public ECMTShortTermJourney permitType(PermitType type, OperatorStore operator) {
        return (ECMTShortTermJourney) super.permitType(type, operator);
    }

    @Override
    public ECMTShortTermJourney yearSelection(YearSelectionPage.YearSelection yearSelection, OperatorStore operator) {
        return (ECMTShortTermJourney) super.yearSelection(yearSelection, operator);
    }

    @Override
    public ECMTShortTermJourney licencePage(OperatorStore operator, World world) {
        return (ECMTShortTermJourney) super.licencePage(operator, world);
    }

    @Override
    public ECMTShortTermJourney shortTermType(PeriodType shortTermType, OperatorStore operator) {
        return (ECMTShortTermJourney) super.shortTermType(shortTermType, operator);
    }


    @Override
    public ECMTShortTermJourney cardDetailsPage() {
        return (ECMTShortTermJourney) PaymentJourney.super.cardDetailsPage();
    }

    @Override
    public ECMTShortTermJourney cardHolderDetailsPage() {
        return (ECMTShortTermJourney) PaymentJourney.super.cardHolderDetailsPage();
    }

    @Override
    public ECMTShortTermJourney confirmAndPay() {
        return (ECMTShortTermJourney) PaymentJourney.super.confirmAndPay();
    }

    public ECMTShortTermJourney submitApplication(LicenceStore licenceStore, World world) {
        licenceStore.setReferenceNumber(SubmittedPage.getReferenceNumber());
        SubmittedPage.goToPermitsDashboard();

        LocalDateTime date = LocalDateTime.now();
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));

        licenceStore.getEcmt().setSubmitDate(date);

        return this;
    }

}