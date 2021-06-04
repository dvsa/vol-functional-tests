package org.dvsa.testing.framework.Journeys.permits.external;


import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitFeePage;
import org.dvsa.testing.lib.pages.external.permit.BaseDeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.*;
import org.junit.Assert;

public class EcmtInternationalRemovalJourney extends BasePermitJourney {

    public static volatile EcmtInternationalRemovalJourney instance = new EcmtInternationalRemovalJourney();

    public static EcmtInternationalRemovalJourney getInstance(){
        if (instance == null) {
            synchronized (EcmtInternationalRemovalJourney.class){
                instance = new EcmtInternationalRemovalJourney();
            }
        }

        return instance;
    }

    public EcmtInternationalRemovalJourney overview(OverviewSection section, OperatorStore operatorStore) {
        String reference = BasePermitPage.getReferenceFromPage();
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
        Assert.assertTrue(operatorStore.hasLicence(reference));
        operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).setReferenceNumber(reference);
       org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(section);
        return this;
    }

    public EcmtInternationalRemovalJourney numberOfPermits(OperatorStore operatorStore) {
        LicenceStore licenceStore =
                operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        licenceStore.getEcmt().setPermitsPerCountry(NumberOfPermitsPage.quantity(licenceStore.getNumberOfAuthorisedVehicles(), PermitType.ANNUAL_BILATERAL));
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney checkYourAnswers() {
        CheckYourAnswerPage.untilOnPage();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney declaration() {
        Declaration.DeclarationConfirmation();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney removalsEligibility(boolean declaration) {
       RemovalsEligibilityPage.declare(declaration);
       BasePermitPage.saveAndContinue();
       return this;
    }

    public EcmtInternationalRemovalJourney cabotagePage() {
        CabotagePage.confirmCabotage();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney certificatesRequiredPage() {
        org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.confirmCertificateRequired();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney permitStartDatePage() {
        PermitStartDatePage.permitDate();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney numberOfPermits() {
        org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
        saveAndContinue();
        return this;
    }


    public EcmtInternationalRemovalJourney declare(boolean declaration) {
        BaseDeclarationPage.declare(declaration);
        BaseDeclarationPage.saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney declare() {
        return declare(true);
    }

    public EcmtInternationalRemovalJourney permitFee() {
        PermitFeePage.untilOnPage();
        PermitFeePage.submitAndPay();
        return this;
    }

}