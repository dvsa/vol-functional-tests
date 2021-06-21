package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SelectYearPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;


public class ShortTermECMTPermitApplicationSteps implements En {

    public ShortTermECMTPermitApplicationSteps(World world, OperatorStore operatorStore) {

        When("^I am on select a year page$", () -> {
            clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
        });

        When("^I am selecting a year for short term ecmt permit$", () -> {
            SelectYearPage.shortTermValidityPeriod();
        });

        When("^I Should see select year page message displayed correctly$", () -> {
            Assert.assertTrue(SelectYearPage.yearMessage());
        });

        When("^I Should see warning displayed correctly$", () -> {
            Assert.assertTrue(SelectYearPage.warningMessage());
        });

        When("^I Should see one or more years to select to display correctly$", SelectYearPage::shortTermValidityPeriod);

    }
}