package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitFeesPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AnnualMultilateralInternalApplicationSteps extends BasePage implements En {

    private World world;

    public AnnualMultilateralInternalApplicationSteps(OperatorStore operatorStore) {

        And("^I pay all the fees$", () -> {
            IrhpPermitsApplyPage.selectCardPayment();
            world.feeAndPaymentJourney.customerPaymentModule();
        });
        Then ("^For Short term APSG, I see only Withdraw application button against on my submitted application details page$", IrhpPermitsApplyPage::isWithdrawButtonPresent);
    }
}

