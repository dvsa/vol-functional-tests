package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.dates.Dates;
import apiCalls.enums.EnforcementArea;
import apiCalls.enums.LicenceType;
import apiCalls.enums.OperatorType;
import apiCalls.enums.TrafficArea;
import apiCalls.enums.UserType;
import enums.UserRoles;
import org.joda.time.LocalDate;

public class APIJourneySteps {

    private World world;
    public static int tmCount;
    Dates date = new Dates(LocalDate::new);

    public APIJourneySteps(World world) throws MissingRequiredArgument {
        this.world = world;
    }

    public void createAdminUser() throws MissingRequiredArgument {
        world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.getUserRoles(), UserRoles.INTERNAL.getUserRoles());
    }

    public void nIAddressBuilder() {
        world.createApplication.setEnforcementArea(EnforcementArea.NORTHERN_IRELAND);
        world.createApplication.setTrafficArea(TrafficArea.NORTHERN_IRELAND);
        world.createApplication.setCountryCode("NI");
        world.createApplication.setNiFlag("Y");
    }

    public void generateAndGrantPsvApplicationPerTrafficArea(String trafficArea, String enforcementArea) {
        TrafficArea TA = TrafficArea.valueOf(trafficArea.toUpperCase());
        EnforcementArea EA = EnforcementArea.valueOf(enforcementArea.toUpperCase());
        world.createApplication.setTrafficArea(TA);
        world.createApplication.setEnforcementArea(EA);
        world.createApplication.setOperatorType("public");
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        world.grantApplication.grantLicence();
        world.grantApplication.payGrantFees();
        world.updateLicence.getLicenceTrafficArea();
    }

    public void createApplication() {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
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

    public void submitApplication() {
        world.createApplication.submitApplication();
        world.applicationDetails.getApplicationLicenceDetails(world.createApplication);
    }

    public void createSpecialRestrictedApplication() {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
        world.createApplication.submitTaxiPhv();
    }

    public void createSpecialRestrictedLicence() {
        world.APIJourneySteps.createSpecialRestrictedApplication();
        world.APIJourneySteps.submitApplication();
    }

    public void createPartialApplication() {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
        world.createApplication.addOperatingCentre();
        world.createApplication.updateOperatingCentre();
        world.createApplication.addFinancialEvidence();
    }

    public void registerAndGetUserDetails(String userType) {
        world.registerUser.registerUser();
        world.userDetails.getUserDetails(userType, world.registerUser.getUserId());
    }

    public void grantLicenceAndPayFees() {
        world.grantApplication.setDateState(date.getFormattedDate(0, 0, 0, "yyyy-MM-dd"));
        world.grantApplication.grantLicence();
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            world.grantApplication.payGrantFees();
        }
    }
}