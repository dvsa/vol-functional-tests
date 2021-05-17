package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class CertificateRequiredPageSteps implements En {

    public CertificateRequiredPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm certificates required page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
        });
        Then("^the certificates required page has the relevant information$", () -> {
            CertificatesRequiredPage.hasPageHeading();
            CertificatesRequiredPage.hasAdvisoryMessages();
            String expectedLicenceNumber= operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber= BasePermitPage.getReference();
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));
        });
        Then("^I should get the certificates required page error message$", CertificatesRequiredPage::errorText);
        Then("^I confirm the Certificates Required checkbox$", CertificatesRequiredPage::CertificatesRequiredConfirmation);
        Then("^the user is navigated to the short term overview page with the status as completed$", () -> {
            String error = "Expected the status of certificates required page to be complete but it wasn't";
            OverviewPage.untilOnPage();
            boolean complete = OverviewPage.checkStatus(OverviewPage.Section.CertificatesRequired,PermitStatus.COMPLETED);
            Assert.assertTrue(error, complete);
        });

    }
}
