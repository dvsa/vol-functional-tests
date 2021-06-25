package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.external.pages.CheckYourAnswerPage;

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

    public ECMTShortTermJourney checkYourAnswersPage() {
        CheckYourAnswerPage.saveAndContinue();
        return this;
    }

    @Override
    public ECMTShortTermJourney beginApplication() {
        return (ECMTShortTermJourney) super.beginApplication();
    }

    @Override
    public ECMTShortTermJourney permitType(OperatorStore operatorStore) {
        return (ECMTShortTermJourney) super.permitType(operatorStore);
    }

    @Override
    public ECMTShortTermJourney permitType() {
        return (ECMTShortTermJourney) super.permitType();
    }


    @Override
    public ECMTShortTermJourney permitType(PermitType type, OperatorStore operator) {
        return (ECMTShortTermJourney) super.permitType(type, operator);
    }

    @Override
    public ECMTShortTermJourney licencePage(OperatorStore operator, World world) {
        return (ECMTShortTermJourney) super.licencePage(operator, world);
    }

    @Override
    public ECMTShortTermJourney shortTermType(PeriodType shortTermType, OperatorStore operator) {
        return (ECMTShortTermJourney) super.shortTermType(shortTermType, operator);
    }
}