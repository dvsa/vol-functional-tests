package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CancelApplicationPageSteps extends BasePage implements En {

    public CancelApplicationPageSteps(OperatorStore operatorStore, World world) {
        And ("^the cancel application page displays the correct text$", () -> {
            CancellationPage.getPageHeading();
            assertTrue(CancellationPage.isAdvisoryTextPresent());
            assertEquals("I confirm that I would like to cancel my application.", CancellationPage.getConfirmCheckboxText());
        });

    }
}
