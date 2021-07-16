package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.PermitUsage;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(OperatorStore operatorStore, World world) {
        Then("^I should get the cabotage page error message$", () -> {
            String errorText = CabotagePage.getErrorText();
            Assert.assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys", errorText);
        });
        Then("^I should get the cabotage page error message for Annual ECMTs$", () -> {
            String errorText = CabotagePage.getErrorText();
            Assert.assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys.", errorText);
        });
    }
}
