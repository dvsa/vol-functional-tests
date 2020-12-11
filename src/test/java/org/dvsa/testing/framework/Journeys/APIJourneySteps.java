package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.dates.DateState;
import apiCalls.Utils.generic.Headers;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.LicenceType;
import apiCalls.enums.OperatorType;
import apiCalls.enums.TrafficArea;
import enums.UserRoles;

import static activesupport.dates.DateState.getDates;

public class APIJourneySteps {

    private World world;
    public static int tmCount;

    public APIJourneySteps(World world) throws MissingRequiredArgument {
        this.world = world;
        Headers.setAPI_HEADER(adminApiHeader());
    }

    public void createAdminUser() throws MissingRequiredArgument {
        world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.getUserRoles(), UserRoles.INTERNAL.getUserRoles());
    }

    public void nIAddressBuilder() {
        world.createLicence.setEnforcementArea("EA-N");
        world.createLicence.setTrafficArea("N");
        world.createLicence.setTown("Belfast");
        world.createLicence.setPostcode("BT28HQ");
        world.createLicence.setCountryCode("NI");
        world.createLicence.setNiFlag("Y");
    }

    public void generateAndGrantPsvApplicationPerTrafficArea(String trafficArea, String enforcementArea) throws Exception {
        world.createLicence.setTrafficArea(trafficArea);
        world.createLicence.setEnforcementArea(enforcementArea);
        world.createLicence.setOperatorType("public");
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        world.grantLicence.grantLicence();
        world.grantLicence.payGrantFees();
        world.updateLicence.getLicenceTrafficArea();
    }

    public void createApplication() {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addPartners();
        world.createApplication.submitTaxiPhv();
        world.createApplication.addOperatingCentre();
        world.createApplication.updateOperatingCentre();
        world.createApplication.addFinancialEvidence();
        world.createApplication.addTransportManager();
        world.createApplication.submitTransport();
        world.createApplication.addTmResponsibilities();
        world.createApplication.submitTmResponsibilities();
        world.createApplication.addVehicleDetails();
        world.createApplication.submitVehicleDeclaration();
        world.createApplication.addFinancialHistory();
        world.createApplication.addApplicationSafetyAndComplianceDetails();
        world.createApplication.addSafetyInspector();
        world.createApplication.addConvictionsDetails();
        world.createApplication.addLicenceHistory();
        world.createApplication.applicationReviewAndDeclare();
    }

    public void createSpecialRestrictedLicence() {
        world.createLicence.createApplication();
        world.createLicence.updateBusinessType();
        world.createLicence.updateBusinessDetails();
        world.createLicence.addAddressDetails();
        world.createLicence.addPartners();
        world.createLicence.submitTaxiPhv();
        world.createLicence.submitApplication();
        world.createLicence.getApplicationLicenceDetails();
    }

    public void submitApplication() {
        world.applicationDetails.setApplicationId(world.createApplication.getApplicationId());
        world.createApplication.submitApplication();
        world.applicationDetails.getApplicationLicenceDetails(world.createApplication);
    }

    public void createPartialApplication() {
        world.createLicence.createApplication();
        world.createLicence.updateBusinessType();
        world.createLicence.updateBusinessDetails();
        world.createLicence.addAddressDetails();
        world.createLicence.addPartners();
        world.createLicence.addOperatingCentre();
        world.createLicence.updateOperatingCentre();
        world.createLicence.addFinancialEvidence();
    }

    public void registerAndGetUserDetails(String userType) {
        world.registerUser.registerUser();
        world.userDetails.getUserDetails(userType, world.registerUser.getUserId(), adminApiHeader());
    }

    public void grantLicenceAndPayFees() {
        world.grantApplication.setDateState(DateState.getDates("current",0));
        world.grantApplication.grantLicence(world.createApplication.getApplicationId(), world.userDetails.getOrganisationId());
        world.grantApplication.payGrantFees(world.createApplication.getApplicationId(), world.userDetails.getOrganisationId());
    }

    public void createLicenceWithTrafficArea(String licenceType, String operator, TrafficArea trafficArea) {
        world.createApplication.setOperatorType(OperatorType.valueOf(operator.toUpperCase()).asString());
        world.createApplication.setLicenceType(LicenceType.valueOf(licenceType.toUpperCase()).asString());

        world.createApplication.setTrafficArea(trafficArea);
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(trafficArea.name()));

        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        world.APIJourneySteps.grantLicenceAndPayFees();
    }

    public static String adminApiHeader() {
        return "e91f1a255e01e20021507465a845e7c24b3a1dc951a277b874c3bcd73dec97a1";
    }
}