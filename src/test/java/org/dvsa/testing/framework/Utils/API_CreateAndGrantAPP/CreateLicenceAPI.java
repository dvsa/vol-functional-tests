package org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP;

import activesupport.MissingRequiredArgument;
import activesupport.faker.FakerUtils;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Utils.API_Builders.*;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.Utils.Generic.PostCode;
import org.dvsa.testing.lib.url.api.URL;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import javax.xml.ws.http.HTTPException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.dvsa.testing.framework.Journeys.APIJourneySteps.adminApiHeader;
import static org.dvsa.testing.framework.Utils.API_Headers.Headers.getHeaders;

public class CreateLicenceAPI extends BaseAPI{

    private static ValidatableResponse apiResponse;
    private static int version = 1;
    private FakerUtils faker = new FakerUtils();

    private String title;
    private String foreName = faker.generateFirstName();
    private String familyName = faker.generateLastName();
    private String birthDate = Int.random(1900, 2018) + "-" + Int.random(1, 12) + "-" + Int.random(1, 28);
    private LinkedHashMap<String, String> registeredAddress = faker.generateAddress();
    private String registeredAddressLine1 = registeredAddress.get("addressLine1");
    private String registeredAddressLine2 = registeredAddress.get("addressLine2");
    private String registeredAddressLine3 = registeredAddress.get("addressLine3");
    private String registeredAddressLine4 = registeredAddress.get("addressLine4");
    private String registeredTown = faker.generateAddress().get("town");
    private LinkedHashMap<String, String> address = faker.generateAddress();
    private String addressLine1 = address.get("addressLine1");
    private String addressLine2 = address.get("addressLine2");
    private String addressLine3 = address.get("addressLine3");
    private String addressLine4 = address.get("addressLine4");
    private String town = faker.generateAddress().get("town");
    private String postcode = PostCode.getRandomRealNottinghamPostcode();
    private String countryCode = "GB";
    private LinkedHashMap<String, String> establishmentAddress = faker.generateAddress();
    private String establishmentAddressLine1 = establishmentAddress.get("addressLine1");
    private String establishmentAddressLine2 = establishmentAddress.get("addressLine2");
    private String establishmentAddressLine3 = establishmentAddress.get("addressLine3");
    private String establishmentAddressLine4 = establishmentAddress.get("addressLine4");
    private String establishmentTown = faker.generateAddress().get("town");
    private LinkedHashMap<String, String> transportConsultantAddress = faker.generateAddress();
    private String transportConsultantAddressLine1 = transportConsultantAddress.get("addressLine1");
    private String transportConsultantAddressLine2 = transportConsultantAddress.get("addressLine2");
    private String transportConsultantAddressLine3 = transportConsultantAddress.get("addressLine3");
    private String transportConsultantAddressLine4 = transportConsultantAddress.get("addressLine4");
    private String transportConsultantTown = faker.generateAddress().get("town");
    private String organisationId;
    private String organisationName = faker.generateCompanyName();
    private String emailAddress = String.format("%s_%s%s.tester@dvsa.com", getForeName(), getFamilyName(), Int.random(10000, 99999));
    private String tmForeName;
    private String tmFamilyName;
    private String tmUserName;
    private String transManEmailAddress;
    private String applicationNumber;
    private String userId;
    private String username;
    private String loginId;
    private String pid;
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
    private LinkedHashMap<String, String> operatingCentreAddress = faker.generateAddress();
    private String operatingCentreAddressLine1 = operatingCentreAddress.get("addressLine1");
    private String operatingCentreAddressLine2 = operatingCentreAddress.get("addressLine2");
    private String operatingCentreAddressLine3 = operatingCentreAddress.get("addressLine3");
    private String operatingCentreAddressLine4 = operatingCentreAddress.get("addressLine4");
    private String operatingCentreTown = operatingCentreAddress.get("town");
    private String applicationStatus;
    private String licenceId;
    private String isInterim;
    private String isOwner;
    private String tmType = "tm_t_i";
    private String hours = "2.0";
    private String phoneNumber;
    private String businessEmailAddress;
    private String psvVehicleSize;

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

    public String getTmForeName() { return tmForeName; }

    public void setTMForeName(String tmForeName) {
        this.tmForeName = tmForeName;
    }

    public String getTmFamilyName() {
        return tmFamilyName;
    }

