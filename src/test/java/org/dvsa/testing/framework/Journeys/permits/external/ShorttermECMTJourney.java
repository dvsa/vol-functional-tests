package org.dvsa.testing.framework.Journeys.permits.external;

import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;

public class ShorttermECMTJourney extends BasePermitJourney {
    public static volatile ShorttermECMTJourney instance = new ShorttermECMTJourney();

    public static ShorttermECMTJourney getInstance(){
        if (instance == null) {
            synchronized (ShorttermECMTJourney.class){
                instance = new ShorttermECMTJourney();
            }
        }

        return instance;
    }
}
