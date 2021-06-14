package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;


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
            YearSelectionPage.hasShortTermECMTHeading();
        });

        When("^I Should see warning displayed correctly$", () -> {
            YearSelectionPage.hasShortTermWarningMessage();
        });

        When("^I Should see one or more years to select to display correctly$", YearSelectionPage::selectShortTermValidityPeriod);

    }
}