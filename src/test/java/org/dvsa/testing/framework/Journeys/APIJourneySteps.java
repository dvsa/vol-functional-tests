package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.CreateLicenceAPI;
import org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP.GrantLicenceAPI;

public class APIJourneySteps {

    private World world;
    public static int tmCount;

    public APIJourneySteps(World world) throws MissingRequiredArgument {
        this.world = world;
        world.createLicence = new CreateLicenceAPI();
        world.grantLicence = new GrantLicenceAPI(world);
    }

    public void createAdminUser() throws MissingRequiredArgument {
        world.updateLicence.createInternalAdminUser();
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
        world.APIJourneySteps.createApplication();
        world.APIJourneySteps.submitApplication();
        world.grantLicence.grantLicence();
        world.grantLicence.payGrantFees();
        world.updateLicence.getLicenceTrafficArea();
        System.out.println("--Licence-Number: " + world.createLicence.getLicenceNumber() + "--");
    }

    public GrantLicenceAPI grantLicence() throws MissingRequiredArgument {
        return new GrantLicenceAPI(world);
    }

    public void createApplication(){
        if(world.createLicence.getApplicationNumber() == null) {
            world.createLicence.createApplication();
            world.createLicence.updateBusinessType();
            world.createLicence.updateBusinessDetails();
            world.createLicence.addAddressDetails();
            world.createLicence.addPartners();
            world.createLicence.submitTaxiPhv();
            world.createLicence.addOperatingCentre();
            world.createLicence.updateOperatingCentre();
            world.createLicence.addFinancialEvidence();
            world.createLicence.addTransportManager();
            world.createLicence.submitTransport();
            world.createLicence.addTmResponsibilities();
            world.createLicence.submitTmResponsibilities();
            world.createLicence.addVehicleDetails();
            world.createLicence.submitVehicleDeclaration();
            world.createLicence.addFinancialHistory();
            world.createLicence.addApplicationSafetyAndComplianceDetails();
            world.createLicence.addSafetyInspector();
            world.createLicence.addConvictionsDetails();
            world.createLicence.addLicenceHistory();
            world.createLicence.applicationReviewAndDeclare();
        }
    }

    public void createSpecialRestrictedLicence(){
        world.createLicence.createApplication();
        world.createLicence.updateBusinessType();
        world.createLicence.updateBusinessDetails();
        world.createLicence.addAddressDetails();
        world.createLicence.addPartners();
        world.createLicence.submitTaxiPhv();
        world.createLicence.submitApplication();
        world.createLicence.getApplicationLicenceDetails();
    }

    public void submitApplication(){
        world.createLicence.submitApplication();
        world.createLicence.getApplicationLicenceDetails();
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

    public void registerAndGetUserDetails(){
        world.createLicence.registerUser();
        world.createLicence.getUserDetails();
    }

    public void grandLicenceAndPayFees(){
        world.grantLicence.grantLicence();
        world.grantLicence.payGrantFees();
    }


    public static String adminApiHeader(){
        return "e91f1a255e01e20021507465a845e7c24b3a1dc951a277b874c3bcd73dec97a1";
    }
}