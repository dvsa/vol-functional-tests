package org.dvsa.testing.framework.Journeys.licence;

import apiCalls.Utils.generic.BaseAPI;
import apiCalls.Utils.generic.Headers;
import apiCalls.Utils.http.RestUtils;
import apiCalls.Utils.volBuilders.FeesBuilder;
import io.restassured.response.ValidatableResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpStatus;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.lib.url.api.ApiUrl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ApplicationFeeApiJourney extends BaseAPI {

    private final World world;
    private final Headers apiHeaders = new Headers();

    public ApplicationFeeApiJourney(World world) {
        this.world = world;
    }

    public String interimFeeTypeId() throws HttpException {
        String endpoint = endpoint("fee-type/latest/");
        ValidatableResponse response = RestUtils.getWithQueryParams(endpoint, Map.of(
                "feeType", "GRANTINT",
                "operatorType", "lcat_gv",
                "licenceType", "ltyp_sn",
                "date", LocalDate.now().toString()
        ), headers());
        requireStatus(response, HttpStatus.SC_OK, endpoint);
        String id = response.extract().jsonPath().getString("results[0].id");
        if (id == null) {
            throw new IllegalStateException("No goods standard national interim fee type returned: "
                    + response.extract().body().asString());
        }
        return id;
    }

    public int createInterimFee(String feeTypeId, LocalDate invoiceDate, BigDecimal amount) throws HttpException {
        String endpoint = endpoint("fee/");
        ValidatableResponse response = RestUtils.post(Map.of(
                "application", world.createApplication.getApplicationId(),
                "feeType", feeTypeId,
                "invoicedDate", invoiceDate.toString(),
                "amount", amount,
                "feeStatus", "lfs_ot",
                "description", "Application fee invoice date ordering regression"
        ), endpoint, headers());
        requireStatus(response, HttpStatus.SC_CREATED, endpoint);
        Integer id = response.extract().jsonPath().get("id.fee");
        if (id == null) {
            throw new IllegalStateException("Created fee response has no fee ID: "
                    + response.extract().body().asString());
        }
        return id;
    }

    public BigDecimal declarationInterimFee() throws HttpException {
        String endpoint = endpoint("application/%s/declaration/"
                .formatted(world.createApplication.getApplicationId()));
        ValidatableResponse response = RestUtils.get(endpoint, headers());
        requireStatus(response, HttpStatus.SC_OK, endpoint);
        String amount = response.extract().jsonPath().getString("interimFee");
        if (amount == null) {
            throw new IllegalStateException("Declaration response has no interim fee: "
                    + response.extract().body().asString());
        }
        return new BigDecimal(amount);
    }

    public BigDecimal grossAmount(int feeId) throws HttpException {
        String endpoint = endpoint("fee/%s/".formatted(feeId));
        ValidatableResponse response = RestUtils.get(endpoint, headers());
        requireStatus(response, HttpStatus.SC_OK, endpoint);
        String amount = response.extract().jsonPath().getString("grossAmount");
        if (amount == null) {
            throw new IllegalStateException("Fee response has no gross amount: "
                    + response.extract().body().asString());
        }
        return new BigDecimal(amount);
    }

    public void payFee(int feeId, BigDecimal amount) throws HttpException {
        String endpoint = endpoint("transaction/pay-outstanding-fees/");
        FeesBuilder payment = new FeesBuilder()
                .withFeeIds(List.of(feeId))
                .withPaymentMethod("fpm_cash")
                .withReceived(amount.doubleValue())
                .withReceiptDate(LocalDate.now().toString())
                .withPayer("Invoice date ordering regression")
                .withSlipNo("123456");
        ValidatableResponse response = RestUtils.post(payment, endpoint, headers());
        requireStatus(response, HttpStatus.SC_CREATED, endpoint);
        if (response.extract().jsonPath().get("id.transaction") == null) {
            throw new IllegalStateException("Payment did not create a transaction for fee " + feeId
                    + ": " + response.extract().body().asString());
        }

        String feeEndpoint = endpoint("fee/%s/".formatted(feeId));
        ValidatableResponse feeResponse = RestUtils.get(feeEndpoint, headers());
        requireStatus(feeResponse, HttpStatus.SC_OK, feeEndpoint);
        String status = feeResponse.extract().jsonPath().getString("feeStatus.id");
        String outstanding = feeResponse.extract().jsonPath().getString("outstanding");
        if (!"lfs_pd".equals(status) || outstanding == null
                || new BigDecimal(outstanding).signum() != 0) {
            throw new IllegalStateException("Fee " + feeId + " was not fully paid after payment: "
                    + feeResponse.extract().body().asString());
        }
    }

    private String endpoint(String path) {
        return ApiUrl.build(world.APIJourney.env, path).toString();
    }

    private Map<String, String> headers() throws HttpException {
        apiHeaders.getApiHeader().put("Authorization", "Bearer " + adminJWT());
        return apiHeaders.getApiHeader();
    }

    private void requireStatus(ValidatableResponse response, int expected, String endpoint) {
        int actual = response.extract().statusCode();
        if (actual != expected) {
            throw new IllegalStateException("Application fee request failed. Endpoint: " + endpoint
                    + " expected status: " + expected + " actual status: " + actual
                    + " body: " + response.extract().body().asString());
        }
    }
}
