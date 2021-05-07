package org.dvsa.testing.framework.stepdefs.permits.internal;

import activesupport.IllegalBrowserException;
import activesupport.aws.s3.S3;
import activesupport.string.Str;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.VolAccountJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.ApplicationDetailsPage;
import org.dvsa.testing.lib.pages.external.permit.FeePaymentConfirmationPage;
import org.dvsa.testing.lib.pages.internal.BaseModel;
import org.dvsa.testing.lib.pages.internal.ResultsPage;
import org.dvsa.testing.lib.pages.internal.SearchNavBar;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import javax.mail.MessagingException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.AwaitingFeePermitSteps.triggerPermitIssuing;

public class IRHPPermitsPageSteps extends BasePage implements En {
    LocalDate ld;
    public IRHPPermitsPageSteps(OperatorStore operator, World world) {
        When("^I am viewing a licences IRHP section$", () -> {
            refreshPage();
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
        });
        Then("^the no issued permits message should be displayed$", () -> Assert.assertTrue("Unable to find the no issued permits message", IrhpPermitsDetailsPage.hasNoPermitsMessage()));
        And("^the no permits applications message should be displayed$", () -> IrhpPermitsDetailsPage.hasNoPermitApplicationsMessage());
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
            VolAccountJourney.getInstance().signin(operator, world);

            // Apply for ECMT applications
            IntStream.rangeClosed(1, world.get("licence.quantity")).forEach((i) -> {
                EcmtApplicationJourney.getInstance().beginApplication();
                ECMTPermitApplicationSteps.completeEcmtApplication(operator, world);
            });

            // trigger issuing
            triggerPermitIssuing();

            // get list of all successful applications
            List<String> successfulApplications = getAllSuccessfulApplications(operator);

            // Search for licence
            viewLicenceOnInternal(Str.find("\\w{2}\\d{7}", successfulApplications.get(0)).get());

            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);

            world.put("ecmt.application.successful", successfulApplications);
        });
        Then("^The issued permit information should be as expected$", () -> {
            LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
            List<LicenceStore> licences = operator.getLicences(world.<List<String>>get("ecmt.application.successful").get(0));

            List<PermitApplication> applications = IrhpPermitsDetailsPage.getIssuedPermits();
            IntStream.rangeClosed(0, applications.size()).forEach(idx -> {
                Assert.assertEquals(applications.get(idx).getReferenceNumber(), licences.get(idx).getEcmt().getFullReferenceNumber());
                Assert.assertEquals(applications.get(idx).getNoOfPermits().intValue(), licences.get(idx).getEcmt().getNumberOfPermits());
                Assert.assertEquals(applications.get(idx).getType(), PermitType.ECMT_ANNUAL);
                Assert.assertEquals(LocalDate.parse(applications.get(idx).getRecdDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), licences.get(idx).getEcmt().getSubmitDate().toLocalDate());
            });
        });
        Then("^internal users should not be able to create ECMT Permit applications$", () -> {
            Assert.assertFalse(
                    "IRHP tab should NOT be present but was",
                    IrhpPermitsDetailsPage.Tab.hasTab(BaseDetailsPage.DetailsTab.IrhpPermits)
            );
        });
        And("^pay outstanding fees$", IRHPPermitsPageSteps::payOutstandingFees);
        Then("^my application should be under consideration$", () -> {
            ApplicationDetailsPage.Header.BREADCRUMB.statusIs(PermitStatus.UNDER_CONSIDERATION);
        });
        Then("^my permit application is under consideration$", () -> {
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.underConsiderationStatusExists();
        });
        And("^I have an ECMT application that's not yet submitted$", () -> {

        });
    }

    public static void payOutstandingFees() {
        waitUntilElementIsEnabled("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L,TimeUnit.SECONDS);
        refreshPage();
        LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.IrhpPermits);
        LicenceDetailsPage.Tab.select(LicenceDetailsPage.DetailsTab.Fees);
        FeesDetailsPage.outstanding();
        FeesDetailsPage.pay();
        BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
        IrhpPermitsApplyPage.selectCardPayment();
        EcmtApplicationJourney.getInstance()
                .cardDetailsPage()
                .cardHolderDetailsPage();
        FeePaymentConfirmationPage.makeMayment();
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

    public static void viewLicenceOnInternal(String licenceNumber) {
        deleteCookies();
        get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true)).toString());
        LoginPage.signIn("usr291", "password");

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
