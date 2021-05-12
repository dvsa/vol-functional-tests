package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.YearSelectionPage;

import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.external.permit.YearSelectionPage.*;

public class YearSelectionPageSteps implements En {

    public YearSelectionPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the Year Selection Page$", () -> {
           CommonSteps.clickToPermitTypePage(world);
           EcmtApplicationJourney.getInstance().permitType(PermitTypePage.PermitType.EcmtAnnual,operatorStore);
        });
        And("^the user is navigated to the permit type page$", PermitTypePage::permitTypePageHeading);
        And("^the page heading on Annual Ecmt Year selection page is displayed correctly$", YearSelectionPage::ecmtApsgPageHeading);
        And("^I select continue button$", YearSelectionPage::continueButton);
        And("^the validity error message is displayed$", YearSelectionPage::errorText);
        When ("^I confirm  the year selection$", () -> {

            if(radioButtonExists()==true){

                EcmtApplicationJourney.getInstance()
                        .yearSelection(YearSelectionPage.YearSelection.YEAR_2021, operatorStore);
}
            else{
                continueButton();
            }

                });

        When ("^the user is navigated to licence selection page$", () -> {
            isPath("/permits/type/\\d+/licence/");
        });

    }
}
