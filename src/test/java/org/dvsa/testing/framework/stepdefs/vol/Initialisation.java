package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.actions.*;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.licence.AdminJourney;
import org.dvsa.testing.framework.Journeys.licence.*;
import org.dvsa.testing.framework.Utils.Generic.DBUtils;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;

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
        world.APIJourney = new APIJourney(world);
        world.continuationJourney = new ContinuationJourney(world);
        world.busRegistrationJourney = new BusRegistrationJourney(world);
        world.directorJourney = new DirectorJourney(world);
        world.dvlaJourney = new DVLAJourney(world);
        world.internalSearchJourney = new InternalSearchJourney(world);
        world.feeAndPaymentJourney = new FeeAndPaymentJourney(world);
        world.internalNavigation = new InternalNavigational(world);
        world.selfServeNavigation = new SelfServeNavigational(world);
        world.adminJourney = new AdminJourney(world);
        world.surrenderJourney = new SurrenderJourney(world);
        world.TMJourney = new TransportManagerJourney(world);
        world.UIJourney = new UIJourney(world);
        world.forgottenCredsJourney = new ForgottenCredsJourney(world);
        world.businessDetailsJourney = new BusinessDetailsJourney(world);
        world.operatingCentreJourney = new  OperatingCentreJourney(world);
        world.safetyComplianceJourney = new SafetyComplianceJourney(world);
        world.safetyInspectorJourney = new SafetyInspectorJourney(world);
        world.transportManagerJourney = new TransportManagerJourney(world);
        world.vehicleDetailsJourney = new VehicleDetailsJourney(world);
        world.financialHistoryJourney = new FinancialHistoryJourney(world);
        world.licenceDetailsJourney = new LicenceDetailsJourney(world);
        world.convictionsAndPenaltiesJourney = new ConvictionsAndPenaltiesJourney(world);
    }
}