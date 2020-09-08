package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.*;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.*;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;

public class Initialisation extends BasePage implements En {

    private World world;

    public Initialisation (World world) {
        this.world = world;
        world.configuration = new Configuration();
        world.globalMethods = new GlobalMethods(world);
        world.createLicence = new CreateLicenceAPI();
        world.grantLicence = new GrantLicenceAPI(world);
        world.updateLicence = new UpdateLicenceAPI(world);
        world.genericUtils = new GenericUtils(world);
        world.APIJourneySteps = new APIJourneySteps(world);
        world.continuationJourneySteps = new ContinuationJourneySteps(world);
        world.busRegistrationJourneySteps = new BusRegistrationJourneySteps(world);
        world.directorJourneySteps = new DirectorJourneySteps(world);
        world.internalSearch = new InternalSearchJourneySteps(world);
        world.feeAndPaymentJourneySteps = new FeeAndPaymentJourneySteps(world);
        world.internalNavigation = new InternalNavigationalJourneySteps(world);
        world.selfServeNavigation = new SelfServeNavigationalJourneySteps(world);
        world.surrenderJourneySteps = new SurrenderJourneySteps(world);
        world.transportManagerJourneySteps = new TransportManagerJourneySteps(world);
        world.UIJourneySteps = new UIJourneySteps(world);
    }
}
