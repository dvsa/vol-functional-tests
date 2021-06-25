package org.dvsa.testing.framework.stepdefs.permits.internal.bilateral;

import Injectors.World;
import activesupport.number.Int;
import activesupport.string.Str;
import apiCalls.Utils.eupaBuilders.internal.irhp.permit.stock.OpenByCountryModel;
import apiCalls.eupaActions.internal.IrhpPermitWindowAPI;
import com.google.common.collect.Lists;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.pages.internal.BaseModel;
import org.dvsa.testing.lib.pages.internal.details.*;
import org.dvsa.testing.lib.pages.internal.details.irhp.InternalAnnualBilateralPermitApplicationPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsPage;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.number.OrderingComparison.*;

public class AnnualBilateralInternalApplicationSteps implements En {

    public World world;
    public AnnualBilateralInternalApplicationSteps(OperatorStore operatorStore) {
        Then("^the date received should have today's date$", () -> {
            InternalAnnualBilateralPermitApplicationPage.untilOnPage();
            LocalDate expectedDate = LocalDate.now();
            LocalDate actualDate = InternalAnnualBilateralPermitApplicationPage.dateReceived();

            // Verifies that default date is today's date
            Assert.assertEquals(expectedDate, actualDate);

            // Edits the date
            InternalAnnualBilateralPermitApplicationPage.dateReceived(LocalDate.now().plusDays(1));

            // Verifies that the date has now changed from the default date of today
            Assert.assertNotEquals(LocalDate.now(), InternalAnnualBilateralPermitApplicationPage.dateReceived());
        });
        And("^the case worker (?:has began an|begins another) annual bilateral permit application$", () -> {
            LicenceDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.applyforPermit();
            IrhpPermitsPage.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
            IrhpPermitsPage.Model.permitType(PermitType.ANNUAL_BILATERAL);
            IrhpPermitsPage.Model.continueButton();
            IrhpPermitsPage.Model.untilModalIsGone(Duration.LONG, TimeUnit.SECONDS);
        });
        When("^I have an application where the date received is in the future$", () -> {
            InternalAnnualBilateralPermitApplicationPage.dateReceived(LocalDate.now().plusDays(Int.random(1, 365)));
            InternalAnnualBilateralPermitApplicationPage.save();
        });
        Then("^the error message for date received not being allowed to be a future date is displayed$", () -> {
            Assert.assertTrue("Error message was not displayed or was incorrect", InternalAnnualBilateralPermitApplicationPage.hasFutureDateReceivedError());
        });
        When("^I save an application with an invalid date received$", () -> {
            InternalAnnualBilateralPermitApplicationPage.dateReceived(Str.randomWord(4), Str.randomWord(2), Str.randomWord(2));
            InternalAnnualBilateralPermitApplicationPage.save();
        });
        Then("^the invalid date error message should be displayed$", () -> {
            Assert.assertTrue("Unable to the invalid date error message", InternalAnnualBilateralPermitApplicationPage.hasInvalidDateError());
        });
        Then("^the total number of authorised vehicles should match what's on the licence$", () -> {
            int expectedNumVehicles = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalStateException::new).getNumberOfAuthorisedVehicles();
            int actualNumVehicles = InternalAnnualBilateralPermitApplicationPage.authorisedVehicles();
            String message = String.format("The total number of authorised vehicles on the page of %d did not match %s", actualNumVehicles, expectedNumVehicles);

            Assert.assertEquals(message, expectedNumVehicles, actualNumVehicles);
        });
        Then("^countries with an open window are displayed in alphabetical order$", () -> {
            OpenByCountryModel windows = IrhpPermitWindowAPI.openByCountry();
            List<String> countriesWithOpenWindow = Lists.newArrayList(windows.countryNames().get());
            List<String> countriesOnPage = InternalAnnualBilateralPermitApplicationPage.countries();

            // All countries with an open window are displayed on the page
            Assert.assertTrue(countriesOnPage.containsAll(countriesWithOpenWindow));

            // Countries on page are displayed in alphabetical order
            IntStream.range(0, countriesOnPage.size() - 1).forEach((idx) -> {
                Assert.assertThat(countriesOnPage.get(idx).compareTo(countriesOnPage.get(idx + 1)), lessThanOrEqualTo(0));
            });
        });
        Then("^the year derived from the valid from date for each country with an open window is correct$", () -> {
            List<InternalAnnualBilateralPermitApplicationPage.Window> actualWindows = InternalAnnualBilateralPermitApplicationPage.openWindows();
            OpenByCountryModel expectedWindows = IrhpPermitWindowAPI.openByCountry();
            expectedWindows.countries().get().forEach((ew) -> {
                Assert.assertTrue(
                        actualWindows.stream().anyMatch((aw) -> aw.getCountry().equalsIgnoreCase(ew.getCountry()) && aw.getYear() == ew.getValidFrom().getYear())
                );
            });
        });
        When("^number of annual bilateral permits is greater than the number of authorised vehicles$", () -> {
            int maxAllowed = operatorStore.getLatestLicence().orElseThrow(IllegalStateException::new).getNumberOfAuthorisedVehicles();
            IntStream.rangeClosed(1, InternalAnnualBilateralPermitApplicationPage.numberOfOpenWindows()).forEach((idx) -> {
                InternalAnnualBilateralPermitApplicationPage.numPermits(idx, Int.random(maxAllowed, 1000));
            });
        });
        And("^I save the annual bilateral permit on internal$", InternalAnnualBilateralPermitApplicationPage::save);
        Then("^the error message for exceeding the maximum number of permits is displayed$", () -> {
            Assert.assertTrue(InternalAnnualBilateralPermitApplicationPage.hasExceedNumberOfPermitsMessage());
        });
        When("^I do not request any number of permits$", () -> {
        });
        Then("^I'm able to save the annual bilateral application$", () -> {
            InternalAnnualBilateralPermitApplicationPage.save();
            IrhpPermitsDetailsPage.untilOnPage();
            Assert.assertThat(IrhpPermitsDetailsPage.getApplications().size(), greaterThan(0));
        });
        When("^I apply for one or more permits$", () -> {
            LicenceStore licence = operatorStore.getLatestLicence().orElseThrow(IllegalStateException::new);
            AnnualBilateralJourney.getInstance().numberOfPermits(licence).save(licence);
        });
        Then("^a fee should be generated$", () -> {
            BaseApplicationDetailsPage.Tab.select(BaseApplicationDetailsPage.DetailsTab.Fees);
            Assert.assertTrue("Expected there to be a fee but was unable to find one", FeesDetailsPage.hasFee());
        });
        And("^check my current fees$", () -> {
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);
            operatorStore.getLatestLicence().orElseThrow(IllegalStateException::new).setFees(FeesPage.fees());
        });
        When("^I update the number of permits$", () -> {
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
           IrhpPermitsApplyPage.viewApplication();
            List<InternalAnnualBilateralPermitApplicationPage.Window> windows = InternalAnnualBilateralPermitApplicationPage.openWindows();
            int permits = windows.get(0).getNumberOfPermits() < windows.get(0).getMaximumNumberOfPermits() ? windows.get(0).getNumberOfPermits() + 1 : windows.get(0).getNumberOfPermits() - 1;
            InternalAnnualBilateralPermitApplicationPage.numPermits(1, permits);
            InternalAnnualBilateralPermitApplicationPage.save();
        });
        Then("^new fees are generated$", () -> {
            List<FeesPage.Fee> oldFees = operatorStore.getLatestLicence().get().getFees();

            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);
            FeesPage.untilOnPage();

            // Checks that the fee numbers on the page now do not match those recorded before
            FeesPage.fees().forEach((nf) -> oldFees.forEach(of -> Assert.assertNotEquals(of.getFeeNo(), nf.getFeeNo())));
        });
        When("^the annual bilateral permit is cancelled$", InternalAnnualBilateralPermitApplicationPage::cancel);
        Then("^there shouldn't be any ongoing applications$", () -> Assert.assertThat(IrhpPermitsDetailsPage.getApplications().size(), comparesEqualTo(0)));
        When("^I'm viewing my saved annual bilateral application$", () -> {
            IrhpPermitsDetailsPage.select(IrhpPermitsDetailsPage.getApplications().get(0).getReferenceNumber());
        });
        Then("^I should save the cancel quick action$", () -> {
            String message = "Expected the cancel quick action button to be present that it wasn't";
            Assert.assertTrue(message, InternalAnnualBilateralPermitApplicationPage.QuickActions.hasCancel());
        });
        But("^not have have the decisions submit button$", () -> {
            String message = "The decisions submit button should not be displayed";
            Assert.assertFalse(message, InternalAnnualBilateralPermitApplicationPage.Decisions.hasSubmit());
        });
        When("^it has all sections completed$", () -> {
            InternalAnnualBilateralPermitApplicationPage.declaration(true);
            InternalAnnualBilateralPermitApplicationPage.save();
            IrhpPermitsDetailsPage.select(IrhpPermitsDetailsPage.getApplications().get(0).getReferenceNumber());
        });
        Then("^there submit button is displayed$", () -> {
            InternalAnnualBilateralPermitApplicationPage.untilOnPage();
            String message = "Expected the decisions submit button to be present but it wasn't";
            Assert.assertTrue(message, InternalAnnualBilateralPermitApplicationPage.Decisions.hasSubmit());
        });
        And("^I save my annual bilateral application$", InternalAnnualBilateralPermitApplicationPage.Decisions::submit);
        And("^a case worker has submitted an annual bilateral application$", () -> {
            LicenceStore licence = operatorStore.getLatestLicence().orElseThrow(IllegalStateException::new);
            AnnualBilateralJourney.getInstance()
                    .numberOfPermits(licence)
                    .declare(licence, true)
                    .save(licence)
                    .select(licence.getLatestAnnualBilateral().get().getReference())
                    .submit();

            BaseModel.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);

            IrhpPermitsApplyPage.selectCardPayment();
            world.feeAndPaymentJourneySteps.customerPaymentModule();
            IrhpPermitsDetailsPage.untilStatusIs(licence.getLatestAnnualBilateral().orElseThrow(IllegalStateException::new).getReference(), PermitStatus.VALID, Duration.LONG, TimeUnit.MINUTES);
        });
        Then("^the maximum number of permits should account of the number of permits I've applied for in other permits$", () -> {
            List<InternalAnnualBilateralPermitApplicationPage.Window> windows = InternalAnnualBilateralPermitApplicationPage.openWindows();
            List<InternalAnnualBilateralPermitApplicationPage.Window> previousPermits = operatorStore.getLatestLicence().get().getLatestAnnualBilateral().get().getWindows();

            IntStream.range(0, windows.size()).forEach((idx) -> {
                int currentMax = windows.get(idx).getMaximumNumberOfPermits();
                int expectedMax;

                if (currentMax == previousPermits.get(idx).getNumberOfPermits()) {
                    expectedMax = currentMax;
                } else {
                    expectedMax = operatorStore.getLatestLicence().get().getNumberOfAuthorisedVehicles() - previousPermits.get(idx).getNumberOfPermits();
                }

                Assert.assertEquals(expectedMax, currentMax);
            });
        });
    }
}