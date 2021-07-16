package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.VolLicenceSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ApplicationSteps extends BasePage implements En {
    public ApplicationSteps(OperatorStore operator, World world) {

        And("^I have (an|all) ongoing Annual Multilateral Application$", (String arg) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());

            int quantity = arg.equals("all") ? VolLicenceSteps.licenceQuantity.get("licence.quantity") : 1;

               IntStream.rangeClosed(1, quantity).forEach((i) -> {
                   HomePageJourney.beginPermitApplication();

                    AnnualMultilateralJourney.INSTANCE
                               .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                               .licencePage(operator, world)
                               .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator);
                   NumberOfPermitsPageJourney.completeMultilateralPage();
                   get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
                   HomePageJourney.selectPermitTab();
            });
        });
        When("^I select a licence already with an ongoing annual multilateral permit$", () -> {
            HomePageJourney.beginPermitApplication();
            AnnualMultilateralJourney.INSTANCE
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world);
        });
        And("^the section is marked as complete on annual multilateral overview page$", () -> {
            OverviewPage.untilOnPage();
            OverviewPageJourney.checkStatus(OverviewSection.CheckYourAnswers, PermitStatus.COMPLETED);
        });
         When("^(?:I submit an annual multilateral permit on external$|" +
                "I have an annual multilateral permit|" +
                "I have a valid annual multilateral permit)", () -> {
             world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
             HomePageJourney.beginPermitApplication();
             AnnualMultilateralJourney.INSTANCE
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator);
             NumberOfPermitsPageJourney.completeMultilateralPage();
             AnnualMultilateralJourney.INSTANCE
                    .checkYourAnswers();

             DeclarationPageJourney.completeDeclaration();
             world.feeAndPaymentJourney.customerPaymentModule();
             AnnualMultilateralJourney.INSTANCE
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
            String selector = String.format(
                    "//*[contains(text(), '%s')]//ancestor-or-self::td//following-sibling::td[last()]/span[contains(text(), '%s')]",
                    reference, PermitStatus.NOT_YET_SUBMITTED);
            refreshPageUntilElementAppears(selector, SelectorType.XPATH);
        });
    }
}