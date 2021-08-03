package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import activesupport.number.Int;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.openqa.selenium.WebElement;

import java.util.List;


public class EcmtApplicationJourney extends BasePermitJourney {

    private static volatile EcmtApplicationJourney instance = null;

    protected EcmtApplicationJourney() {
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static EcmtApplicationJourney getInstance() {
        if (instance == null) {
            synchronized (EcmtApplicationJourney.class) {
                instance = new EcmtApplicationJourney();
            }
        }

        return instance;
    }

    public EcmtApplicationJourney numberOfPermitsPage(int maxNumberOfPermits) {
        List<WebElement> numberOfPermitFields = findAll("//*[contains(@class, 'field')]//input[@type='number']", SelectorType.XPATH);
        numberOfPermitFields.forEach(numberOfPermitsField -> {
            Integer randomNumberOfPermitsLessThanMax = Int.random(maxNumberOfPermits);
                    numberOfPermitsField.sendKeys(String.valueOf(randomNumberOfPermitsLessThanMax));
        });
        NumberOfPermitsPage.saveAndContinue();
        return this;
    }

    public EcmtApplicationJourney feeOverviewPage() {
        PermitFeePage.saveAndContinue();
        return this;
    }

    @Override
    public EcmtApplicationJourney permitType(PermitType type) {
        return (EcmtApplicationJourney) super.permitType(type);
    }

    @Override
    public EcmtApplicationJourney licencePage(World world) {
        return (EcmtApplicationJourney) super.licencePage(world);
    }

}