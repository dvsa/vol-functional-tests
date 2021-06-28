package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.PermitTypePage;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.untilElementIsPresent;
import static org.junit.Assert.assertEquals;

public class PermitTypeSteps implements En {
    public PermitTypeSteps(OperatorStore operatorStore, World world) {
        And("^I am applying for annual bilateral permit$", () -> {
            clickToPermitTypePage(world);
            untilElementIsPresent("//h1[contains(text(),'Select a permit type or certificate to apply for')]", SelectorType.XPATH,10L, TimeUnit.SECONDS);
        });
        And("^Help text under Bilateral permits is displayed correctly$", () -> {
            String helpText = PermitTypePage.getPermitTypeBilateralHelpText();
            assertEquals("These permits are for travelling to or transiting through individual countries. A separate " +
                            "permit is needed for each country you intend to travel to or transit through. " +
                            "Each country will have a specific validity period for these permits."
                    , helpText);
        });

        When("^I select Bilateral permit on permit type selection page and click continue$", () -> {
            EcmtApplicationJourney.getInstance().permitType(PermitType.ANNUAL_BILATERAL, operatorStore);
        });

        Then("^I am navigated to Bilaterals licence selection page$", () -> {
            untilElementIsPresent("//h1[@class='govuk-fieldset__heading']",SelectorType.XPATH,10L, TimeUnit.SECONDS);
        });


    }
}

