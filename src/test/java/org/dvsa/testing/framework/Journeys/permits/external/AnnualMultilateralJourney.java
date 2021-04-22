package org.dvsa.testing.framework.Journeys.permits.external;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;


import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class AnnualMultilateralJourney extends BasePermitJourney implements PaymentJourney {

    public static final AnnualMultilateralJourney INSTANCE = new AnnualMultilateralJourney();

    public AnnualMultilateralJourney overviewPage(OverviewPage.Section section, OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        OverviewPage.untilOnPage();

        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        licence.setReferenceNumber(OverviewPage.reference());
        AnnualMultilateralStore permit = licence
                .getLatestAnnualMultilateral()
                .orElseGet(AnnualMultilateralStore::new);
        licence.addAnnualMultilateral(permit);
        permit.setReference(OverviewPage.reference());

        OverviewPage.select(section);
        return this;
    }

    public AnnualMultilateralJourney numberOfPermitsPage(int maxNumberOfPermits, OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        licence.getLatestAnnualMultilateral().get().setNumberOfPermits(NumberOfPermitsPage.quantity(maxNumberOfPermits));
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney numberOfPermitsPage(OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        int authVehicles = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new).getNumberOfAuthorisedVehicles();
        numberOfPermitsPage(authVehicles, operatorStore);

        operatorStore.getCurrentLicence().get().getLatestAnnualMultilateral().get().setApplicationDate();
        return this;
    }

    public AnnualMultilateralJourney checkYourAnswers() throws MalformedURLException, IllegalBrowserException {
        CheckYourAnswersPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney declaration(boolean declaration) throws MalformedURLException, IllegalBrowserException {
        DeclarationPage.declare(declaration);
        DeclarationPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney feeOverviewPage() throws MalformedURLException, IllegalBrowserException {
        FeeOverviewPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney submit() throws MalformedURLException, IllegalBrowserException {
        ProportionOfInternationalJourneyPage.untilElementIsPresent("//h2[@class='govuk-heading-m']", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
        ApplicationSubmitPage.finish();
        return this;
    }

    // Overrides /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public AnnualMultilateralJourney go(ApplicationType applicationType, String endpoint) throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.go(applicationType, endpoint);
    }

    @Override
    public AnnualMultilateralJourney go(ApplicationType applicationType) throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.go(applicationType);
    }

    @Override
    public AnnualMultilateralJourney beginApplication() throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.beginApplication();
    }

    @Override
    public AnnualMultilateralJourney permitType(OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.permitType(operatorStore);
    }

    @Override
    public AnnualMultilateralJourney permitType() throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.permitType();
    }

    @Override
    public AnnualMultilateralJourney permitType(PermitTypePage.PermitType type, OperatorStore operator) throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.permitType(type, operator);
    }

    @Override
    public AnnualMultilateralJourney licencePage(OperatorStore operator, World world) throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.licencePage(operator, world);
    }

    @Override
    public AnnualMultilateralJourney signin(World world) throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.signin(world);
    }

    @Override
    public AnnualMultilateralJourney signin(OperatorStore operator, World world) throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) super.signin(operator, world);
    }

    @Override
    public AnnualMultilateralJourney cardDetailsPage() throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) PaymentJourney.super.cardDetailsPage();
    }

    @Override
    public AnnualMultilateralJourney cardHolderDetailsPage() throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) PaymentJourney.super.cardHolderDetailsPage();
    }

    @Override
    public AnnualMultilateralJourney confirmAndPay() throws MalformedURLException, IllegalBrowserException {
        return (AnnualMultilateralJourney) PaymentJourney.super.confirmAndPay();
    }

}
