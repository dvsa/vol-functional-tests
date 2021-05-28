package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenWindowModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitFeePage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.Declaration;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.dvsa.testing.lib.pages.external.permit.enums.sections.BilateralSection;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class AnnualBilateralSteps extends BasePage implements En {
    public AnnualBilateralSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {

        And("^I am on the country selection page$", () -> {
            PermitTypePage.untilElementIsPresent("//h1[contains(text(),'Select a permit type or certificate to apply for')]", SelectorType.XPATH,10L, TimeUnit.SECONDS);
            EcmtApplicationJourney.getInstance().permitType(PermitType.ANNUAL_BILATERAL, operatorStore);
            untilElementIsPresent("//h1[@class='govuk-fieldset__heading']", SelectorType.XPATH,10L, TimeUnit.SECONDS);
            EcmtApplicationJourney.getInstance().licencePage(operatorStore, world);
            CountrySelectionPage.untilOnPage();
        });
        Then("^I should be on the bilateral overview page$", OverviewPage::untilOnPage);
        Then("^the number of permits value is entered$", org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage::setNumberOfPermitsAndSetRespectiveValues);
        Then("^I select the fee tab on the selfserve$", () -> {
             waitAndClick("//a[contains(text(),'Home')]",SelectorType.XPATH);
             HomePage.selectTab(Tab.FEES);
        });
        Then("^the outstanding fees are displayed properly$", () -> {
            HomePage.FeesTab.hasOutstandingFees();
            HomePage.FeesTab.outstanbding(true);
        });
        Then("^I select save and continue button on select countries page$", CountrySelectionPage::saveAndContinue);
        Then("^countries are displayed in alphabetical order$", () -> {
            List<String> countries = CountrySelectionPage.countries();

            for (int idx = 0; idx < countries.size() - 1; idx++) {
                Assert.assertTrue(countries.get(idx).substring(0, 1).compareTo(countries.get(idx + 1).substring(0, 1)) <= 0);
            }
        });
        Then("^I can see the countries selected is listed in alphabetical order$", () -> {
            List<String> countries = CountrySelectionPage.countriesSelected();

            for (int idx = 0; idx < countries.size() - 1; idx++) {
                Assert.assertTrue(countries.get(idx).substring(0, 1).compareTo(countries.get(idx + 1).substring(0, 1)) <= 0);
            }
        });
        Then("^the bilateral countries page should display its error message$", CountrySelectionPage::hasErrorMessage);
        When("^I select a country from the bilateral countries page$", () -> {
            CountrySelectionPage.untilOnPage();
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);

            List<Country> countries = CountrySelectionPage.randomCountries();
            licence.getEcmt().setRestrictedCountries(countries);
        });
        Given("^I'm on the bilateral check your answers page$", () -> {
            AnnualBilateralJourney.getInstance().licencePage(operatorStore, world);
            OverviewPage.clickOverviewSection(OverviewSection.Countries);
            AnnualBilateralJourney.getInstance().countries(operatorStore)
                    .numberOfPermits(operatorStore);
            CheckYourAnswerPage.untilOnPage();
        });
        When("^I choose to change the bilateral countries section$", () -> {
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.clickChangeAnswer(BilateralSection.Country);
        });
        Then("^I should be on the bilateral countries page$", CountrySelectionPage::untilOnPage);
        And("^my previously selected countries should be remembered$", () -> {
            List<Country> countries = operatorStore.getLatestLicence().get().getEcmt().getRestrictedCountriesName();
            List<String> expectedCountries = countries.stream().map(Country::toString).collect(Collectors.toList());
            List<String> actualCountries = CountrySelectionPage.selectedCountries();

            Assert.assertThat(expectedCountries, equalTo(actualCountries));
        });
        Then("^I am able to complete an annual bilateral permit application$", () -> {
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            CheckYourAnswerPage.saveAndContinue();
            OverviewPage.untilOnPage();
            OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
            AnnualBilateralJourney.getInstance().declare(true)
                    .permitFee();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
            ApplicationSubmitPage.finish();
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            String titleText = getText("//h2[contains(text(),'Issued permits and certificates')]", SelectorType.XPATH);
            Assert.assertEquals(titleText, "Issued permits and certificates");
        });

        Given("^I have (a valid |applied for an )annual bilateral noway cabotage only permit$", (String notValid) -> {

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);

            int quantity = operatorStore.getLicences().size();

            IntStream.rangeClosed(1, quantity).forEach((i) -> {
                HomePage.applyForLicenceButton();
                AnnualBilateralJourney.getInstance().permitType(PermitType.ANNUAL_BILATERAL, operatorStore).licencePage(operatorStore, world);
                AnnualBilateralJourney.getInstance().norway(operatorStore);
                OverviewPage.untilOnPage();
                OverviewPage.clickCountrySection(Country.Norway);
                EssentialInformationPage.saveAndContinue();
                AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
                PermitUsagePage.untilOnPage();
                AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
                BilateralJourneySteps.clickYesToCabotage();
                BasePermitPage.saveAndContinue();
                NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
                BasePermitPage.saveAndContinue();
                BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
                OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
                licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
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
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickNoToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
                licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
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
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            PermitUsagePage.saveAndContinue();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
            licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
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
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().turkey(operatorStore);
            OverviewPage.untilOnPage();
        });
        Given("^I have selected Morocco and I am on the Bilateral application overview page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().morocco(operatorStore);
            OverviewPage.untilOnPage();
        });
        Given("^I have selected Ukraine and I am on the Bilateral application overview page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().ukraine(operatorStore);
            OverviewPage.untilOnPage();
        });
        Given("^I accept declaration and submit the application$", () -> {
            OverviewPage.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPage.untilOnPage();

            // Checking declaration page content
            DeclarationPage.hasPageHeading();
            DeclarationPage.hasBilateralAdvisoryTexts();
            DeclarationPage.hasWarningText();
            Declaration.saveAndContinue();
            DeclarationPage.hasErrorText();
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
            world.selfServeNavigation.navigateToNavBarPage("home");
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            AnnualBilateralJourney.getInstance().permitType(PermitType.ANNUAL_BILATERAL, operatorStore);
            LicenceStore licence = operatorStore.getLicences().get(0);
            BaseLicencePage.licence(licence.getLicenceNumber());
            SelectALicencePage.saveAndContinue();
        });
        Then("^I should be informed that there is already an active permit application for this licence$", SelectALicencePage::hasActivePermitMessage);
        Then("^I should be on the bilateral overview page for the active application already on the licence$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertTrue(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9, 18)));
        });
        Then("^I should be on the bilateral declaration page$", DeclarationPage::untilOnPage);
        When("^I save and continue on bilateral check your answers page$", CheckYourAnswerPage::saveAndContinue);
        And("^I have completed (an|all) annual bilateral application$", (String oneOrAll) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());            int quantity = oneOrAll.equalsIgnoreCase("all") ? operatorStore.getLicences().size() : 1;

            IntStream.rangeClosed(1, quantity).forEach((i) -> {
                HomePage.selectTab(Tab.PERMITS);
                HomePage.applyForLicenceButton();
                AnnualBilateralJourney.getInstance()
                            .permitType(PermitType.ANNUAL_BILATERAL, operatorStore);
                AnnualBilateralJourney.getInstance().licencePage(operatorStore, world);
                OverviewPage.clickOverviewSection(OverviewSection.Countries);
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
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
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
        And("^I navigate to the annual bilateral overview page$", () -> {
            String path = OverviewPage.RESOURCE_URL.replaceFirst("\\\\d\\+", operatorStore.getLatestLicence().get().getEcmt().getReferenceNumber());
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), path).toString());
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
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
            String actual = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(expectedReference, actual);
        });

        When("I select Norway in the filter list and click Apply filter", () -> {
           ValidAnnualBilateralPermitsPage.filter();
            ValidAnnualBilateralPermitsPage.selectNorway();
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
            Assert.assertTrue(org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.getLabel().contains(ValidAnnualBilateralPermitsPage.type()));

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
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
        });
        And("^I am on the annual bilateral number of permit page for bilateral standard permits no cabatoge path$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
        });
        And("^I am on the annual bilateral number of permit page for bilateral standard and cabotage permits path$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickNoToCabotage();
            BasePermitPage.saveAndContinue();
        });
        And("^I am on the annual bilateral number of permit page for bilateral standard and cabotage permits with cabotage confirmation path$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickYesToCabotage();
            AnnualBilateralJourney.getInstance().cabotageConfirmation(world,licenceStore);
            BasePermitPage.saveAndContinue();
        });
        Then("the page heading and the advisory text on standard and cabotage permits for cabotage only page are displayed correctly according to the selection", () -> {
            String actual = String.valueOf(PermitUsagePage.getJourney());
            String actual1 = actual.toLowerCase().substring(0,actual.length()-1);
            Assert.assertTrue(getElementValueByText("//div[contains(@class,'field')]//label", SelectorType.XPATH).contains(actual1 + " " + "permit"));
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasPageHeading();

        });
        Then("the page heading and the advisory text are displayed correctly according to the selection", () -> {
            String actual = String.valueOf(licenceStore.getEcmt().getJourneyType());
            String actual1 = actual.toLowerCase().substring(0,actual.length()-1);
            Assert.assertEquals(getElementValueByText("//div[contains(@class,'field')]//label",SelectorType.XPATH),"Cabotage"+" "+actual1+" "+"permit");
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasPageHeading();
            NumberOfPermitsPage.hasBilateralAdvisoryText();
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
            org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage.hasPageHeading();
        });
    }
    //TODO: This can all be refactored. There is so much duplication.
}