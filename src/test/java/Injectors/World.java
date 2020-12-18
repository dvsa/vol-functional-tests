package Injectors;

import apiCalls.actions.*;
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
    public GrantLicence grantApplication;
    public GrantLicenceAPI grantLicence;
    public GenericUtils genericUtils;
    public UpdateLicence updateLicence;
    public DirectorJourneySteps directorJourneySteps;
    public DVLAJourneySteps dvlaJourneySteps;
    public InternalSearchJourneySteps internalSearch;
    public InternalNavigationalJourneySteps internalNavigation;
    public SelfServeNavigationalJourneySteps selfServeNavigation;
    public SurrenderJourneySteps surrenderJourneySteps;
    public TransportManagerJourneySteps TMJourneySteps;
    public UIJourneySteps UIJourneySteps;
    public GlobalMethods globalMethods;
    public FeeAndPaymentJourneySteps feeAndPaymentJourneySteps;
}