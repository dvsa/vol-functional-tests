package org.dvsa.testing.framework.stepdefs.permits.common;

import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import activesupport.system.Properties;
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
import org.dvsa.testing.framework.Journeys.permits.external.VolAccountJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.RandomUtils;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.ChangeYourPasswordPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
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
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.internal.IRHPPermitsPageSteps.payOutstandingFees;
import static org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue;
import static org.hamcrest.CoreMatchers.*;

public class CommonSteps extends BasePage implements En {

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
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
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
            CheckYourAnswersPage.saveAndContinue();
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

            BaseInternalJourney.getInstance().openLicence(
                    licence.getLicenceId()
            ).signin();
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

            LoginPage.signIn(world.get("username"), world.get("password"));
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
            EcmtApplicationJourney.getInstance().permitType(PermitTypePage.PermitType.toEnum(type), operator);
            world.put("origin", getURL()); // Used to test a scenario for licence page
        });
        And("^I am on the Annual ECMT licence selection page$", () -> {
            signIn(world);
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtAnnual, operator);
            YearSelectionPage.EcmtValidityPeriod();

        });
        When("^I login to self-serve on VOL$", () -> {
            LoginPage.signIn(operator.getUsername(), operator.getPassword());

            if (ChangeYourPasswordPage.onPage()) {
                operator.setPassword(Str.randomWord(7).concat("1Pp"));
                ChangeYourPasswordPage.update(operator.getPreviousPassword(), operator.getPassword());
            }
        });
        Then("^the user is navigated to the next page$", () -> {
            Assert.assertNotEquals(world.get("origin"), getURL().toString());
        });
        Then("^I will get an error message on the licence page$", () -> {
            Assert.assertTrue(LicencePage.hasErrorMessagePresent());
        });
        When("^I use the application back button$", BasePermitPage::back);
        Then("^I should be taken to the next section$", () -> {
            java.net.URL url = world.get("origin");
            Assert.assertThat(getURL(), is(not(equalTo(url))));
        });
        Then("^I should not be taken to the next section$", () -> {
            java.net.URL url = world.get("origin");
            Assert.assertTrue("The current URL path does not match the expected one", isPath(url.getPath()));
        });
        Then("^I should get an error message$", () -> {
            NumberOfTripsPage.maxErrorMessage();
            //  boolean hasError = BasePage.hasErrorMessagePresent();
            // Assert.assertTrue("Error message was not displayed on the page", hasError);
        });
        When("^I save and return to overview$", BasePermitPage::overview);
        When("^I go back$", BasePermitPage::back);
        When("^I sign on as an external user$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());

            VolAccountJourney.getInstance().signin(operator, world);
        });
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
            BaseInternalJourney.getInstance().openLicence(
                    OrganisationAPI.dashboard(operator.getOrganisationId()).getDashboard().getLicences().get(0).getLicenceId()
            ).signin();
            payOutstandingFees();
            FeesDetailsPage.pay();
        });
        And("^I am on the permits dashboard on external$", () -> {
            signIn(world);
            HomePage.selectTab(Tab.PERMITS);
        });
        Then("^Information and Text appear correctly$", () -> {

            Permits.permitDashboardText();
        });
    }

    public static void payAndGrantApplication(@NotNull World world) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer version = world.get("version");

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

        OverviewModel overview = new OverviewModel()
                .withApplicationId(world.get("applicationId"))
                .withLeadTcArea(world.get("trafficArea"))
                .withOverrideOppositionDate(Boolean.TRUE)
                .withTrackingModel(tracking)
                .withVersion(++version);

        // Service Calls
        CaseWorkerAPI.overview(overview);
        ApplicationFeesModel applicationFees = ApplicationAPI.outstandingFees(world.get("applicationId"));
        BigDecimal totalOutstandingFee = applicationFees.getTotalGrossAmount();

        List<Integer> feeIds = applicationFees.getOutstandingFeeIds();


        // SETUP: CaseWorker#payOutstandingFee
        FeesModel fees = new FeesModel()
                .withFeeIds(feeIds)
                .withOrganisationId(world.get("organisationId"))
                .withApplicationId(world.get("applicationId"))
                .withPaymentMethod(PaymentMethod.Cash)
                .withReceived(totalOutstandingFee)
                .withReceiptDate(LocalDateTime.now().format(dateFormatter))
                .withPayer(world.get("person.firstName"))
                .withSlipNo(Str.randomNumbers(6));

        // Service Calls
        ApplicationAPI.payOutstandingFee(fees);

        // SETUP: CaseWorker#grantApplication
        GrantApplicationModel grantApplication = new GrantApplicationModel()
                .withId(world.get("applicationId"))
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
        signIn(world);
        HomePage.selectTab(Tab.PERMITS);
        HomePage.applyForLicenceButton();
    }

    public static void clickToPage(@NotNull OperatorStore operatorStore, @NotNull World world, @NotNull PermitSection section) throws MalformedURLException, IllegalBrowserException {
        beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
        OverviewPage.section(section);
    }

    public static void beginEcmtApplicationAndGoToOverviewPage(World world, OperatorStore operatorStore) throws MalformedURLException, IllegalBrowserException {
        LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);

        clickToPermitTypePage(world);
        EcmtApplicationJourney.getInstance()
                .permitType(PermitTypePage.PermitType.EcmtAnnual, operatorStore);
        YearSelectionPage.EcmtValidityPeriod();
        EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
    }

    public static void signIn(World world) {
        LoginPage.signIn(world.get("username"), world.get("password"));
        String newPassword = Str.randomWord(7).concat("13JSs");
        if (ChangeYourPasswordPage.onPage()) {
            ChangeYourPasswordPage.update(world.get("password"), newPassword);

            world.put("password", newPassword);
        }
    }
}
