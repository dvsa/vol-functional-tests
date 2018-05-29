package org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP;

import activesupport.MissingRequiredArgument;
import activesupport.http.RestUtils;
import activesupport.system.Properties;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.framework.Utils.API_Builders.*;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.framework.stepdefs.World;
import org.dvsa.testing.lib.url.api.URL;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.dvsa.testing.framework.Utils.API_Headers.Headers.getHeaders;

public class GrantLicenceAPI {

    private ValidatableResponse apiResponse;
    private static String internalHeader = "e91f1a255e01e20021507465a845e7c24b3a1dc951a277b874c3bcd73dec97a1";
    int version = 1;
    private List outstandingFeesIds;
    private int feeId;
    private World world;

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));

    public GrantLicenceAPI(World world) throws MissingRequiredArgument {
        this.world = world;
    }

    public void createOverview(String applicationNumber) throws MalformedURLException {
        int overviewVersion = 1;
        String status = "1";
        String overrideOption = "Y";
        String transportArea = "D";
        String trackingId = "12345";
        String overviewResource = URL.build(env,String.format("application/%s/overview/", applicationNumber)).toString();
        Headers.headers.put("x-pid", internalHeader);

        do {
            TrackingBuilder tracking = new TrackingBuilder().withId(trackingId).withVersion(overviewVersion).withAddressesStatus(status).withBusinessDetailsStatus(status).withBusinessTypeStatus(status)
                    .withCommunityLicencesStatus(status).withConditionsUndertakingsStatus(status).withConvictionsPenaltiesStatus(status).withFinancialEvidenceStatus(status)
                    .withFinancialHistoryStatus(status).withLicenceHistoryStatus(status).withOperatingCentresStatus(status).withPeopleStatus(status).withSafetyStatus(status)
                    .withTransportManagersStatus(status).withTypeOfLicenceStatus(status).withDeclarationsInternalStatus(status).withVehiclesDeclarationsStatus(status).withVehiclesStatus(status).withVehiclesPsvStatus(status);
            OverviewBuilder overview = new OverviewBuilder().withId(applicationNumber).withVersion(version).withLeadTcArea(transportArea).withOverrideOppositionDate(overrideOption)
                    .withTracking(tracking);
            apiResponse = RestUtils.put(overview, overviewResource, getHeaders());
            version++;
            if (version > 20) {
                version = 1;
            }
        } while (apiResponse.extract().statusCode() == HttpStatus.SC_CONFLICT);
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
    }

    public void getOutstandingFees(String applicationNumber) throws MalformedURLException {
        String getOutstandingFeesResource = URL.build(env, String.format("application/%s/outstanding-fees/", applicationNumber)).toString();
        apiResponse = RestUtils.get(getOutstandingFeesResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_OK));
        outstandingFeesIds = apiResponse.extract().response().body().jsonPath().getList("outstandingFees.id");
    }

    public void payOutstandingFees(String organisationId, String applicationNumber) throws MalformedURLException {
        int feesAmount = 405;
        String payer = "apiUser";
        String paymentMethod = "fpm_cash";
        String slipNo = "123456";

        String payOutstandingFeesResource = URL.build(env,"transaction/pay-outstanding-fees/").toString();
        FeesBuilder feesBuilder = new FeesBuilder().withFeeIds(outstandingFeesIds).withOrganisationId(organisationId).withApplicationId(applicationNumber)
                .withPaymentMethod(paymentMethod).withReceived(feesAmount).withReceiptDate(GenericUtils.getDates("current",0)).withPayer(payer).withSlipNo(slipNo);
        apiResponse = RestUtils.post(feesBuilder,payOutstandingFeesResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
    }

    public void grant(String applicationNumber) throws MalformedURLException {
        String grantApplicationResource = URL.build(env,String.format("application/%s/grant/", applicationNumber)).toString();
        GrantApplicationBuilder grantApplication = new GrantApplicationBuilder().withId(applicationNumber).withDuePeriod("9").withCaseworkerNotes("This notes are from the API");
        apiResponse = RestUtils.put(grantApplication, grantApplicationResource, getHeaders());
        if (apiResponse.extract().response().asString().contains("fee")) {
            feeId = apiResponse.extract().response().jsonPath().getInt("id.fee");
        }
    }

    public void payGrantFees() throws MalformedURLException {
        int feesAmount = 405;
        String payer = "apiUser";
        String paymentMethod = "fpm_cash";
        String slipNo = "123456";
        String organisationId = world.createLicence.getOrganisationId();
        String applicationNumber = world.createLicence.getApplicationNumber();
        feeId = world.grantLicence.feeId;

        String payOutstandingFeesResource = URL.build(env,"transaction/pay-outstanding-fees/").toString();
        FeesBuilder feesBuilder = new FeesBuilder().withFeeIds(Collections.singletonList(feeId)).withOrganisationId(organisationId).withApplicationId(applicationNumber)
                .withPaymentMethod(paymentMethod).withReceived(feesAmount).withReceiptDate(GenericUtils.getDates("current",0)).withPayer(payer).withSlipNo(slipNo);
        apiResponse = RestUtils.post(feesBuilder,payOutstandingFeesResource, getHeaders());
        assertThat(apiResponse.statusCode(HttpStatus.SC_CREATED));
    }

    public void variationGrant(String applicationNumber) throws MalformedURLException {
        String grantApplicationResource = URL.build(env,String.format("variation/%s/grant/", applicationNumber)).toString();
        GenericBuilder grantVariationBuilder = new GenericBuilder().withId(applicationNumber);
        apiResponse = RestUtils.put(grantVariationBuilder, grantApplicationResource, getHeaders());
        System.out.println(apiResponse.extract().body().asString());
    }
}