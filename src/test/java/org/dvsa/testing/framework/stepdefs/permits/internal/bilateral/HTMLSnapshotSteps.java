package org.dvsa.testing.framework.stepdefs.permits.internal.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.pages.internal.bilateral.AnnualBilateralSnapshotPage;
import org.dvsa.testing.lib.pages.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.lib.pages.internal.doc.PermitApplicationDocPage;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class HTMLSnapshotSteps extends BasePage implements En {

    public HTMLSnapshotSteps(OperatorStore operator, World world, LicenceStore licenceStore) {

        Then("^an HTML snapshot for annual bilateral permit is generated$", () -> {
            String expectedDescription = DocsAndAttachmentsPage.stapshotTitle(licenceStore.getReferenceNumber(), PermitType.ANNUAL_BILATERAL);
            DocsAndAttachmentsPage.selectSnapshot(licenceStore.getReferenceNumber(),PermitType.ANNUAL_BILATERAL);
            DocsAndAttachmentsPage.Doc actualDoc = DocsAndAttachmentsPage.snapshotDoc(licenceStore.getReferenceNumber(), PermitType.ANNUAL_BILATERAL);
            Assert.assertEquals(expectedDescription, actualDoc.getDescription());
            Assert.assertEquals(DocsAndAttachmentsPage.Category.Permits, actualDoc.getCategory());
            Assert.assertEquals(DocsAndAttachmentsPage.Subcategory.Application, actualDoc.getSubcategory());
        });
        Then("^text for annual bilateral snapshot is displayed as expected$", () -> {

            ArrayList<String> tab = new ArrayList<String> (getDriver().getWindowHandles());
            getDriver().switchTo().window(tab.get(tab.size() - 1));

            untilUrlPathIs(PermitApplicationDocPage.RESOURCE, TimeUnit.SECONDS, Duration.LONG);
            LicenceStore licence = operator.getCurrentLicence().get();

            //To verify HTML Snapshot page is displayed
            AnnualBilateralSnapshotPage.untilOnPage();

            //Verify the page heading is displayed correctly
            String expectedHeading = String.format("%s %s", operator.getOrganisationName(),licenceStore.getReferenceNumber());
            Assert.assertEquals(expectedHeading, AnnualBilateralSnapshotPage.heading());
            String actualLicence = AnnualBilateralSnapshotPage.get(AnnualBilateralSnapshotPage.Header.LicenceSelected);

            //verify the licence number is displayed
            String licence1= operator.getCurrentLicenceNumber().toString().substring(9, 18);
            Assert.assertTrue(String.valueOf(actualLicence.contains(licence1)),true);

            //countries selected
            Assert.assertEquals(BasePage.getElementValueByText("//dl[2]//dt[1]", SelectorType.XPATH),"Countries selected");
            Assert.assertEquals(BasePage.getElementValueByText("//dl[2]//dd[1]",SelectorType.XPATH),operator.getCountry());

            //Questions answered for
            Assert.assertEquals(BasePage.getElementValueByText("//dl[3]//dt[1]",SelectorType.XPATH),"Questions answered for");
            Assert.assertEquals(BasePage.getElementValueByText("//dl[3]//dd[1]",SelectorType.XPATH),operator.getCountry());

            //Period for which you need permits
            Assert.assertEquals(BasePage.getElementValueByText("//dl[4]//dt[1]",SelectorType.XPATH),"Period for which you need permits");
            String expected= String.valueOf(operator.getCurrentBilateralPeriodType());
            Assert.assertEquals(BasePage.getElementValueByText("//dl[4]//dd[1]",SelectorType.XPATH),expected);

            //What do you need to use your permits for
            Assert.assertEquals(BasePage.getElementValueByText("//dl[5]//dt[1]",SelectorType.XPATH),"What do you need to use your permits for?");
            String expected1= PermitUsagePage.getJourney();
            Assert.assertEquals(BasePage.getElementValueByText("//dl[5]//dd[1]",SelectorType.XPATH),expected1);

            //Do you need to carry out cabotage?
            Assert.assertEquals(BasePage.getElementValueByText("//dl[6]//dt[1]",SelectorType.XPATH),"Do you need to carry out cabotage?");
            Assert.assertEquals(BasePage.getElementValueByText("//dl[6]//dd[1]",SelectorType.XPATH),"I only need permits for cabotage");

            //How many permits do you need?
            String permitLabel = NumberOfPermitsPageJourneySteps.getLabel();
            String permitValue = String.valueOf(NumberOfPermitsPageJourneySteps.getPermitValue());
            String expected3= permitValue + " " + permitLabel + "s";
            Assert.assertEquals(BasePage.getElementValueByText("//dl[7]//dt[1]",SelectorType.XPATH),"How many permits do you need?");
            Assert.assertEquals(BasePage.getElementValueByText("//dl[7]//dd[1]",SelectorType.XPATH),expected3);

            //advisory text

            Assert.assertTrue("You will comply fully with the conditions of use under which the permits may be used.",true);
            Assert.assertTrue("Your driver will carry the permit for the entire outbound and return journey and present it to any competent authority or inspectors.",true);
            Assert.assertTrue("You understand that a permit is required for all countries you are travelling to and transiting through.",true);
            Assert.assertTrue("I declare that the statements and information provided are true and that my application is correct. I understand that it is an offence to make a false declaration to obtain a permit.",true);

            //verify the return address
            AnnualBilateralSnapshotPage.assertAddress();
        });

    }
}