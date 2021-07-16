package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenWindowModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.pageObjects.enums.*;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualBilateralPermit;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnnualBilateralSteps extends BasePage implements En {
    public AnnualBilateralSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        Then("^I should be on the overview page$", () -> {
            OverviewPage.untilOnPage();
            OverviewPageJourney.hasPageHeading();
        });
        When("^I select a country from the bilateral countries page$", () -> {
            CountrySelectionPage.untilOnPage();
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);

            List<Country> countries = CountrySelectionPage.randomCountries();
            licence.getEcmt().setRestrictedCountries(countries);
        });

        Given("^I have (a valid |applied for an )annual bilateral noway cabotage only permit$", (String notValid) -> {

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();

            int quantity = operatorStore.getLicences().size();

            IntStream.rangeClosed(1, quantity).forEach((i) -> {
                HomePage.applyForLicenceButton();
                AnnualBilateralJourney.getInstance().permitType(PermitType.ANNUAL_BILATERAL, operatorStore).licencePage(operatorStore, world);
                AnnualBilateralJourney.getInstance().norway(operatorStore);
                OverviewPage.untilOnPage();
                OverviewPage.clickCountrySection(Country.Norway);
                EssentialInformationPageJourney.completePage();
                AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
                PermitUsagePage.untilOnPage();
                AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
                BilateralJourneySteps.clickYesToCabotage();
                BasePermitPage.saveAndContinue();
                NumberOfPermitsPageJourney.completePage();
                BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
                OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
                licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
                DeclarationPageJourney.completeDeclaration();
                AnnualBilateralJourney.getInstance()
                            .permitFee();
                world.feeAndPaymentJourney.customerPaymentModule();
                SubmittedPage.untilOnPage();
                SubmittedPage.goToPermitsDashboard();
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
            EssentialInformationPageJourney.completePage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickNoToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
            licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
            DeclarationPageJourney.completeDeclaration();
            AnnualBilateralJourney.getInstance()
                        .permitFee();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();
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
            EssentialInformationPageJourney.completePage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            PermitUsagePage.saveAndContinue();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
            licenceStore.setReferenceNumber(BasePermitPage.getReferenceFromPage());
            DeclarationPageJourney.completeDeclaration();
            AnnualBilateralJourney.getInstance()
                        .permitFee();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();
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
            OverviewPageJourney.clickBilateralOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPage.untilOnPage();

            // Checking declaration page content
            DeclarationPageJourney.hasPageHeading();
            assertTrue(DeclarationPage.isBilateralAdvisoryTextPresent());
            assertTrue(DeclarationPage.isWarningTextPresent());
            DeclarationPage.saveAndContinue();
            DeclarationPageJourney.hasErrorText();
            DeclarationPageJourney.completeDeclaration();
            PermitFeePage.untilOnPage();
            PermitFeePage.submitAndPay();
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();

        });
        Then("^I should be informed that there is already an active permit application for this licence$", () -> {
            String message = SelectALicencePage.getActivePermitMessage();
            assertTrue(message.contains("You've already started an application using this licence. Click 'Save and continue' to access this application"));
        });
        And("^I have completed (an|all) annual bilateral application$", (String oneOrAll) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());            int quantity = oneOrAll.equalsIgnoreCase("all") ? operatorStore.getLicences().size() : 1;

            IntStream.rangeClosed(1, quantity).forEach((i) -> {
                HomePageJourney.beginPermitApplication();
                AnnualBilateralJourney.getInstance()
                            .permitType(PermitType.ANNUAL_BILATERAL, operatorStore);
                AnnualBilateralJourney.getInstance().licencePage(operatorStore, world);
                OverviewPageJourney.clickOverviewSection(OverviewSection.Countries);
                AnnualBilateralJourney.getInstance().countries(operatorStore);
                NumberOfPermitsPageJourney.completeBilateralPage();
                AnnualBilateralJourney.getInstance().checkYourAnswers();
                DeclarationPageJourney.completeDeclaration();
                AnnualBilateralJourney.getInstance()
                            .permitFee();
                world.feeAndPaymentJourney.customerPaymentModule();
                EcmtApplicationJourney.getInstance()
                            .submitApplication(operatorStore.getLatestLicence().get(), world);
            });
        });
        And("I am viewing an issued annual bilateral permit on self-serve", () -> {
            HomePage.PermitsTab.selectFirstValidPermit();
            ValidPermitsPage.untilOnPage();
        });
        And("the user is in the annual bilateral list page", () -> {
            ValidPermitsPage.untilOnPage();
            ValidPermitsPageJourney.hasBilateralHeading();

        });
        Then("^the table of annual bilateral permits is as expected$", () -> {
            OpenByCountryModel stock = IrhpPermitWindowAPI.openByCountry();
            String message =  "Expected all permits to have a status of 'Pending' but one or more DIDN'T!!!";
            List<ValidAnnualBilateralPermit> permits = ValidPermitsPage.annualBilateralPermits();

            List<OpenWindowModel> windows = stock.openWindowsFor(permits.stream().map(p -> p.getCountry().toString()).toArray(String[]::new));

            // Verify status is pending
            // Changed to assert is VALID
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));

            // Verify that Type is displayed as per the selection
            Assert.assertTrue(NumberOfPermitsPageJourney.getLabel().contains(ValidPermitsPage.getType()));

            // Check permit number is in ascending order grouped by country
            Map<Country, List<String>> grouped = permits.stream().collect(
                    Collectors.groupingBy(ValidAnnualBilateralPermit::getCountry,
                            Collectors.mapping(ValidAnnualBilateralPermit::getApplication,
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
    }
    //TODO: This can all be refactored. There is so much duplication.
}