package org.dvsa.testing.framework.Journeys.permits.external;

import apiCalls.Utils.eupaBuilders.enums.TrafficArea;
import apiCalls.Utils.eupaBuilders.external.enums.LicenceType;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.VolLicenceSteps;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;

public class VolLicenceApplicationJourney {

    private static VolLicenceApplicationJourney instance = null;

    protected VolLicenceApplicationJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static VolLicenceApplicationJourney getInstance() {
        if (instance == null) {
            synchronized (VolLicenceApplicationJourney.class){
                instance = new VolLicenceApplicationJourney();
            }
        }

        return instance;
    }

    // TODO: Remove world once #applyForLicence has been rewritten
    public VolLicenceApplicationJourney createLicence(LicenceType licenceType, TrafficArea trafficArea, OperatorStore operator, World world){
        VolLicenceSteps.applyForLicence(operator, world, licenceType, trafficArea);
        CommonSteps.payAndGrantApplication(world);

        return this;
    }

}
