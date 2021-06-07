package org.dvsa.testing.framework.stepdefs.permits.internal.ecmtremoval;

import activesupport.number.Int;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.FeePaymentConfirmationPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.LoginInIntPage;
import org.dvsa.testing.lib.pages.internal.BaseModel;
import org.dvsa.testing.lib.pages.internal.details.BaseApplicationDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsPage;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.newPages.Driver.DriverUtils.get;
import static org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage.*;

public class ECMTRemovalInternalApplicationSteps implements En {
    public ECMTRemovalInternalApplicationSteps(OperatorStore operatorStore) {

        And("^the case worker apply for an ECMT Removal application$", () -> {
            LicenceDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.applyforPermit();
            IrhpPermitsPage.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
            IrhpPermitsPage.Model.permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL);
            IrhpPermitsPage.Model.continueButton();
            IrhpPermitsPage.Model.untilModalIsGone(Duration.LONG, TimeUnit.SECONDS);

            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);

            //apply application

            int numberOfPermits = Int.random(1, licenceStore.getNumberOfAuthorisedVehicles());

            //Fill application
            IrhpPermitsApplyPage.removalsEligibility();
            IrhpPermitsApplyPage.certificatesRequired();
            IrhpPermitsApplyPage.cabotageEligibility();
            IrhpPermitsApplyPage.permitStartDate();
            IrhpPermitsApplyPage.numberOfPermitRemoval();
            declare(true);
            operatorStore.withLicences(licenceStore);

            //Save application
            saveIRHP();
            untilOnPage();
        });
        And("^I'm viewing my submitted ECMT Removal application$", IrhpPermitsApplyPage::viewApplication);
        And("^I pay fee for the ECMT removal application$", () -> {
            BaseApplicationDetailsPage.Tab.select(BaseApplicationDetailsPage.DetailsTab.Fees);
            IrhpPermitsApplyPage.selectApplication();
            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            selectCardPayment();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage();
            FeePaymentConfirmationPage.makeMayment();
            FeesDetailsPage.untilFeePaidNotification();
        });

        And("^the application goes to valid status$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            HomePage.PermitsTab.untilPermitHasStatus(
                    licenceStore.getLicenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );
        });
        And("^the case worker submits partial ECMT Removal application$", () -> {
            LicenceDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.applyforPermit();
            IrhpPermitsPage.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
            IrhpPermitsPage.Model.permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL);
            IrhpPermitsPage.Model.continueButton();
            IrhpPermitsPage.Model.untilModalIsGone(Duration.LONG, TimeUnit.SECONDS);

            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);

            //apply application

            int numberOfPermits = Int.random(1, licenceStore.getNumberOfAuthorisedVehicles());

            //Fill application
            IrhpPermitsApplyPage.removalsEligibility();
            IrhpPermitsApplyPage.certificatesRequired();
            IrhpPermitsApplyPage.cabotageEligibility();
            IrhpPermitsApplyPage.permitStartDate();
            IrhpPermitsApplyPage.numberOfPermitRemoval();
            declare(true);
            operatorStore.withLicences(licenceStore);

            //Save application
            IrhpPermitsApplyPage.saveIRHP();
        });
        And("^I submit the application$", () -> {
           LicenceDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
           IrhpPermitsApplyPage.viewApplication();
           declare(true);
           saveIRHP();
           IrhpPermitsApplyPage.viewApplication();
           IrhpPermitsApplyPage.submitIRHP();
        });
    }
}
