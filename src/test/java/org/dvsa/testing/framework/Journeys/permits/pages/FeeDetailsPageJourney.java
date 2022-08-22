package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;
import static org.dvsa.testing.framework.pageObjects.BasePage.waitAndClick;

public class FeeDetailsPageJourney {

    public static void whileFeesPresentWaveFee() {
        while(FeesDetailsPage.hasFee()) {
            FeesDetailsPage.select1stFee();
            FeesDetailsPage.confirmWaive();
            FeesDetailsPage.enterWaiveNote();
            FeesDetailsPage.clickRecommend();
            FeesDetailsPage.clickApprove();
            waitAndClick("//*[contains(text(),'Close')]",SelectorType.XPATH);
            LicenceDetailsPageJourney.clickFeesTab();
        }
    }
}
