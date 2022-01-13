package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.dates.Dates;
import apiCalls.enums.*;
import org.joda.time.LocalDate;

public class APIJourney {

    private World world;
    public static int tmCount;
    Dates date = new Dates(LocalDate::new);

    public APIJourney(World world) throws MissingRequiredArgument {
        this.world = world;
    }

    public void createAdminUser() throws MissingRequiredArgument {
        world.updateLicence.createInternalUser(UserRoles.SYSTEM_ADMIN.asString(), UserType.INTERNAL.asString());
    }

    public void nIAddressBuilder() {
        world.createApplication.setEnforcementArea(EnforcementArea.NORTHERN_IRELAND);
        world.createApplication.setTrafficArea(TrafficArea.NORTHERN_IRELAND);
        world.createApplication.setCountryCode("NI");
        world.createApplication.setNiFlag("Y");
    }

    public void generateAndGrantPsvApplicationPerTrafficArea(String trafficArea, String enforcementArea) {
        world.createApplication.setTrafficArea(TrafficArea.valueOf(trafficArea.toUpperCase()));
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(enforcementArea.toUpperCase()));
        world.createApplication.setOperatorType(OperatorType.PUBLIC.name());
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.APIJourney.submitApplication();
        world.grantApplication.grantLicence();
        world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        world.updateLicence.getLicenceTrafficArea();
    }

    public void createApplication() {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
        world.createApplication.submitTaxiPhv();
        if (!world.licenceCreation.isLGVOnlyLicence())
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
        world.APIJourney.createSpecialRestrictedApplication();
        world.APIJourney.submitApplication();
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
        if (world.licenceCreation.isGoodsLicence()) {
            world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        }
    }
}