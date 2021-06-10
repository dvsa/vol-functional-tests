package org.dvsa.testing.framework.Journeys.permits.external;


import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;

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

    public EcmtInternationalRemovalJourney overview(OverviewSection section) {
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(section);
        return this;
    }

    public EcmtInternationalRemovalJourney checkYourAnswers() {
        CheckYourAnswerPage.untilOnPage();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney declaration() {
        DeclarationPage.confirmDeclaration();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney removalsEligibility(boolean declaration) {
       RemovalsEligibilityPage.confirmCheckbox();
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
        DeclarationPage.confirmDeclaration();
        DeclarationPage.saveAndContinue();
        return this;
    }

}