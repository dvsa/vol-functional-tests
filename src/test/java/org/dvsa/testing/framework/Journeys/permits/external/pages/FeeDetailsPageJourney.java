package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.pageObjects.internal.details.FeesDetailsPage;

public class FeeDetailsPageJourney {

    public static void whileFeesPresentWaveFee() {
        while(FeesDetailsPage.hasFee()) {
            FeesDetailsPage.select1stFee();
            FeesDetailsPage.confirmWaive();
            FeesDetailsPage.enterWaiveNote();
            FeesDetailsPage.clickRecommend();
            FeesDetailsPage.confirmWaive();
        }
    }
}