    public void setTMFamilyName(String tmFamilyName) { this.tmFamilyName = tmFamilyName; }

    public String getTransManEmailAddress() { return transManEmailAddress; }

    public void setTransManEmailAddress(String transManEmailAddress) { this.transManEmailAddress = transManEmailAddress; }

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

    public String getAddressLine2() { return addressLine2; }

    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getAddressLine3() { return addressLine3; }

    public void setAddressLine3(String addressLine3) { this.addressLine3 = addressLine3; }

    public String getAddressLine4() { return addressLine4; }

    public void setAddressLine4(String addressLine4) { this.addressLine4 = addressLine4; }

    public String getTown() { return town; }

    public void setTown(String town) { this.town = town; }

    public String getOperatingCentreAddressLine1() {
        return operatingCentreAddressLine1;
    }

    public void setOperatingCentreAddressLine1(String operatingCentreAddressLine1) { this.operatingCentreAddressLine1 = operatingCentreAddressLine1; }

    public String getOperatingCentreAddressLine2() { return operatingCentreAddressLine2; }

    public void setOperatingCentreAddressLine2(String operatingCentreAddressLine2) { this.operatingCentreAddressLine2 = operatingCentreAddressLine2; }

    public String getOperatingCentreAddressLine3() { return operatingCentreAddressLine3; }

    public void setOperatingCentreAddressLine3(String operatingCentreAddressLine3) { this.operatingCentreAddressLine3 = operatingCentreAddressLine3; }

    public String getOperatingCentreAddressLine4() { return operatingCentreAddressLine4; }

    public void setOperatingCentreAddressLine4(String operatingCentreAddressLine4) { this.operatingCentreAddressLine4 = operatingCentreAddressLine4; }

    public String getOperatingCentreTown() { return operatingCentreTown; }

    public void setOperatingCentreTown(String operatingCentreTown) { this.operatingCentreTown = operatingCentreTown; }

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

    public void setBusinessEmailAddress(String businessEmailAddress) { this.businessEmailAddress = businessEmailAddress; }

    public String getPsvVehicleSize() { return psvVehicleSize; }

    private void setPsvVehicleSize(String psvVehicleSize) { this.psvVehicleSize = psvVehicleSize; }

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
        String registerResource = URL.build(env, "user/selfserve/register").toString();
        Headers.headers.put("api", "dvsa");
        setLoginId(String.format("%s.%s%s", getForeName(), getFamilyName(), Int.random(1000,9999)));

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
        String natureOfBusiness = faker.generateNatureOfBusiness();
        String updateBusinessDetailsResource = URL.build(env, String.format("organisation/business-details/application/%s", getApplicationNumber())).toString();

