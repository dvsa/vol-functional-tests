package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.internal.details.DocsAndAttachmentsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.Category;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
import org.dvsa.testing.lib.newPages.internal.details.enums.Subcategory;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsDetailsPage;
import org.dvsa.testing.lib.newPages.BasePage;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.newPages.internal.details.DocsAndAttachmentsPage.snapshotTitle;

public class HtmlSnapshotSteps extends BasePage implements En {
    public HtmlSnapshotSteps(OperatorStore operator, World world, LicenceStore licenceStore) {
        And("A case worker is reviewing my docs & attachments", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.DocsAndAttachments);
        });
        Then("^An HTML snapshot for my annual multilateral permit is generated$", () -> {
            String expectedDescription = snapshotTitle(operator.getLatestLicence().get().getReferenceNumber(), PermitType.ANNUAL_MULTILATERAL);
            DocsAndAttachmentsPage.Doc actualDoc = DocsAndAttachmentsPage.snapshotDoc(licenceStore.getReferenceNumber(), PermitType.ANNUAL_MULTILATERAL);
            Assert.assertEquals(expectedDescription, actualDoc.getDescription());
            Assert.assertEquals(Category.Permits, actualDoc.getCategory());
            Assert.assertEquals(Subcategory.Application, actualDoc.getSubcategory());
        });

        And("^A case worker is reviewing my annual multilateral snapshot$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            AnnualMultilateralJourney.INSTANCE
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator);
            NumberOfPermitsPageJourney.completeMultilateralPage();
            AnnualMultilateralJourney.INSTANCE
                    .checkYourAnswers();
            DeclarationPageJourney.completeDeclaration();
            world.feeAndPaymentJourney.customerPaymentModule();
            AnnualMultilateralJourney.INSTANCE
                    .submit();

            HomePage.PermitsTab.untilPermitHasStatus(
                    operator.getCurrentLicence().get().getReferenceNumber(),
                    PermitStatus.VALID,
                    Duration.LONG,
                    TimeUnit.MINUTES
            );

            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.DocsAndAttachments);
            DocsAndAttachmentsPage.selectSnapshot(operator.getCurrentLicence().get().getReferenceNumber(), PermitType.ANNUAL_MULTILATERAL);
        });
    }
}
