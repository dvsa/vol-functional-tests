package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import apiCalls.actions.CreateApplication;
import apiCalls.actions.GetUserDetails;
import apiCalls.actions.RegisterUser;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.*;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.*;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;

public class Initialisation extends BasePage implements En {

    private World world;

    /***
     * World variable passed through to all classes regardless for instantiation purposes.
     * I.e. variable initialisations that requires other values from other classes.
     */

    public Initialisation (World world) {
        this.world = world;
        world.createApplication = new CreateApplication();
        world.registerUser = new RegisterUser();
        world.getUserDetails = new GetUserDetails();
        world.configuration = new Configuration(world);
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
        world.TMJourneySteps = new TransportManagerJourneySteps(world);
        world.UIJourneySteps = new UIJourneySteps(world);
    }
}
