package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
<<<<<<< HEAD
import activesupport.string.Str;
import activesupport.system.Properties;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.ProportionOfInternationalJourneyPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.internal.BaseModel;
import org.dvsa.testing.lib.newPages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsPage;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.external.pages.SectorPage;
import org.dvsa.testing.lib.newPages.external.enums.JourneyProportion;
import org.dvsa.testing.lib.newPages.external.enums.Sector;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ReceiptPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.openqa.selenium.WebDriver;

public class ApplicationSubmittedPageSteps extends BasePage implements En {

    public ApplicationSubmittedPageSteps(World world)
    {
        Then("^I open the receipt and it should open in a new window$", () -> {
            WebDriver driver = getBrowser();
            SubmittedPage.openReceipt();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            driver.switchTo().window(windows[1]);
            ReceiptPage.untilOnPage();
            driver.switchTo().window(windows[0]);
        });
    }
}
