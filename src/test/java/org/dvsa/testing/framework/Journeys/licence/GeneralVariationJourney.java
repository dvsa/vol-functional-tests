package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class GeneralVariationJourney extends BasePage {

    public World world;

    public GeneralVariationJourney(World world) {
        this.world = world;
    }

    public void signInAndBeginLGVAuthorisationVariation() {
        if (world.licenceCreation.isLGVOnlyLicence()) {
            signInAndBeginLicenceAuthorisationVariation();
        } else {
            beginOperatingCentreVariation();
        }
    }

    public void signInAndBeginLicenceAuthorisationVariation() {
        beginVariation(SelfServeSection.LICENCE_AUTHORISATION);
    }

    public void beginOperatingCentreVariation() {
        beginVariation(SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
    }

    public void beginVariation(SelfServeSection selfServePage) {
        //world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", selfServePage);
        world.UIJourney.changeLicenceForVariation();
    }

}
