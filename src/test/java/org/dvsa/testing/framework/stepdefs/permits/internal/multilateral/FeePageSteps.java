package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.FeeSection;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FeePageSteps extends BasePage implements En {

    public FeePageSteps(OperatorStore operator, World world) {

        Then("^the fees are to be updated to reflect changes$", () -> {
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.Fees);
            List<FeesPage.Fee> actualFees = FeesPage.fees();
            List<FeesPage.Fee> expectedFees = operator.getCurrentLicence().get().getFees();
            Assert.assertNotEquals(expectedFees, actualFees);
        });
    }
}
