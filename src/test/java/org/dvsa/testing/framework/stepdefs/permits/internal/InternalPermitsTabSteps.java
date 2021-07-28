package org.dvsa.testing.framework.stepdefs.permits.internal;

import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.external.pages.ValidPermitsPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class InternalPermitsTabSteps implements En {
    public InternalPermitsTabSteps(OperatorStore operatorStore) {
        When("^I'm viewing the permits tab$", () -> {
            String reference = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalArgumentException::new)
                    .getReferenceNumber();

            LicenceDetailsPageJourney.clickIRHPTab();

            IrhpPermitsDetailsPage.untilOnPage();
            String message = "Permit status did not change to the desired status within the specified time limit";
            assertTrue(message, IrhpPermitsDetailsPage.isStatusPresentForReference(reference, PermitStatus.VALID, Duration.LONG, TimeUnit.MINUTES));
            IrhpPermitsDetailsPage.select(reference);
        });
        Then("^the annual bilateral permit table has the expected format$", () -> {
       //     Assert.assertThat(Permits.annualBilateralPermits().size(), is(greaterThanOrEqualTo(1)));
        });
        Then("^the annual ECMT permit table has the expected format$", () -> {
           Assert.assertThat(ValidPermitsPage.annualECMTPermits().size(), is(greaterThanOrEqualTo(1)));
        });
    }
}
