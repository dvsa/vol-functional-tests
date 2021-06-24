package org.dvsa.testing.framework.stepdefs.permits.internal;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.external.pages.ValidPermitsPage;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;

public class InternalPermitsTabSteps implements En {
    public InternalPermitsTabSteps(OperatorStore operatorStore) {
        When("^I'm viewing the permits tab$", () -> {
            String reference = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalArgumentException::new)
                    .getReferenceNumber();

            BaseDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);

            IrhpPermitsDetailsPage.untilOnPage();
            IrhpPermitsDetailsPage.untilStatusIs(reference, PermitStatus.VALID, Duration.LONG, TimeUnit.MINUTES);
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
