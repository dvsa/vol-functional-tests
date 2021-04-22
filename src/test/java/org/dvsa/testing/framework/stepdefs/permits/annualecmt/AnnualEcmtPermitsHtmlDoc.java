package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.pages.internal.doc.PermitApplicationDocPage;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AnnualEcmtPermitsHtmlDoc extends BasePage implements En {
    public AnnualEcmtPermitsHtmlDoc(OperatorStore operatorStore) {
        When("^I view the annual ECMT Permits documentation$", () -> {
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.DocsAndAttachments);
           DocsAndAttachmentsPage.select(
                   operatorStore.getLicences().get(0).getEcmt().getFullReferenceNumber(),
                    Duration.LONG,
                    TimeUnit.MINUTES
            );
        });
        When("^I view the annual ECMT Permits documentation on internal$", () -> {
            IrhpPermitsApplyPage.viewApplication();
            BasePage.waitAndClick("//a[@id='menu-licence_irhp_applications-document']",SelectorType.XPATH);
            BasePage.waitAndClick("//tbody/tr/td/a",SelectorType.XPATH);
        });
        Then("^the annual ECMT Permits HTML document should have the correct information$", () -> {
            ArrayList<String> tab = new ArrayList<String> (getDriver().getWindowHandles());
            getDriver().switchTo().window(tab.get(tab.size() - 1));
            untilUrlPathIs(PermitApplicationDocPage.RESOURCE, TimeUnit.SECONDS, Duration.LONG);
            String selectLicence1= operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            LicenceStore selectedLicence = operatorStore.getLatestLicence().get();
            // Check heading contains correct heading
            Assert.assertTrue(String.valueOf(PermitApplicationDocPage.referenceNumber().contains(selectLicence1)),true);
            // Verify Euro 6
            Assert.assertTrue(
                    toBool(PermitApplicationDocPage.get(PermitApplicationDocPage.Section.Euro6))
            );
            // Verify Cabotage
            Assert.assertTrue(
                    toBool(PermitApplicationDocPage.get(PermitApplicationDocPage.Section.Cabotage))
            );
            // Verify restricted countries
            Assert.assertEquals(selectedLicence.getEcmt().hasRestrictedCountries(),
                    toBool(Str.find("(?i)(yes|no)", PermitApplicationDocPage.get(PermitApplicationDocPage.Section.RestrictedCountries)).get())
            );
            // Verify percentage of international journey
            Assert.assertEquals(
                    selectedLicence.getEcmt().getInternationalBusiness().toString(),
                    PermitApplicationDocPage.get(PermitApplicationDocPage.Section.PercentageOfInternationalTrips)
            );
            Assert.assertEquals(
                    selectedLicence.getEcmt().getSector().toString(),
                    PermitApplicationDocPage.get(PermitApplicationDocPage.Section.Sector)
            );
        });
    }

    private boolean toBool(String value) {
        return value.trim().equalsIgnoreCase("yes");
    }

}
