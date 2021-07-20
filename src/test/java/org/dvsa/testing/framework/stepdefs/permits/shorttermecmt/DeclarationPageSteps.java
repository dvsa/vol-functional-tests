package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;

public class DeclarationPageSteps implements En {

    public DeclarationPageSteps (OperatorStore operatorStore, World world) {

        Then("^I should see the correct heading on the declaration page$", DeclarationPageJourney::hasPageHeading);
        Then("^the declaration page has a reference number", DeclarationPage::hasReference);

    }
}
