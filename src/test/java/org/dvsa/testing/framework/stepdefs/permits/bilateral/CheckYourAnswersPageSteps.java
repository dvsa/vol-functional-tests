package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.IllegalBrowserException;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import java.net.MalformedURLException;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.untilOnPage;

public class CheckYourAnswersPageSteps implements En {
    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore)throws MalformedURLException, IllegalBrowserException {
        Then("^I am on the Bilateral check your answers page$", () -> {
            clickToPermitTypePage(world);
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
        });
        Then("^Country name displayed on the Bilateral check your answers page is the one clicked on the overview page$", () -> {
            CheckYourAnswersPage.untilOnCheckYourAnswersPage();
            Assert.assertEquals(CheckYourAnswersPage.getCountry(),operatorStore.getCountry());
        });

        Then("^the page heading on bilateral check your answers page is correct$", () -> {
            String expectedPageHeading = "Check your answers";
            String actualPageHeading = CheckYourAnswersPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);
        });
        Then("^I see four sections displayed on the table correctly$", () -> {
            CheckYourAnswersPage.newSection();
        });

        Then("^Period type displayed on the check your answers page is the one I selected on the Period selection page$", () -> {
            Assert.assertEquals(CheckYourAnswersPage.getPeriod(),operatorStore.getCurrentBilateralPeriodType().toString());
        });
        Then("^Journey type displayed on the check your answers page is the one I selected on the Permits usage$", () -> {
            Assert.assertEquals(CheckYourAnswersPage.getJourney(),PermitUsagePage.getJourney());
        });
        Then("^Value of do you need to carry out cabotage, will always be 'YES'$", () -> {
            CheckYourAnswersPage.cabotageValue();
        });
        Then("^Value of How many permits you need, will be the one saved on the number of permits page$", () -> {
            String permitlabel = operatorStore.getPermit();
            String permitvalue =String.valueOf(NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(CheckYourAnswersPage.getPermitValue(),permitvalue+" "+permitlabel+"s");
        });
        When("^I click Confirm and return to overview$", () -> {
            CheckYourAnswersPage.confirmAndReturnToOverview();
        });
        Then("^the status of Answer questions for individual countries section for the selected country is set as complete$", () -> {
            String s1= BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]",SelectorType.XPATH);
            Assert.assertEquals(s1,"COMPLETED");
        });

        Then("^I click change against Period for which you need permits section$", () -> {
             CheckYourAnswersPage.periodChange();
        });
        Then("^I am navigated to the Bilateral period selection page$", () -> {
            PeriodSelectionPage.untilOnPeriodSelectionPage();
        });
        And("^I change period to be Bilateral and Standard permits on the period selection and continue to be on the check your answers page$", () -> {
          AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits, operatorStore);
          PeriodSelectionPage.warningText();
          PeriodSelectionPage.saveAndContinueButton();
          PermitUsagePage.untilOnPermitUsagePage();
          PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
          PermitUsagePage.continueButton();
          CabotagePage.noButton();
          BasePermitPage.bilateralSaveAndContinue();
          NumberOfPermitsPage.numberOfPermits();
          AnnualBilateralJourney.getInstance().permit(operatorStore);
          BasePermitPage.bilateralSaveAndContinue();
          CheckYourAnswersPage.untilOnCheckYourAnswersPage();
        });
        And("^I change period to be Bilateral Standard permits no Cabotage on the period selection and continue to be on the check your answers page$", () -> {
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage, operatorStore);
            PeriodSelectionPage.warningText();
            PeriodSelectionPage.saveAndContinueButton();
            PermitUsagePage.untilOnPermitUsagePage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.continueButton();
            NumberOfPermitsPage.numberOfPermits();
            AnnualBilateralJourney.getInstance().permit(operatorStore);
            BasePermitPage.bilateralSaveAndContinue();
            CheckYourAnswersPage.untilOnCheckYourAnswersPage();
        });

        Then("^Value of Do you need to carry out cabotage, will be as per the selection after changing the period selection to Bilaterals Standard and Cabotage permits$", () -> {
            CheckYourAnswersPage.stdAndCabCabotageValue();
        });
        Then("^Do you need to carry out cabotage, will not be displayed if the period type is Bilaterals Standard permits no Cabotage$", () -> {
            CheckYourAnswersPage.cabotagedoesNotExist();
        });
        Then("^Value of How many permits you need, will be the one saved on the number of permits page for Bileterals Standard permits no Cabotage$", () -> {
            String permitlabel = operatorStore.getPermit();
            String permitvalue =String.valueOf(NumberOfPermitsPage.getPermitValue());
            Assert.assertEquals(CheckYourAnswersPage.getpermitValueforNonCabotage(),permitvalue+" "+permitlabel+"s");
        });
    }
}
