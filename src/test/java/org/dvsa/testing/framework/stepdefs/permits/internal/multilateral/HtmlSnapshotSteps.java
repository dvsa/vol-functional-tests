package org.dvsa.testing.framework.stepdefs.permits.internal.multilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;

public class HtmlSnapshotSteps extends BasePage implements En {
    public HtmlSnapshotSteps(World world) {
        And("A case worker is reviewing my docs & attachments", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsDetailsPage.Tab.select(DetailsTab.DocsAndAttachments);
        });
    }
}
