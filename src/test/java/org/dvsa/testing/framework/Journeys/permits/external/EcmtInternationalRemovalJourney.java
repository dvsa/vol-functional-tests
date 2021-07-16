package org.dvsa.testing.framework.Journeys.permits.external;


import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

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

    public EcmtInternationalRemovalJourney checkYourAnswers() {
        CheckYourAnswerPage.untilOnPage();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney removalsEligibility(boolean declaration) {
       RemovalsEligibilityPage.confirmCheckbox();
       BasePermitPage.saveAndContinue();
       return this;
    }

    public EcmtInternationalRemovalJourney cabotagePage() {
        CabotagePage.confirmWontUndertakeCabotage();
        saveAndContinue();
        return this;
    }

    public EcmtInternationalRemovalJourney certificatesRequiredPage() {
        CertificatesRequiredPage.completePage();
        return this;
    }

    public EcmtInternationalRemovalJourney permitStartDatePage() {
        PermitStartDatePage.permitDate();
        saveAndContinue();
        return this;
    }

}