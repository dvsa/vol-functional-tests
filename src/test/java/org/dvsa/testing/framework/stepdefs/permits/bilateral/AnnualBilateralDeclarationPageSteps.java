package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
<<<<<<< HEAD
import activesupport.system.Properties;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.Driver.DriverUtils;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertTrue;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

public class AnnualBilateralDeclarationPageSteps extends BasePage implements En {
    public AnnualBilateralDeclarationPageSteps(World world) {
        Then("^there's a guidance notes link to the correct gov page$", BasePermitPage::hasInternationalAuthorisationGovGuidanceLink);
    }
}
