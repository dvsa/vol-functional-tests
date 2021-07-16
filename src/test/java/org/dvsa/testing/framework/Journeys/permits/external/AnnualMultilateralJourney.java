package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitFeePage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;


import java.util.concurrent.TimeUnit;

public class AnnualMultilateralJourney extends BasePermitJourney {

    public static final AnnualMultilateralJourney INSTANCE = new AnnualMultilateralJourney();

    public AnnualMultilateralJourney overviewPage(OverviewSection section, OperatorStore operatorStore) {
        OverviewPage.untilOnPage();

        LicenceStore licence = operatorStore.getCurrentLicence().orElseThrow(IllegalStateException::new);
        licence.setReferenceNumber(BasePermitPage.getReferenceFromPage());
        AnnualMultilateralStore permit = licence
                .getLatestAnnualMultilateral()
                .orElseGet(AnnualMultilateralStore::new);
        licence.addAnnualMultilateral(permit);
        permit.setReference(BasePermitPage.getReferenceFromPage());

        OverviewPageJourney.clickOverviewSection(section);
        return this;
    }

    public AnnualMultilateralJourney checkYourAnswers() {
        CheckYourAnswerPage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney feeOverviewPage() {
        PermitFeePage.saveAndContinue();
        return this;
    }

    public AnnualMultilateralJourney submit() {
        untilElementIsPresent("//h2[@class='govuk-heading-m']", SelectorType.XPATH, 10L, TimeUnit.SECONDS);
        SubmittedPage.goToPermitsDashboard();
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
    public AnnualMultilateralJourney permitType(OperatorStore operatorStore) {
        return (AnnualMultilateralJourney) super.permitType(operatorStore);
    }

    @Override
    public AnnualMultilateralJourney permitType() {
        return (AnnualMultilateralJourney) super.permitType();
    }

    @Override
    public AnnualMultilateralJourney permitType(PermitType type, OperatorStore operator) {
        return (AnnualMultilateralJourney) super.permitType(type, operator);
    }

    @Override
    public AnnualMultilateralJourney licencePage(OperatorStore operator, World world) {
        return (AnnualMultilateralJourney) super.licencePage(operator, world);
    }

}
