package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;

public class CertificatesRequiredPageSteps implements En {

    public CertificatesRequiredPageSteps(World world, OperatorStore operatorStore) {

        And("^I am on the Ecmt Certificates required Page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
            CabotagePage.wontCarryCabotage(true);
        });
        And("^The application reference is displayed on the page$",() -> {

                CertificatesRequiredPage.hasAppRef();

    });
        And("^The main page heading is as per the AC$",() -> CertificatesRequiredPage.hasTheText());
        And("^Correct advisory text is shown below the page heading$", () -> CertificatesRequiredPage.hasAdvisoryMessagesEcmt());
        And("^The advisory text contains bold characters at the right places$", () -> CertificatesRequiredPage.isfontbold());
        And("^There is one checkbox with right label and not checked by default$", () -> CertificatesRequiredPage.checkBoxNotSelected());
        And("^if I don't select the checkbox and click Save and Continue button$", () -> BasePermitPage.saveAndContinue());
        Then("^I am presented with a validation error message$", () -> CertificatesRequiredPage.notconfirmed());
        And("^if I don't select the checkbox and click Save and Return to Overview button$", () -> BasePermitPage.overview());
        Then("^I am presented with same validation error message$", () -> CertificatesRequiredPage.notconfirmed());
        And("^if I select the checkbox and click Save and Return to Overview button$", () -> CertificatesRequiredPage.checkBoxClickedReturnOverview());
        And("^I select the checkbox and click Save and Continue button$", () -> CertificatesRequiredPage.checkBoxClickedSaveContinue());
        Then("^I am taken to the Restricted countries page$", () -> RestrictedCountriesPage.hasPageHeading());

    }
}