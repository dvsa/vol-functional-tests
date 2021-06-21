package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenWindowModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.Country;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.licence.LicenceBasePage;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.Declaration;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.ValidECMTRemovalPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.hamcrest.Matchers;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.signInAndAcceptCookies;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.untilOnPage;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class AnnualBilateralSteps extends BasePage implements En {
    public AnnualBilateralSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {

        And("^I am on the country selection page$", () -> {
            PermitTypePage.untilElementIsPresent("//h1[contains(text(),'Select a permit type or certificate to apply for')]", SelectorType.XPATH,10L, TimeUnit.SECONDS);
            EcmtApplicationJourney.getInstance().permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore);
            LicenceBasePage.untilElementIsPresent("//h1[@class='govuk-fieldset__heading']", SelectorType.XPATH,10L, TimeUnit.SECONDS);
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            CountrySelectionPage.untilOnPage();
        });
        Then("^I should be on the bilateral overview page$", OverviewPage::untilOnOverviewPage);
        Then("^the number of permits value is entered$", NumberOfPermitsPage::numberOfPermits);
        Then("^I select the fee tab on the selfserve$", () -> {
             waitAndClick("//a[contains(text(),'Home')]",SelectorType.XPATH);
             HomePage.selectTab(Tab.FEES);
        });
        Then("^the outstanding fees are displayed properly$", () -> {
            HomePage.FeesTab.hasOutstandingFees();
            HomePage.FeesTab.outstanbding(true);
        });
        Then("^bilateral overview licence reference number is correct$", () -> {
            String expectedLicenceNumber = operatorStore.getLatestLicence().get().getLicenceNumber();
            String actualReferenceNumber = RestrictedCountriesPage.reference();
            Assert.assertThat(actualReferenceNumber, MatchesPattern.matchesPattern(expectedLicenceNumber.concat(" / \\d+")));
        });
        Then("^I select save and continue button on select countries page$", CountrySelectionPage::continueButton);
        Then("^the page heading on bilateral overview page is correct$", () -> {
            String expectedPageHeading = "Select the country you are transporting goods to";
            String actualPageHeading = OverviewPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);
        });
        Then("^countries are displayed in alphabetical order$", () -> {
            List<String> countries = RestrictedCountriesPage.countries();

            for (int idx = 0; idx < countries.size() - 1; idx++) {
                Assert.assertTrue(countries.get(idx).substring(0, 1).compareTo(countries.get(idx + 1).substring(0, 1)) <= 0);
            }
        });
        Then("^I can see the countries selected is listed in alphabetical order$", () -> {
            List<String> countries = RestrictedCountriesPage.countriesSelected();

            for (int idx = 0; idx < countries.size() - 1; idx++) {
                Assert.assertTrue(countries.get(idx).substring(0, 1).compareTo(countries.get(idx + 1).substring(0, 1)) <= 0);
            }
        });
        Then("^the bilateral countries page should display its error message$", RestrictedCountriesPage::hasErrorMessage);
        When("^I select a country from the bilateral countries page$", () -> {
            RestrictedCountriesPage.untilOnPage();
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);

            List<Country> countries = RestrictedCountriesPage.randomCountries();
            licence.getEcmt().setRestrictedCountries(countries);
        });
        When("^I select countries from the bilateral countries page$", () -> {
            RestrictedCountriesPage.untilOnPage();
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);

            List<Country> countries = RestrictedCountriesPage.randomCountries();
            licence.getEcmt().setRestrictedCountries(countries);
        });
        Then("^I should be on the bilateral number of permits page$", NumberOfPermitsPage::untilOnPage);
        Given("^I'm on the bilateral check your answers page$", () -> {
            AnnualBilateralJourney.getInstance().licencePage(operatorStore, world);
            OverviewPage.select(OverviewPage.Section.Countries);
            AnnualBilateralJourney.getInstance().countries(operatorStore)
                    .numberOfPermits(operatorStore);
            CheckYourAnswersPage.untilOnPage();
        });
        When("^I choose to change the bilateral countries section$", () -> {
            CheckYourAnswersPage.untilOnPage();
            CheckYourAnswersPage.change(CheckYourAnswersPage.Section.Country);
        });
        Then("^I should be on the bilateral countries page$", RestrictedCountriesPage::untilOnPage);
        And("^my previously selected countries should be remembered$", () -> {
            List<Country> countries = operatorStore.getLatestLicence().get().getEcmt().getRestrictedCountriesName();
            List<String> expectedCountries = countries.stream().map(Country::toString).collect(Collectors.toList());
            List<String> actualCountries = RestrictedCountriesPage.selectedCountries();

            Assert.assertThat(expectedCountries, equalTo(actualCountries));
        });
        And("^my selected licence does not have an existing annual bilateral permit$", () -> {
            // Here for readability and to stop cucumber from throwing an exception
        });
        Then("^I am able to complete an annual bilateral permit application$", () -> {
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.continueButton();
            CabotagePage.yesButton();
            BasePermitPage.bilateralSaveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            BasePermitPage.bilateralSaveAndContinue();
            CheckYourAnswersPage.confirmAndReturnToOverview();
            OverviewPage.untilOnOverviewPage();
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true)
                    .permitFee();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
            ApplicationSubmitPage.finish();
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            BilateralDashboardPage.BilateralsDashboardPageIssuedCheck();
        });

        Given("^I have (a valid |applied for an )annual bilateral noway cabotage only permit$", (String notValid) -> {

            signInAndAcceptCookies(world);
            HomePage.selectTab(Tab.PERMITS);

            int quantity = operatorStore.getLicences().size();

            IntStream.rangeClosed(1, quantity).forEach((i) -> {
                HomePage.applyForLicenceButton();
                AnnualBilateralJourney.getInstance().permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore).licencePage(operatorStore, world);
                AnnualBilateralJourney.getInstance().norway(operatorStore);
                OverviewPage.untilOnOverviewPage();
                OverviewPage.clickNorway();
                EssentialInformationPage.bilateralEssentialInfoContinueButton();
                AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
                PermitUsagePage.untilOnPermitUsagePage();
                AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
                CabotagePage.yesButton();
                BasePermitPage.saveAndContinue();
                NumberOfPermitsPage.numberOfPermits();
                String expected= BasePermitPage.getElementValueByText("//label[contains(text(),'Cabotage')]",SelectorType.XPATH);
                operatorStore.setPermit(expected);
                BasePermitPage.saveAndContinue();
                BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
                OverviewPage.selectDeclaration();
                licenceStore.setReferenceNumber(DeclarationPage.reference());
                AnnualBilateralJourney.getInstance().declare(true)
                            .permitFee();
                 EcmtApplicationJourney.getInstance()
                            .cardDetailsPage()
                            .cardHolderDetailsPage()
                            .confirmAndPay();
                BaseApplicationSubmitPage.untilSubmittedPageLoad();
                ApplicationSubmitPage.finish();
                String reference1 = String.valueOf(operatorStore.getCurrentLicenceNumber());
                HomePage.PermitsTab.untilPermitHasStatus(
                        operatorStore.getCurrentLicence().get().getReferenceNumber(),
                        PermitStatus.VALID,
                        Duration.LONG,
                        TimeUnit.MINUTES
                );
            });

        });
        Given("^I have (a valid |applied for an )annual bilateral norway standard and cabotage permit$", (String notValid) -> {

            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.continueButton();
            CabotagePage.noButton();
            BasePermitPage.bilateralSaveAndContinue();
            NumberOfPermitsPage.numberOfPermitsNew();
            NumberOfPermitsPage.setCabotageValue(NumberOfPermitsPage.getCabotageValue());
            NumberOfPermitsPage.setStandardValue(NumberOfPermitsPage.getStandardValue());
            NumberOfPermitsPage.setFieldCount(NumberOfPermitsPage.getFieldCount());
            NumberOfPermitsPage.setLabel(NumberOfPermitsPage.permitLabel());
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            BasePermitPage.bilateralSaveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
                licenceStore.setReferenceNumber(DeclarationPage.reference());
                AnnualBilateralJourney.getInstance().declare(true)
                        .permitFee();
                EcmtApplicationJourney.getInstance()
                            .cardDetailsPage()
                            .cardHolderDetailsPage()
                            .confirmAndPay();
                BaseApplicationSubmitPage.untilSubmittedPageLoad();
                ApplicationSubmitPage.finish();
                String reference1 = String.valueOf(operatorStore.getCurrentLicenceNumber());
                HomePage.PermitsTab.untilPermitHasStatus(
                        operatorStore.getCurrentLicence().get().getReferenceNumber(),
                        PermitStatus.VALID,
                        Duration.LONG,
                        TimeUnit.MINUTES
                );
            });

        Given("^I have (a valid |applied for an )annual bilateral norway standard no cabotage permit$", (String notValid) -> {

            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            PermitUsagePage.continueButton();
            BasePermitPage.bilateralSaveAndContinue();
            NumberOfPermitsPage.numberOfPermitsNew();
            NumberOfPermitsPage.setFieldCount(NumberOfPermitsPage.getFieldCount());
            NumberOfPermitsPage.setLabel(NumberOfPermitsPage.permitLabel());
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            BasePermitPage.bilateralSaveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
            licenceStore.setReferenceNumber(DeclarationPage.reference());
            AnnualBilateralJourney.getInstance().declare(true)
                        .permitFee();
             EcmtApplicationJourney.getInstance()
                        .cardDetailsPage()
                        .cardHolderDetailsPage()
                        .confirmAndPay();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
            ApplicationSubmitPage.finish();
            String reference1 = String.valueOf(operatorStore.getCurrentLicenceNumber());
            HomePage.PermitsTab.untilPermitHasStatus(
                    operatorStore.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );
        });
        Given("^I have selected Turkey and I am on the Bilateral application overview page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().turkey(operatorStore);
            OverviewPage.untilOnOverviewPage();
        });
        Given("^I have selected Morocco and I am on the Bilateral application overview page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().morocco(operatorStore);
            OverviewPage.untilOnOverviewPage();
        });
        Given("^I have selected Ukraine and I am on the Bilateral application overview page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().ukraine(operatorStore);
            OverviewPage.untilOnOverviewPage();
        });
        Given("^I accept declaration and submit the application$", () -> {
            OverviewPage.selectDeclaration();
            DeclarationPage.untilOnDeclarationPage();

            // Checking declaration page content
            DeclarationPage.pageHeading();
            DeclarationPage.advisoryTexts();
            DeclarationPage.warningText();
            Declaration.acceptAndContinue();
            DeclarationPage.errorText();
            org.dvsa.testing.lib.pages.external.permit.DeclarationPage.declare(true);
            Declaration.saveAndContinue();
            PermitFeePage.untilOnPage();
            PermitFeePage.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();

        });
        When("^I try applying with a licence that has an existing application$", () -> {
            OverviewPage.overviewToHome();
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            AnnualBilateralJourney.getInstance().permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore);
            LicenceStore licence = operatorStore.getLicences().get(0);
            LicencePage.licence(licence.getLicenceNumber());
            LicencePage.saveAndContinue();
        });
        Then("^I should be informed that there is already an active permit application for this licence$", () -> {
            Assert.assertTrue("Message informing user that the selected licence already has an active " +
                    "application was not displayed", LicencePage.hasActivePermitMessage());
        });
        Then("^I should be on the bilateral overview page for the active application already on the licence$", () -> {
            OverviewPage.untilOnPage();
            String actualReference = OverviewPage.reference();
            Assert.assertTrue(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9, 18)));
        });
        Then("^I should be on the bilateral declaration page$", DeclarationPage::untilOnPage);
        When("^I save and continue on bilateral check your answers page$", CheckYourAnswersPage::saveAndContinue);
        And("^I have completed (an|all) annual bilateral application$", (String oneOrAll) -> {
            signInAndAcceptCookies(world);
            int quantity = oneOrAll.equalsIgnoreCase("all") ? operatorStore.getLicences().size() : 1;

            IntStream.rangeClosed(1, quantity).forEach((i) -> {
                HomePage.selectTab(Tab.PERMITS);
                HomePage.applyForLicenceButton();
                AnnualBilateralJourney.getInstance()
                            .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore);
                AnnualBilateralJourney.getInstance().licencePage(operatorStore, world);
                OverviewPage.select(OverviewPage.Section.Countries);
                AnnualBilateralJourney.getInstance().countries(operatorStore)
                            .numberOfPermits(operatorStore)
                            .checkYourAnswers()
                            .declare(true)
                            .permitFee();
                 EcmtApplicationJourney.getInstance()
                            .cardDetailsPage()
                            .cardHolderDetailsPage()
                            .confirmAndPay()
                            .submitApplication(operatorStore.getLatestLicence().get(), world);
            });
            System.out.println("WOO");
        });
        And("^I have partial annual bilateral applications$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.setReferenceNumber(OverviewPage.reference());
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            CabotagePage.yesButton();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            operatorStore.setPermit(NumberOfPermitsPage.permitValue);
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            });
        Then("^ongoing permits should be sorted by reference number in descending order$", () -> {
            List<PermitApplication> actualApplications = HomePage.PermitsTab.ongoingPermitApplications();
            IntStream.range(0, actualApplications.size() - 1).forEach((i) -> {
                String ref2 = actualApplications.get(i).getReferenceNumber();
                String ref1 = actualApplications.get(i + 1).getReferenceNumber();
                Assert.assertThat(ref1, Matchers.greaterThanOrEqualTo(ref2));
            });
        });
        And("^I continue my current annual bilateral permit$", () -> {
            signInAndAcceptCookies(world);
            get(OverviewPage.url(operatorStore.getLatestLicence().get().getEcmt().getReferenceNumber()));
        });
        Then("^the check your answers section should be complete$", () -> {
            //     boolean isComplete = OverviewPage.checkStatus(OverviewPage.Section.CheckYourAnswers, PermitStatus.COMPLETED);
            //   Assert.assertTrue("The 'check your answers' section status is not complete", isComplete);
        });
        And("^I navigate to the annual bilateral overview page$", () -> {
            String path = OverviewPage.RESOURCE.replaceFirst("\\\\d\\+", operatorStore.getLatestLicence().get().getEcmt().getReferenceNumber());
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), path).toString());
            OverviewPage.untilOnPage();
        });
        And("I am viewing an issued annual bilateral permit on self-serve", () -> {
            LicenceStore licence = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalStateException::new);
            HomePage.PermitsTab.select(licence.getLicenceNumber());
            ValidAnnualBilateralPermitsPage.untilOnPage();
        });
        And("the relevant error message for annual bilateral number of permits page is displayed", () -> {
            Assert.assertEquals(getElementValueByText("//p[@class='error__text']",SelectorType.XPATH),"Enter the number of permits you require");
        });
        And("the user is in the annual bilateral list page", ValidAnnualBilateralPermitsPage::untilOnPage);

        And("^the licence number is displayed in Annual bilateral list page$", () -> {
            String expectedReference = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            String actual = ValidECMTRemovalPermitsPage.reference();
            Assert.assertEquals(expectedReference, actual);
        });

        When("I select Norway in the filter list and click Apply filter", () -> {
           ValidAnnualBilateralPermitsPage.filter();
            ValidAnnualBilateralPermitsPage.selectNorway();
        });
        Then("the results are filtered successfully", () -> {

        });
        Then("^the table of annual bilateral permits is as expected$", () -> {
            OpenByCountryModel stock = IrhpPermitWindowAPI.openByCountry();
            String message =  "Expected all permits to have a status of 'Pending' but one or more DIDN'T!!!";
            OperatorStore store = operatorStore;
            List<ValidAnnualBilateralPermitsPage.Permit> permits = ValidAnnualBilateralPermitsPage.permits();

            List<OpenWindowModel> windows = stock.openWindowsFor(permits.stream().map(p -> p.getCountry().toString()).toArray(String[]::new));

            // Verify status is pending
            // Changed to assert is VALID
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));

            // Verify that Type is displayed as per the selection
            Assert.assertTrue(operatorStore.getPermit().contains(ValidAnnualBilateralPermitsPage.type()));

            // Check permit number is in ascending order grouped by country
            Map<Country, List<String>> grouped = permits.stream().collect(
                    Collectors.groupingBy(ValidAnnualBilateralPermitsPage.Permit::getCountry,
                            Collectors.mapping(ValidAnnualBilateralPermitsPage.Permit::getApplication,
                                    Collectors.toList()))
            );

            grouped.forEach((k, v) -> IntStream.range(0, v.size() - 1).forEach(i -> Assert.assertThat(v.get(i),
                    lessThanOrEqualTo(v.get(i+1)))));

            // Verify it's listed by country going in ascending order
            IntStream.range(0, permits.size() - 1).forEach((idx) -> {
                List<LocalDate> expiryDates = stock.openWindowsFor(permits.get(idx).getCountry().toString())
                                .stream().map(win -> win.getIrhpPermitStockModel().getValidTo()).collect(Collectors.toList());

                // Check Country order
                Assert.assertTrue(permits.get(idx).getCountry().compareTo(permits.get(idx + 1).getCountry()) <= 0);
                // Check expiry date is in ascending order
                Assert.assertTrue(
                        permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                                permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
                );
                // Check expiry date matches that of stock window
                Assert.assertThat(expiryDates, hasItem(permits.get(idx).getExpiryDate()));
            });
        });
        And("^I am on the annual bilateral number of permit page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            CabotagePage.yesButton();
            BasePermitPage.saveAndContinue();
        });
        And("^I am on the annual bilateral number of permit page for bilateral standard permits no cabatoge path$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
        });
        And("^I am on the annual bilateral number of permit page for bilateral standard and cabotage permits path$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            CabotagePage.noButton();
            BasePermitPage.saveAndContinue();
        });
        And("^I am on the annual bilateral number of permit page for bilateral standard and cabotage permits with cabotage confirmation path$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.continueButton();
            CabotagePage.yesButton();
            AnnualBilateralJourney.getInstance().cabotageConfirmation(world,licenceStore);
            BasePermitPage.saveAndContinue();
        });
        Then("the page heading and the advisory text on standard and cabotage permits for cabotage only page are displayed correctly according to the selection", () -> {
            String actual = String.valueOf(PermitUsagePage.getJourney());
            String actual1 = actual.toLowerCase().substring(0,actual.length()-1);
            Assert.assertTrue(getElementValueByText("//div[contains(@class,'field')]//label", SelectorType.XPATH).contains(actual1 + " " + "permit"));
            NumberOfPermitsPage.pageHeading();

        });
        Then("the page heading and the advisory text are displayed correctly according to the selection", () -> {
            String actual = String.valueOf(licenceStore.getEcmt().getJourneyType());
            String actual1 = actual.toLowerCase().substring(0,actual.length()-1);
            Assert.assertEquals(getElementValueByText("//div[contains(@class,'field')]//label",SelectorType.XPATH),"Cabotage"+" "+actual1+" "+"permit");
            NumberOfPermitsPage.pageHeading();
            NumberOfPermitsPage.advisoryText();
        });
        Then("the page heading and the advisory text on standard permits no cabotage page are displayed correctly according to the selection", () -> {
            String actual = String.valueOf(licenceStore.getEcmt().getJourneyType());
            String actual1 = actual.toLowerCase().substring(0,actual.length()-1);
            Assert.assertEquals(getElementValueByText("//div[contains(@class,'field')]//label",SelectorType.XPATH),"Standard"+" "+actual1+" "+"permit");
            if(actual1.contains("multiple")) {
                Assert.assertEquals(getElementValueByText("//p[@class='hint']", SelectorType.XPATH), "Allows an unlimited number of journeys to, and transits through, this country.");
            }
            else
                Assert.assertEquals(getElementValueByText("//p[@class='hint']",SelectorType.XPATH),"Valid for one outward and return journey and transit.");
            NumberOfPermitsPage.pageHeading();
        });
    }
}