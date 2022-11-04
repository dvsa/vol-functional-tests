package org.dvsa.testing.framework.stepdefs.permits.internal;

import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InternalPermitsTabSteps implements En {
    public InternalPermitsTabSteps() {
        When("^I'm viewing the permits tab$", () -> {

            String referenceNumber = BasePermitJourney.getFullReferenceNumber();
            LicenceDetailsPageJourney.clickIRHPTab();

            IrhpPermitsDetailsPage.untilOnPage();
            String message = "Permit status did not change to the desired status within the specified time limit";
            assertTrue(IrhpPermitsDetailsPage.isStatusPresentForReference(referenceNumber, PermitStatus.VALID, Duration.LONG, TimeUnit.MINUTES),message);
            IrhpPermitsDetailsPage.select(referenceNumber);
        });
        Then("^the annual bilateral permit table has the expected format$", () -> {
       //     Assert.assertThat(Permits.annualBilateralPermits().size(), is(greaterThanOrEqualTo(1)));
        });
        Then("^the annual ECMT permit table has the expected format$", () -> {
          assertThat(ValidPermitsPage.ECMTInternationalRemovalPermits().size(), is(greaterThanOrEqualTo(1)));
        });
    }
}