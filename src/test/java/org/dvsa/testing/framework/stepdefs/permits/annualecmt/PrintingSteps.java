package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.string.Str;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.internal.PrintingJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.permits.Permits;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.internal.IRHPPermitsPageSteps.viewLicenceOnInternal;

public class PrintingSteps implements En {
    public PrintingSteps(OperatorStore operatorStore, World world) {
        When("^my issued permits are printed$", () -> {
            List<String> successfulPermits = world.get("ecmt.application.successful");

            PrintingJourney.getInstance()
                    .printIrhpPermit(successfulPermits.toArray(new String[0]));

            // Get back to IRHP Permits page
            viewLicenceOnInternal(Str.find("\\w{2}\\d{7}", successfulPermits.get(0)).get());
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            IrhpPermitsDetailsPage.select(successfulPermits.get(0));
            Permits.untilAllStatusesAre(PermitStatus.PRINTED, Duration.LONG, TimeUnit.MINUTES);
        });
    }
}
