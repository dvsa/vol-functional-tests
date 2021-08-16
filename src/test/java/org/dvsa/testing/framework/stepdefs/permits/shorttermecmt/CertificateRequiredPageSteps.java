package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.external.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

import static org.junit.Assert.assertEquals;

public class CertificateRequiredPageSteps implements En {

    public CertificateRequiredPageSteps(World world) {
        Then("^I should get the certificates required page error message$", () -> {
            String errorText = CertificatesRequiredPage.getErrorText();
            assertEquals(errorText, "Tick to confirm you understand that each vehicle and trailer must have the matching certificates.");
        });
        Then("^I confirm the Certificates Required checkbox$", CertificatesRequiredPage::confirmCertificateRequired);

    }
}
