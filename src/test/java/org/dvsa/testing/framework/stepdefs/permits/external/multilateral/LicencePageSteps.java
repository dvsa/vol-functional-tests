package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.multilateral.LicencePage;
import org.junit.Assert;

public class LicencePageSteps implements En {
    public LicencePageSteps(OperatorStore operatorStore) {
        Then("^My licence should be an option to apply for multilateral permit$", () -> {
            LicencePage.untilOnPage();
            operatorStore.getLicences().forEach(l -> {
                    Assert.assertTrue(LicencePage.hasLicence(l.getLicenceNumber()));

            });
        });
        When("^I select the licence to apply for an annual multilateral permit with$", () -> {
            LicencePage.randomLicnece();
            LicencePage.saveAndContinue();
        });
    }
}
