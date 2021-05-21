package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class PermitTypePageSteps implements En {
    public PermitTypePageSteps(OperatorStore operatorStore, World world) {
        And("^I am on the permit type page$", () -> {
            clickToPermitTypePage(world);
        });
        Then ("^the page heading is displayed as per the story$", PermitTypePage::permitTypePageHeading);
        Then ("^continue button is selected after confirming the permit type$", () -> {
            ShorttermECMTJourney.getInstance()
                    .permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
        });
        Then ("continue button is selected without confirming the permit type$", PermitTypePage::continueButton);
        Then ("^the error message is displayed in the permit type page$", PermitTypePage::permitTypeErrorText);
        Then ("^I click cancel button$", () ->{
            PermitTypePage.cancel();
        });

    }
}
