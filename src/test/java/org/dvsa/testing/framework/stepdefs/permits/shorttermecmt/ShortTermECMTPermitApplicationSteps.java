package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ShortTermECMTPermitApplicationSteps implements En {

    public ShortTermECMTPermitApplicationSteps(World world, OperatorStore operatorStore) {

        When("^I am on select a year page$", () -> {
            clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance()
                    .permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
        });

        When("^I am selecting a year for short term ecmt permit$", () -> {
            YearSelectionPage.selectShortTermValidityPeriod();
        });

        When("^I Should see select year page message displayed correctly$", () -> {
            String heading = YearSelectionPage.getPageHeading();
            assertEquals("Permits requested will be valid for 2021", heading);
        });

        When("^I Should see warning displayed correctly$", () -> {
            assertTrue(YearSelectionPage.isShortTermWarningMessagePresent());
        });

        When("^I Should see one or more years to select to display correctly$", YearSelectionPage::selectShortTermValidityPeriod);

    }
}