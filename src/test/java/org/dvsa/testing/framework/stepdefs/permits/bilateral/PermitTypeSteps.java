package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.licence.LicenceBasePage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class PermitTypeSteps implements En {
    public PermitTypeSteps(OperatorStore operatorStore, World world) {
        And("^I am applying for annual bilateral permit$", () -> {
            clickToPermitTypePage(world);
            PermitTypePage.untilElementIsPresent("//h1[contains(text(),'Select a permit type or certificate to apply for')]", SelectorType.XPATH,10L, TimeUnit.SECONDS);
        });
        And("^Help text under Bilateral permits is displayed correctly$", () -> {
            PermitTypePage.permitTypeBilateralHelpText();
        });

        When("^I select Bilateral permit on permit type selection page and click continue$", () -> {
            EcmtApplicationJourney.getInstance().permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore);
        });

        Then("^I am navigated to Bilaterals licence selection page$", () -> {
            LicenceBasePage.untilElementIsPresent("//h1[@class='govuk-fieldset__heading']",SelectorType.XPATH,10L, TimeUnit.SECONDS);
        });


    }
}

