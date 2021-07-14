package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.enums.sections.ApplicationSection;
import org.dvsa.testing.lib.newPages.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.lib.newPages.internal.details.DocumentsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
import org.dvsa.testing.lib.newPages.internal.details.enums.DocumentHeading;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.util.ArrayList;

public class AnnualEcmtPermitsHtmlDoc extends BasePage implements En {

    private World world;

    public AnnualEcmtPermitsHtmlDoc(World world, OperatorStore operatorStore) {
        When("^I view the annual ECMT Permits documentation$", () -> {
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.IrhpPermits);
            String permitApplicationNumber = getText("//td[@data-heading='Reference number']/a", SelectorType.XPATH);
            clickByLinkText(permitApplicationNumber);
            clickByLinkText("Docs & Attachments");
            DocsAndAttachmentsPage.select(permitApplicationNumber);
        });
        Then("^the annual ECMT Permits HTML document should have the correct information$", () -> {
            ArrayList<String> tab = new ArrayList<String> (getDriver().getWindowHandles());
            getDriver().switchTo().window(tab.get(tab.size() - 1));
            DocumentsPage.untilOnPage();
            String selectLicence1 = world.applicationDetails.getLicenceNumber();
            LicenceStore selectedLicence = operatorStore.getLatestLicence().get();
            // Check heading contains correct heading
            Assert.assertTrue(String.valueOf(getText("h2").contains(selectLicence1)),true);
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
