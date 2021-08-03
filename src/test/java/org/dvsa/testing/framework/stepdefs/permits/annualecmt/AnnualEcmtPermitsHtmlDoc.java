package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.string.Str;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.ApplicationSection;
import org.dvsa.testing.framework.pageObjects.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.DocumentsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.util.ArrayList;

public class AnnualEcmtPermitsHtmlDoc extends BasePage implements En {

    private World world;

    public AnnualEcmtPermitsHtmlDoc(World world) {
        When("^I view the annual (bilateral|ECMT) permits documentation$", (String permitType) -> {
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
            // Check heading contains correct heading
            Assert.assertTrue(String.valueOf(getText("h2").contains(selectLicence1)),true);
            // Verify Euro 6
            Assert.assertFalse(DocumentsPage.getSectionBody(ApplicationSection.CheckIfYouNeedECMTPermits).isEmpty());
            // Verify Cabotage
            Assert.assertFalse(DocumentsPage.getSectionBody(ApplicationSection.Cabotage).isEmpty());
            // Verify restricted countries
            Assert.assertEquals(BasePermitJourney.getCountriesWithLimitedPermitsChoice(),
                    (Str.find("(?i)(yes|no)", DocumentsPage.getSectionBody(ApplicationSection.RestrictedCountries)).get()).trim().equalsIgnoreCase("yes")
            );
            //TODO hasRestrictedCountries is defaulting to false because it is a null boolean. Needs changing.

            // Verify percentage of international journey
            Assert.assertFalse(DocumentsPage.getSectionBody(ApplicationSection.NumberOfPermits).isEmpty());
            Assert.assertFalse(DocumentsPage.getSectionBody(ApplicationSection.Euro6).isEmpty());
        });
    }
// Could improve tests to check matches answers given earlier.
}
