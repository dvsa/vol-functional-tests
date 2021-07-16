package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.junit.Assert;

import java.util.List;

public class DashboardSteps implements En {
    public DashboardSteps(OperatorStore operator) {

        Then("my submitted multilateral permit is on the permits dashboard", () -> {
            HomePage.untilOnPage();
            LicenceStore licence = operator.getCurrentLicence().orElseThrow(IllegalStateException::new);
            AnnualMultilateralStore permit = licence.getLatestAnnualMultilateral()
                    .orElseThrow(IllegalAccessError::new);
            List<PermitApplication> actualPermits = HomePage.PermitsTab.getIssuedPermitApplications();
            HomePage.PermitsTab.selectFirstValidPermit();


            actualPermits.forEach( p -> {
                Assert.assertEquals(p.getLicenceNumber(),permit.getReference().substring(0,9));
                Assert.assertEquals(permit.totalNumberOfPermits(), p.getNoOfPermits().intValue());
                Assert.assertEquals(operator.getCurrentPermitType().get().toString(), p.getType().toString());
            });
        });

    }
}
