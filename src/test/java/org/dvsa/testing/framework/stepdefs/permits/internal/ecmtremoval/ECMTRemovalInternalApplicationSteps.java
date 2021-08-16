package org.dvsa.testing.framework.stepdefs.permits.internal.ecmtremoval;

import Injectors.World;
<<<<<<< HEAD
import activesupport.number.Int;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.internal.BaseModel;
import org.dvsa.testing.lib.newPages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage.*;

public class ECMTRemovalInternalApplicationSteps implements En {
    public World world;

    public ECMTRemovalInternalApplicationSteps(World world) {

        And("^the case worker apply for an ECMT Removal application$", () -> {
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
        });
        And("^I pay fee for the ECMT removal application$", () -> {
            LicenceDetailsPageJourney.clickFeesTab();
            IrhpPermitsApplyPage.selectApplication();
            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            selectCardPayment();
            world.feeAndPaymentJourney.customerPaymentModule();
            FeesDetailsPage.untilFeePaidNotification();
        });

        And("^the application goes to valid status$", () -> {
            CommonSteps.waitUntilPermitHasStatus(world);
        });
    }
}
