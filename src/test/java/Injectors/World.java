package Injectors;

import apiCalls.actions.*;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.licence.*;
import org.dvsa.testing.framework.Journeys.licence.AdminJourneys.*;
import org.dvsa.testing.framework.Journeys.licence.TransportManagerJourney;
import org.dvsa.testing.framework.Utils.Generic.DBUtils;
import org.dvsa.testing.framework.Utils.Generic.DataGenerator;
import org.dvsa.testing.framework.Utils.Generic.FormattedStrings;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;


public class World {
    public GetApplicationDetails applicationDetails;
    public RegisterUser registerUser;
    public GetUserDetails userDetails;
    public CreateApplication createApplication;
    public GrantLicence grantApplication;
    public InternalDetails internalDetails;
    public UpdateLicence updateLicence;

    public Configuration configuration;
    public DBUtils DBUtils;
    public FormattedStrings formattedStrings;
    public GenericUtils genericUtils;
    public GlobalMethods globalMethods;
    public LicenceCreation licenceCreation;

    public APIJourney APIJourney;
    public UIJourney UIJourney;

    public ForgottenCredsJourney forgottenCredsJourney;
    public BusRegistrationJourney busRegistrationJourney;
    public ContinuationJourney continuationJourney;
    public DirectorJourney directorJourney;
    public DVLAJourney dvlaJourney;
    public FeeAndPaymentJourney feeAndPaymentJourney;
    public InternalSearchJourney internalSearchJourney;
    public InternalNavigation internalNavigation;
    public OperatingCentreJourney operatingCentreJourney;
    public SelfServeNavigation selfServeNavigation;
    public SurrenderJourney surrenderJourney;
    public TransportManagerJourney TMJourney;
    public BusinessDetailsJourney businessDetailsJourney;
    public SafetyComplianceJourney safetyComplianceJourney;
    public SafetyInspectorJourney safetyInspectorJourney;
    public TransportManagerJourney transportManagerJourney;
    public VehicleDetailsJourney vehicleDetailsJourney;
    public FinancialHistoryJourney financialHistoryJourney;
    public LicenceDetailsJourney licenceDetailsJourney;
    public ConvictionsAndPenaltiesJourney convictionsAndPenaltiesJourney;
    public UserRegistrationJourney userRegistrationJourney;
    public DataGenerator DataGenerator;
    public UserAccountJourney UserAccountJourney;
    public PublicHolidayJourney publicHolidayJourney;
    public TaskAllocationRulesJourney taskAllocationRulesJourney;
    public TaskAllocation taskAllocation;
    public PrintingAndScanningJourney printingAndScanningJourney;
    public PSVJourney psvJourney;

    public GovSignInJourney govSignInJourney;


    public TypeOfLicenceJourney typeOfLicence;
    public GeneralVariationJourney generalVariationJourney;
    public TrailersJourney trailersJourney;
}