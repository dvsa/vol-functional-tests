package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.type.Permit;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.enums.sections.MultilateralSection;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesPage;
import org.dvsa.testing.framework.pageObjects.internal.details.enums.DetailsTab;
import org.dvsa.testing.framework.pageObjects.internal.irhp.InternalAnnualBilateralPermitApplicationPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsDetailsPage;
import org.junit.Assert;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class NumberOfPermitPageSteps extends BasePermitPage implements En {

    public NumberOfPermitPageSteps(World world, OperatorStore operatorStore) {
        Then("^should see the text box for each year for Annual Multilateral permit stock with an open window$", () -> {
            //need to add steps later
            throw new PendingException();
        });
        Then("^the user is on annual multilateral check your answers page$", (StepdefBody.A0) CheckYourAnswerPage::untilOnPage);
        Then("^I should be on the ECMT number of permits page$", () -> {
            assertTrue(isTitlePresent("How many permits do you need?",10 ));
        });
    }
}
