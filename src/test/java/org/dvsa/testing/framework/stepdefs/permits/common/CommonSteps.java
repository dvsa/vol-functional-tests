package org.dvsa.testing.framework.stepdefs.permits.common;

import Injectors.World;
import activesupport.string.Str;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.enums.Boolean;
import apiCalls.Utils.eupaBuilders.enums.TrafficArea;
import apiCalls.Utils.eupaBuilders.external.StandardResponseModel;
import apiCalls.Utils.eupaBuilders.internal.*;
import apiCalls.Utils.eupaBuilders.internal.enums.PaymentMethod;
import apiCalls.Utils.eupaBuilders.internal.enums.Status;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import apiCalls.eupaActions.external.ApplicationAPI;
import apiCalls.eupaActions.internal.CaseWorkerAPI;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.common.RandomUtils;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.VolLicenceSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.internal.IRHPPermitsPageSteps.payOutstandingFees;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertTrue;

public class CommonSteps extends BasePage implements En {

    public static Map<String, java.net.URL> origin;

    public CommonSteps(OperatorStore operator, World world) {
        Given("^I am on the VOL self-serve site$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());
        });

        Given("^I am on the VOL self-serve site with cookies$", () -> {
            refreshPage();
            String ssl="//*[@id='details-button']";
            String proceed="#proceed-link";
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());
            waitAndClick(ssl, SelectorType.XPATH);
            waitAndClick(proceed, SelectorType.CSS);
        });

        Given("^I have began applying for an (?:ECMT|Annual Bilateral|Annual Multilateral|Short-term ECMT) Permit$", () -> {
            clickToPermitTypePage(world);
        });
        Then ("^I submit the annual ECMT APGG application$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            BasePermitPage.saveAndContinue();
            RestrictedCountriesPage.deliverToRestrictedCountry(false);
            RestrictedCountriesPage.saveAndContinue();
            NumberOfPermitsPageJourney.completeECMTPage();
            EmissionStandardsPageJourney.completePage();
            BasePermitPage.saveAndContinue();
            DeclarationPageJourney.completeDeclaration();
            PermitFeePage.submitAndPay();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.goToPermitsDashboard();
        });
        And("^I Grant the application on internal$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operator.getOrganisationId()).getDashboard().getLicences().get(0);
            operator.setCurrentLicenceNumber(licence.getLicNo());
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
        });
        And("^I accept and pay the issuing fee on Selfserve$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            HomePage.PermitsTab.selectFirstOngoingApplication();
            ApplicationIssuingFeePage.acceptAndPay();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.goToPermitsDashboard();
            HomePage.PermitsTab.untilPermitHasStatus(
                    operator.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES);
        });
        Then("^the application can be viewed in issued permits table$", () -> {
            HomePage.PermitsTab.untilPermitHasStatus(
                    operator.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES);
        });
        And("^I am on the (Annual ECMT|Annual Bilateral \\(EU and EEA\\)|Annual Multilateral \\(EU and EEA\\)) licence page$", (String type) -> {
            clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance().permitType(PermitType.getEnum(type), operator);
            CommonSteps.origin.put("origin", getURL()); // Used to test a scenario for licence page
        });
        And("^I am on the Annual ECMT licence selection page$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operator);
            YearSelectionPage.selectECMTValidityPeriod();

        });
        Then("^I will get an error message on the licence page$", () -> {
            assertTrue(SelectALicencePage.isErrorMessagePresent());
        });
        Then("^I should be taken to the next section$", () -> {
            java.net.URL url = CommonSteps.origin.get("origin");
            Assert.assertThat(getURL(), is(not(equalTo(url))));
        });
        Then("^I should get an error message$", () -> {
             boolean hasError = BasePage.isErrorMessagePresent();
             assertTrue("Error message was not displayed on the page", hasError);
        });
        When("^I save and return to overview$", BasePermitPage::clickReturnToOverview);
        When("^I sign on as an external user$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());        });
        And("^all fees have been waived$", () -> {
            LicenceDetailsPageJourney.clickFeesTab();
            FeeDetailsPageJourney.whileFeesPresentWaveFee();
        });
        And("^A case worker begins to process my fee payment$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            payOutstandingFees(world);
            FeesDetailsPage.pay();
        });
        And("^I am on the permits dashboard on external$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
        });
        Then("^Information and Text appear correctly$", () -> {
            assertTrue(HomePage.PermitsTab.isPermitDashboardTextPresent());
        });
    }

    public static void payAndGrantApplication(@NotNull World world) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer version = VolLicenceSteps.version.get("version");

        // SETUP: CaseWorker#overview
        TrackingModel tracking = new TrackingModel()
                .withTrackingId(RandomUtils.number(5))
                .withAddressesStatus(Status.Unknown)
                .withBusinessDetailsStatus(Status.Unknown)
                .withBusinessTypeStatus(Status.Unknown)
                .withCommunityLicencesStatus(Status.Unknown)
                .withConditionsUndertakingsStatus(Status.Unknown)
                .withConvictionsPenaltiesStatus(Status.Unknown)
                .withFinancialEvidenceStatus(Status.Unknown)
                .withFinancialHistoryStatus(Status.Unknown)
                .withLicenceHistoryStatus(Status.Unknown)
                .withOperatingCentresStatus(Status.Unknown)
                .withPeopleStatus(Status.Unknown)
                .withSafetyStatus(Status.Unknown)
                .withTransportManagersStatus(Status.Unknown)
                .withTypeOfLicenceStatus(Status.Unknown)
                .withDeclarationsInternalStatus(Status.Unknown)
                .withVehiclesDeclarationsStatus(Status.Unknown)
                .withVehiclesStatus(Status.Unknown)
                .withVehiclesPsvStatus(Status.Unknown)
                .withVersion(1);

        String applicationId = VolLicenceSteps.applicationId.get("applicationId");
        TrafficArea trafficArea = VolLicenceSteps.trafficArea.get("trafficArea");
        OverviewModel overview = new OverviewModel()
                .withApplicationId(applicationId)
                .withLeadTcArea(trafficArea)
                .withOverrideOppositionDate(Boolean.TRUE)
                .withTrackingModel(tracking)
                .withVersion(++version);

        // Service Calls
        CaseWorkerAPI.overview(overview);
        ApplicationFeesModel applicationFees = ApplicationAPI.outstandingFees(VolLicenceSteps.applicationId.get("applicationId"));
        BigDecimal totalOutstandingFee = applicationFees.getTotalGrossAmount();

        List<Integer> feeIds = applicationFees.getOutstandingFeeIds();


        // SETUP: CaseWorker#payOutstandingFee
        FeesModel fees = new FeesModel()
                .withFeeIds(feeIds)
                .withOrganisationId(VolLicenceSteps.organisationId.get("organisationId"))
                .withApplicationId(VolLicenceSteps.applicationId.get("applicationId"))
                .withPaymentMethod(PaymentMethod.Cash)
                .withReceived(totalOutstandingFee)
                .withReceiptDate(LocalDateTime.now().format(dateFormatter))
                .withPayer(VolLicenceSteps.personFirstName.get("person.firstName"))
                .withSlipNo(Str.randomNumbers(6));

        // Service Calls
        ApplicationAPI.payOutstandingFee(fees);

        // SETUP: CaseWorker#grantApplication
        GrantApplicationModel grantApplication = new GrantApplicationModel()
                .withId(VolLicenceSteps.applicationId.get("applicationId"))
                .withDuePeriod("9")
                .withAuthority("grant_authority_dl")
                .withCaseworkerNotes(Str.randomWord(50, 100));

        // Service Calls
        StandardResponseModel responseModel = CaseWorkerAPI.grantApplication(grantApplication);

        totalOutstandingFee = BigDecimal.valueOf(Integer.MAX_VALUE / 10000);

        // Pay grant fee
        feeIds.clear();
        feeIds.add(responseModel.getId().getFeeId() == null ? 0 : responseModel.getId().getFeeId());
        fees.withFeeIds(feeIds);
        fees.withReceived(totalOutstandingFee);
        ApplicationAPI.payOutstandingFee(fees);
    }

    public static void clickToPermitTypePage(@NotNull World world) {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        HomePageJourney.beginPermitApplication();
    }

    public static void clickToPage(@NotNull OperatorStore operatorStore, @NotNull World world, @NotNull OverviewSection section) {
        beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
        OverviewPageJourney.clickOverviewSection(section);
    }

    public static void beginEcmtApplicationAndGoToOverviewPage(World world, OperatorStore operatorStore) {
        clickToPermitTypePage(world);
        EcmtApplicationJourney.getInstance()
                .permitType(PermitType.ECMT_ANNUAL, operatorStore);
        YearSelectionPage.selectECMTValidityPeriod();
        EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
        OverviewPage.untilOnPage();
    }
}
