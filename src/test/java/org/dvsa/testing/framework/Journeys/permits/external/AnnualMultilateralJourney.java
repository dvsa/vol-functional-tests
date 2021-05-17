package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;


import java.util.concurrent.TimeUnit;

public class AnnualMultilateralJourney extends BasePermitJourney implements PaymentJourney {

    public static final AnnualMultilateralJourney INSTANCE = new AnnualMultilateralJourney();

    public AnnualMultilateralJourney overviewPage(OverviewPage.Section section, OperatorStore operatorStore) {
        OverviewPage.untilOnPage();

        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        licence.setReferenceNumber(BasePermitPage.getReference());
        AnnualMultilateralStore permit = licence
                .getLatestAnnualMultilateral()
                .orElseGet(AnnualMultilateralStore::new);
        licence.addAnnualMultilateral(permit);
        permit.setReference(BasePermitPage.getReference());

        OverviewPage.select(section);
        return this;
    }

    public AnnualMultilateralJourney numberOfPermitsPage(int maxNumberOfPermits, OperatorStore operatorStore) {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        licence.getLatestAnnualMultilateral().get().setNumberOfPermits(NumberOfPermitsPage.quantity(maxNumberOfPermits));
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney numberOfPermitsPage(OperatorStore operatorStore) {
        int authVehicles = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).getNumberOfAuthorisedVehicles();
        numberOfPermitsPage(authVehicles, operatorStore);

        operatorStore.getCurrentLicence().get().getLatestAnnualMultilateral().get().setApplicationDate();
        return this;
    }

    public AnnualMultilateralJourney checkYourAnswers() {
        CheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney declaration(boolean declaration) {
        DeclarationPage.declare(declaration);
        DeclarationPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney feeOverviewPage() {
        FeeOverviewPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney submit() {
        ProportionOfInternationalJourneyPage.untilElementIsPresent("//h2[@class='govuk-heading-m']", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
        ApplicationSubmitPage.finish();
        return this;
    }

    // Overrides /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public AnnualMultilateralJourney go(ApplicationType applicationType, String endpoint) {
        return (AnnualMultilateralJourney) super.go(applicationType, endpoint);
    }

    @Override
    public AnnualMultilateralJourney go(ApplicationType applicationType) {
        return (AnnualMultilateralJourney) super.go(applicationType);
    }

    @Override
    public AnnualMultilateralJourney beginApplication() {
        return (AnnualMultilateralJourney) super.beginApplication();
    }

    @Override
    public AnnualMultilateralJourney permitType(OperatorStore operatorStore) {
        return (AnnualMultilateralJourney) super.permitType(operatorStore);
    }

    @Override
    public AnnualMultilateralJourney permitType() {
        return (AnnualMultilateralJourney) super.permitType();
    }

    @Override
    public AnnualMultilateralJourney permitType(PermitTypePage.PermitType type, OperatorStore operator) {
        return (AnnualMultilateralJourney) super.permitType(type, operator);
    }

    @Override
    public AnnualMultilateralJourney licencePage(OperatorStore operator, World world) {
        return (AnnualMultilateralJourney) super.licencePage(operator, world);
    }

    @Override
    public AnnualMultilateralJourney cardDetailsPage() {
        return (AnnualMultilateralJourney) PaymentJourney.super.cardDetailsPage();
    }

    @Override
    public AnnualMultilateralJourney cardHolderDetailsPage() {
        return (AnnualMultilateralJourney) PaymentJourney.super.cardHolderDetailsPage();
    }

    @Override
    public AnnualMultilateralJourney confirmAndPay() {
        return (AnnualMultilateralJourney) PaymentJourney.super.confirmAndPay();
    }

}
