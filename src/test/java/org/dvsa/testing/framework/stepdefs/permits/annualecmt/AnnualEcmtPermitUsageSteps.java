package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.enums.AnnualEcmtPermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PermitUsagePage;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.isPath;

public class AnnualEcmtPermitUsageSteps implements En {

    public AnnualEcmtPermitUsageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Annual Ecmt permit usage page$", () -> {

        });
        Then("^the Annual Ecmt permit usage page has an application reference number$", () -> {
            String actualReference = PermitUsagePage.reference();
            System.out.println(actualReference);
            String aa = operatorStore.getCurrentLicenceNumber().toString();
            System.out.println(aa);
        });
        And("^the page heading on the Annual Ecmt permit usage page is displayed correctly$", () -> {
            PermitUsagePage.hasPageHeading();
        });
        And("^the Annual Ecmt permit  usage buttons are displayed  unselected by default$", () -> {
            Assert.assertTrue("not selected", PermitUsagePage.hasNotUsageConfirmed());
        });

        Then("^the Annual Ecmt permit usage page has advisory messages$", () -> {
            PermitUsagePage.hasAdvisoryMessages();
            PermitUsagePage.checkAdvisoryText();
            PermitUsagePage.hasLinkNotPresent();
        });


        Then("^I should get error message on the Annual Ecmt permit usage page$", PermitUsagePage::getErrorText);


        Then("^I should be on the Annual Ecmt permit overview page$", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/"));
        });

        When("^I confirm the permit usage on Annual Ecmt permit$", () -> AnnualEcmtPermitUsage.random());

        Then("^the user is navigated to the overview page with the Annual Ecmt permit usage section status displayed as completed$", () -> {
            boolean permitUsage = OverviewPage.checkStatus(OverviewPage.Section.HowwillyouusethePermits.toString(),PermitStatus.COMPLETED);
            Assert.assertTrue(permitUsage);
        });

    }
}
