package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
<<<<<<< HEAD
import activesupport.aws.s3.S3;
import activesupport.string.Str;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.VolLicenceSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.PermitApplication;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.ApplicationDetailsPage;
import org.dvsa.testing.lib.newPages.internal.BaseModel;
import org.dvsa.testing.lib.newPages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.newPages.BasePage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.junit.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IRHPPermitsPageSteps extends BasePage implements En {
    private static World world;

    public IRHPPermitsPageSteps(World world) {
        When("^I am viewing a licences IRHP section$", () -> {
            refreshPage();
            LicenceDetailsPageJourney.clickIRHPTab();
        });
        Then("^the no issued permits message should be displayed$", () -> assertTrue("Unable to find the no issued permits message", IrhpPermitsDetailsPage.isNoPermitsMessagePresent()));
        And("^the no permits applications message should be displayed$", IrhpPermitsDetailsPage::isNoPermitApplicationsMessagePresent);
        Then("^the ongoing permit application is to be as expected$", () -> {
            List<PermitApplication> applications = IrhpPermitsDetailsPage.getApplications();

            applications.stream().forEach((permit)->{
               Assert.assertTrue(permit.getReferenceNumber().contains(world.applicationDetails.getLicenceNumber()));
               Assert.assertEquals(permit.getNoOfPermits().intValue(), 1);
               Assert.assertEquals(permit.getType(), PermitType.ECMT_ANNUAL);
               Assert.assertEquals(LocalDate.parse(permit.getRecdDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.now());
            });
        });
        Then("^internal users should not be able to create ECMT Permit applications$", () -> {
            Assert.assertFalse(
                    "IRHP tab should NOT be present but was",
                    IrhpPermitsDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits)
            );
        });
        And("^pay outstanding fees$", () -> {
            IRHPPermitsPageSteps.payOutstandingFees(world);
        });
        Then("^my application should be under consideration$", () -> {
            assertEquals("UNDER CONSIDERATION", getText("//*[@class='govuk-summary-list__value']/span", SelectorType.XPATH));
        });
        Then("^my permit application is under consideration$", () -> {
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.underConsiderationStatusExists();
        });
    }

    public static void payOutstandingFees(World world) {
        refreshPage();
        LicenceDetailsPageJourney.clickIRHPTab();
        LicenceDetailsPageJourney.clickFeesTab();
        FeesDetailsPage.outstanding();
        FeesDetailsPage.pay();
        BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
        IrhpPermitsApplyPage.selectCardPayment();
        world.feeAndPaymentJourney.customerPaymentModule();
        FeesDetailsPage.untilFeePaidNotification();
    }

}
