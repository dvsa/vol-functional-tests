package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import apiCalls.actions.*;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.licence.*;
import org.dvsa.testing.framework.Journeys.licence.AdminJourneys.*;
import org.dvsa.testing.framework.Journeys.licence.InternalNavigation;
import org.dvsa.testing.framework.Journeys.licence.SelfServeNavigation;
import org.dvsa.testing.framework.Utils.Generic.DBUtils;
import org.dvsa.testing.framework.Utils.Generic.DataGenerator;
import org.dvsa.testing.framework.Utils.Generic.FormattedStrings;
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
        world.internalDetails = new InternalDetails();
        world.updateLicence = new UpdateLicence(world.createApplication);
        world.configuration = new Configuration(world);
        world.globalMethods = new GlobalMethods(world);
        world.DBUtils = new DBUtils(world);
        world.formattedStrings = new FormattedStrings(world);
        world.licenceCreation = new LicenceCreation(world);
        world.genericUtils = new GenericUtils(world);
        world.APIJourney = new APIJourney(world);
        world.continuationJourney = new ContinuationJourney(world);
        world.busRegistrationJourney = new BusRegistrationJourney(world);
        world.directorJourney = new DirectorJourney(world);
        world.dvlaJourney = new DVLAJourney(world);
        world.internalSearchJourney = new InternalSearchJourney(world);
        world.feeAndPaymentJourney = new FeeAndPaymentJourney(world);
        world.internalNavigation = new InternalNavigation(world);
        world.operatingCentreJourney = new OperatingCentreJourney(world);
        world.selfServeNavigation = new SelfServeNavigation(world);
        world.surrenderJourney = new SurrenderJourney(world);
        world.TMJourney = new TransportManagerJourney(world);
        world.UIJourney = new UIJourney(world);
        world.DataGenerator = new DataGenerator(world);
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
        world.generalVariationJourney = new GeneralVariationJourney(world);
        world.userRegistrationJourney = new UserRegistrationJourney(world);
        world.UserAccountJourney = new UserAccountJourney(world);
        world.publicHolidayJourney = new PublicHolidayJourney(world);
        world.taskAllocationRulesJourney = new TaskAllocationRulesJourney(world);
        world.taskAllocation = new TaskAllocation(world);
        world.printingAndScanningJourney = new PrintingAndScanningJourney(world);
        world.typeOfLicence = new TypeOfLicenceJourney(world);
        world.psvJourney = new PSVJourney(world);
        world.trailersJourney = new TrailersJourney(world);
        world.systemMessagesJourney = new SystemMessagesJourney(world);
    }
}