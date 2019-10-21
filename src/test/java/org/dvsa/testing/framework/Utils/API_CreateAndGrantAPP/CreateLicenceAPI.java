package org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP;

import activesupport.MissingRequiredArgument;
import activesupport.http.RestUtils;
import activesupport.number.Int;
import activesupport.string.Str;
import activesupport.system.Properties;
import enums.BusinessType;
import enums.LicenceType;
import enums.OperatorType;
import enums.UserRoles;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.*;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;
import org.dvsa.testing.framework.Utils.API_Builders.*;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.url.api.URL;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import javax.xml.ws.http.HTTPException;
import java.util.HashMap;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dvsa.testing.framework.Journeys.APIJourneySteps.adminApiHeader;
import static org.dvsa.testing.framework.Utils.API_Headers.Headers.getHeaders;

public class CreateLicenceAPI extends BaseAPI{

    private static ValidatableResponse apiResponse;
    private static int version = 1;

    private String title;
    private String foreName;
    private String familyName;
    private String birthDate = Int.random(1900, 2018) + "-" + Int.random(1, 12) + "-" + Int.random(1, 28);
    private String addressLine1 = "API House";
    private String town = "Nottingham";
    private String postcode = "NG23HX";
    private String countryCode = "GB";
    private String organisationName = Str.randomWord(10);
    private String emailAddress = Str.randomWord(6).concat(".tester@dvsa.com");
    private String transManEmailAddress = Str.randomWord(6).concat(".TM@dvsa.com");
    private String applicationNumber;
    private String userId;
    private String tmUserName;
    private String username;
    private String loginId;
    private String pid;
    private String organisationId;
    private String licenceNumber;
    private String transportManagerApplicationId;
    private String companyNumber = String.valueOf(Int.random(00000000, 99999999));
    private String licenceType = System.getProperty("licenceType"); //"standard_international"
    private String businessType = System.getProperty("businessType"); //"limited_company"
    private String operatorType = System.getProperty("operatorType"); //goods
    private String niFlag = System.getProperty("ni"); //"Y|N"
    private String trafficArea = "D";
    private String enforcementArea = "EA-D";
    private String restrictedVehicles = "2";
    private String applicationStatus;
    private String licenceId;
    private String businessName = "API";
    private String isInterim;
    private String isOwner;
    private String tmType = "tm_t_i";
    private String hours = "2.0";
    private String phoneNumber;
    private String businessEmailAddress;

    private int noOfVehiclesRequired = 5;

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getTmType() {
        return tmType;
    }

