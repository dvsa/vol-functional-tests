package org.dvsa.testing.framework.stepdefs.permits.internal.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.BilateralSection;
import org.dvsa.testing.framework.pageObjects.internal.details.DocumentsPage;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class HTMLSnapshotSteps extends BasePage implements En {

    public HTMLSnapshotSteps(World world) {
        Then("^text for annual bilateral snapshot is displayed as expected$", () -> {

            ArrayList<String> tab = new ArrayList<String> (getDriver().getWindowHandles());
            getDriver().switchTo().window(tab.get(tab.size() - 1));

            DocumentsPage.untilOnPage();

            // To verify HTML Snapshot page is displayed
            DocumentsPage.untilOnPage();

            // Verify the page heading is displayed correctly
            String expectedHeading = String.format("%s %s", world.createApplication.getOrganisationName(), BasePermitJourney.getFullReferenceNumber());
            assertEquals(expectedHeading, DocumentsPage.getSubHeading());

            // Verify the licence number is displayed
            assertTrue(DocumentsPage.getSectionBody(BilateralSection.Licence).contains(world.applicationDetails.getLicenceNumber()));

            // Countries selected
            assertEquals(DocumentsPage.getSectionBody(BilateralSection.CountriesSelected),"Morocco");

            // Questions answered for
            assertEquals(DocumentsPage.getSectionBody(BilateralSection.QuestionsAnsweredFor),"Morocco");

            // Permit type
            assertEquals(DocumentsPage.getSectionBody(BilateralSection.PermitType),"Empty Entry single journey permit");

            // Number of Permits
            assertEquals(DocumentsPage.getSectionBody(BilateralSection.NumberOfPermits),"3");

            //advisory text
            Assert.assertEquals("By applying for this permit, you confirm that you:", getText("//div[@class='printable__section']/p[1]", SelectorType.XPATH));
            Assert.assertEquals("will comply fully with the conditions of use under which the permit may be used", getText("//div[@class='printable__section']/ul/li[1]", SelectorType.XPATH));
            Assert.assertEquals("will carry the permit for the entire outbound and return journey and present it to any competent authority or inspectors", getText("//div[@class='printable__section']/ul/li[2]", SelectorType.XPATH));
            Assert.assertEquals("You also confirm that you understand that a permit is required for all countries you are travelling to and transiting through.", getText("//p[@class='govuk-body']", SelectorType.XPATH));
            Assert.assertEquals("I declare that the statements and information provided are true and that my application is correct. I understand that it is an offence to make a false declaration to obtain a permit or certificate.", getText("//p/strong", SelectorType.XPATH));

            //verify the return address
            String address = DocumentsPage.getAddress();
            assertEquals("International Road Haulage Permit Office, Hillcrest House, 386 Harehills Lane, Leeds, LS9 6NF", address);
        });

    }
}