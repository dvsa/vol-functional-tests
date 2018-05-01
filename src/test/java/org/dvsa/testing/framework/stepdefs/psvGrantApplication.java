package org.dvsa.testing.framework.stepdefs;

        import cucumber.api.java8.En;
        import org.dvsa.testing.framework.stepdefs.Utils.External.CreateInterimGoodsLicenceAPI;
        import org.dvsa.testing.framework.stepdefs.Utils.External.CreateInterimPsvLicenceAPI;
        import org.dvsa.testing.framework.stepdefs.Utils.Internal.GrantApplicationAPI;

        import static org.dvsa.testing.framework.stepdefs.Utils.Internal.InternalGenericUtils.payPsvFees;

public class psvGrantApplication implements En {
    public psvGrantApplication() {
        GrantApplicationAPI grantApp = new GrantApplicationAPI();
        CreateInterimPsvLicenceAPI psvApp = new CreateInterimPsvLicenceAPI();
        CreateInterimGoodsLicenceAPI goodsApp = new CreateInterimGoodsLicenceAPI();

        Given("^I have a psv application which is under consideration$", () -> {
            if (psvApp.getApplicationNumber() == null) {
                psvApp.createAndSubmitPsvApp();
            }
        });
        When("^I pay my application fees$", () -> {
            payPsvFees(grantApp, psvApp);
        });
        Then("^my application should be granted$", () -> {
            grantApp.payGrantFees(psvApp.getOrganisationId(), psvApp.getApplicationNumber());
        });
    }
}