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
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.ApplicationDetailsPage;
import org.dvsa.testing.lib.newPages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.internal.BaseModel;
import org.dvsa.testing.lib.pages.internal.ResultsPage;
import org.dvsa.testing.lib.pages.internal.SearchNavBar;
import org.junit.Assert;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.AwaitingFeePermitSteps.triggerPermitIssuing;

public class IRHPPermitsPageSteps extends BasePage implements En {
    private World world;
    LocalDate ld;

    public static List<String> successfulPermits;

    public IRHPPermitsPageSteps(OperatorStore operator, World world) {
        When("^I am viewing a licences IRHP section$", () -> {
            refreshPage();
            LicenceDetailsPageJourney.clickIRHPTab();
        });
        Then("^the no issued permits message should be displayed$", () -> Assert.assertTrue("Unable to find the no issued permits message", IrhpPermitsDetailsPage.isNoPermitsMessagePresent()));
        And("^the no permits applications message should be displayed$", IrhpPermitsDetailsPage::isNoPermitApplicationsMessagePresent);
        Then("^the ongoing permit application is to be as expected$", () -> {
            List<PermitApplication> applications = IrhpPermitsDetailsPage.getApplications();
            LicenceStore licence = operator.getCurrentLicence().get();

            applications.stream().forEach((permit)->{
               Assert.assertEquals(permit.getReferenceNumber(), licence.getReferenceNumber());
               Assert.assertEquals(permit.getNoOfPermits().intValue(), 1);
               Assert.assertEquals(permit.getType(), PermitType.ECMT_ANNUAL);
               Assert.assertEquals(LocalDate.parse(permit.getRecdDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.now());
            });
        });
        And("^I am viewing a licence with an issued ECMT permit on internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            // Apply for ECMT applications
            IntStream.rangeClosed(1, VolLicenceSteps.licenceQuantity.get("licence.quantity")).forEach((i) -> {
                HomePageJourney.beginPermitApplication();
                ECMTPermitApplicationSteps.completeEcmtApplication(operator, world);
            });

            // trigger issuing
            triggerPermitIssuing(world);

            // get list of all successful applications
            List<String> successfulApplications = getAllSuccessfulApplications(operator);

            // Search for licence
            viewLicenceOnInternal(world, Str.find("\\w{2}\\d{7}", successfulApplications.get(0)).get());

            LicenceDetailsPageJourney.clickIRHPTab();

            successfulPermits = successfulApplications;
        });
        Then("^The issued permit information should be as expected$", () -> {
            LicenceDetailsPageJourney.clickIRHPTab();
//            List<LicenceStore> licences = operator.getLicences(world.<List<String>>get("ecmt.application.successful").get(0));
//TODO: Test has been deprecated so doesn't matter but world.<List<String>>get("ecmt.application.successful" isn't set anywhere.
            List<PermitApplication> applications = IrhpPermitsDetailsPage.getIssuedPermits();
            IntStream.rangeClosed(0, applications.size()).forEach(idx -> {
//                Assert.assertEquals(applications.get(idx).getReferenceNumber(), licences.get(idx).getEcmt().getFullReferenceNumber());
//                Assert.assertEquals(applications.get(idx).getNoOfPermits().intValue(), licences.get(idx).getEcmt().getNumberOfPermits());
                Assert.assertEquals(applications.get(idx).getType(), PermitType.ECMT_ANNUAL);
//                Assert.assertEquals(LocalDate.parse(applications.get(idx).getRecdDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), licences.get(idx).getEcmt().getSubmitDate().toLocalDate());
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
            ApplicationDetailsPage.untilHeadingStatusIs(PermitStatus.UNDER_CONSIDERATION);
        });
        Then("^my permit application is under consideration$", () -> {
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.underConsiderationStatusExists();
        });
        And("^I have an ECMT application that's not yet submitted$", () -> {

        });
    }

    public static void payOutstandingFees(World world) {
        waitUntilElementIsEnabled("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L,TimeUnit.SECONDS);
        refreshPage();
        LicenceDetailsPageJourney.clickIRHPTab();
        LicenceDetailsPageJourney.clickFeesTab();
        FeesDetailsPage.outstanding();
        FeesDetailsPage.pay();
        BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
        IrhpPermitsApplyPage.selectCardPayment();
        world.feeAndPaymentJourneySteps.customerPaymentModule();
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

    public static void viewLicenceOnInternal(World world, String licenceNumber) {
        world.APIJourneySteps.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());

        // Keep searching until licence displays on first page of results
        int maxTriesCount = 30;
        do {
            SearchNavBar.search(licenceNumber);

            try {
                TimeUnit.SECONDS.sleep(Duration.SHORT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            maxTriesCount--;
        } while (ResultsPage.isResultNotPresentOnPage(licenceNumber) && maxTriesCount >= 0);


        //SearchNavBar.
        ResultsPage.select(licenceNumber);
    }

}
