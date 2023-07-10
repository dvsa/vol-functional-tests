package org.dvsa.testing.framework.Injectors;

import apiCalls.actions.*;
import org.dvsa.testing.framework.Global.Configuration;
import org.dvsa.testing.framework.Global.GlobalMethods;
import org.dvsa.testing.framework.Journeys.licence.*;
import org.dvsa.testing.framework.Journeys.licence.AdminJourneys.*;
import org.dvsa.testing.framework.Journeys.licence.TransportManagerJourney;
import org.dvsa.testing.framework.Journeys.permits.*;
import org.dvsa.testing.framework.Utils.Generic.DBUtils;
import org.dvsa.testing.framework.Utils.Generic.DataGenerator;
import org.dvsa.testing.framework.Utils.Generic.FormattedStrings;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.stepdefs.vol.ExternalSearch;
import org.dvsa.testing.framework.stepdefs.vol.GovSignIn;


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
    public GovSignInJourney GovSignInJourney;

    public ForgottenCredsJourney forgottenCredsJourney;
    public BusRegistrationJourney busRegistrationJourney;
    public ContinuationJourney continuationJourney;
    public DirectorJourney directorJourney;
    public GovSignIn govSignIn;
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

    public TypeOfLicenceJourney typeOfLicenceJourney;
    public GeneralVariationJourney generalVariationJourney;
    public TrailersJourney trailersJourney;
    public SystemMessagesJourney systemMessagesJourney;
    public SubmitApplicationJourney submitApplicationJourney;
    public GrantApplicationJourney grantApplicationJourney;

    public EcmtInternationalRemovalJourney ecmtInternationalRemovalJourney;
    public EcmtApplicationJourney ecmtApplicationJourney;
    public IRHPPageJourney irhpPageJourney;
    public BasePermitJourney basePermitJourney;
    public AnnualBilateralJourney annualBilateralJourney;
    public ShortTermECMTJourney shortTermECMTJourney;
    public CountriesWithLimitedPermitsPage countriesWithLimitedPermitsPage;
    public YearSelectionPage yearSelectionPage;
    public PermitFeePage permitFeePage;
    public ExternalSearch externalSearch;
}