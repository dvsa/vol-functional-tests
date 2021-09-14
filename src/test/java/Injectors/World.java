package Injectors;

import apiCalls.actions.*;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.licence.AdminJourney;
import org.dvsa.testing.framework.Journeys.licence.*;
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

    public AdminJourney adminJourney;
    public APIJourney APIJourney;
    public UIJourney UIJourney;

    public ForgottenCredsJourney forgottenCredsJourney;
    public BusRegistrationJourney busRegistrationJourney;
    public ContinuationJourney continuationJourney;
    public DirectorJourney directorJourney;
    public DVLAJourney dvlaJourney;
    public FeeAndPaymentJourney feeAndPaymentJourney;
    public InternalSearchJourney internalSearchJourney;
    public InternalNavigational internalNavigation;
    public SelfServeNavigational selfServeNavigation;
    public SurrenderJourney surrenderJourney;
    public TransportManagerJourney TMJourney;

}