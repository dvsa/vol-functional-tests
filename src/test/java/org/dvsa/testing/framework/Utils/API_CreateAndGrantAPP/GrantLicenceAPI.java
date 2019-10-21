package org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP;

import Injectors.World;
import activesupport.MissingRequiredArgument;
import activesupport.http.RestUtils;
import activesupport.system.Properties;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;
import org.dvsa.testing.framework.Utils.API_Builders.*;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.url.api.URL;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import javax.xml.ws.http.HTTPException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.dvsa.testing.framework.Utils.API_Headers.Headers.getHeaders;

public class GrantLicenceAPI extends BaseAPI{

    private ValidatableResponse apiResponse;
    private List outstandingFeesIds;
    private int feeId;
    private World world;

    private List<Double> feesToPay = new ArrayList<>();

    public GrantLicenceAPI(World world) throws MissingRequiredArgument {
        this.world = world;
    }


    public void createOverview(String applicationNumber) {
        String overviewResource = URL.build(env, String.format("application/%s/overview/", applicationNumber)).toString();
        Headers.headers.put("x-pid", world.APIJourneySteps.adminApiHeader());
        String status = "1";
        String overrideOption = "Y";
        String transportArea = "D";
        String trackingId = fetchApplicationInformation(applicationNumber, "applicationTracking.id", null);
        int applicationVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "version", "1"));
        int applicationTrackingVersion = Integer.parseInt(fetchApplicationInformation(applicationNumber, "applicationTracking.version", "1"));

        TrackingBuilder tracking = new TrackingBuilder().withId(trackingId).withVersion(applicationTrackingVersion).withAddressesStatus(status).withBusinessDetailsStatus(status).withBusinessTypeStatus(status)
                .withCommunityLicencesStatus(status).withConditionsUndertakingsStatus(status).withConvictionsPenaltiesStatus(status).withFinancialEvidenceStatus(status)
                .withFinancialHistoryStatus(status).withLicenceHistoryStatus(status).withOperatingCentresStatus(status).withPeopleStatus(status).withSafetyStatus(status)
                .withTransportManagersStatus(status).withTypeOfLicenceStatus(status).withDeclarationsInternalStatus(status).withVehiclesDeclarationsStatus(status).withVehiclesStatus(status).withVehiclesPsvStatus(status)
                .withTaxiPhvStatus(status);
        OverviewBuilder overview = new OverviewBuilder().withId(applicationNumber).withVersion(applicationVersion).withLeadTcArea(transportArea).withOverrideOppositionDate(overrideOption)
                .withTracking(tracking);
        apiResponse = RestUtils.put(overview, overviewResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void getOutstandingFees(String applicationNumber) {
        String getOutstandingFeesResource = URL.build(env, String.format("application/%s/outstanding-fees/", applicationNumber)).toString();
        apiResponse = RestUtils.get(getOutstandingFeesResource, getHeaders());
        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        } else if (!apiResponse.extract().response().body().jsonPath().getList("outstandingFees.id").isEmpty()) {
            outstandingFeesIds = apiResponse.extract().response().body().jsonPath().getList("outstandingFees.id");
            List<String> fees = apiResponse.extract().response().body().jsonPath().get("outstandingFees.grossAmount");
            for (String d : fees) {
                try {
                    feesToPay.add(Double.parseDouble(d));
                } catch (NumberFormatException e) {
                    e.fillInStackTrace();
                }

            }
        }
    }

    public void payOutstandingFees(String organisationId, String applicationNumber) {
        String payer = "apiUser";
        String paymentMethod = "fpm_cash";
        String slipNo = "123456";

        String payOutstandingFeesResource = URL.build(env, "transaction/pay-outstanding-fees/").toString();
        FeesBuilder feesBuilder = new FeesBuilder().withFeeIds(outstandingFeesIds).withOrganisationId(organisationId).withApplicationId(applicationNumber)
                .withPaymentMethod(paymentMethod).withReceived(feesToPay.stream().mapToDouble(Double::doubleValue).sum()).withReceiptDate(GenericUtils.getDates("current", 0)).withPayer(payer).withSlipNo(slipNo);
        apiResponse = RestUtils.post(feesBuilder, payOutstandingFeesResource, getHeaders());
        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void grant(String applicationNumber) {
        String grantApplicationResource = URL.build(env, String.format("application/%s/grant/", applicationNumber)).toString();
        GrantApplicationBuilder grantApplication = new GrantApplicationBuilder().withId(applicationNumber).withDuePeriod("9").withCaseworkerNotes("This notes are from the API");
        apiResponse = RestUtils.put(grantApplication, grantApplicationResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        } else if (apiResponse.extract().response().asString().contains("fee")) {
            feeId = apiResponse.extract().response().jsonPath().getInt("id.fee");
        }
    }

    public ValidatableResponse payGrantFees() {
        String payer = "apiUser";
        Double grantFees = 401.00;
        String paymentMethod = "fpm_cash";
        String slipNo = "123456";
        String organisationId = world.createLicence.getOrganisationId();
        String applicationNumber = world.createLicence.getApplicationNumber();
        feeId = world.grantLicence.feeId;

        String payOutstandingFeesResource = URL.build(env, "transaction/pay-outstanding-fees/").toString();
        FeesBuilder feesBuilder = new FeesBuilder().withFeeIds(Collections.singletonList(feeId)).withOrganisationId(organisationId).withApplicationId(applicationNumber)
                .withPaymentMethod(paymentMethod).withReceived(grantFees).withReceiptDate(GenericUtils.getDates("current", 0)).withPayer(payer).withSlipNo(slipNo);
        apiResponse = RestUtils.post(feesBuilder, payOutstandingFeesResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_CREATED) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        }
        return apiResponse;
    }

    public ValidatableResponse grantLicence() {
        if (world.updateLicence.getVariationApplicationNumber() != null) {
            world.grantLicence.createOverview(world.updateLicence.getVariationApplicationNumber());
            world.grantLicence.variationGrant(world.updateLicence.getVariationApplicationNumber());
        } else {
            world.grantLicence.createOverview(world.createLicence.getApplicationNumber());
            world.grantLicence.getOutstandingFees(world.createLicence.getApplicationNumber());
            world.grantLicence.payOutstandingFees(world.createLicence.getOrganisationId(), world.createLicence.getApplicationNumber());
            world.grantLicence.grant(world.createLicence.getApplicationNumber());
        }
        return apiResponse;
    }

    private void variationGrant(String applicationNumber) {
        String grantApplicationResource = URL.build(env, String.format("variation/%s/grant/", applicationNumber)).toString();
        GenericBuilder grantVariationBuilder = new GenericBuilder().withId(applicationNumber);
        apiResponse = RestUtils.put(grantVariationBuilder, grantApplicationResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        }
    }

    public void refuse(String applicationNumber) {
        String grantApplicationResource = URL.build(env, String.format("application/%s/refuse/", applicationNumber)).toString();
        GrantApplicationBuilder grantApplication = new GrantApplicationBuilder().withId(applicationNumber).withCaseworkerNotes("This notes are from the API");
        apiResponse = RestUtils.put(grantApplication, grantApplicationResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        } else if (apiResponse.extract().response().asString().contains("fee")) {
            feeId = apiResponse.extract().response().jsonPath().getInt("id.fee");
        }
    }

    public void withdraw(String applicationNumber) {
        String grantApplicationResource = URL.build(env, String.format("application/%s/withdraw/", applicationNumber)).toString();
        GrantApplicationBuilder grantApplication = new GrantApplicationBuilder().withId(applicationNumber).withReason("reg_in_error");
        apiResponse = RestUtils.put(grantApplication, grantApplicationResource, getHeaders());

        if (apiResponse.extract().statusCode() != HttpStatus.SC_OK) {
            System.out.println(apiResponse.extract().statusCode());
            System.out.println(apiResponse.extract().response().asString());
            throw new HTTPException(apiResponse.extract().statusCode());
        } else if (apiResponse.extract().response().asString().contains("fee")) {
            feeId = apiResponse.extract().response().jsonPath().getInt("id.fee");
        }
    }
}