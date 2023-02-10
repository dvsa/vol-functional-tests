package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenWindowModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualBilateralPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnualBilateralSteps extends BasePage {
    private final World world;

    public AnnualBilateralSteps(World world) {
        this.world = world;
    }

    @Then("I should be on the overview page")
    public void iShouldBeOnTheOverviewPage() {
        OverviewPage.untilOnPage();
        OverviewPageJourney.hasPageHeading();
    }

    @Given("I have a valid annual bilateral norway standard and cabotage permit")
    public void iHaveAValidAnnualBilateralNorwayStandardAndCabotagePermit() {
        AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilPermitFeePage(world, PeriodType.BilateralsStandardAndCabotagePermits, Country.Norway, false);
        AnnualBilateralJourney.completePayFees(world);
        SubmittedPageJourney.goToPermitsDashboard();
        CommonSteps.waitUntilPermitHasStatus(world);
    }

    @Given("I have (a valid |applied for an )annual bilateral norway standard no cabotage permit")
    public void IHaveAValidAppliedForAnAnnualBilateralNorwayStandardNoCabotagePermit(String notValid) {
        AnnualBilateralJourney.startBilateralJourneyTypeAndSelectCabotageUntilPermitFeePage(world, PeriodType.BilateralsStandardPermitsNoCabotage, Country.Norway, null);
        AnnualBilateralJourney.completePayFees(world);
        SubmittedPageJourney.goToPermitsDashboard();
        CommonSteps.waitUntilPermitHasStatus(world);
    }

    @Given("I have selected Turkey and I am on the Bilateral application overview page")
    public void iHaveSelectedTurkeyAndIAmOnTheBilateralApplicationOverviewPage() {
        AnnualBilateralJourney.startBilateralCountryJourneyAndSelectCountry(world, "Turkey");
    }

    @Given("I have selected Morocco and I am on the Bilateral application overview page")
    public void iHaveSelectedMoroccoAndIAmOnTheBilateralApplicationOverviewPage() {
        AnnualBilateralJourney.startBilateralCountryJourneyAndSelectCountry(world, "Morocco");
    }

    @Given("I have selected Ukraine and I am on the Bilateral application overview page")
    public void iHaveSelectedUkraineAndIAmOnTheBilateralApplicationOverviewPage() {
        AnnualBilateralJourney.startBilateralCountryJourneyAndSelectCountry(world, "Ukraine");
    }

    @Given("I accept declaration and submit the application")
    public void iAcceptDeclarationAndSubmitTheApplication() {
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
    }

    @Then("I should be informed that there is already an active permit application for this licence")
    public void iShouldBeInformedThatThereIsAlreadyAnActivePermitApplicationForThisLicence() {
        String message = SelectALicencePage.getActivePermitMessage();
        assertTrue(message.contains("You've already started an application using this licence. Click 'Save and continue' to access this application"));
    }

    @And("I am viewing an issued annual bilateral permit on self-serve")
    public void iAmViewingAnIssuedAnnualBilateralPermitOnSelfServe() {
        HomePage.PermitsTab.selectFirstValidPermit();
        ValidPermitsPage.untilOnPage();
    }

    @And("the user is in the annual bilateral list page")
    public void theUserIsInTheAnnualBilateralListPage() {
        ValidPermitsPage.untilOnPage();
        ValidPermitsPageJourney.hasBilateralHeading();
    }

    @Then("the table of annual bilateral permits is as expected")
    public void theTableOfAnnualBilateralPermitsIsAsExpected() throws HttpException {
        OpenByCountryModel stock = IrhpPermitWindowAPI.openByCountry();
        String message = "Expected all permits to have a status of 'Pending' but one or more DIDN'T!!!";
        List<ValidAnnualBilateralPermit> permits = ValidPermitsPage.annualBilateralPermits();

        List<OpenWindowModel> windows = stock.openWindowsFor(permits.stream().map(p -> p.getCountry().toString()).toArray(String[]::new));

        // Verify status is pending
        // Changed to assert is VALID
        assertTrue(permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID), message);

        // Verify that Type is displayed as per the selection
        assertTrue(NumberOfPermitsPageJourney.getLabel().contains(ValidPermitsPage.getType()));

        // Check permit number is in ascending order grouped by country
        Map<Country, List<String>> grouped = permits.stream().collect(
                Collectors.groupingBy(ValidAnnualBilateralPermit::getCountry,
                        Collectors.mapping(ValidAnnualBilateralPermit::getApplication,
                                Collectors.toList()))
        );

        grouped.forEach((k, v) -> IntStream.range(0, v.size() - 1).forEach(i -> assertThat(v.get(i),
                lessThanOrEqualTo(v.get(i + 1)))));

        // Verify it's listed by country going in ascending order
        IntStream.range(0, permits.size() - 1).forEach((idx) -> {
            List<LocalDate> expiryDates = stock.openWindowsFor(permits.get(idx).getCountry().toString())
                    .stream().map(win -> win.getIrhpPermitStockModel().getValidTo()).collect(Collectors.toList());

            // Check Country order
            assertTrue(permits.get(idx).getCountry().compareTo(permits.get(idx + 1).getCountry()) <= 0);
            // Check expiry date is in ascending order
            assertTrue(
                    permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                            permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
            );
            // Check expiry date matches that of stock window
            assertThat(expiryDates, hasItem(permits.get(idx).getExpiryDate()));
        });
    }
}