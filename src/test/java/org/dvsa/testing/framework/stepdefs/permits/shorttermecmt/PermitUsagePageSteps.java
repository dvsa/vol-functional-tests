package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PeriodSelectionPageOne;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SelectYearPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.isPath;

public class PermitUsagePageSteps implements En {

    public PermitUsagePageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm permit usage page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore)
                    .licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
        });
        Then("^the shortterm ecmt permit usage page has an application reference number$", () -> {
            String actualReference = BasePermitPage.getReference();
            System.out.println(actualReference);
            String aa = operatorStore.getCurrentLicenceNumber().toString();
            System.out.println(aa);
        });
        And("^the page heading on the permit usage page is displayed correctly$", () -> {
            PermitUsagePage.hasPageHeading();
        });
        And("^the short term ecmt permit usage buttons are displayed  unselected by default$", () -> {
            Assert.assertTrue("not selected", PermitUsagePage.hasNotUsageConfirmed());
        });

        Then("^the shortterm ecmt permit usage page has advisory messages$", () -> {
            PermitUsagePage.hasAdvisoryMessages();
            PermitUsagePage.checkAdvisoryText();
            PermitUsagePage.hasLinkNotPresent();
        });

        And("^when I save and continue without selecting any radio button$", BasePermitPage::saveAndContinue);

        Then("^I should get error message on the permit usage page$", () -> {
            PermitUsagePage.getErrorText();
        });

        And("^when I save and return to overview without selecting any radio button$", BasePermitPage::saveAndContinue);

        Then("^I should be on the short term ECMT overview page$", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/"));
        });

        When("^I confirm the permit usage$", () -> PermitUsagePage.permitUsage(PermitUsage.random()));

        Then("^the user is navigated to the overview page with the permits usage section status displayed as completed$", () -> {
            boolean permitUsage = OverviewPage.checkStatus(OverviewPage.Section.HowwillyouusethePermits.toString(),PermitStatus.COMPLETED);
            Assert.assertTrue(permitUsage);
        });

    }
}
