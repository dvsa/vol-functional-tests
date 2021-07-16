package org.dvsa.testing.framework.stepdefs.permits.internal.bilateral;

import Injectors.World;
import activesupport.number.Int;
import activesupport.string.Str;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import com.google.common.collect.Lists;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.InternalAnnualBilateralPermitApplicationPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.assertTrue;

public class AnnualBilateralInternalApplicationSteps implements En {

    public World world;
    public AnnualBilateralInternalApplicationSteps(OperatorStore operatorStore) {
        And("^the case worker (?:has began an|begins another) annual bilateral permit application$", () -> {
            LicenceDetailsPageJourney.clickIRHPTab();
            IrhpPermitsApplyPage.applyforPermit();
            IRHPPageJourney.completeModal(PermitType.ANNUAL_BILATERAL);
        });
        When("^I'm viewing my saved annual bilateral application$", () -> {
            IrhpPermitsDetailsPage.select(IrhpPermitsDetailsPage.getApplications().get(0).getReferenceNumber());
        });
        And("^I save my annual bilateral application$", InternalAnnualBilateralPermitApplicationPage.Decisions::submit);
    }
}