package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.enums.PermitType;

public class ECMTShortTermJourney extends BasePermitJourney  {

    private static volatile ECMTShortTermJourney instance = null;

    protected ECMTShortTermJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static ECMTShortTermJourney getInstance() {
        if (instance == null) {
            synchronized (ECMTShortTermJourney.class) {
                instance = new ECMTShortTermJourney();
            }
        }

        return instance;
    }

    @Override
    public ECMTShortTermJourney permitType(PermitType type) {
        return (ECMTShortTermJourney) super.permitType(type);
    }

    @Override
    public ECMTShortTermJourney licencePage(World world) {
        return (ECMTShortTermJourney) super.licencePage(world);
    }
}