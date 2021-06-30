package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.lib.newPages.internal.details.DocumentsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
import org.dvsa.testing.lib.newPages.internal.details.enums.DocumentHeading;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AnnualEcmtPermitsHtmlDoc extends BasePage implements En {

    private World world;

    public AnnualEcmtPermitsHtmlDoc(World world, OperatorStore operatorStore) {
        When("^I view the annual ECMT Permits documentation$", () -> {
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.DocsAndAttachments);
            DocsAndAttachmentsPage.select(
                   operatorStore.getLicences().get(0).getEcmt().getFullReferenceNumber(),
                    Duration.LONG,
                    TimeUnit.MINUTES
            );
        });
        Then("^the annual ECMT Permits HTML document should have the correct information$", () -> {
            ArrayList<String> tab = new ArrayList<String> (getDriver().getWindowHandles());
            getDriver().switchTo().window(tab.get(tab.size() - 1));
            untilUrlPathIs(DocumentsPage.RESOURCE, TimeUnit.SECONDS, Duration.LONG);
            String selectLicence1= operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            LicenceStore selectedLicence = operatorStore.getLatestLicence().get();
            // Check heading contains correct heading
            Assert.assertTrue(String.valueOf(BasePermitPage.getReferenceFromPage().contains(selectLicence1)),true);
            // Verify Euro 6
            Assert.assertTrue(
                    toBool(DocumentsPage.getSectionHeading(DocumentHeading.Euro6))
            );
            // Verify Cabotage
            Assert.assertTrue(
                    toBool(DocumentsPage.getSectionHeading(DocumentHeading.Cabotage))
            );
            // Verify restricted countries
            Assert.assertEquals(selectedLicence.getEcmt().hasRestrictedCountries(),
                    toBool(Str.find("(?i)(yes|no)", DocumentsPage.getSectionHeading(DocumentHeading.RestrictedCountries)).get())
            );
            // Verify percentage of international journey
            Assert.assertEquals(
                    selectedLicence.getEcmt().getInternationalBusiness().toString(),
                    DocumentsPage.getSectionHeading(DocumentHeading.PercentageOfInternationalTrips)
            );
            Assert.assertEquals(
                    selectedLicence.getEcmt().getSector().toString(),
                    DocumentsPage.getSectionHeading(DocumentHeading.Sector)
            );
        });
    }

    private boolean toBool(String value) {
        return value.trim().equalsIgnoreCase("yes");
    }

}
