package Injectors;

import org.dvsa.testing.framework.Journeys.APIJourneySteps;
import org.dvsa.testing.framework.Journeys.ContinuationJourneySteps;
import org.dvsa.testing.framework.Journeys.UIJourneySteps;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.CreateLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.GrantLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.UpdateLicenceAPI;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;

public class World {
    public CreateLicenceAPI createLicence;
    public GrantLicenceAPI grantLicence;
    public GenericUtils genericUtils;
    public UpdateLicenceAPI updateLicence;
    public UIJourneySteps UIJourneySteps;
    public ContinuationJourneySteps continuationJourneySteps;
    public APIJourneySteps APIJourneySteps;
}