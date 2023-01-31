package org.dvsa.testing.framework.stepdefs.permits.internal.ecmtremoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage.*;

public class ECMTRemovalInternalApplicationSteps{
    private final World world;
    public ECMTRemovalInternalApplicationSteps(World world) {
        this.world = world;
    }
    @And("the case worker apply for an ECMT Removal application")
    public void theCaseWorkerApplyForAnECMT() {
        LicenceDetailsPageJourney.clickIRHPTab();
        IRHPPageJourney.completeModal(PermitType.ECMT_INTERNATIONAL_REMOVAL);

        //Fill application
        removalsEligibility();
        certificatesRequired();
        cabotageEligibility();
        permitStartDate();
        numberOfPermitRemoval();
        declare(true);

        //Save application
        saveIRHP();
    }

    @And("I pay fee for the ECMT removal application")
    public void iPayFeeForTheECMT() {
        LicenceDetailsPageJourney.clickFeesTab();
        IrhpPermitsApplyPage.selectApplication();
        BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
        selectCardPayment();
        world.feeAndPaymentJourney.customerPaymentModule();
        FeesDetailsPage.untilFeePaidNotification();
    }

    @And("the application goes to valid status")
    public void theApplicationGoesToValid() {
        CommonSteps.waitUntilPermitHasStatus(world);
    }
}