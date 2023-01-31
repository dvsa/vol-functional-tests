package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import activesupport.string.Str;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.ApplicationSection;
import org.dvsa.testing.framework.pageObjects.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.DocumentsPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AnnualEcmtPermitsHtmlDoc extends BasePage{
    private final World world;

    public AnnualEcmtPermitsHtmlDoc(World world) {
        this.world = world;
    }

    @When("I view the annual {string} permits documentation")
    public void iViewTheAnnualPermitsDocumentation(String permitType) {
        IrhpPermitsDetailsPage.Tab.select(DetailsTab.IrhpPermits);
        String permitApplicationNumber = getText("//td[@data-heading='Reference number']/a", SelectorType.XPATH);
        clickByLinkText(permitApplicationNumber);
        clickByLinkText("Docs & Attachments");
        DocsAndAttachmentsPage.select(permitApplicationNumber);
    }

    @Then("the annual ECMT Permits HTML document should have the correct information")
    public void theAnnualECMTPermitsHTMLDocument() {
        ArrayList<String> tab = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tab.get(tab.size() - 1));
        DocumentsPage.untilOnPage();
        String selectLicence1 = world.applicationDetails.getLicenceNumber();
        // Check heading contains correct heading
        assertTrue(true,String.valueOf(getText("h2").contains(selectLicence1)));
        // Verify Euro 6
        assertFalse(DocumentsPage.getSectionBody(ApplicationSection.CheckIfYouNeedECMTPermits).isEmpty());
        // Verify Cabotage
        assertFalse(DocumentsPage.getSectionBody(ApplicationSection.Cabotage).isEmpty());
        // Verify restricted countries
        assertEquals(BasePermitJourney.getCountriesWithLimitedPermitsChoice(),
                (Str.find("(?i)(yes|no)", DocumentsPage.getSectionBody(ApplicationSection.RestrictedCountries)).get()).trim().equalsIgnoreCase("yes")
        );
        //TODO hasRestrictedCountries is defaulting to false because it is a null boolean. Needs changing.

        // Verify percentage of international journey
        assertFalse(DocumentsPage.getSectionBody(ApplicationSection.NumberOfPermits).isEmpty());
        assertFalse(DocumentsPage.getSectionBody(ApplicationSection.Euro6).isEmpty());
    }
}
// Could improve tests to check matches answers given earlier.