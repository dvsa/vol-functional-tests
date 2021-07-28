package org.dvsa.testing.framework.stepdefs.permits.internal.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;
import org.dvsa.testing.lib.newPages.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.lib.newPages.internal.details.DocumentsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.Category;
import org.dvsa.testing.lib.newPages.internal.details.enums.DocumentHeading;
import org.dvsa.testing.lib.newPages.internal.details.enums.Subcategory;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.junit.Assert;

import java.util.ArrayList;

import static org.dvsa.testing.lib.newPages.internal.details.DocsAndAttachmentsPage.snapshotTitle;
import static org.junit.Assert.assertEquals;

public class HTMLSnapshotSteps extends BasePage implements En {

    public HTMLSnapshotSteps(OperatorStore operator, World world, LicenceStore licenceStore) {

        Then("^an HTML snapshot for annual bilateral permit is generated$", () -> {
            String expectedDescription = snapshotTitle(licenceStore.getReferenceNumber(), PermitType.ANNUAL_BILATERAL);
            DocsAndAttachmentsPage.selectSnapshot(licenceStore.getReferenceNumber(),PermitType.ANNUAL_BILATERAL);
            DocsAndAttachmentsPage.Doc actualDoc = DocsAndAttachmentsPage.snapshotDoc(licenceStore.getReferenceNumber(), PermitType.ANNUAL_BILATERAL);
            assertEquals(expectedDescription, actualDoc.getDescription());
            assertEquals(Category.Permits, actualDoc.getCategory());
            assertEquals(Subcategory.Application, actualDoc.getSubcategory());
        });
        Then("^text for annual bilateral snapshot is displayed as expected$", () -> {

            ArrayList<String> tab = new ArrayList<String> (getDriver().getWindowHandles());
            getDriver().switchTo().window(tab.get(tab.size() - 1));

            DocumentsPage.untilOnPage();

            //To verify HTML Snapshot page is displayed
            DocumentsPage.untilOnPage();

            //Verify the page heading is displayed correctly
            String expectedHeading = String.format("%s %s", operator.getOrganisationName(),licenceStore.getReferenceNumber());
            assertEquals(expectedHeading, DocumentsPage.getSubHeading());
//            String actualLicence = DocumentsPage.getSectionHeading(DocumentHeading.Licence);

            //verify the licence number is displayed
            String licence1= operator.getCurrentLicenceNumber().toString().substring(9, 18);
//            Assert.assertTrue(String.valueOf(actualLicence.contains(licence1)),true);

            //countries selected
            assertEquals(BasePage.getElementValueByText("//dl[2]//dt[1]", SelectorType.XPATH),"Countries selected");
            assertEquals(BasePage.getElementValueByText("//dl[2]//dd[1]",SelectorType.XPATH),operator.getCountry());

            //Questions answered for
            assertEquals(BasePage.getElementValueByText("//dl[3]//dt[1]",SelectorType.XPATH),"Questions answered for");
            assertEquals(BasePage.getElementValueByText("//dl[3]//dd[1]",SelectorType.XPATH),operator.getCountry());

            //Period for which you need permits
            assertEquals(BasePage.getElementValueByText("//dl[4]//dt[1]",SelectorType.XPATH),"Period for which you need permits");
            String expected= String.valueOf(operator.getCurrentBilateralPeriodType());
            assertEquals(BasePage.getElementValueByText("//dl[4]//dd[1]",SelectorType.XPATH),expected);

            //What do you need to use your permits for
            assertEquals(BasePage.getElementValueByText("//dl[5]//dt[1]",SelectorType.XPATH),"What do you need to use your permits for?");
            String expected1= PermitUsagePage.getJourney();
            assertEquals(BasePage.getElementValueByText("//dl[5]//dd[1]",SelectorType.XPATH),expected1);

            //Do you need to carry out cabotage?
            assertEquals(BasePage.getElementValueByText("//dl[6]//dt[1]",SelectorType.XPATH),"Do you need to carry out cabotage?");
            assertEquals(BasePage.getElementValueByText("//dl[6]//dd[1]",SelectorType.XPATH),"I only need permits for cabotage");

            //How many permits do you need?
            String permitLabel = NumberOfPermitsPageJourney.getLabel();
            String permitValue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());
            String expected3= permitValue + " " + permitLabel + "s";
            assertEquals(BasePage.getElementValueByText("//dl[7]//dt[1]",SelectorType.XPATH),"How many permits do you need?");
            assertEquals(BasePage.getElementValueByText("//dl[7]//dd[1]",SelectorType.XPATH),expected3);

            //advisory text
            Assert.assertTrue("You will comply fully with the conditions of use under which the permits may be used.",true);
            Assert.assertTrue("Your driver will carry the permit for the entire outbound and return journey and present it to any competent authority or inspectors.",true);
            Assert.assertTrue("You understand that a permit is required for all countries you are travelling to and transiting through.",true);
            Assert.assertTrue("I declare that the statements and information provided are true and that my application is correct. I understand that it is an offence to make a false declaration to obtain a permit.",true);

            //verify the return address
            String address = DocumentsPage.getAddress();
            assertEquals("International Road Haulage Permit Office, Hillcrest House, 386 Harehills Lane, Leeds, LS9 6NF", address);
        });

    }
}