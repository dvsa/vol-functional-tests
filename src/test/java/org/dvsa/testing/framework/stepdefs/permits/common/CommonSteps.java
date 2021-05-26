package org.dvsa.testing.framework.stepdefs.permits.common;

import activesupport.string.Str;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.enums.TrafficArea;
import apiCalls.Utils.eupaBuilders.external.StandardResponseModel;
import apiCalls.Utils.eupaBuilders.internal.*;
import apiCalls.Utils.eupaBuilders.internal.enums.PaymentMethod;
import apiCalls.Utils.eupaBuilders.internal.enums.Status;
import apiCalls.Utils.eupaBuilders.enums.Boolean;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import apiCalls.eupaActions.external.ApplicationAPI;
import apiCalls.eupaActions.internal.CaseWorkerAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.RandomUtils;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.VolLicenceSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.ChangeYourPasswordPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.EuroEmissioStandardsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PermitFee;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
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
import static org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue;
import static org.hamcrest.CoreMatchers.*;

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
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CabotagePage.wontCarryCabotage(true);
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.certificatesRequired(true);
            BasePermitPage.saveAndContinue();
            RestrictedCountriesPage.deliverToRestrictedCountry(false);
            BasePermitPage.saveAndContinue();
            euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            BasePermitPage.saveAndContinue();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
            PermitFee.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BaseApplicationSubmitPage.finish();
        });
        And("^I Grant the application on internal$", () -> {
            LicenceModel licence = OrganisationAPI.dashboard(operator.getOrganisationId()).getDashboard().getLicences().get(0);
            operator.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            System.out.println(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
        });
        And("^I accept and pay the issuing fee on Selfserve$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            String licence1= operator.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
            ApplicationIssuingFeePage.acceptAndPay();
            EcmtApplicationJourney.getInstance().cardDetailsPage().cardHolderDetailsPage().confirmAndPay();
            BaseApplicationSubmitPage.finish();
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
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operator);
            YearSelectionPage.EcmtValidityPeriod();

        });
        When("^I login to self-serve on VOL$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            if (ChangeYourPasswordPage.onPage()) {
                operator.setPassword(Str.randomWord(7).concat("1Pp"));
                ChangeYourPasswordPage.update(operator.getPreviousPassword(), operator.getPassword());
            }
        });
        Then("^the user is navigated to the next page$", () -> {
            Assert.assertNotEquals(CommonSteps.origin.get("origin"), getURL().toString());
        });
        Then("^I will get an error message on the licence page$", () -> {
            Assert.assertTrue(LicencePage.hasErrorMessagePresent());
        });
        When("^I use the application back button$", BasePermitPage::back);
        Then("^I should be taken to the next section$", () -> {
            java.net.URL url = CommonSteps.origin.get("origin");
            Assert.assertThat(getURL(), is(not(equalTo(url))));
        });
        Then("^I should not be taken to the next section$", () -> {
            java.net.URL url = CommonSteps.origin.get("origin");
            Assert.assertTrue("The current URL path does not match the expected one", isPath(url.getPath()));
        });
        Then("^I should get an error message$", () -> {
            boolean hasError = BasePage.hasErrorMessagePresent();
             Assert.assertTrue("Error message was not displayed on the page", hasError);
        });
        When("^I save and return to overview$", BasePermitPage::overview);
        When("^I go back$", BasePermitPage::back);
        When("^I sign on as an external user$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());        });
        And("^all fees have been waived$", () -> {
            LicenceDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);
            while (FeesDetailsPage.hasFee()) {
                FeesDetailsPage.select1stFee();
                FeesDetailsPage.waive(true);
                FeesDetailsPage.waiveNote(Str.randomWord(180));
                FeesDetailsPage.recommandWaiver();
                FeesDetailsPage.waive(FeesDetailsPage.Decision.Approve);
            }
        });
        And("^A case worker begins to process my fee payment$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            payOutstandingFees();
            FeesDetailsPage.pay();
        });
        And("^I am on the permits dashboard on external$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
        });
        Then("^Information and Text appear correctly$", () -> {

            Permits.permitDashboardText();
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
        HomePage.selectTab(Tab.PERMITS);
        HomePage.applyForLicenceButton();
    }

    public static void clickToPage(@NotNull OperatorStore operatorStore, @NotNull World world, @NotNull OverviewSection section) {
        beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(section);
    }

    public static void beginEcmtApplicationAndGoToOverviewPage(World world, OperatorStore operatorStore) {
        LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);

        clickToPermitTypePage(world);
        EcmtApplicationJourney.getInstance()
                .permitType(PermitType.ECMT_ANNUAL, operatorStore);
        YearSelectionPage.EcmtValidityPeriod();
        EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
    }
}
