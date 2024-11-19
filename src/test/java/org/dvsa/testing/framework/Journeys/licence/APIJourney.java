package org.dvsa.testing.framework.Journeys.licence;

import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.SecretsManager;
import activesupport.dates.Dates;
import apiCalls.enums.*;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.joda.time.LocalDate;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class APIJourney {

    private final World world;
    public static int tmCount;
    Dates date = new Dates(LocalDate::new);

    private static final Lock lock = new ReentrantLock();

    public APIJourney(World world) throws MissingRequiredArgument {
        this.world = world;
    }

    public synchronized void createAdminUser() throws MissingRequiredArgument, HttpException {
        world.updateLicence.createInternalUser(UserRoles.SYSTEM_ADMIN.asString(), UserType.INTERNAL.asString());
    }

    public synchronized void nIAddressBuilder() {
        lock.lock();
        try {
        world.createApplication.setEnforcementArea(EnforcementArea.NORTHERN_IRELAND);
        world.createApplication.setTrafficArea(TrafficArea.NORTHERN_IRELAND);
        world.createApplication.setCountryCode("NI");
        world.createApplication.setNiFlag("Y");
        }  finally {
            {
                lock.unlock();
            }
        }
    }

    public synchronized void generateAndGrantPsvApplicationPerTrafficArea(String trafficArea, String enforcementArea) throws HttpException {
        lock.lock();
        try {
        world.createApplication.setTrafficArea(TrafficArea.valueOf(trafficArea.toUpperCase()));
        world.createApplication.setEnforcementArea(EnforcementArea.valueOf(enforcementArea.toUpperCase()));
        world.createApplication.setOperatorType(OperatorType.PUBLIC.name());
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.APIJourney.submitApplication();
        world.grantApplication.grantLicence();
        world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        world.updateLicence.getLicenceTrafficArea();
        }  finally {
            {
                lock.unlock();
            }
        }
    }

    public synchronized void createApplication() throws HttpException {
        lock.lock();
        try {
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
        }  finally {
            {
                lock.unlock();
            }
        }
}

    public synchronized void submitApplication() throws HttpException {
        lock.lock();
        try {
            world.createApplication.submitApplication();
            world.applicationDetails.getApplicationLicenceDetails();
        } finally {
            lock.unlock();
        }
    }
    public synchronized void createSpecialRestrictedApplication() throws HttpException {
        lock.lock();
        try {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
        world.createApplication.submitTaxiPhv();
        } finally {
            lock.unlock();
        }
    }

    public synchronized void createSpecialRestrictedLicence() throws HttpException {
        world.APIJourney.createSpecialRestrictedApplication();
        world.APIJourney.submitApplication();
    }

    public synchronized void createPartialApplication() throws HttpException {
        lock.lock();
        try {
        world.createApplication.startApplication();
        world.createApplication.addBusinessType();
        world.createApplication.addBusinessDetails();
        world.createApplication.addAddressDetails();
        world.createApplication.addDirectors();
        world.createApplication.addOperatingCentre();
        world.createApplication.updateOperatingCentre();
        world.createApplication.addFinancialEvidence();
    }  finally {
        {
            lock.unlock();
        }
    }
    }

    public synchronized void registerAndGetUserDetails(String userType) throws HttpException {
        lock.lock();
        world.registerUser.registerUser();
        //For cognito we need to do an initial login to get the token back, otherwise the api will return a password challenge
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.userDetails.getUserDetails(userType, world.registerUser.getUserId(), world.registerUser.getUserName(), SecretsManager.getSecretValue("internalNewPassword"));
        lock.unlock();
    }

    public synchronized void registerConsultantAndGetUserDetails(String userType) throws HttpException {
        lock.lock();
        try {
            world.registerConsultantAndOperator.register();
            // For cognito we need to do an initial login to get the token back, otherwise the api will return a password challenge
            world.selfServeNavigation.navigateToLogin(world.registerConsultantAndOperator.getUserName(), world.registerConsultantAndOperator.getEmailAddress());
            world.userDetails.getUserDetails(userType, world.registerConsultantAndOperator.getUserId(), world.registerConsultantAndOperator.getUserName(), SecretsManager.getSecretValue("internalNewPassword"));
        } finally {
            lock.unlock();
        }
    }

    public synchronized void grantLicenceAndPayFees() throws HttpException {
        lock.lock();
        world.grantApplication.setDateState(date.getFormattedDate(0, 0, 0, "yyyy-MM-dd"));
        world.grantApplication.grantLicence();
        if (world.licenceCreation.isGoodsLicence()) {
            world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
        }
        lock.unlock();
    }

}