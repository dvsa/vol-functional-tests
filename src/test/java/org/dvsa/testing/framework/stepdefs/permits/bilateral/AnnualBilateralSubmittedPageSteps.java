package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.junit.Assert;

public class AnnualBilateralSubmittedPageSteps extends BasePage implements En {
    public AnnualBilateralSubmittedPageSteps(World world) {
        Then("^I should not see the view receipt link$", () -> {
            Assert.assertFalse("'View  Receipt' link  should NOT be displayed but was", SubmittedPage.hasViewReceipt()
            );
        });
    }
}