package org.dvsa.testing.framework.Journeys.permits.external;


import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.BaseCheckYourAnswersPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CheckYourAnswersPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.DeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PermitFeePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.*;
import org.junit.Assert;

import java.net.MalformedURLException;

import static org.dvsa.testing.lib.pages.external.permit.BasePermitPage.saveAndContinue;

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
    public EcmtInternationalRemovalJourney overview(org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.Section section, OperatorStore operatorStore) {
        String reference = BasePermitPage.getReference();
        org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.untilOnPage();
        Assert.assertTrue(operatorStore.hasLicence(reference));
        operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).setReferenceNumber(reference);
       OverviewPage.select(section);
        return this;
    }

    public EcmtInternationalRemovalJourney numberOfPermits(OperatorStore operatorStore) {
        LicenceStore licenceStore =
                operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        NumberOfPermitsPage.untilOnPage();
        licenceStore.getEcmt().setPermitsPerCountry(NumberOfPermitsPage.quantity(licenceStore.getNumberOfAuthorisedVehicles()));
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney checkYourAnswers() {
        CheckYourAnswersPage.untilOnPage();
        BaseCheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney declaration() {
        Declaration.DeclarationConfirmation();
        DeclarationPage.acceptAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney removalsEligibility(boolean declaration) {
       RemovalsEligibilityPage.declare(declaration);
       BasePermitPage.saveAndContinue();
       return this;
    }

    public EcmtInternationalRemovalJourney cabotagePage() {
        CabotagePage.cabotageConfirmation();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney certificatesRequiredPage() {
        CertificatesRequiredPage.CertificateRequiredConfirmation();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney permitStartDatePage() {
        PermitStartDatePage.permitDate();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney numberOfPermits() {
        NumberofPermitsPage.numberOfPermits();
        saveAndContinue();
        return this;
    }


    public EcmtInternationalRemovalJourney declare(boolean declaration) {
        DeclarationPage.declare(declaration);
        DeclarationPage.acceptAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney declare() {
        return declare(true);
    }

    public EcmtInternationalRemovalJourney permitFee() {
        PermitFeePage.untilOnPage();
        org.dvsa.testing.lib.pages.external.permit.bilateral.PermitFeePage.submitAndPay();
        return this;
    }

}