package Injectors;

import apiCalls.actions.CreateApplication;
import apiCalls.actions.GetApplicationDetails;
import apiCalls.actions.GetUserDetails;
import apiCalls.actions.RegisterUser;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.*;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.CreateLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.GrantLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.UpdateLicenceAPI;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;

public class World {
    public APIJourneySteps APIJourneySteps;
    public BusRegistrationJourneySteps busRegistrationJourneySteps;
    public CreateApplication createApplication;
    public GetApplicationDetails applicationDetails;
    public Configuration configuration;
    public CreateLicenceAPI createLicence;
    public ContinuationJourneySteps continuationJourneySteps;
    public RegisterUser registerUser;
    public GetUserDetails userDetails;
    public GrantLicenceAPI grantLicence;
    public GenericUtils genericUtils;
    public UpdateLicenceAPI updateLicence;
    public DirectorJourneySteps directorJourneySteps;
    public InternalSearchJourneySteps internalSearch;
    public InternalNavigationalJourneySteps internalNavigation;
    public SelfServeNavigationalJourneySteps selfServeNavigation;
    public SurrenderJourneySteps surrenderJourneySteps;
    public TransportManagerJourneySteps TMJourneySteps;
    public UIJourneySteps UIJourneySteps;
    public GlobalMethods globalMethods;
    public FeeAndPaymentJourneySteps feeAndPaymentJourneySteps;
}