package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.PermitApplication;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.ValidPermit.ValidAnnualECMTPermit;
import org.dvsa.testing.framework.pageObjects.external.pages.ApplicationIssuingFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ValidPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

public class ValidPermitsPageSteps extends BasePage implements En {

    public static Map<String, PermitApplication> userPermitsSelected = new HashMap();
    private World world;

    public ValidPermitsPageSteps(World world) {
        And("^have valid permits$", () -> {
            EcmtApplicationJourney.completeEcmtApplication(world);
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
            sleep(5000);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);

            // select one at random
            HomePage.PermitsTab.selectFirstOngoingApplication();
            ApplicationIssuingFeePage.acceptAndPay();

            // pay for application
            world.feeAndPaymentJourney.customerPaymentModule();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();

            // Refresh until permit is valid
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            HomePage.PermitsTab.selectFirstValidPermit();

        });
        Then("^the user is in the annual ECMT list page$",()  ->{
            Assert.assertTrue(isPath("/permits/valid/\\d+"));
            String title = BasePage.getElementValueByText("h1.govuk-heading-l",SelectorType.CSS).trim();
            Assert.assertEquals("Annual ECMT", title);
        });
        And ("^I select return to permit dashboard hyperlink", ValidPermitsPage::returnToPermitDashboard);
        Then ("^the licence number is displayed above the page heading",  () ->{
            String expectedReference= world.applicationDetails.getLicenceNumber();
            Assert.assertEquals(expectedReference, BasePermitPage.getReferenceFromPage());
        });
        Then ("^the ECMT application licence number is displayed above the page heading",  () ->{
            String expectedReference = world.applicationDetails.getLicenceNumber();
            String actual = BasePermitPage.getElementValueByText("//span[@class='govuk-caption-xl']", SelectorType.XPATH);
            Assert.assertEquals(expectedReference, actual);
        });
        Then("^the ECMT permit list page table should display all relevant fields$", () -> {
            String message = "Expected all permits to have a status of 'valid'";
            List<ValidAnnualECMTPermit> permits = ValidPermitsPage.annualECMTPermits();
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));
            IntStream.range(0, permits.size() - 1).forEach((idx) -> Assert.assertTrue(
                    permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                            permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
            ));
        });
        //When ("^the user is on self-serve permits dashboard", HomePageJourney::selectPermitTab);
        Then("^the user is on self-serve permits dashboard$", HomePageJourney::selectPermitTab);
    }

    public static void untilAnyPermitStatusMatch(PermitStatus status) {
        untilPermitStatusIsNot(HomePage.PermitsTab::isAnyPermitWithStatus, status );
    }

    private static <T> void untilPermitStatusIsNot(Predicate<T> p, T status) {
        int maxTries = 30;

        while(!p.test(status) && maxTries > 0) {
            refreshPage();

            if (p.test(status))
                break;

            try {
                TimeUnit.SECONDS.sleep(Duration.CENTURY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            maxTries--;
        }

        if (maxTries <= 0 && !p.test(status))
            throw new RuntimeException("Permit status did not meet desired criterion state");
    }
}
