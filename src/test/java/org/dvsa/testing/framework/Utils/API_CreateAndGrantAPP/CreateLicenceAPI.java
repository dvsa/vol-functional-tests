package org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP;

import activesupport.MissingRequiredArgument;
import activesupport.http.RestUtils;
import activesupport.system.Properties;
import enums.BusinessType;
import enums.LicenceType;
import enums.OperatorType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.framework.Utils.API_Builders.*;
import activesupport.string.Str;
import activesupport.number.Int;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.api.URL;

import java.net.MalformedURLException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dvsa.testing.framework.Utils.API_Headers.Headers.getHeaders;

public class CreateLicenceAPI {

    private static ValidatableResponse apiResponse;

    private static String businessVersion = "1";
    private static String adminUserHeader = "e91f1a255e01e20021507465a845e7c24b3a1dc951a277b874c3bcd73dec97a1";

    private String title;
    private String foreName;
    private String familyName;
    private String birthDate = String.valueOf(Int.random(1900, 2018) + "-" + Int.random(1, 12) + "-" + Int.random(1, 28));
    private String addressLine1 = "API House";
    private String town = "Nottingham";
    private String postcode = "NG23HX";
    private String countryCode = "GB";
    private String organisationName = Str.randomWord(10);
    private String emailAddress = Str.randomWord(6).concat(".tester@dvsa.com");
    private String transManEmailAddress = Str.randomWord(6).concat(".TM@dvsa.com");
    private String applicationNumber;
    private String userId;
    private String username = "apiTM";
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
    private String licenceStatus;
    private String licenceId;
    public String businessName = "API";

