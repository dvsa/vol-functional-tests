package org.dvsa.testing.framework.stepdefs.permits.internal;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.pages.LicenceDetailsPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.BaseModel;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class IRHPPermitsPageSteps extends BasePage {
    private final World world;

    public IRHPPermitsPageSteps(World world) {
        this.world = world;
    }

    @When("I am viewing a licences IRHP section")
    public void iAmViewingALicenceIRHPSection() {
        refreshPage();
        LicenceDetailsPageJourney.clickIRHPTab();
    }
    @Then("the no issued permits message should be displayed")
    public void theNoIssuedPermitsMessageShouldBeDisplayed() {
        assertTrue(IrhpPermitsDetailsPage.isNoPermitsMessagePresent(),"Unable to find the no issued permits message");
    }
    @And("the no permits applications message should be displayed")
    public void theNoPermitsApplicationsMessageShouldBeDisplayed() {
        IrhpPermitsDetailsPage.isNoPermitApplicationsMessagePresent();
    }
    @Then("the ongoing permit application is to be as expected")
    public void theOngoingPermitApplicationIsToBeAsExpected() {
        List<PermitApplication> applications = IrhpPermitsDetailsPage.getApplications();

        applications.forEach((permit) -> {
            assertTrue(permit.getReferenceNumber().contains(world.applicationDetails.getLicenceNumber()));
            assertEquals(permit.getNoOfPermits().intValue(), 1);
            assertEquals(permit.getType(), PermitType.ECMT_ANNUAL);
            assertEquals(LocalDate.parse(permit.getRecdDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.now());
        });
    }
    @Then("internal users should not be able to create ECMT Permit applications")
    public void internalUsersShouldNotBeAbleToCreateECMTPermitApplications() {
        assertFalse(
                IrhpPermitsDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits)
        );
    }
    @And("pay outstanding fees")
    public void payOutstandingFees() {
        IRHPPermitsPageSteps.payOutstandingFees(world);
    }

    @Then("my application should be under consideration")
    public void myApplicationShouldBeUnderConsideration() {
        assertEquals("UNDER CONSIDERATION", getText("//*[@class='govuk-summary-list__value']/span", SelectorType.XPATH));
    }
    @Then("my permit application is under consideration")
    public void myPermitApplicationIsUnderConsideration() {
        IrhpPermitsDetailsPage.Tab.select(DetailsTab.IrhpPermits);
        IrhpPermitsApplyPage.underConsiderationStatusExists();
    }
    public static void payOutstandingFees(World world) {
        refreshPage();
        LicenceDetailsPageJourney.clickIRHPTab();
        LicenceDetailsPageJourney.clickFeesTab();
        FeesDetailsPage.outstanding();
        FeesDetailsPage.pay();
        BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
        IrhpPermitsApplyPage.selectCardPayment();
        world.feeAndPaymentJourney.customerPaymentModule();
        FeesDetailsPage.untilFeePaidNotification();
    }
}