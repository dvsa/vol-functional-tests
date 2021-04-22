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
    public EcmtInternationalRemovalJourney overview(org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.Section section, OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        String reference = org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.reference();
        org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.untilOnPage();
        Assert.assertTrue(operatorStore.hasLicence(reference));
        operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).setReferenceNumber(reference);
       OverviewPage.select(section);
        return this;
    }

    public EcmtInternationalRemovalJourney numberOfPermits(OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        LicenceStore licenceStore =
                operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);

        NumberOfPermitsPage.untilOnPage();
        licenceStore.getEcmt().setPermitsPerCountry(NumberOfPermitsPage.quantity(licenceStore.getNumberOfAuthorisedVehicles()));
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney checkYourAnswers() throws MalformedURLException, IllegalBrowserException {
        CheckYourAnswersPage.untilOnPage();
        BaseCheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney declaration() throws MalformedURLException, IllegalBrowserException {
        Declaration.DeclarationConfirmation();
        DeclarationPage.acceptAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney removalsEligibility(boolean declaration) throws MalformedURLException, IllegalBrowserException {
       RemovalsEligibilityPage.declare(declaration);
       BasePermitPage.saveAndContinue();
       return this;
    }

    public EcmtInternationalRemovalJourney cabotagePage() throws MalformedURLException, IllegalBrowserException {
        CabotagePage.cabotageConfirmation();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney certificatesRequiredPage() throws MalformedURLException, IllegalBrowserException {
        CertificatesRequiredPage.CertificateRequiredConfirmation();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney permitStartDatePage() throws MalformedURLException, IllegalBrowserException {
        PermitStartDatePage.permitDate();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney numberOfPermits() throws MalformedURLException, IllegalBrowserException {
        NumberofPermitsPage.numberOfPermits();
        saveAndContinue();
        return this;
    }


    public EcmtInternationalRemovalJourney declare(boolean declaration) throws MalformedURLException, IllegalBrowserException {
        DeclarationPage.declare(declaration);
        DeclarationPage.acceptAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney declare() throws MalformedURLException, IllegalBrowserException {
        return declare(true);
    }

    public EcmtInternationalRemovalJourney permitFee() throws MalformedURLException, IllegalBrowserException {
        PermitFeePage.untilOnPage();
        org.dvsa.testing.lib.pages.external.permit.bilateral.PermitFeePage.submitAndPay();
        return this;
    }

}