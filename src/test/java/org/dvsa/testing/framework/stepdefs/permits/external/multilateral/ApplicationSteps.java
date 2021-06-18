package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.VolLicenceSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ApplicationSteps extends BasePage implements En {
    public ApplicationSteps(OperatorStore operator, World world) {

        And("^I have (an|all) ongoing Annual Multilateral Application$", (String arg) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());

            int quantity = arg.equals("all") ? VolLicenceSteps.licenceQuantity.get("licence.quantity") : 1;

               IntStream.rangeClosed(1, quantity).forEach((i) -> {
                    HomePage.selectTab(Tab.PERMITS);
                    HomePage.applyForLicenceButton();

                    AnnualMultilateralJourney.INSTANCE
                               .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                               .licencePage(operator, world)
                               .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator)
                               .numberOfPermitsPage(operator);
                   get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
                   HomePage.selectTab(Tab.PERMITS);
            });
        });
        When("^I select a licence already with an ongoing annual multilateral permit$", () -> {
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world);
        });
        And("^the section is marked as complete on annual multilateral overview page$", () -> {
            OverviewPage.untilOnPage();
            OverviewPage.checkStatus(OverviewSection.CheckYourAnswers, PermitStatus.COMPLETED);
        });
         When("^(?:I submit an annual multilateral permit on external$|" +
                "I have an annual multilateral permit|" +
                "I have a valid annual multilateral permit)", () -> {
             world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator)
                    .numberOfPermitsPage(operator)
                    .checkYourAnswers();

             DeclarationPageJourneySteps.completeDeclaration();
             AnnualMultilateralJourney.INSTANCE
                    .feeOverviewPage()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .submit();
            HomePage.PermitsTab.untilPermitHasStatus(
                    operator.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );
        });
        And("^my annual multilateral permit has 'Not Yet Submitted' status$", () -> {
            String reference = operator.getCurrentLicence().orElseThrow(IllegalStateException::new).getLatestAnnualMultilateral().get().getReference();
            HomePage.PermitsTab.untilPermitWithStatus(reference, PermitStatus.NOT_YET_SUBMITTED, Duration.MEDIUM, TimeUnit.SECONDS);
        });
    }
}