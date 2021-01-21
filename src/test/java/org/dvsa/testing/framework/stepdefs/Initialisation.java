package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import apiCalls.actions.*;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.*;
import org.dvsa.testing.framework.Utils.Generic.DBUtils;
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
        world.registerUser = new RegisterUser();
        world.userDetails = new GetUserDetails();
        world.createApplication = new CreateApplication(world.registerUser, world.userDetails);
        world.applicationDetails = new GetApplicationDetails(world.createApplication);
        world.grantApplication = new GrantLicence(world.createApplication);
        world.updateLicence = new UpdateLicence(world.createApplication);
        world.configuration = new Configuration(world);
        world.globalMethods = new GlobalMethods(world);
        world.DBUtils = new DBUtils(world);
        world.licenceCreation = new LicenceCreation(world);
        world.genericUtils = new GenericUtils(world);
        world.APIJourneySteps = new APIJourneySteps(world);
        world.continuationJourneySteps = new ContinuationJourneySteps(world);
        world.busRegistrationJourneySteps = new BusRegistrationJourneySteps(world);
        world.directorJourneySteps = new DirectorJourneySteps(world);
        world.dvlaJourneySteps = new DVLAJourneySteps(world);
        world.internalSearch = new InternalSearchJourneySteps(world);
        world.feeAndPaymentJourneySteps = new FeeAndPaymentJourneySteps(world);
        world.internalNavigation = new InternalNavigationalJourneySteps(world);
        world.selfServeNavigation = new SelfServeNavigationalJourneySteps(world);
        world.surrenderJourneySteps = new SurrenderJourneySteps(world);
        world.TMJourneySteps = new TransportManagerJourneySteps(world);
        world.UIJourneySteps = new UIJourneySteps(world);
    }
}
