package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import activesupport.number.Int;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


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

    public EcmtApplicationJourney cabotagePage(LicenceStore licenceStore) {

        CabotagePage.confirmWontUndertakeCabotage();
        CabotagePage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney numberOfPermitsPage(int maxNumberOfPermits, OperatorStore operatorStore) {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        List<WebElement> numberOfPermitFields = findAll("//*[contains(@class, 'field')]//input[@type='number']", SelectorType.XPATH);
        numberOfPermitFields.forEach(numberOfPermitsField -> {
            Integer randomNumberOfPermitsLessThanMax = Int.random(maxNumberOfPermits);
                    numberOfPermitsField.sendKeys(String.valueOf(randomNumberOfPermitsLessThanMax));
        });
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney numberOfPermitsPage(OperatorStore operatorStore) {
        int authVehicles = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).getNumberOfAuthorisedVehicles();
        numberOfPermitsPage(authVehicles,operatorStore);
        operatorStore.getCurrentLicence().get().getEcmt().setSubmitDate(LocalDateTime.now());
        return this;
    }

    public EcmtApplicationJourney checkYourAnswersPage() {
        EcmtApplicationJourney.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney declaration(boolean declaration) {
        DeclarationPage.confirmDeclaration();
        DeclarationPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney feeOverviewPage() {
        PermitFeePage.saveAndContinue();
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
    public EcmtApplicationJourney permitType(PermitType type, OperatorStore operator) {
        return (EcmtApplicationJourney) super.permitType(type, operator);
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
        licenceStore.setReferenceNumber(SubmittedPage.getReferenceNumber().substring(22,40));
        SubmittedPage.goToPermitsDashboard();

        LocalDateTime date = LocalDateTime.now();
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));

        licenceStore.getEcmt().setSubmitDate(date);

        return this;
    }

}