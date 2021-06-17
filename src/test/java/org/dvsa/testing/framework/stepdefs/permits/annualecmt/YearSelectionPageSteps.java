package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.external.permit.BasePermitPage.saveAndContinue;
import static org.junit.Assert.assertEquals;

public class YearSelectionPageSteps implements En {

    public YearSelectionPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the Year Selection Page$", () -> {
           CommonSteps.clickToPermitTypePage(world);
           EcmtApplicationJourney.getInstance().permitType(PermitType.ECMT_ANNUAL,operatorStore);
        });
        And("^the user is navigated to the permit type page$", PermitTypePage::permitTypePageHeading);
        And("^the page heading on Annual Ecmt Year selection page is displayed correctly$", () -> {
            if (YearSelectionPage.isYearChoicePresent()) {
                assertEquals(YearSelectionPage.getPageHeading(), "Select which year you want permits for");
            } else {
                assertEquals(YearSelectionPage.getPageHeading(), "Permits requested will be valid for 2021");
            }
        });
        And("^I select continue button$", YearSelectionPage::saveAndContinue);
        And("^the validity error message is displayed$", () -> {
            saveAndContinue();
            String errorText = YearSelectionPage.getErrorText();
            assertEquals( "You must select one year to continue", errorText);
        });
        When ("^I confirm  the year selection$", () -> {
            if (YearSelectionPage.isYearChoicePresent()) {
                YearSelectionPage.selectECMTValidityPeriod();
            } else {
                saveAndContinue();
            }
        });
        When ("^the user is navigated to licence selection page$", () -> {
            isPath("/permits/type/\\d+/licence/");
        });
    }
}
