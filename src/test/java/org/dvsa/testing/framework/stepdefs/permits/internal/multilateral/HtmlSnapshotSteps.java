package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.pages.internal.doc.PermitApplicationDocPage;
import org.dvsa.testing.lib.pages.internal.multilateral.AnnualMultilateralSnapshotPage;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class HtmlSnapshotSteps extends BasePage implements En {
    public HtmlSnapshotSteps(OperatorStore operator, World world, LicenceStore licenceStore) {
        And("A case worker is reviewing my docs & attachments", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.DocsAndAttachments);
        });
        Then("^An HTML snapshot for my annual multilateral permit is generated$", () -> {
            String expectedDescription = DocsAndAttachmentsPage.stapshotTitle(operator.getLatestLicence().get().getReferenceNumber(), PermitType.ANNUAL_MULTILATERAL);
            DocsAndAttachmentsPage.Doc actualDoc = DocsAndAttachmentsPage.snapshotDoc(licenceStore.getReferenceNumber(), PermitType.ANNUAL_MULTILATERAL);
            Assert.assertEquals(expectedDescription, actualDoc.getDescription());
            Assert.assertEquals(DocsAndAttachmentsPage.Category.Permits, actualDoc.getCategory());
            Assert.assertEquals(DocsAndAttachmentsPage.Subcategory.Application, actualDoc.getSubcategory());
        });

        And("^A case worker is reviewing my annual multilateral snapshot$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator);
            NumberOfPermitsPageJourneySteps.completeMultilateralPage();
            AnnualMultilateralJourney.INSTANCE
                    .checkYourAnswers();
            DeclarationPageJourneySteps.completeDeclaration();
            AnnualMultilateralJourney.INSTANCE
                    .feeOverviewPage()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .submit();

            HomePage.PermitsTab.untilPermitHasStatus(
                    operator.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsDetailsPage.Tab.select(BaseDetailsPage.DetailsTab.DocsAndAttachments);
            DocsAndAttachmentsPage.selectSnapshot(operator.getCurrentLicence().get().getReferenceNumber(), PermitType.ANNUAL_MULTILATERAL);
        });
        Then("^all text for annual multilateral snapshot is as expected$", () -> {

            ArrayList<String> tab = new ArrayList<String> (getDriver().getWindowHandles());
            getDriver().switchTo().window(tab.get(tab.size() - 1));

            untilUrlPathIs(PermitApplicationDocPage.RESOURCE, TimeUnit.SECONDS, Duration.LONG);
            LicenceStore licence = operator.getCurrentLicence().get();

            //AC03
            AnnualMultilateralSnapshotPage.untilOnPage();

            //AC04
            String expectedHeading = String.format("%s %s", operator.getOrganisationName(), licence.getReferenceNumber());
            Assert.assertEquals(expectedHeading, AnnualMultilateralSnapshotPage.heading());
            String actualLicence = AnnualMultilateralSnapshotPage.get(AnnualMultilateralSnapshotPage.Header.LicenceSelected);

            //AC06
            String licence1= operator.getCurrentLicenceNumber().toString().substring(9, 18);
            Assert.assertTrue(String.valueOf(actualLicence.contains(licence1)),true);

            //AC07
          /*  List<Permit> actualPermits = AnnualMultilateralSnapshotPage.permits();
            Assert.assertThat(actualPermits, Matchers.containsInAnyOrder(licence.getLatestAnnualMultilateral().get().getNumberOfPermits().toArray()));
*/
            //AC10
            AnnualMultilateralSnapshotPage.assertAddress();
        });

    }
}
