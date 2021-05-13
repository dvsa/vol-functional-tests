package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;

public class PermitPageSteps implements En {

    public PermitPageSteps(OperatorStore operatorStore, World world) {
        Then("^the shortterm ECMT page heading is displayed as per the story$", PermitTypePage::permitTypePageHeading);
    }
}


