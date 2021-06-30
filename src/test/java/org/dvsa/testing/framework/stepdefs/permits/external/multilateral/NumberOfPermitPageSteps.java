package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.common.type.Permit;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.external.enums.sections.MultilateralSection;
import org.dvsa.testing.lib.newPages.internal.irhp.InternalAnnualBilateralPermitApplicationPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesPage;
import org.junit.Assert;

import java.util.List;

public class NumberOfPermitPageSteps extends BasePermitPage implements En {

    public NumberOfPermitPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the number of permits required page$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            AnnualMultilateralJourney
                    .INSTANCE
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operatorStore)
                    .licencePage(operatorStore, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operatorStore);
        });
        Then("^the reference number and heading are displayed correct", () -> {
            String expectedLicenceNumber = operatorStore.getLatestLicence()
                    .orElseThrow(IllegalAccessError::new)
                    .getReferenceNumber();
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(expectedLicenceNumber, actualReferenceNumber);
            NumberOfPermitsPageJourney.hasPageHeading();
        });
        Then("^should see the text box for each year for Annual Multilateral permit stock with an open window$", () -> {
            //need to add steps later
            throw new PendingException();
        });
        When("^I choose to change the number of permits section$", () -> {
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.clickChangeAnswer(MultilateralSection.NumberOfPermits);
        });
        And("^my previously selected values are remembered", () -> {
           List<Permit> actualPermits = NumberOfPermitsPage.currentMultilateralPermits();
           List<Permit> expectedPermits = operatorStore.getCurrentLicence()
                   .orElseThrow(IllegalStateException::new)
                   .getLatestAnnualMultilateral().get().getNumberOfPermits();

           actualPermits.stream().forEach(p -> {
               Assert.assertTrue(expectedPermits.stream().anyMatch(p::equals));
           });
        });
        When("^I specify my number of multilateral permits$", NumberOfPermitsPageJourney::completeMultilateralPage);
        Then("^the number of permits section on the annual multilateral overview page is complete$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.NumberOfPaymentsRequired, PermitStatus.COMPLETED);
        });
        Then("^the user is on annual multilateral check your answers page$", (StepdefBody.A0) CheckYourAnswerPage::untilOnPage);
        When("^the case worker is viewing current fees$", () -> {
            LicenceStore licence = operatorStore.getCurrentLicence().get();
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);

            licence.setFees(FeesPage.fees());
        });
        Then("^the relevant error message is displayed$", () -> {
        });
        And("^I enter zero as value in the number of permits fields$", () -> {
            findAll("//*[contains(@class, 'field')]//input[@type='text']", SelectorType.XPATH)
                    .stream().forEach(x -> {x.sendKeys("0");});
        });
        And("^I specify the number of permits I require for my multilateral permit$", NumberOfPermitsPageJourney::completeMultilateralPage);
        When("^I update the number of permits for my multilateral permit$", () -> {
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.IrhpPermits);
            IrhpPermitsApplyPage.viewApplication();
            InternalAnnualBilateralPermitApplicationPage.numPermits(1, 1);
            InternalAnnualBilateralPermitApplicationPage.save();
        });
    }
}
