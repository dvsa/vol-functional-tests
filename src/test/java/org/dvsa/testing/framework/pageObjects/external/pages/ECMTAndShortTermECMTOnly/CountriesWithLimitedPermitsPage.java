package org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class CountriesWithLimitedPermitsPage extends BasePermitPage {

    World world;
    public CountriesWithLimitedPermitsPage(World world){
        this.world = world;
    }
    public void chooseNoCountriesWithLimitedPermits() {
        world.basePermitJourney.setCountriesWithLimitedPermitsChoice(false);
        waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
        saveAndContinue();
    }
}