package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import activesupport.IllegalBrowserException;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ApplicationSteps extends BasePage implements En {
    public ApplicationSteps(OperatorStore operator, World world) {

        And("^I have (an|all) ongoing Annual Multilateral Application$", (String arg) -> {
            AnnualMultilateralJourney.INSTANCE
                    .signin(operator, world);

            int quantity = arg.equals("all") ? world.get("licence.quantity") : 1;

               IntStream.rangeClosed(1, quantity).forEach((i) -> {
                    HomePage.selectTab(Tab.PERMITS);
                    HomePage.applyForLicenceButton();

                   try {
                       AnnualMultilateralJourney.INSTANCE
                               .permitType(PermitTypePage.PermitType.AnnualMultilateral, operator)
                               .licencePage(operator, world)
                               .overviewPage(OverviewPage.Section.NumberOfPaymentsRequired, operator)
                               .numberOfPermitsPage(operator);
                   } catch (MalformedURLException e) {
                       e.printStackTrace();
                   } catch (IllegalBrowserException e) {
                       e.printStackTrace();
                   }
                   get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
                   HomePage.selectTab(Tab.PERMITS);
            });
        });
        When("^I select a licence already with an ongoing annual multilateral permit$", () -> {
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitTypePage.PermitType.AnnualMultilateral, operator)
                    .licencePage(operator, world);
        });
        And("^the section is marked as complete on annual multilateral overview page$", () -> {
            String error = "Expected the status of check your answers to be complete but it wasn't";
            OverviewPage.untilOnPage();
            boolean answersComplete = OverviewPage.checkStatus(OverviewPage.Section.CheckYourAnswers, PermitStatus.COMPLETED);

            Assert.assertTrue(error, answersComplete);
        });
         When("^(?:I submit an annual multilateral permit on external$|" +
                "I have an annual multilateral permit|" +
                "I have a valid annual multilateral permit)", () -> {
            AnnualMultilateralJourney.INSTANCE
                    .signin(operator, world)
                    .beginApplication()
                    .permitType(PermitTypePage.PermitType.AnnualMultilateral, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewPage.Section.NumberOfPaymentsRequired, operator)
                    .numberOfPermitsPage(operator)
                    .checkYourAnswers()
                    .declaration(true)
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