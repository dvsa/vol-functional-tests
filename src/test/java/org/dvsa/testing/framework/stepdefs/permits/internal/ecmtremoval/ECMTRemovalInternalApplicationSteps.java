package org.dvsa.testing.framework.stepdefs.permits.internal.ecmtremoval;

import Injectors.World;
import activesupport.number.Int;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage.*;

public class ECMTRemovalInternalApplicationSteps implements En {
    public World world;

    public ECMTRemovalInternalApplicationSteps(World world, OperatorStore operatorStore) {

        And("^the case worker apply for an ECMT Removal application$", () -> {
            LicenceDetailsPageJourney.clickIRHPTab();
            IrhpPermitsApplyPage.applyforPermit();
            IRHPPageJourney.completeModal(PermitType.ECMT_INTERNATIONAL_REMOVAL);

            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);

            //apply application

            int numberOfPermits = Int.random(1, 5);

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
            LicenceDetailsPageJourney.clickFeesTab();
            IrhpPermitsApplyPage.selectApplication();
            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            selectCardPayment();
            world.feeAndPaymentJourney.customerPaymentModule();
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
            LicenceDetailsPageJourney.clickIRHPTab();
            IrhpPermitsApplyPage.applyforPermit();
            IRHPPageJourney.completeModal(PermitType.ECMT_INTERNATIONAL_REMOVAL);

            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);

            //apply application

            int numberOfPermits = Int.random(1, 5);

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
            LicenceDetailsPageJourney.clickIRHPTab();
           IrhpPermitsApplyPage.viewApplication();
           declare(true);
           saveIRHP();
           IrhpPermitsApplyPage.viewApplication();
           IrhpPermitsApplyPage.submitIRHP();
        });
    }
}
