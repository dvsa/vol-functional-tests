package Injectors;

import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.*;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.CreateLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.GrantLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.UpdateLicenceAPI;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;

public class World {
    public Configuration configuration;
    public CreateLicenceAPI createLicence;
    public GrantLicenceAPI grantLicence;
    public GenericUtils genericUtils;
    public UpdateLicenceAPI updateLicence;
    public APIJourneySteps APIJourneySteps;
    public ContinuationJourneySteps continuationJourneySteps;
    public BusRegistrationJourneySteps busRegistrationJourneySteps;
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