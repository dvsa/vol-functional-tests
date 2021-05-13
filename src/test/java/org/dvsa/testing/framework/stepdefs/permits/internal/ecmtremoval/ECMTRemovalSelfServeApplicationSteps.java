package org.dvsa.testing.framework.stepdefs.permits.internal.ecmtremoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.LoginInIntPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SubmittedPage;

import java.util.concurrent.TimeUnit;

public class ECMTRemovalSelfServeApplicationSteps extends BasePage implements En {

    public  ECMTRemovalSelfServeApplicationSteps(OperatorStore operatorStore, World world) {

        And("^I have logged on selfserve site in int environment$",() ->{
            get("https://ssap1.int.olcs.dvsacloud.uk/auth/login/");
        });
        And("^username and passwords retrieved from excel row \"([^\"]*)\" dataset$", (String arg0) -> {
            LoginInIntPage.readExcel2(0);
        });
        And("^I apply for an ECMT removal permit$",() -> {
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            EcmtApplicationJourney.getInstance().permitType(PermitTypePage.PermitType.EcmtInternationalRemoval,operatorStore);
            LoginInIntPage.selectLicence();
            EcmtInternationalRemovalJourney.getInstance()
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage()
                    .numberOfPermits()
                    .checkYourAnswers()
                    .declaration();
            EcmtApplicationJourney.getInstance()
                    .feeOverviewPage()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            SubmittedPage.untilElementIsPresent("//h1[@class='govuk-panel__title']", SelectorType.XPATH,10, TimeUnit.SECONDS);
            BaseApplicationSubmitPage.finish();

        });
    }
}