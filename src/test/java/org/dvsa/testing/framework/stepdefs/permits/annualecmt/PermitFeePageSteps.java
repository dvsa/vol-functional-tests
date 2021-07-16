package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.string.Str;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.Utils.store.permit.AnnualMultilateralStore;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.type.Fee;
import org.dvsa.testing.framework.pageObjects.type.Permit;
import org.dvsa.testing.framework.pageObjects.enums.*;
import org.dvsa.testing.framework.pageObjects.external.pages.EssentialInformationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;


public class PermitFeePageSteps extends BasePermitPage implements En {
    public PermitFeePageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {

        And("^I select the submit and pay link from overview page$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.SubmitAndPay);
        });
        Then("^I select return to overview link on the permit fee page$", PermitFeePage::clickReturnToOverview);
        Then("^I should be on the permit fee page$", PermitFeePage::untilOnPage);
        Then("^the page heading is displayed correctly on the fee page$", PermitFeePage::untilOnPage);
        Then("^the Fee-breakdown sub-heading can be seen below the fee summary table$", () -> {
            String subHeading = getText("//h2[contains(text(),'Fee breakdown')]", SelectorType.XPATH);
            Assert.assertEquals("Fee breakdown", subHeading);
        });
        Then("^my fee should be tiered as expected$", () -> {
            List<Fee> feeBreakdowns = PermitFeePage.Fees();

            AnnualMultilateralStore permit = operatorStore.getCurrentLicence().get().getLatestAnnualMultilateral().get();
            feeBreakdowns.stream().forEach(fb -> {
                Assert.assertTrue(permit.getNumberOfPermits().stream().map(Permit::getYear).anyMatch(py -> py.equalsIgnoreCase(fb.getYear())));
                Permit existingPermit = permit.getNumberOfPermits().stream().filter(ep -> ep.getYear().equals(fb.getYear()))
                        .findFirst().get();
                Assert.assertEquals(existingPermit.getFeePerPermit().get().intValue(), fb.getFeePerPermit());
            });

        });
    }
}