        AddressBuilder address = new AddressBuilder().withAddressLine1(registeredAddressLine1).withAddressLine2(registeredAddressLine2).withAddressLine3(registeredAddressLine3)
                .withAddressLine4(registeredAddressLine4).withTown(registeredTown).withPostcode(postcode);
        UpdateBusinessDetailsBuilder businessDetails = new UpdateBusinessDetailsBuilder()
                .withId(getApplicationNumber()).withCompanyNumber(companyNumber).withNatureOfBusiness(natureOfBusiness).withLicence(licenceNumber)
                .withVersion(organisationVersion).withName(organisationName).withAddress(address);
        apiResponse = RestUtils.put(businessDetails, updateBusinessDetailsResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            LOGGER.info("ERROR CODE: ".concat(Integer.toString(apiResponse.extract().statusCode())));
            LOGGER.info("RESPONSE MESSAGE: ".concat(apiResponse.extract().response().asString()));
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void addAddressDetails() {
        String phoneNumber = "0712345678";
        String businessEmail = String.format("%s.volBusiness@dvsa.com", organisationName.replace(" ", "_").replace(",", ""));
        String applicationAddressResource = URL.build(env, String.format("application/%s/addresses/", applicationNumber)).toString();
        AddressBuilder correspondenceAddress = new AddressBuilder().withAddressLine1(addressLine1).withAddressLine2(addressLine2).withAddressLine3(addressLine3)
                .withAddressLine4(addressLine4).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
        AddressBuilder establishmentAddress = new AddressBuilder().withAddressLine1(establishmentAddressLine1).withAddressLine2(establishmentAddressLine2).withAddressLine3(establishmentAddressLine3)
                .withAddressLine4(establishmentAddressLine4).withTown(establishmentTown).withPostcode(postcode).withCountryCode(countryCode);
        ContactDetailsBuilder contactDetailsBuilder = new ContactDetailsBuilder().withPhoneNumber(phoneNumber).withEmailAddress(businessEmail);
        AddressBuilder transportConsultantAddress = new AddressBuilder().withAddressLine1(transportConsultantAddressLine1).withAddressLine2(transportConsultantAddressLine2).withAddressLine3(transportConsultantAddressLine3)
                .withAddressLine4(transportConsultantAddressLine4).withTown(transportConsultantTown).withPostcode(postcode).withCountryCode(countryCode);
        String transportConsultantPhone = "07987654321";
        String transportConsultantName = String.format("%s %s", faker.generateFirstName(), faker.generateLastName());
        String transportConsultantEmail = String.format("%s.volTConsultant@dvsa.com", transportConsultantName.replace(" ", "_").replace(",", ""));
        ContactDetailsBuilder transportConsultantContact = new ContactDetailsBuilder().withPhoneNumber(transportConsultantPhone).withEmailAddress(transportConsultantEmail);
        TransportConsultantBuilder transportConsultant = new TransportConsultantBuilder().withConfirmation("Y").withAddress(transportConsultantAddress).withName(transportConsultantName)
                .withWrittenPermissionToEngage("Y").withContact(transportConsultantContact);
        ApplicationAddressBuilder addressBuilder = new ApplicationAddressBuilder().withId(applicationNumber).withConsultant(transportConsultant).withContact(contactDetailsBuilder)
                .withCorrespondenceAddress(correspondenceAddress).withEstablishmentAddress(establishmentAddress);

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
        String permissionOption = "Y";
        OperatingCentreBuilder operatingCentreBuilder = new OperatingCentreBuilder();

        if (operatorType.equals("goods")) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddressLine1).withAddressLine2(operatingCentreAddressLine2).
                    withAddressLine3(operatingCentreAddressLine3).withAddressLine4(operatingCentreAddressLine4).withTown(operatingCentreTown).withPostcode(getPostcode()).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(getApplicationNumber()).withNoOfVehiclesRequired(String.valueOf(getNoOfVehiclesRequired()))
                    .withNoOfTrailersRequired(String.valueOf(getNoOfVehiclesRequired())).withPermission(permissionOption).withAddress(address);
        }
        if (operatorType.equals("public") && (!licenceType.equals("special_restricted"))) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddressLine1).withAddressLine2(operatingCentreAddressLine2).
                    withAddressLine3(operatingCentreAddressLine3).withAddressLine4(operatingCentreAddressLine4).withTown(operatingCentreTown).withPostcode(getPostcode()).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(applicationNumber).withNoOfVehiclesRequired(String.valueOf(noOfVehiclesRequired)).withPermission(permissionOption).withAddress(address);
        }
        if (operatorType.equals("public") && (licenceType.equals("restricted"))) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddressLine1).withAddressLine2(operatingCentreAddressLine2).
                    withAddressLine3(operatingCentreAddressLine3).withAddressLine4(operatingCentreAddressLine4).withTown(operatingCentreTown).withPostcode(getPostcode()).withCountryCode(countryCode);
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
                    .withTrafficArea(getTrafficArea()).withEnforcementArea(getEnforcementArea()).withTotCommunityLicences(1)
                    .withTAuthTrailers(Integer.parseInt(String.valueOf(noOfVehiclesRequired))).withVersion(applicationVersion);
        }
        if (operatorType.equals("public") && (!licenceType.equals("restricted"))) {
            updateOperatingCentre.withId(getApplicationNumber()).withTotAuthVehicles(noOfVehiclesRequired)
                    .withTrafficArea(getTrafficArea()).withEnforcementArea(getEnforcementArea()).withTotCommunityLicences(1).withVersion(applicationVersion);
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
        setTMForeName(faker.generateFirstName());
        setTMFamilyName(faker.generateLastName());
        setTmUserName(String.format("%s.%s%s", getTmForeName(), getTmFamilyName(), Int.random(1000, 9999)));
        setTransManEmailAddress(String.format("%s_%s.TheTransportManager@dvsa.com", getTmForeName(), getTmFamilyName()));
        String hasEmail = "Y";
        String addTransportManager = URL.build(env, "transport-manager/create-new-user/").toString();
        TransportManagerBuilder transportManagerBuilder = new TransportManagerBuilder().withApplication(getApplicationNumber()).withFirstName(getTmForeName())
                .withFamilyName(getTmFamilyName()).withHasEmail(hasEmail).withUsername(getTmUserName()).withEmailAddress(getTransManEmailAddress()).withBirthDate(birthDate);
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
        LinkedHashMap<String, String> TMAddress = faker.generateAddress();
        String TMAddressLine1 = TMAddress.get("addressLine1");
        String TMAddressLine2 = TMAddress.get("addressLine2");
        String TMAddressLine3 = TMAddress.get("addressLine3");
        String TMAddressLine4 = TMAddress.get("addressLine4");
        String TMAddressTown = TMAddress.get("town");
        AddressBuilder Address = new AddressBuilder().withAddressLine1(TMAddressLine1).withAddressLine2(TMAddressLine2).withAddressLine3(TMAddressLine3)
                .withAddressLine4(TMAddressLine4).withPostcode(postcode).withTown(TMAddressTown).withCountryCode(countryCode);
        TmRespBuilder tmRespBuilder = new TmRespBuilder().withEmail(getTransManEmailAddress()).withPlaceOfBirth(TMAddressTown).withHomeAddress(Address).withWorkAddress(Address).withTmType(tmType).withIsOwner(isOwner)
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

        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

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
                        .withPlatedWeight(String.valueOf(GenericUtils.getRandomNumberInts(0, 9999))).withVersion(applicationVersion);
                assert vehiclesResource != null;
                apiResponse = RestUtils.post(vehiclesDetails, vehiclesResource, getHeaders());
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
        setPsvVehicleSize("psvvs_both");
        String psvNoSmallVhlConfirmation = "Y";
        String psvOperateSmallVhl = "Y";
        String psvSmallVhlNotes = "submitted through the API";
        String psvLimousines = "Y";
        String psvNoLimousineConfirmation = "Y";
        String psvOnlyLimousinesConfirmation = "Y";
        String vehicleDeclarationResource = URL.build(env, String.format(String.format("application/%s/vehicle-declaration", applicationNumber))).toString();
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));

        VehicleDeclarationBuilder vehicleDeclarationBuilder = new VehicleDeclarationBuilder().withId(applicationNumber).withPsvVehicleSize(getPsvVehicleSize())
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

        LinkedHashMap<String, String> safetyInspectorAddress = faker.generateAddress();
        String safetyInspectorAddressLine1 = safetyInspectorAddress.get("addressLine1");
        String safetyInspectorAddressLine2 = safetyInspectorAddress.get("addressLine2");
        String safetyInspectorAddressLine3 = safetyInspectorAddress.get("addressLine3");
        String safetyInspectorAddressLine4 = safetyInspectorAddress.get("addressLine4");
        String safetyInspectorAddressTown = safetyInspectorAddress.get("town");

        String safetyInspectorResource = URL.build(env, String.format("application/%s/workshop", applicationNumber)).toString();
        AddressBuilder addressBuilder = new AddressBuilder().withAddressLine1(safetyInspectorAddressLine1).withAddressLine2(safetyInspectorAddressLine2).withAddressLine3(safetyInspectorAddressLine3)
                .withAddressLine4(safetyInspectorAddressLine4).withTown(safetyInspectorAddressTown).withPostcode(postcode).withCountryCode(countryCode);
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

        LinkedHashMap<String, String> taxiPhvAddress = faker.generateAddress();
        String taxiPhvAddressLine1 = taxiPhvAddress.get("addressLine1");
        String taxiPhvAddressLine2 = taxiPhvAddress.get("addressLine2");
        String taxiPhvAddressLine3 = taxiPhvAddress.get("addressLine3");
        String taxiPhvAddressLine4 = taxiPhvAddress.get("addressLine4");
        String taxiPhvAddressTown = taxiPhvAddress.get("town");

        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            String submitResource = URL.build(env, String.format("application/%s/taxi-phv", applicationNumber)).toString();
            AddressBuilder addressBuilder = new AddressBuilder().withAddressLine1(taxiPhvAddressLine1).withAddressLine2(taxiPhvAddressLine2).withAddressLine3(taxiPhvAddressLine3)
                    .withAddressLine4(taxiPhvAddressLine4).withTown(taxiPhvAddressTown).withPostcode(postcode).withCountryCode(countryCode);
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