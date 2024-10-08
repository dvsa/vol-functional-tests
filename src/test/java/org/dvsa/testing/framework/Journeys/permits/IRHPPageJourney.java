package org.dvsa.testing.framework.Journeys.permits;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsPage;

import java.util.concurrent.TimeUnit;

public class IRHPPageJourney extends BasePermitJourney {

    public IRHPPageJourney(World world) {
        super(world);
    }

    public void completeModal(PermitType permitType) {
        IrhpPermitsApplyPage.applyForPermit();
        IrhpPermitsPage.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
        IrhpPermitsPage.Model.permitType(permitType);
        if (permitType == PermitType.ECMT_ANNUAL || permitType == PermitType.SHORT_TERM_ECMT) {
            waitAndSelectByIndex("//select[@id='yearList']", SelectorType.XPATH,1);
        }
        IrhpPermitsPage.Model.waitForContinueToBeClickable();
        IrhpPermitsPage.Model.continueButton();
        IrhpPermitsPage.Model.untilModalIsGone();
    }

    public void logInToInternalAndIRHPGrantApplication() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        String browser = String.valueOf(getURL());
        get(browser+"irhp-application/");
        IrhpPermitsApplyPage.viewApplication();
        IrhpPermitsApplyPage.grantApplication();
        IrhpPermitsApplyPage.continueButton();
    }
}
