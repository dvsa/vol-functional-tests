package Injectors;

import apiCalls.actions.*;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.*;
import org.dvsa.testing.framework.Utils.Generic.DBUtils;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;

public class World {
    public GetApplicationDetails applicationDetails;
    public RegisterUser registerUser;
    public GetUserDetails userDetails;
    public CreateApplication createApplication;
    public GrantLicence grantApplication;
    public UpdateLicence updateLicence;

    public Configuration configuration;
    public DBUtils DBUtils;
    public GenericUtils genericUtils;
    public GlobalMethods globalMethods;
    public LicenceCreation licenceCreation;

    public APIJourneySteps APIJourneySteps;
    public BusRegistrationJourneySteps busRegistrationJourneySteps;
    public ContinuationJourneySteps continuationJourneySteps;
    public DirectorJourneySteps directorJourneySteps;
    public DVLAJourneySteps dvlaJourneySteps;
    public FeeAndPaymentJourneySteps feeAndPaymentJourneySteps;
    public InternalSearchJourneySteps internalSearch;
    public InternalNavigationalJourneySteps internalNavigation;
    public SelfServeNavigationalJourneySteps selfServeNavigation;
    public SurrenderJourneySteps surrenderJourneySteps;
    public TransportManagerJourneySteps TMJourneySteps;
    public UIJourneySteps UIJourneySteps;
}