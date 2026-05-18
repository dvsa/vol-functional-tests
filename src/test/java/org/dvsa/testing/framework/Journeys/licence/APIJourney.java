package org.dvsa.testing.framework.Journeys.licence;

import activesupport.MissingRequiredArgument;
import activesupport.aws.s3.SecretsManager;
import activesupport.dates.Dates;
import activesupport.system.Properties;
import apiCalls.enums.*;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.joda.time.LocalDate;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.apache.commons.codec.DecoderException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class APIJourney {

    private final World world;
    public static int tmCount;
    Dates date = new Dates(LocalDate::new);
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    private final Lock writeLock = rwLock.writeLock();

    public APIJourney(World world) throws MissingRequiredArgument {
        this.world = world;
    }

    public void createAdminUser() throws MissingRequiredArgument, HttpException {
        writeLock.lock();
        try {
            world.updateLicence.createInternalUser(UserRoles.SYSTEM_ADMIN.asString(), UserType.INTERNAL.asString());
        } finally {
            writeLock.unlock();
        }
    }

    public void nIAddressBuilder() {
        writeLock.lock();
        try {
            world.createApplication.setEnforcementArea(EnforcementArea.NORTHERN_IRELAND);
            world.createApplication.setTrafficArea(TrafficArea.NORTHERN_IRELAND);
            world.createApplication.setCountryCode("NI");
            world.createApplication.setNiFlag("Y");
        } finally {
            writeLock.unlock();
        }
    }

    public void generateAndGrantPsvApplicationPerTrafficArea(String trafficArea, String enforcementArea, String userType) throws HttpException {
        writeLock.lock();
        try {
            world.createApplication.setTrafficArea(TrafficArea.valueOf(trafficArea.toUpperCase()));
            world.createApplication.setEnforcementArea(EnforcementArea.valueOf(enforcementArea.toUpperCase()));
            world.createApplication.setOperatorType(OperatorType.PUBLIC.name());
            world.APIJourney.registerAndGetUserDetails(userType);
            world.APIJourney.createApplication();
            world.APIJourney.submitApplication();
            world.grantApplication.grantLicence();
            world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
            world.updateLicence.getLicenceTrafficArea();
        } finally {
            writeLock.unlock();
        }
    }

    public void createApplication() throws HttpException {
        writeLock.lock();
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
            world.createApplication.addFinancialHistory();
            world.createApplication.addApplicationSafetyAndComplianceDetails();
            world.createApplication.addSafetyInspector();
            world.createApplication.addConvictionsDetails();
            world.createApplication.addLicenceHistory();
            world.createApplication.applicationReviewAndDeclare();
        } finally {
            writeLock.unlock();
        }
    }

    public void submitApplication() throws HttpException {
        writeLock.lock();
        try {
            world.createApplication.submitApplication();
            world.applicationDetails.getApplicationLicenceDetails();
        } finally {
            writeLock.unlock();
        }
    }

    public void createSpecialRestrictedApplication() throws HttpException {
        writeLock.lock();
        try {
            world.createApplication.startApplication();
            world.createApplication.addBusinessType();
            world.createApplication.addBusinessDetails();
            world.createApplication.addAddressDetails();
            world.createApplication.addDirectors();
            world.createApplication.submitTaxiPhv();
        } finally {
            writeLock.unlock();
        }
    }

    public void createSpecialRestrictedLicence() throws HttpException {
        writeLock.lock();
        try {
            world.APIJourney.createSpecialRestrictedApplication();
            world.APIJourney.submitApplication();
        } finally {
            writeLock.unlock();
        }
    }

    public void createPartialApplication() throws HttpException {
        writeLock.lock();
        try {
            world.createApplication.startApplication();
            world.createApplication.addBusinessType();
            world.createApplication.addBusinessDetails();
            world.createApplication.addAddressDetails();
            world.createApplication.addDirectors();
            world.createApplication.addOperatingCentre();
            world.createApplication.updateOperatingCentre();
            world.createApplication.addFinancialEvidence();
        } finally {
            writeLock.unlock();
        }
    }


    public void registerAndGetUserDetails(String userType) throws HttpException {
        writeLock.lock();
        try {
            if (userType.equalsIgnoreCase("consultant")) {
                world.registerConsultantAndOperator.register();
                world.selfServeNavigation.navigateToLogin(
                        world.registerConsultantAndOperator.getConsultantDetails().getUserName(),
                        world.registerConsultantAndOperator.getConsultantDetails().getEmailAddress()
                );
                world.userDetails.getUserDetails(UserType.EXTERNAL.asString(),
                        world.registerConsultantAndOperator.getConsultantDetails().getUserId(),
                        world.registerConsultantAndOperator.getConsultantDetails().getUserName(),
                        SecretsManager.getSecretValue("internalNewPassword")
                );
            } else {
                world.registerUser.registerUser();
                world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
                String password;
                if (env.toString().equalsIgnoreCase("local")) {
                    password = world.globalMethods.getDecodedTempPassword(world.registerUser.getEmailAddress());
                } else {
                    password = SecretsManager.getSecretValue("internalNewPassword");
                }

                world.userDetails.getUserDetails(UserType.EXTERNAL.asString(),
                        world.registerUser.getUserId(),
                        world.registerUser.getUserName(),
                        password);
            }
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    public void grantLicenceAndPayFees() throws HttpException {
        writeLock.lock();
        try {
            world.grantApplication.setDateState(date.getFormattedDate(0, 0, 0, "yyyy-MM-dd"));
            world.grantApplication.grantLicence();
            if (world.licenceCreation.isGoodsLicence()) {
                world.grantApplication.payGrantFees(world.createApplication.getNiFlag());
            }
        } finally {
            writeLock.unlock();
        }
    }
}