    private static int version = 1;
    private int noOfVehiclesRequired = 5;

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    public CreateLicenceAPI() throws MissingRequiredArgument {
        if (licenceType == null){
            operatorType = "goods";
            licenceType = "standard_national";
            businessType = "limited_company";
            niFlag = "N";
            trafficArea = "D";
            enforcementArea = "EA-D";
        }
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
    public String getLicenceNumber() { return licenceNumber; }
    public void setNoOfVehiclesRequired(int noOfVehiclesRequired) {
        this.noOfVehiclesRequired = noOfVehiclesRequired;
    }
    public int getNoOfVehiclesRequired() { return noOfVehiclesRequired; }
    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
    public String getApplicationNumber() {
        return applicationNumber;
    }
    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }
    public String getOrganisationId() { return organisationId; }
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
    public String getPid() {
        return pid;
    }
    private void setPid(String pid) {
        this.pid = pid;
    }
    public String getLicenceId() { return licenceId; }
    public void setLicenceId(String licenceId) { this.licenceId = licenceId; }
    public String getLicenceType() {
        return licenceType;
    }
    public void setLicenceType(String licenceType) {
        this.licenceType = licenceType;
    }
    public String getTransportManagerApplicationId() {
        return transportManagerApplicationId;
    }
    private void setTransportManagerApplicationId(String transportManagerApplicationId) { this.transportManagerApplicationId = transportManagerApplicationId; }
    public void setTrafficArea(String trafficArea) {
        this.trafficArea = trafficArea;
    }
    public String getTrafficArea() { return trafficArea; }
    public String getEnforcementArea() {
        return enforcementArea;
    }
    public void setEnforcementArea(String enforcementArea) {
        this.enforcementArea = enforcementArea;
    }
    public static String getAdminUserHeader() {
        return adminUserHeader;
    }
    public String getOperatorType() {
        return operatorType;
    }
    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }
    public static void setAdminUserHeader(String adminUserHeader) { CreateLicenceAPI.adminUserHeader = adminUserHeader; }
    public String getLicenceStatus() {
        return licenceStatus;
    }
    public void setLicenceStatus(String licenceStatus) {
        this.licenceStatus = licenceStatus;
    }
    public String getRestrictedVehicles() { return restrictedVehicles; }
    public void setRestrictedVehicles(String restrictedVehicles) { this.restrictedVehicles = restrictedVehicles; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getNiFlag() { return niFlag; }
    public void setNiFlag(String niFlag) { this.niFlag = niFlag; }
    public String getOrganisationName() { return organisationName; }
    public void setOrganisationName(String organisationName) { this.organisationName = organisationName; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }


    public void createAndSubmitApp() throws Exception {
        registerUser();
        getUserDetails();
        createApplication();
        updateBusinessType();
        updateBusinessDetails();
        addAddressDetails();
        addPartners();
        submitTaxiPhv();
        addOperatingCentre();
        updateOperatingCentre();
        addFinancialEvidence();
        addTransportManager();
        submitTransport();
        vehicles();
        submitVehicleDeclaration();
        addFinancialHistory();
        addApplicationSafetyAndComplianceDetails();
        addSafetyInspector();
        addConvictionsDetails();
        addLicenceHistory();
        applicationReviewAndDeclare();
        submitApplication();
        getApplicationLicenceDetails();
    }

    public void registerUser(){
        setTitle("title_mr");
        setForeName("Vol-API-".concat(Str.randomWord(3).toLowerCase()));
        setFamilyName("Ann");
        String registerResource = URL.build(env, "user/selfserve/register").toString();
        Headers.headers.put("api", "dvsa");
        setLoginId(Str.randomWord(8));

        PersonBuilder personBuilder = new PersonBuilder().withTitle(getTitle()).withForename(getForeName()).withFamilyName(getFamilyName()).withBirthDate(birthDate);
        ContactDetailsBuilder contactDetailsBuilder = new ContactDetailsBuilder().withEmailAddress(emailAddress).withPerson(personBuilder);
        SelfServeUserRegistrationDetailsBuilder selfServeUserRegistrationDetailsBuilder = new SelfServeUserRegistrationDetailsBuilder().withLoginId(getLoginId()).withContactDetails(contactDetailsBuilder)
                .withOrganisationName(organisationName).withBusinessType(String.valueOf(BusinessType.getEnum(businessType)));
        apiResponse = RestUtils.post(selfServeUserRegistrationDetailsBuilder, registerResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
        userId = apiResponse.extract().jsonPath().getString("id.user");
        if(apiResponse.extract().statusCode() != HttpStatus.SC_CREATED){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void getUserDetails(){
        Headers.headers.put("x-pid", adminUserHeader);

        String userDetailsResource = URL.build(env, String.format("user/selfserve/%s", userId)).toString();
        apiResponse = RestUtils.get(userDetailsResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        setPid(apiResponse.extract().jsonPath().getString("pid"));
        organisationId = apiResponse.extract().jsonPath().prettyPeek().getString("organisationUsers.organisation.id");
        setOrganisationId(organisationId);
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void createApplication(){
        String createApplicationResource = URL.build(env, "application").toString();
        Headers.headers.put("x-pid", pid);
        HashMap<String, String> headers = getHeaders();
        ApplicationBuilder applicationBuilder = new ApplicationBuilder().withOperatorType(String.valueOf(OperatorType.getEnum(operatorType)))
                .withLicenceType(String.valueOf(LicenceType.getEnum(licenceType))).withNiFlag(niFlag).withOrganisation(organisationId);
        apiResponse = RestUtils.post(applicationBuilder, createApplicationResource, headers);
        assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
        applicationNumber = apiResponse.extract().jsonPath().getString("id.application");
        licenceNumber = apiResponse.extract().jsonPath().getString("id.licence");
        setApplicationNumber(applicationNumber);
        if(apiResponse.extract().statusCode() != HttpStatus.SC_CREATED){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void updateBusinessType(){
        String updateBusinessTypeResource = URL.build(env, String.format("organisation/%s/business-type/", organisationId)).toString();
        do {
            BusinessTypeBuilder businessTypeBuilder = new BusinessTypeBuilder().withBusinessType(String.valueOf(BusinessType.getEnum(businessType))).withVersion(businessVersion)
                    .withId(organisationId).withApplication(applicationNumber);
            apiResponse = RestUtils.put(businessTypeBuilder, updateBusinessTypeResource, getHeaders());
            version++;
            if (version > 20) {
                version = 1;
            }
        } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void updateBusinessDetails(){
        String natureOfBusiness = "apiTesting";
        String updateBusinessDetailsResource = URL.build(env, String.format("organisation/business-details/application/%s", licenceNumber)).toString();

        do {
            AddressBuilder address = new AddressBuilder().withAddressLine1(addressLine1).withTown(town).withPostcode(postcode);
            UpdateBusinessDetailsBuilder businessDetails = new UpdateBusinessDetailsBuilder()
                    .withId(applicationNumber).withCompanyNumber(companyNumber).withNatureOfBusiness(natureOfBusiness).withLicence(licenceNumber)
                    .withVersion(businessVersion).withName(businessName).withAddress(address);
            apiResponse = RestUtils.put(businessDetails, updateBusinessDetailsResource, getHeaders());
            version++;
            if (version > 20) {
                version = 1;
            }
        } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addAddressDetails(){
        String phoneNumber = "0712345678";
        String establishmentAddress = "establishment";
        String applicationAddressResource = URL.build(env, String.format("application/%s/addresses/", applicationNumber)).toString();
        AddressBuilder address = new AddressBuilder().withAddressLine1(establishmentAddress).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
        ContactDetailsBuilder contactDetailsBuilder = new ContactDetailsBuilder().withPhoneNumber(phoneNumber).withEmailAddress(emailAddress);
        ApplicationAddressBuilder addressBuilder = new ApplicationAddressBuilder().withId(applicationNumber).withConsultant("Consult").withContact(contactDetailsBuilder)
                .withCorrespondenceAddress(address).withEstablishmentAddress(address);
        apiResponse = RestUtils.put(addressBuilder, applicationAddressResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addPartners(){
        String addPersonResource = URL.build(env, String.format("application/%s/people/", applicationNumber)).toString();
        PersonBuilder addPerson = new PersonBuilder().withId(applicationNumber).withTitle(getTitle()).withForename(getForeName()).withFamilyName(getFamilyName()).withBirthDate(birthDate);
        apiResponse = RestUtils.post(addPerson, addPersonResource, getHeaders());
        assertThat(apiResponse.extract().statusCode() == HttpStatus.SC_CREATED);
        if(apiResponse.extract().statusCode() != HttpStatus.SC_CREATED){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addOperatingCentre(){
        String operatingCentreResource = URL.build(env, String.format("application/%s/operating-centre/", applicationNumber)).toString();
        String permissionOption = "Y";
        String operatingCentreAddress = "API_Operating_Centre";
        OperatingCentreBuilder operatingCentreBuilder = new OperatingCentreBuilder();

        if (operatorType.equals("goods") && (!licenceType.equals("special_restricted"))|| (getNiFlag().equals("Y"))) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddress).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(applicationNumber).withNoOfVehiclesRequired(String.valueOf(noOfVehiclesRequired))
                    .withNoOfTrailersRequired(String.valueOf(noOfVehiclesRequired)).withPermission(permissionOption).withAddress(address);
        }
        if (operatorType.equals("public") && (!licenceType.equals("special_restricted"))) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddress).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(applicationNumber).withNoOfVehiclesRequired(String.valueOf(noOfVehiclesRequired)).withPermission(permissionOption).withAddress(address);
        }
        if (operatorType.equals("public") && (licenceType.equals("restricted"))) {
            AddressBuilder address = new AddressBuilder().withAddressLine1(operatingCentreAddress).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
            operatingCentreBuilder.withApplication(applicationNumber).withNoOfVehiclesRequired(restrictedVehicles).withPermission(permissionOption).withAddress(address);
        }
        if (!licenceType.equals("special_restricted")) {
            apiResponse = RestUtils.post(operatingCentreBuilder, operatingCentreResource, getHeaders());
            assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_CREATED){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void updateOperatingCentre(){
        String updateOperatingCentreResource = URL.build(env, String.format("application/%s/operating-centres", applicationNumber)).toString();
        OperatingCentreUpdater updateOperatingCentre = new OperatingCentreUpdater();

        do {
            if (operatorType.equals("goods") && (!licenceType.equals("special_restricted"))|| (getNiFlag().equals("Y"))) {
                updateOperatingCentre.withId(applicationNumber).withTotAuthVehicles(noOfVehiclesRequired)
                        .withTrafficArea(trafficArea).withEnforcementArea(enforcementArea).withTAuthTrailers(Integer.parseInt(String.valueOf(noOfVehiclesRequired))).withVersion(version);
            }
            if (operatorType.equals("public") && (!licenceType.equals("special_restricted"))) {
                updateOperatingCentre.withId(applicationNumber).withTotAuthVehicles(noOfVehiclesRequired)
                        .withTrafficArea(trafficArea).withEnforcementArea(enforcementArea).withTotCommunityLicences(noOfVehiclesRequired).withVersion(version);
            }
            if (operatorType.equals("public") && (licenceType.equals("restricted"))) {
                updateOperatingCentre.withId(applicationNumber).withTotAuthVehicles(Integer.parseInt(restrictedVehicles))
                        .withTrafficArea(trafficArea).withEnforcementArea(enforcementArea).withTotCommunityLicences(Integer.parseInt(restrictedVehicles)).withVersion(version);
            }
            if  (!licenceType.equals("special_restricted")) {
                apiResponse = RestUtils.put(updateOperatingCentre, updateOperatingCentreResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            }
        }
        while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
        if(apiResponse.extract().statusCode() == HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addFinancialEvidence(){
        String financialEvidenceResource = URL.build(env, String.format("application/%s/financial-evidence", applicationNumber)).toString();
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit financial details
        } else {
            do {
                FinancialEvidenceBuilder financialEvidenceBuilder = new FinancialEvidenceBuilder().withId(applicationNumber).withVersion(version).withFinancialEvidenceUploaded(0);
                apiResponse = RestUtils.put(financialEvidenceBuilder, financialEvidenceResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addTransportManager(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String hasEmail = "Y";
            String addTransportManager = URL.build(env, "transport-manager/create-new-user/").toString();
            TransportManagerBuilder transportManagerBuilder = new TransportManagerBuilder().withApplication(applicationNumber).withFirstName(foreName)
                    .withFamilyName(familyName).withHasEmail(hasEmail).withUsername(username.concat(getLoginId())).withEmailAddress(transManEmailAddress).withBirthDate(birthDate);
            apiResponse = RestUtils.post(transportManagerBuilder, addTransportManager, getHeaders());
            assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
            setTransportManagerApplicationId(apiResponse.extract().jsonPath().getString("id.transportManagerApplicationId"));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_CREATED){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void submitTransport(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String submitTransportManager = URL.build(env, String.format("transport-manager-application/%s/submit", applicationNumber)).toString();
            GenericBuilder genericBuilder = new GenericBuilder().withId(transportManagerApplicationId).withVersion(1);
            apiResponse = RestUtils.put(genericBuilder, submitTransportManager, getHeaders());
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void vehicles(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String hasEnteredReg = "N";
            String vehiclesResource = null;

            if (operatorType.equals("goods")) {
                vehiclesResource = URL.build(env, String.format("application/%s/vehicles", applicationNumber)).toString();
            }
            if (operatorType.equals("public")) {
                vehiclesResource = URL.build(env, String.format("application/%s/psv-vehicles", applicationNumber)).toString();
            }

            do {
                VehiclesBuilder vehiclesBuilder = new VehiclesBuilder().withId(applicationNumber).withHasEnteredReg(hasEnteredReg).withVersion(version);
                apiResponse = RestUtils.put(vehiclesBuilder, vehiclesResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void submitVehicleDeclaration(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String psvVehicleSize = "psvvs_both";
            String psvNoSmallVhlConfirmation = "Y";
            String psvOperateSmallVhl = "Y";
            String psvSmallVhlNotes = "submitted through the API";
            String psvLimousines = "Y";
            String psvNoLimousineConfirmation = "Y";
            String psvOnlyLimousinesConfirmation = "Y";
            String vehicleDeclarationResource = URL.build(env, String.format(String.format("application/%s/vehicle-declaration", applicationNumber))).toString();
            do {
                VehicleDeclarationBuilder vehicleDeclarationBuilder = new VehicleDeclarationBuilder().withId(applicationNumber).withPsvVehicleSize(psvVehicleSize)
                        .withPsvLimousines(psvLimousines).withPsvNoSmallVhlConfirmation(psvNoSmallVhlConfirmation).withPsvOperateSmallVhl(psvOperateSmallVhl).withPsvSmallVhlNotes(psvSmallVhlNotes)
                        .withPsvNoLimousineConfirmation(psvNoLimousineConfirmation).withPsvOnlyLimousinesConfirmation(psvOnlyLimousinesConfirmation).withVersion(version);
                apiResponse = RestUtils.put(vehicleDeclarationBuilder, vehicleDeclarationResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addFinancialHistory(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String financialHistoryAnswer = "N";
            String insolvencyAnswer = "false";
            String financialHistoryResource = URL.build(env, String.format("application/%s/financial-history", applicationNumber)).toString();

            do {
                FinancialHistoryBuilder financialHistoryBuilder = new FinancialHistoryBuilder().withId(applicationNumber).withVersion(String.valueOf(version)).withBankrupt(financialHistoryAnswer)
                        .withLiquidation(financialHistoryAnswer).withReceivership(financialHistoryAnswer).withAdministration(financialHistoryAnswer).withAdministration(financialHistoryAnswer)
                        .withDisqualified(financialHistoryAnswer).withInsolvencyDetails(insolvencyAnswer).withInsolvencyConfirmation(insolvencyAnswer);
                apiResponse = RestUtils.put(financialHistoryBuilder, financialHistoryResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addApplicationSafetyAndComplianceDetails(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String tachographIns = "tach_na";
            String safetyInsVaries = "N";
            String safetyConfirmationOption = "Y";
            String applicationSafetyResource = URL.build(env, String.format("application/%s/safety", applicationNumber)).toString();

            do {
                LicenceBuilder licence = new LicenceBuilder().withId(licenceNumber).withVersion(version).withSafetyInsVaries(safetyInsVaries).withSafetyInsVehicles(String.valueOf(noOfVehiclesRequired))
                        .withSafetyInsTrailers(String.valueOf(noOfVehiclesRequired)).withTachographIns(tachographIns);
                ApplicationSafetyBuilder applicationSafetyBuilder = new ApplicationSafetyBuilder().withId(applicationNumber).withVersion(version)
                        .withSafetyConfirmation(safetyConfirmationOption).withLicence(licence);
                apiResponse = RestUtils.put(applicationSafetyBuilder, applicationSafetyResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addSafetyInspector(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String safetyInspectorResource = URL.build(env, String.format("application/%s/workshop", applicationNumber)).toString();
            AddressBuilder addressBuilder = new AddressBuilder().withAddressLine1(addressLine1).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
            ContactDetailsBuilder contactDetailsBuilder = new ContactDetailsBuilder().withFao(foreName).withAddress(addressBuilder);
            SafetyInspectorBuilder safetyInspectorBuilder = new SafetyInspectorBuilder().withApplication(applicationNumber).withLicence(licenceNumber).withIsExternal("N")
                    .withContactDetails(contactDetailsBuilder);
            apiResponse = RestUtils.post(safetyInspectorBuilder, safetyInspectorResource, getHeaders());
            assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_CREATED){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addConvictionsDetails(){
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String previousConvictionsResource = URL.build(env, String.format("application/%s/previous-convictions", applicationNumber)).toString();

            do {
                CaseConvictionsPenaltiesBuilder convictionsPenaltiesBuilder = new CaseConvictionsPenaltiesBuilder().withId(applicationNumber).withConvictionsConfirmation("Y")
                        .withPrevConviction("N").withVersion(version);
                apiResponse = RestUtils.put(convictionsPenaltiesBuilder, previousConvictionsResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void addLicenceHistory(){

        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            // no need to submit details
        } else {
            String optionResponse = "N";
            String licenceHistoryResource = URL.build(env, String.format("application/%s/licence-history", applicationNumber)).toString();

            do {
                LicenceHistoryBuilder licenceHistoryBuilder = new LicenceHistoryBuilder().withId(applicationNumber).withPrevHadLicence(optionResponse).withPrevHasLicence(optionResponse)
                        .withPrevBeenAtPi(optionResponse).withPrevBeenDisqualifiedTc(optionResponse).withPrevBeenRefused(optionResponse).withPrevBeenRevoked(optionResponse).withPrevPurchasedAssets(optionResponse)
                        .withVersion(version);
                apiResponse = RestUtils.put(licenceHistoryBuilder, licenceHistoryResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
            assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        }
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void applicationReviewAndDeclare(){
        String interimReason = "Testing through the API";
        String isInterim = "Y";
        String declarationConfirmation = "Y";
        String signatureRequired = "sig_physical_signature";
        DeclarationsAndUndertakings undertakings = new DeclarationsAndUndertakings();
        String reviewResource = URL.build(env, String.format("application/%s/declaration/", applicationNumber)).toString();

        do {
            if (operatorType.equals("goods")) {
                undertakings.withId(applicationNumber).withVersion(String.valueOf(version)).withInterimRequested(isInterim)
                        .withInterimReason(interimReason).withSignatureType(signatureRequired).withDeclarationConfirmation(declarationConfirmation);
            } else {
                undertakings.withId(applicationNumber).withVersion(String.valueOf(version))
                        .withSignatureType(signatureRequired).withDeclarationConfirmation(declarationConfirmation);
            }
            apiResponse = RestUtils.put(undertakings, reviewResource, getHeaders());
            version++;
            if (version > 20) {
                version = 1;
            }
        } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void submitApplication(){
        String submitResource = URL.build(env, String.format("application/%s/submit", applicationNumber)).toString();

        do {
            GenericBuilder genericBuilder = new GenericBuilder().withId(applicationNumber).withVersion(version);
            apiResponse = RestUtils.put(genericBuilder, submitResource, getHeaders());
            version++;
            if (version > 20) {
                version = 1;
            }
        } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void getApplicationLicenceDetails(){
        Headers.headers.put("x-pid", adminUserHeader);

        String getApplicationResource = URL.build(env, String.format("application/%s", applicationNumber)).toString();
        apiResponse = RestUtils.get(getApplicationResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        setLicenceId(apiResponse.extract().jsonPath().getString("licence.id"));
        setLicenceNumber(apiResponse.extract().jsonPath().getString("licence.licNo"));
        setLicenceStatus(apiResponse.extract().jsonPath().getString("licenceType.status.olbsKey"));
        if(apiResponse.extract().statusCode() != HttpStatus.SC_OK){
            System.out.println(apiResponse.extract().response().asString());
        }
    }

    public void submitTaxiPhv(){
        String phLicenceNumber = "phv123456";
        String councilName = "nottinghamshire";
        if (operatorType.equals("public") && (licenceType.equals("special_restricted"))) {
            String submitResource = URL.build(env, String.format("application/%s/taxi-phv", applicationNumber)).toString();
            do {
                AddressBuilder addressBuilder = new AddressBuilder().withAddressLine1(addressLine1).withTown(town).withPostcode(postcode).withCountryCode(countryCode);
                PhvTaxiBuilder taxiBuilder = new PhvTaxiBuilder().withId(applicationNumber).withPrivateHireLicenceNo(phLicenceNumber).withCouncilName(councilName).withLicence(licenceNumber).withAddress(addressBuilder);
                apiResponse = RestUtils.post(taxiBuilder, submitResource, getHeaders());
                version++;
                if (version > 20) {
                    version = 1;
                }
            } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
        } else {
            // do nothing"
        }
    }
}