    public void setTmType(String tmType) {
        this.tmType = tmType;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    private void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getTransManEmailAddress() {
        return transManEmailAddress;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }


    public void setTransManEmailAddress(String transManEmailAddress) {
        this.transManEmailAddress = transManEmailAddress;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setNoOfVehiclesRequired(int noOfVehiclesRequired) {
        this.noOfVehiclesRequired = noOfVehiclesRequired;
    }

    public int getNoOfVehiclesRequired() {
        return noOfVehiclesRequired;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getForeName() {
        return foreName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTmUserName(String tmUserName) {
        this.tmUserName = tmUserName;
    }

    public String getTmUserName() {
        return tmUserName;
    }

    public String getPid() {
        return pid;
    }

    private void setPid(String pid) {
        this.pid = pid;
    }

    public String getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(String licenceId) {
        this.licenceId = licenceId;
    }

    public String getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(String licenceType) {
        this.licenceType = licenceType;
    }

    public String getTransportManagerApplicationId() {
        return transportManagerApplicationId;
    }

    private void setTransportManagerApplicationId(String transportManagerApplicationId) {
        this.transportManagerApplicationId = transportManagerApplicationId;
    }

    public void setTrafficArea(String trafficArea) {
        this.trafficArea = trafficArea;
    }

    public String getTrafficArea() {
        return trafficArea;
    }

    public String getEnforcementArea() {
        return enforcementArea;
    }

    public void setEnforcementArea(String enforcementArea) {
        this.enforcementArea = enforcementArea;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String licenceStatus) {
        this.applicationStatus = licenceStatus;
    }

    public String getRestrictedVehicles() {
        return restrictedVehicles;
    }

    public void setRestrictedVehicles(String restrictedVehicles) {
        this.restrictedVehicles = restrictedVehicles;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getNiFlag() {
        return niFlag;
    }

    public void setNiFlag(String niFlag) {
        this.niFlag = niFlag;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getIsInterim() {
        return isInterim;
    }

    public void setIsInterim(String isInterim) {
        this.isInterim = isInterim;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBusinessEmailAddress() {
        return businessEmailAddress;
    }

    public void setBusinessEmailAddress(String businessEmailAddress) {
        this.businessEmailAddress = businessEmailAddress;
    }

    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    private static final Logger LOGGER = LogManager.getLogger(CreateLicenceAPI.class);

    public CreateLicenceAPI() throws MissingRequiredArgument {
        if (licenceType == null) {
            operatorType = "goods";
            licenceType = "standard_international";
            businessType = "limited_company";
            niFlag = "N";
            isInterim = "N";
            isOwner = "Y";
        }
    }

    public void registerUser() {
        setTitle("title_mr");
        setForeName("Vol-API-".concat(Str.randomWord(3).toLowerCase()));
        setFamilyName("Ann");
        String registerResource = URL.build(env, "user/selfserve/register").toString();
        Headers.headers.put("api", "dvsa");
        setLoginId(Str.randomWord(8));

        PersonBuilder personBuilder = new PersonBuilder().withTitle(getTitle()).withForename(getForeName()).withFamilyName(getFamilyName()).withBirthDate(getBirthDate());
        ContactDetailsBuilder contactDetailsBuilder = new ContactDetailsBuilder().withEmailAddress(getEmailAddress()).withPerson(personBuilder);

        SelfServeUserRegistrationDetailsBuilder selfServeUserRegistrationDetailsBuilder = new SelfServeUserRegistrationDetailsBuilder().withLoginId(getLoginId()).withContactDetails(contactDetailsBuilder)
                .withOrganisationName(getOrganisationName()).withBusinessType(String.valueOf(BusinessType.getEnum(getBusinessType())));

        apiResponse = RestUtils.post(selfServeUserRegistrationDetailsBuilder, registerResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        } else {
            userId = apiResponse.extract().jsonPath().getString("id.user");
        }
    }

    public ValidatableResponse getUserDetails(String userType, String userId, String header) {
        String userDetailsResource;
        Headers.headers.put("x-pid", header);

        if (userType.equals(UserRoles.EXTERNAL.getUserRoles())) {
            userDetailsResource = URL.build(env, String.format("user/%s/%s", userType, this.userId)).toString();
            apiResponse = RestUtils.get(userDetailsResource, getHeaders());
            setPid(apiResponse.extract().jsonPath().getString("pid"));
            setOrganisationId(apiResponse.extract().jsonPath().prettyPeek().getString("organisationUsers.organisation.id"));
        } else if (userType.equals(UserRoles.INTERNAL.getUserRoles())) {
            userDetailsResource = URL.build(env, String.format("user/%s/%s", userType, userId)).toString();
            apiResponse = RestUtils.get(userDetailsResource, getHeaders());
        }

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
        return apiResponse;
    }

    public void createApplication() {
        String createApplicationResource = URL.build(env, "application").toString();
        Headers.headers.put("x-pid", pid);
        HashMap<String, String> headers = getHeaders();
        ApplicationBuilder applicationBuilder = new ApplicationBuilder().withOperatorType(String.valueOf(OperatorType.getEnum(getOperatorType())))
                .withLicenceType(String.valueOf(LicenceType.getEnum(getLicenceType()))).withNiFlag(getNiFlag()).withOrganisation(getOrganisationId());
        apiResponse = RestUtils.post(applicationBuilder, createApplicationResource, headers);
        applicationNumber = apiResponse.extract().jsonPath().getString("id.application");
        licenceNumber = apiResponse.extract().jsonPath().getString("id.licence");
        setApplicationNumber(applicationNumber);

        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void updateBusinessType() {
        String organisationVersion = fetchApplicationInformation(applicationNumber, "licence.organisation.version", "1");
        String updateBusinessTypeResource = URL.build(env, String.format("organisation/%s/business-type/", getOrganisationId())).toString();

        BusinessTypeBuilder businessTypeBuilder = new BusinessTypeBuilder().withBusinessType(String.valueOf(BusinessType.getEnum(getBusinessType()))).withVersion(organisationVersion)
                .withId(getOrganisationId()).withApplication(getApplicationNumber());
        apiResponse = RestUtils.put(businessTypeBuilder, updateBusinessTypeResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void updateBusinessDetails() {
        String organisationVersion = fetchApplicationInformation(applicationNumber, "licence.organisation.version", "1");
        String natureOfBusiness = "apiTesting";
        String updateBusinessDetailsResource = URL.build(env, String.format("organisation/business-details/application/%s", getApplicationNumber())).toString();

        AddressBuilder address = new AddressBuilder().withAddressLine1(addressLine1).withTown(town).withPostcode(postcode);
        UpdateBusinessDetailsBuilder businessDetails = new UpdateBusinessDetailsBuilder()
                .withId(getApplicationNumber()).withCompanyNumber(companyNumber).withNatureOfBusiness(natureOfBusiness).withLicence(licenceNumber)
                .withVersion(organisationVersion).withName(businessName).withAddress(address);
        apiResponse = RestUtils.put(businessDetails, updateBusinessDetailsResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addAddressDetails() {
        String phoneNumber = "0712345678";
        String businessEmail = Str.randomWord(6).concat(".volBusiness@dvsa.com");
        String applicationAddressResource = URL.build(env, String.format("application/%s/addresses/", applicationNumber)).toString();
        AddressBuilder address = new AddressBuilder().withAddressLine1(addressLine1).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
        ContactDetailsBuilder contactDetailsBuilder = new ContactDetailsBuilder().withPhoneNumber(phoneNumber).withEmailAddress(businessEmail);
        ApplicationAddressBuilder addressBuilder = new ApplicationAddressBuilder().withId(applicationNumber).withConsultant("Consult").withContact(contactDetailsBuilder)
                .withCorrespondenceAddress(address).withEstablishmentAddress(address);
        apiResponse = RestUtils.put(addressBuilder, applicationAddressResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
        setBusinessEmailAddress(businessEmail);
        setPhoneNumber(phoneNumber);
    }

    public void addPartners() {
        String addPersonResource = URL.build(env, String.format("application/%s/people/", applicationNumber)).toString();
        PersonBuilder addPerson = new PersonBuilder().withId(applicationNumber).withTitle(getTitle()).withForename(getForeName()).withFamilyName(getFamilyName()).withBirthDate(birthDate);
        apiResponse = RestUtils.post(addPerson, addPersonResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addOperatingCentre() {
        String operatingCentreResource = URL.build(env, String.format("application/%s/operating-centre/", applicationNumber)).toString();
        int buildingNumber = Int.random(0, 1000);
        String permissionOption = "Y";
        String operatingCentreAddress;
        operatingCentreAddress = String.valueOf(buildingNumber).concat(" API_Operating_Centre");
        OperatingCentreBuilder operatingCentreBuilder = new OperatingCentreBuilder();

        if (operatorType.equals("goods")) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddress).withTown(town).withPostcode(getPostcode()).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(getApplicationNumber()).withNoOfVehiclesRequired(String.valueOf(getNoOfVehiclesRequired()))
                    .withNoOfTrailersRequired(String.valueOf(getNoOfVehiclesRequired())).withPermission(permissionOption).withAddress(address);
        }
        if (operatorType.equals("public") && (!licenceType.equals("special_restricted"))) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddress).withTown(town).withPostcode(getPostcode()).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(applicationNumber).withNoOfVehiclesRequired(String.valueOf(noOfVehiclesRequired)).withPermission(permissionOption).withAddress(address);
        }
        if (operatorType.equals("public") && (licenceType.equals("restricted"))) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddress).withTown(town).withPostcode(getPostcode()).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(getApplicationNumber()).withNoOfVehiclesRequired(String.valueOf(restrictedVehicles)).withPermission(permissionOption).withAddress(address);
        }
        if (!licenceType.equals("special_restricted")) {
            apiResponse = RestUtils.post(operatingCentreBuilder, operatingCentreResource, getHeaders());
        }

        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void updateOperatingCentre() {
        if (licenceType.equals("special_restricted")) {
            return;
        }
        String updateOperatingCentreResource = URL.build(env, String.format("application/%s/operating-centres", applicationNumber)).toString();
        OperatingCentreUpdater updateOperatingCentre = new OperatingCentreUpdater();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        if (operatorType.equals("goods")) {
            updateOperatingCentre.withId(applicationNumber).withTotAuthVehicles(noOfVehiclesRequired)
                    .withTrafficArea(getTrafficArea()).withEnforcementArea(getEnforcementArea()).withTotCommunityLicences(Integer.parseInt(restrictedVehicles))
                    .withTAuthTrailers(Integer.parseInt(String.valueOf(noOfVehiclesRequired))).withVersion(applicationVersion);
        }
        if (operatorType.equals("public") && (!licenceType.equals("restricted"))) {
            updateOperatingCentre.withId(getApplicationNumber()).withTotAuthVehicles(noOfVehiclesRequired)
                    .withTrafficArea(getTrafficArea()).withEnforcementArea(getEnforcementArea()).withTotCommunityLicences(noOfVehiclesRequired).withVersion(applicationVersion);
        }

        if (operatorType.equals("public") && (licenceType.equals("restricted"))) {
            updateOperatingCentre.withId(getApplicationNumber()).withTotAuthVehicles(Integer.valueOf(String.valueOf(restrictedVehicles)))
                    .withTrafficArea(getTrafficArea()).withEnforcementArea(getEnforcementArea()).withVersion(applicationVersion);
        }

        apiResponse = RestUtils.put(updateOperatingCentre, updateOperatingCentreResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addFinancialEvidence() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }

        String financialEvidenceResource = URL.build(env, String.format("application/%s/financial-evidence", applicationNumber)).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        FinancialEvidenceBuilder financialEvidenceBuilder = new FinancialEvidenceBuilder().withId(applicationNumber).withVersion(applicationVersion).withFinancialEvidenceUploaded(0);
        apiResponse = RestUtils.put(financialEvidenceBuilder, financialEvidenceResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addTransportManager() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }
        int randNumber = Int.random(0, 2000);
        tmUserName = "apiTM".concat(getLoginId()).concat(String.valueOf(randNumber));
        String hasEmail = "Y";
        String addTransportManager = URL.build(env, "transport-manager/create-new-user/").toString();
        TransportManagerBuilder transportManagerBuilder = new TransportManagerBuilder().withApplication(getApplicationNumber()).withFirstName(getForeName())
                .withFamilyName(getFamilyName()).withHasEmail(hasEmail).withUsername(getTmUserName()).withEmailAddress(getTransManEmailAddress()).withBirthDate(birthDate);
        apiResponse = RestUtils.post(transportManagerBuilder, addTransportManager, getHeaders());
        setTransportManagerApplicationId(apiResponse.extract().jsonPath().getString("id.transportManagerApplicationId"));

        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void submitTransport() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }
        String submitTransportManager = URL.build(env, String.format("transport-manager-application/%s/submit", getApplicationNumber())).toString();
        GenericBuilder genericBuilder = new GenericBuilder().withId(getTransportManagerApplicationId()).withVersion(1);
        apiResponse = RestUtils.put(genericBuilder, submitTransportManager, getHeaders());
        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addTmResponsibilities() {
        if (getOperatorType().equals("public") && (getLicenceType().equals("special_restricted"))) {
            return;
        }
        String tmApplicationNo = getTransportManagerApplicationId();
        String addTMresp = URL.build(env, String.format("transport-manager-application/%s/update-details/", tmApplicationNo)).toString();
        int applicationVersion = Integer.parseInt(fetchTMApplicationInformation(tmApplicationNo, "version", "1"));
        AddressBuilder Address = new AddressBuilder().withAddressLine1(addressLine1).withPostcode(postcode).withTown(town).withCountryCode(countryCode);
        TmRespBuilder tmRespBuilder = new TmRespBuilder().withEmail(emailAddress).withPlaceOfBirth(town).withHomeAddress(Address).withWorkAddress(Address).withTmType(tmType).withIsOwner(isOwner)
                .withHoursMon(hours).withHoursTue(hours).withHoursWed(hours).withHoursThu(hours).withHoursThu(hours).withHoursFri(hours).withHoursSat(hours).withHoursSun(hours).withDob(birthDate)
                .withId(tmApplicationNo).withVersion(applicationVersion);
        apiResponse = RestUtils.put(tmRespBuilder, addTMresp, getHeaders());
        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void submitTmResponsibilities() {
        if (getOperatorType().equals("public") && (getLicenceType().equals("special_restricted"))) {
            return;
        }
        String tmApplicationNo = getTransportManagerApplicationId();
        String submitTmResp = URL.build(env, String.format("transport-manager-application/%s/submit", tmApplicationNo)).toString();
        int applicationVersion = Integer.parseInt(fetchTMApplicationInformation(tmApplicationNo, "version", "1"));

        GenericBuilder genericBuilder = new GenericBuilder().withId(transportManagerApplicationId).withVersion(applicationVersion);
        apiResponse = RestUtils.put(genericBuilder, submitTmResp, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addVehicleDetails() {
        if (getOperatorType().equals("special_restricted")) {
            return;
        }
        String vehiclesResource = null;
        String vrm;

        if (getOperatorType().equals("goods")) {
            vehiclesResource = URL.build(env, String.format("application/%s/goods-vehicles", getApplicationNumber())).toString();
        }
        if (getOperatorType().equals("public")) {
            vehiclesResource = URL.build(env, String.format("application/%s/psv-vehicles", getApplicationNumber())).toString();
        }
        do {
            for (int i = 0; i < getNoOfVehiclesRequired(); i++) {
                vrm = Str.randomWord(2).concat(String.valueOf(GenericUtils.getRandomNumberInts(99, 99)).concat(Str.randomWord(3)))
                        .toLowerCase();
                VehiclesBuilder vehiclesDetails = new VehiclesBuilder().withId(getApplicationNumber()).withApplication(getApplicationNumber()).withHasEnteredReg("Y").withVrm(vrm)
                        .withPlatedWeight(String.valueOf(GenericUtils.getRandomNumberInts(0, 9999))).withVersion(version);
                assert vehiclesResource != null;
                apiResponse = RestUtils.post(vehiclesDetails, vehiclesResource, getHeaders());
                System.out.println("This is the VRM: ".concat(vrm));
            }
        }
        while ((apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT) || (apiResponse.extract().statusCode() == HttpStatus.SC_BAD_REQUEST)
                || (apiResponse.extract().statusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY));

        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void submitVehicleDeclaration() {
        if (licenceType.equals("special_restricted")) {
            return;
        }
        String psvVehicleSize = "psvvs_both";
        String psvNoSmallVhlConfirmation = "Y";
        String psvOperateSmallVhl = "Y";
        String psvSmallVhlNotes = "submitted through the API";
        String psvLimousines = "Y";
        String psvNoLimousineConfirmation = "Y";
        String psvOnlyLimousinesConfirmation = "Y";
        String vehicleDeclarationResource = URL.build(env, String.format(String.format("application/%s/vehicle-declaration", applicationNumber))).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        VehicleDeclarationBuilder vehicleDeclarationBuilder = new VehicleDeclarationBuilder().withId(applicationNumber).withPsvVehicleSize(psvVehicleSize)
                .withPsvLimousines(psvLimousines).withPsvNoSmallVhlConfirmation(psvNoSmallVhlConfirmation).withPsvOperateSmallVhl(psvOperateSmallVhl).withPsvSmallVhlNotes(psvSmallVhlNotes)
                .withPsvNoLimousineConfirmation(psvNoLimousineConfirmation).withPsvOnlyLimousinesConfirmation(psvOnlyLimousinesConfirmation).withVersion(applicationVersion);
        apiResponse = RestUtils.put(vehicleDeclarationBuilder, vehicleDeclarationResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addFinancialHistory() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }
        String financialHistoryAnswer = "N";
        String insolvencyAnswer = "false";
        String financialHistoryResource = URL.build(env, String.format("application/%s/financial-history", applicationNumber)).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        FinancialHistoryBuilder financialHistoryBuilder = new FinancialHistoryBuilder().withId(applicationNumber).withVersion(String.valueOf(applicationVersion)).withBankrupt(financialHistoryAnswer)
            .withLiquidation(financialHistoryAnswer).withReceivership(financialHistoryAnswer).withAdministration(financialHistoryAnswer).withAdministration(financialHistoryAnswer)
            .withDisqualified(financialHistoryAnswer).withInsolvencyDetails(insolvencyAnswer).withInsolvencyConfirmation(insolvencyAnswer);
        apiResponse = RestUtils.put(financialHistoryBuilder, financialHistoryResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addApplicationSafetyAndComplianceDetails() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }
        String tachographIns = "tach_na";
        String safetyInsVaries = "N";
        String safetyConfirmationOption = "Y";
        String applicationSafetyResource = URL.build(env, String.format("application/%s/safety", applicationNumber)).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        LicenceBuilder licence = new LicenceBuilder().withId(licenceNumber).withVersion(version).withSafetyInsVaries(safetyInsVaries).withSafetyInsVehicles(String.valueOf(noOfVehiclesRequired))
            .withSafetyInsTrailers(String.valueOf(noOfVehiclesRequired)).withTachographIns(tachographIns);
        ApplicationSafetyBuilder applicationSafetyBuilder = new ApplicationSafetyBuilder().withId(applicationNumber).withVersion(applicationVersion)
            .withSafetyConfirmation(safetyConfirmationOption).withLicence(licence);
        apiResponse = RestUtils.put(applicationSafetyBuilder, applicationSafetyResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addSafetyInspector() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }
        String safetyInspectorResource = URL.build(env, String.format("application/%s/workshop", applicationNumber)).toString();
        AddressBuilder addressBuilder = new AddressBuilder().withAddressLine1(addressLine1).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
        ContactDetailsBuilder contactDetailsBuilder = new ContactDetailsBuilder().withFao(foreName).withAddress(addressBuilder);
        SafetyInspectorBuilder safetyInspectorBuilder = new SafetyInspectorBuilder().withApplication(applicationNumber).withLicence(licenceNumber).withIsExternal("N")
            .withContactDetails(contactDetailsBuilder);
        apiResponse = RestUtils.post(safetyInspectorBuilder, safetyInspectorResource, getHeaders());
        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addConvictionsDetails() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }
        String previousConvictionsResource = URL.build(env, String.format("application/%s/previous-convictions", applicationNumber)).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        CaseConvictionsPenaltiesBuilder convictionsPenaltiesBuilder = new CaseConvictionsPenaltiesBuilder().withId(applicationNumber).withConvictionsConfirmation("Y")
                .withPrevConviction("N").withVersion(applicationVersion);
        apiResponse = RestUtils.put(convictionsPenaltiesBuilder, previousConvictionsResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addLicenceHistory() {
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            return;
        }
        String optionResponse = "N";
        String licenceHistoryResource = URL.build(env, String.format("application/%s/licence-history", applicationNumber)).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        LicenceHistoryBuilder licenceHistoryBuilder = new LicenceHistoryBuilder().withId(applicationNumber).withPrevHadLicence(optionResponse).withPrevHasLicence(optionResponse)
                .withPrevBeenAtPi(optionResponse).withPrevBeenDisqualifiedTc(optionResponse).withPrevBeenRefused(optionResponse).withPrevBeenRevoked(optionResponse).withPrevPurchasedAssets(optionResponse)
                .withVersion(applicationVersion);
        apiResponse = RestUtils.put(licenceHistoryBuilder, licenceHistoryResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void submitTaxiPhv() {
        String phLicenceNumber = "phv123456";
        String councilName = "nottinghamshire";
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            String submitResource = URL.build(env, String.format("application/%s/taxi-phv", applicationNumber)).toString();
            AddressBuilder addressBuilder = new AddressBuilder().withAddressLine1(addressLine1).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
            PhvTaxiBuilder taxiBuilder = new PhvTaxiBuilder().withId(applicationNumber).withPrivateHireLicenceNo(phLicenceNumber).withCouncilName(councilName).withLicence(licenceNumber).withAddress(addressBuilder);
            apiResponse = RestUtils.post(taxiBuilder, submitResource, getHeaders());
            if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
                LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
                LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
                throw new HTTPException(apiResponse.extract().statusCode());
            }
        }
    }

    public void applicationReviewAndDeclare() {
        String interimReason = "Testing through the API";
        String declarationConfirmation = "Y";
        String signatureRequired = "sig_physical_signature";
        DeclarationsAndUndertakings undertakings = new DeclarationsAndUndertakings();
        String reviewResource = URL.build(env, String.format("application/%s/declaration/", applicationNumber)).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        if (operatorType.equals("goods") && (getIsInterim().equals("Y"))) {
            undertakings.withId(applicationNumber).withVersion(String.valueOf(applicationVersion)).withInterimRequested(getIsInterim())
                    .withInterimReason(interimReason).withSignatureType(signatureRequired).withDeclarationConfirmation(declarationConfirmation);
        } else {
            undertakings.withId(applicationNumber).withVersion(String.valueOf(applicationVersion))
                    .withSignatureType(signatureRequired).withDeclarationConfirmation(declarationConfirmation);
        }
        apiResponse = RestUtils.put(undertakings, reviewResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void submitApplication() {
        String submitResource = URL.build(env, String.format("application/%s/submit", applicationNumber)).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        GenericBuilder genericBuilder = new GenericBuilder().withId(applicationNumber).withVersion(applicationVersion);
        apiResponse = RestUtils.put(genericBuilder, submitResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void getApplicationLicenceDetails() {
        Headers.headers.put("x-pid", adminApiHeader());

        String getApplicationResource = URL.build(env, String.format("application/%s", applicationNumber)).toString();
        apiResponse = RestUtils.get(getApplicationResource, getHeaders());
        setLicenceId(apiResponse.extract().jsonPath().getString("licence.id"));
        setLicenceNumber(apiResponse.extract().jsonPath().getString("licence.licNo"));
        setApplicationStatus(apiResponse.extract().jsonPath().getString("licenceType.status.olbsKey"));
        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }
}