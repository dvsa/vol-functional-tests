package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.number.Int;
import activesupport.string.Str;
import apiCalls.Utils.eupaBuilders.GenericModel;
import apiCalls.Utils.eupaBuilders.enums.TrafficArea;
import apiCalls.Utils.eupaBuilders.enums.Boolean;
import apiCalls.Utils.eupaBuilders.external.*;
import apiCalls.Utils.eupaBuilders.external.enums.*;
import apiCalls.eupaActions.OrganisationAPI;
import apiCalls.eupaActions.external.ApplicationAPI;
import apiCalls.eupaActions.external.UserAPI;
import apiCalls.eupaActions.internal.LicenceAPI;
import cucumber.api.java8.En;
import org.apache.commons.lang.StringUtils;
import org.dvsa.testing.framework.Journeys.permits.external.VolAccountCreationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.VolLicenceApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.RandomUtils;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.pages.internal.details.LicenceDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.sections.Decisions;
import org.jetbrains.annotations.NotNull;


import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class VolLicenceSteps implements En {

    public VolLicenceSteps(OperatorStore operator, World world) {
         Given("^I have (\\d+ )?(?:a )?valid (PublicService|Goods) (standard_international |standard_national |restricted |special_restricted )?VOL (licences?)( including Northern Ireland)?$", (String quantityOfLicences, String licenceGroupType, String licenceType, String multiple, String withNI) -> {
            List<Map<String, Object>> licences = new ArrayList<>();
            world.put("licences", licences);
            world.put("licence.type.group", licenceGroupType);

            // Note that an operator can only have a single licence in any one traffic area
            List<TrafficArea> validTrafficAreas = new LinkedList<>(
                    Arrays.asList(
                    TrafficArea.Midlands,
                    TrafficArea.East,
                    TrafficArea.London,
                    TrafficArea.NorthEast,
                    TrafficArea.NorthWest,
                    TrafficArea.Scotland,
                    TrafficArea.Wales,
                    TrafficArea.West
                    )
            );

            //int numOfLicences = multiple.equals("licences") ? Int.random(2, validTrafficAreas.size() - 1) : 1;
            int numOfLicences = multiple.equals("licences") ? 2 : 1;

            numOfLicences = quantityOfLicences == null ? numOfLicences : setValidQuantity(Integer.parseInt(quantityOfLicences.trim()));

            VolLicenceSteps.registerUser(world, operator);

            IntStream.rangeClosed(1, numOfLicences).forEach((i)-> {
                TrafficArea trafficArea = (withNI != null && world.get("withNI") == null) ? TrafficArea.NorthernIreland : VolLicenceSteps.uniqueRandTrafficArea(validTrafficAreas);
                world.put("withNI", true);
                world.put("trafficArea", trafficArea);
                LicenceType licenceTypeEnum = (licenceType == null) ? LicenceType.random() : getEnum(licenceType);
                VolLicenceSteps.applyForLicence(operator, world, licenceTypeEnum, trafficArea);
                CommonSteps.payAndGrantApplication(world);
            });

            world.put("licence.quantity", numOfLicences);
        });
        Given("^I have a (curtailed|suspended) Goods (standard_international |standard_national |restricted |special_restricted )VOL licence$", (String state, String licenceType) -> {
            // TODO: Remove use of line below
            world.put("licence.type.group", "Goods");
            VolLicenceSteps.registerUser(world, operator);

            TrafficArea trafficArea = TrafficArea.randomExcept(TrafficArea.NorthernIreland);
            VolLicenceApplicationJourney.getInstance().createLicence(getEnum(licenceType), trafficArea, operator, world);

            BaseInternalJourney.getInstance().openLicence(
                    OrganisationAPI.dashboard(operator.getOrganisationId()).getDashboard().getLicences().get(0).getLicenceId()
            ).signin();

            switch (state) {
                case "curtailed":
                    LicenceDetailsPage.Section.decisions.curtail();
                    break;
                case "suspended":
                    LicenceDetailsPage.Section.decisions.suspended();
                    break;
            }

            Decisions.Model.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            Decisions.Model.legislation(Decisions.Model.Document.Art82Curtail);
            Decisions.Model.affectNow();
            Decisions.Model.untilModalIsGone(Duration.LONG, TimeUnit.SECONDS);
        });
    }

    private static int setValidQuantity(int quantity){
        return quantity > 7 ? 7 : quantity;
    }

    private static void registerUser(@NotNull World world, OperatorStore operator){
        // SETUP: User#register
        PersonModel person = new PersonModel()
                .withTitle(Title.randomEnum())
                .withForename(Str.randomWord(3, 10))
                .withFamilyName(Str.randomWord(3, 10))
                .withBirthDate(Int.random(1900, 2018), Int.random(1, 12), Int.random(1, 28));

        world.put("person.firstName", person.getForename());

        ContactDetailsModel operatorContactDetails = new ContactDetailsModel()
                .withEmailAddress(RandomUtils.email())
                .withPerson(person);

        UserRegistrationDetailsModel registeringUser = new UserRegistrationDetailsModel()
                .withUsername("Test"+Str.randomWord(10,20)+"EUP"+Int.random(100,6000))
                .withContactDetails(operatorContactDetails)
                .withOrganisationName("automatedTest".concat(Str.randomWord(5)))
                .withBusinessType(BusinessType.LimitedCompany);

        VolAccountCreationJourney.getInstance().register(registeringUser, operator);

        world.put("username", operator.getUsername()).put("password", operator.getPassword()).put("person", operator.getUserDetails());
    }

    public static World applyForLicence(OperatorStore operator, @NotNull World world, @NotNull LicenceType licenceType, TrafficArea trafficArea){
        LicenceStore licenceStore = new LicenceStore();
        operator.withLicences(licenceStore);

        PersonModel operatorDetails = operator.getUserDetails();
        UserModel registeredUserDetails = UserAPI.get(operatorDetails);
        Integer transportManagerApplicationId;
        String organisationId;
        String applicationId;
        String licenceID;
        Integer numVehicles = Int.random(2, 13);
        Integer numTrailers = Int.random(2, numVehicles);
        world.put("numberOfAuthorisedVehicles", numVehicles);
        world.put("numberOfAuthorisedTrailers", numTrailers);

        licenceStore.setNumberOfAuthorisedVehicles(numVehicles);
        licenceStore.setNumberOfAuthorisedTrailers(numTrailers);
        licenceStore.getEcmt().setNumberOfPermits(numVehicles);
        Integer version = 1;

        organisationId = String.valueOf(
                registeredUserDetails
                        .getOrganisationUserModels()
                        .get(0)
                        .getOrganisationModel()
                        .getOrganisationId()
        );
        operator.setOrganisationId(organisationId);
        world.put("organisationId", organisationId);

        // SETUP: Application#create
        ApplicationModel application = new ApplicationModel()
                .withOperatorType(OperatorType.valueOf(world.get("licence.type.group")))
                .withLicenceType(licenceType)
                .withNiFlag(Boolean.FALSE)
                .withOrganisation(world.get("organisationId"));

        // Service Calls
        StandardResponseModel response = ApplicationAPI.create(application);
        applicationId = String.valueOf(response.getId().getApplicationId());
        licenceID = String.valueOf(response.getId().getLicenceId());

        world.put("applicationId", applicationId);
        world.put("licenceNumber", licenceID);

        // SETUP: Application#businessDetails
        BusinessTypeModel businessType = new BusinessTypeModel()
                .withBusinessType(BusinessType.LimitedCompany).withOrganisationId(organisationId)
                .withApplication(applicationId)
                .withVersion(version);

        // Service Calls
        ApplicationAPI.businessType(businessType);
        String postcode = String.format(
                "N%s%d %s%s",
                RandomUtils.letter("GN"),
                Int.random(1, 18),
                RandomUtils.number(1),
                Str.randomWord(2)
        );
        // SETUP: Application#businessDetails
        AddressModel businessAddress = new AddressModel()
                .withAddressLine1(String.format("Automated Test - %s", Str.randomWord(5)))
                .withTown(Str.randomWord(4,10))
                .withPostcode(postcode.toUpperCase())
                .withCountryCode(Country.GB)
                .withVersion(String.valueOf(++version));

        String organisationName = getOrRandomOrganisationName(world);
        world.put("organisationName", organisationName);
        operator.setOrganisationName(organisationName);
        String companyNumber = getOrRandomCompanyNumber(world);
        world.put("companyNumber", companyNumber);

        BusinessDetailsModel businessDetails = new BusinessDetailsModel()
                .withApplicationId(world.get("applicationId"))
                .withCompanyNumber(companyNumber)
                .withNatureOfBusiness("Automated Test - Nature of Business")
                .withLicence(world.get("licenceNumber"))
                .withName(organisationName)
                .withAddress(businessAddress)
                .withVersion(version - 1);

        // Service Calls
        ApplicationAPI.businessDetails(businessDetails);

        // SETUP: Application#address
        ContactDetailsModel companyContactDetails = new ContactDetailsModel()
                .withPhoneNumber(RandomUtils.number(11))
                .withEmailAddress(RandomUtils.email());

        ApplicationContactDetailsModel applicationContactDetails = new ApplicationContactDetailsModel()
                .withApplicationNumber(world.get("applicationId"))
                .withConsultant("Automation Test - Consultant ".concat(Str.randomWord(5)))
                .withContact(companyContactDetails)
                .withCorrespondenceAddress(businessAddress)
                .withEstablishmentAddress(businessAddress);

        // Service Calls
        ApplicationAPI.address(applicationContactDetails);
        operator.getUserDetails().setApplicationId(applicationId);
        ApplicationAPI.partner(operator.getUserDetails());

        // SETUP: Application#operatingCnetre
        OperatingCentreModel operatingCentre = new OperatingCentreModel()
                .withApplicationId(applicationId)
                .withNoOfVehiclesRequired(numVehicles)
                .withNoOfTrailersRequired(numTrailers)
                .withPermission(Boolean.TRUE)
                .withAddress(businessAddress);

        // Service Calls
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ApplicationAPI.operatingCentre(operatingCentre);

        // SETUP: Application#operatingCentreDetails
        OperatingCentreDetailsModel operatingCentreDetails = new OperatingCentreDetailsModel()
                .withApplicationNumber(applicationId)
                .withTotAuthVehicles(numVehicles)
                .withTrafficArea(trafficArea)
                .withEnforcementArea(EnforcementArea.Unknown)
                .withTAuthTrailers(numTrailers)
                .withTotCommunityLicences(numVehicles)
                .withVersion(version);

        // Service Calls
        ApplicationAPI.operatingCentreDetails(operatingCentreDetails);

        // SETUP: Application#financialEvidence
        FinancialEvidenceModel financialEvidence = new FinancialEvidenceModel()
                .withApplicationNumber(applicationId)
                .withFinancialEvidenceUploaded(0)
                .withVersion(++version);

        // Service Calls
        ApplicationAPI.financialEvidence(financialEvidence);

        // SETUP: Application#transportManager
        TransportManagerModel transportManager = new TransportManagerModel()
                .withApplication(applicationId)
                .withFirstName(Str.randomWord(3, 10))
                .withFamilyName(Str.randomWord(3, 10))
                .withUsername("AutomatedTest".concat(Str.randomWord(27)))
                .withHasEmail(Boolean.TRUE)
                .withEmailAddress(RandomUtils.email())
                .withBirthDate(Int.random(1900, 2018), Int.random(1, 12), Int.random(1, 28));

        // Service Calls
        transportManagerApplicationId = ApplicationAPI.transportManager(transportManager).getId().getTransportManagerApplicationId();
        ApplicationAPI.submitTransport(Integer.parseInt(applicationId), transportManagerApplicationId);

        // SETUP: Application#vehicles
        VehiclesModel vehicles = new VehiclesModel()
                .withId(applicationId)
                .withHasEnteredReg(Boolean.FALSE)
                .withVersion(++version);

        // Service Calls
        if (world.<String>get("licence.type.group").toLowerCase().equals("goods")){
            ApplicationAPI.goodsVehicles(vehicles);
        } else {
            ApplicationAPI.psvVehicles(vehicles);

            VehicleDeclarationModel vehicleDeclarationModel = new VehicleDeclarationModel()
                    .withId(applicationId)
                    .withPsvVehicleSize("psvvs_both")
                    .withPsvNoSmallVhlConfirmation("Y")
                    .withPsvOperateSmallVhl("Y")
                    .withPsvSmallVhlNotes("Automated test")
                    .withPsvNoLimousineConfirmation("Y")
                    .withPsvOnlyLimousinesConfirmation("Y")
                    .withPsvLimousines("Y")
                    .withVersion(++version);

            ApplicationAPI.vehicleDeclaration(vehicleDeclarationModel);
        }

        // SETUP: Application#financialHistory
        FinancialHistoryModel financialHistory = new FinancialHistoryModel()
                .withApplicationNumber(applicationId)
                .withBankrupt(Boolean.FALSE)
                .withLiquidation(Boolean.FALSE)
                .withReceivership(Boolean.FALSE)
                .withAdministration(Boolean.FALSE)
                .withDisqualified(Boolean.FALSE)
                .withInsolvencyDetails(false)
                .withInsolvencyConfirmation(false)
                .withVersion(++version);

        // Service Calls
        ApplicationAPI.financialHistory(financialHistory);

        // SETUP: Application#safetyAndCompliance
        LicenceModel licence = new LicenceModel()
                .withLicenceNumber(licenceID)
                .withSafetyInsVaries(Boolean.FALSE)
                .withSafetyInsVehicles(String.valueOf(numVehicles))
                .withSafetyInsTrailers(String.valueOf(numTrailers))
                .withTachographIns(TachographIns.NONE)
                .withVersion(version + 2);

        SafetyModel safety = new SafetyModel()
                .withApplicationNumber(applicationId)
                .withSafetyConfirmation(Boolean.TRUE)
                .withLicenceModel(licence)
                .withVersion(++version);

        // Service Calls
        ApplicationAPI.safetyAndCompliance(safety);

        ContactDetailsModel inspectorContactDetails = new ContactDetailsModel()
                .withFao(world.get("person.firstName"))
                .withAddress(businessAddress);

        // SETUP: Application#safetyInspector
        SafetyInspectorModel safetyInspector = new SafetyInspectorModel()
                .withApplicationNumber(applicationId)
                .withLicenceNumber(licenceID)
                .withIsExternal(Boolean.FALSE)
                .withContactDetails(inspectorContactDetails);

        // Service Calls
        ApplicationAPI.safetyInspector(safetyInspector);

        // SETUP: Application#convictions
        ConvictionsModel convictions = new ConvictionsModel()
                .withApplicationNumber(applicationId)
                .withConvictionsConfirmation(Boolean.TRUE)
                .withPrevConviction(Boolean.FALSE)
                .withVersion(++version);

        // Service Calls
        ApplicationAPI.convictions(convictions);

        // SETUP: Application#licenceHistory
        LicenceHistoryModel licenceHistory = new LicenceHistoryModel()
                .withApplicationNumber(applicationId)
                .withPrevHadLicence(Boolean.FALSE)
                .withPrevHasLicence(Boolean.FALSE)
                .withPrevBeenAtPi(Boolean.FALSE)
                .withPrevBeenDisqualifiedTc(Boolean.FALSE)
                .withPrevBeenRefused(Boolean.FALSE)
                .withPrevBeenRevoked(Boolean.FALSE)
                .withPrevPurchasedAssets(Boolean.FALSE)
                .withVersion(++version);

        // Service Calls
        ApplicationAPI.licenceHistory(licenceHistory);

        // SETUP: Application#declaration
        DeclarationModel declaration = new DeclarationModel()
                .withApplicationNumber(applicationId)
                .withSignatureType(SignatureType.Physical)
                .withDeclarationConfirmation(Boolean.TRUE)
                .withVersion(++version);

        if (world.<String>get("licence.type.group").toLowerCase().equals("goods")){
            declaration
                    .withInterimReason("Automated Test - ".concat(Str.randomWord(50)))
                    .withInterimRequested(Boolean.TRUE);
        }

        // Service Calls
        ApplicationAPI.declaration(declaration);

        // SETUP: Application#submit
        GenericModel generic = new GenericModel()
                .withId(applicationId)
                .withVersion(++version);

        // Service Calls
        ApplicationAPI.submit(generic);
        world.put("version", version);

        String licenceNumber = LicenceAPI.licenceNumber(licenceID);

        Map<String, Object> licenceData = new HashMap<>();
        licenceData.put("vehicles", numVehicles);
        licenceData.put("trailers", numTrailers);
        licenceData.put("number", licenceID);
        licenceData.put("licenceNumber", licenceNumber);

        licenceStore.setLicenceNumber(licenceNumber);
        addLicence(world, licenceData);

        return world;
    }

    private static void addLicence(World world, Map<String, Object> licence) {
        List<Map<String, Object>> licences =  world.get("licences");
        if (licences != null) {
            licences.add(licence);
            world.put("licences", licences);
        }
    }

    private static String getOrRandomCompanyNumber(World world) {
        return world.containsKey("companyNumber") ? world.get("companyNumber") : StringUtils.rightPad(String.valueOf(Int.random(99999999)), 8, "0");
    }

    private static String getOrRandomOrganisationName(@NotNull World world) {
        return world.containsKey("organisationName") ? world.get("organisationName") : "Automated Test - Business ".concat(Str.randomWord(5));
    }

    private static TrafficArea uniqueRandTrafficArea(List<TrafficArea> validTrafficAreas){
        TrafficArea trafficArea;

        int index = Int.random(0, validTrafficAreas.size() - 1);
        trafficArea = validTrafficAreas.get(index);
        validTrafficAreas.remove(trafficArea);

        return trafficArea;
    }

    private LicenceType getEnum(@NotNull String licenceType) {
        LicenceType licenceEnum;
        licenceType = StringUtils.deleteWhitespace(licenceType).toLowerCase();

        switch (licenceType) {
            case "standard_international":
                licenceEnum = LicenceType.StandardInternational;
                break;
            case "standard_national":
                licenceEnum = LicenceType.StandardNational;
                break;
            case "restricted":
                licenceEnum = LicenceType.Restricted;
                break;
            case "special_restricted":
                licenceEnum = LicenceType.SpecialRestricted;
                break;
            default:
                throw new IllegalArgumentException("Unsupported enum type: ".concat(licenceType));
        }

        return licenceEnum;
    }
}
