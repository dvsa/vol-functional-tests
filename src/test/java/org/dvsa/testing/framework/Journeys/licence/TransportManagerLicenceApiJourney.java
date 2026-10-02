package org.dvsa.testing.framework.Journeys.licence;

import activesupport.system.Properties;
import apiCalls.Utils.generic.BaseAPI;
import apiCalls.Utils.generic.Headers;
import apiCalls.Utils.http.RestUtils;
import io.restassured.response.ValidatableResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpStatus;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.lib.url.api.ApiUrl;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransportManagerLicenceApiJourney extends BaseAPI {

    private final World world;
    private final Headers apiHeaders = new Headers();
    private ValidatableResponse lastResponse;

    public TransportManagerLicenceApiJourney(World world) {
        this.world = world;
    }

    public ValidatableResponse getLastResponse() {
        return lastResponse;
    }

    public ValidatableResponse searchTransportManagerLicences(String licenceId, String transportManagerId)
            throws HttpException {
        EnvironmentType environment = EnvironmentType.getEnum(Properties.get("env", true));
        String endpoint = ApiUrl.build(environment, "transport-manager-licence").toString();

        Map<String, String> queryParams = new HashMap<>();
        if (licenceId != null) {
            queryParams.put("licence", licenceId);
        }
        if (transportManagerId != null) {
            queryParams.put("transportManager", transportManagerId);
        }

        apiHeaders.getApiHeader().put("Authorization", "Bearer " + adminJWT());
        lastResponse = RestUtils.getWithQueryParams(endpoint, queryParams, apiHeaders.getApiHeader());

        int statusCode = lastResponse.extract().statusCode();
        if (statusCode != HttpStatus.SC_OK) {
            String body = lastResponse.extract().body().asString();
            String diagnosis = body.contains("Too many parameters")
                    ? " This is the known applyListFilters() defect: the second where() call replaces the licence"
                    + " condition instead of adding to it, leaving the licence parameter bound but unreferenced."
                    + " The fix is to use andWhere() for the transport manager condition."
                    : "";
            throw new IllegalStateException(
                    "Transport manager licence search failed. Endpoint: " + endpoint
                            + " params: " + queryParams
                            + " status: " + statusCode
                            + " body: " + body
                            + diagnosis);
        }
        return lastResponse;
    }

    public int reportedCount() {
        return lastResponse.extract().jsonPath().getInt("count");
    }

    public int returnedRowCount() {
        return results().size();
    }

    public List<String> returnedLicenceIds() {
        return idsAt("results.licence.id");
    }

    public List<String> returnedTransportManagerIds() {
        return idsAt("results.transportManager.id");
    }

    private List<Map<String, Object>> results() {
        List<Map<String, Object>> results = lastResponse.extract().jsonPath().getList("results");
        if (results == null) {
            throw new IllegalStateException(
                    "Expected a 'results' list on the transport manager licence search response, body was: "
                            + lastResponse.extract().body().asString());
        }
        return results;
    }

    private List<String> idsAt(String jsonPath) {
        results();
        List<Object> rawIds = lastResponse.extract().jsonPath().getList(jsonPath);
        if (rawIds == null) {
            return List.of();
        }
        return rawIds.stream().map(String::valueOf).collect(Collectors.toList());
    }
}
