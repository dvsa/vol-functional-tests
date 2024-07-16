package org.dvsa.testing.framework.Journeys.licence;

import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.SecretsManager;
import activesupport.dates.Dates;
import apiCalls.enums.*;
import org.apache.hc.core5.http.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.joda.time.LocalDate;
import java.util.UUID;

public class APIJourney {

    private final World world;
    public static int tmCount;

    private static final Logger LOGGER = LogManager.getLogger(APIJourney.class);
    Dates date = new Dates(LocalDate::new);

    public APIJourney(World world) throws MissingRequiredArgument {
        this.world = world;
    }

    public void createAdminUser() throws MissingRequiredArgument, HttpException {
        //     String requestId = UUID.randomUUID().toString();
        //   LOGGER.info("RequestID: {}, Creating internal admin user with role: {} and type: {}", requestId, UserRoles.SYSTEM_ADMIN.asString(), UserType.INTERNAL.asString());
        world.updateLicence.createInternalUser(UserRoles.SYSTEM_ADMIN.asString(), UserType.INTERNAL.asString());
        //    LOGGER.info("RequestID: {}, Internal admin user creation completed.", requestId);    }
    }

    public void nIAddressBuilder() {
        world.createApplication.setEnforcementArea(EnforcementArea.NORTHERN_IRELAND);
        world.createApplication.setTrafficArea(TrafficArea.NORTHERN_IRELAND);
        world.createApplication.setCountryCode("NI");
        world.createApplication.setNiFlag("Y");
    }

    public void generateAndGrantPsvApplicationPerTrafficArea(String trafficArea, String enforcementArea) throws HttpException {
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

    public void createApplication() throws HttpException {
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

    public void submitApplication() throws HttpException {
        world.createApplication.submitApplication();
        world.applicationDetails.getApplicationLicenceDetails();
    }

    public void createSpecialRestrictedApplication() throws HttpException {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
        world.createApplication.submitTaxiPhv();
    }

    public void createSpecialRestrictedLicence() throws HttpException {
        world.APIJourney.createSpecialRestrictedApplication();
        world.APIJourney.submitApplication();
    }

    public void createPartialApplication() throws HttpException {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
        world.createApplication.addOperatingCentre();
        world.createApplication.updateOperatingCentre();
        world.createApplication.addFinancialEvidence();
    }

    public void registerAndGetUserDetails(String userType) throws HttpException {
        world.registerUser.registerUser();
        //For cognito we need to do an initial login to get the token back, otherwise the api will return a password challenge
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.userDetails.getUserDetails(userType, world.registerUser.getUserId(), world.registerUser.getUserName(), SecretsManager.getSecretValue("internalNewPassword"));
    }

    public void grantLicenceAndPayFees() throws HttpException {
        world.grantApplication.setDateState(date.getFormattedDate(0, 0, 0, "yyyy-MM-dd"));
        world.grantApplication.grantLicence();
        if (world.licenceCreation.isGoodsLicence()) {
            world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        }
    }
}