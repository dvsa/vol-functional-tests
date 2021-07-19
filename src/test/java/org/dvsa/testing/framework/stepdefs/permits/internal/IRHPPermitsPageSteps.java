package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import activesupport.aws.s3.S3;
import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.VolLicenceSteps;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.ApplicationDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IRHPPermitsPageSteps extends BasePage implements En {
    private static World world;
    LocalDate ld;

    public static List<String> successfulPermits;

    public IRHPPermitsPageSteps(OperatorStore operator, World world) {
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
        And("^I have an ECMT application that's not yet submitted$", () -> {

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

    private List<String> getAllSuccessfulApplications(OperatorStore operator) throws MessagingException {
        return operator.getLicences().stream()
                .map((licence) -> licence.getEcmt().getFullReferenceNumber())
                .map(reference -> S3.getEcmtCorrespondences(operator.getEmail(), reference))
                .filter(email -> Str.find("(?<==0A=0A)Application status: Successful", email).isPresent())
                .map(email -> Str.find("(?<=Your ECMT permit application )(\\w{2}\\d{7} / \\d{5})", email).get())
                .collect(Collectors.toList());
    }

    public static void viewLicenceOnInternal() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewLicence();
    }

}
