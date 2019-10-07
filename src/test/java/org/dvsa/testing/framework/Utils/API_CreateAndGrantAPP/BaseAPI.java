package org.dvsa.testing.framework.Utils.API_CreateAndGrantAPP;

import activesupport.MissingRequiredArgument;
import activesupport.http.RestUtils;
import activesupport.system.Properties;
import io.restassured.response.ValidatableResponse;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;
import org.dvsa.testing.framework.Utils.API_Headers.Headers;
import org.dvsa.testing.lib.url.api.URL;
import org.dvsa.testing.lib.url.utils.EnvironmentType;

public class BaseAPI {

    protected EnvironmentType env;

    public BaseAPI() {
        try {
            env = EnvironmentType.getEnum(Properties.get("env", true));
        } catch (MissingRequiredArgument missingRequiredArgument) {
            missingRequiredArgument.printStackTrace();
        }
    }

    protected String retrieveData(String url, String jsonPath, String defaultReturn) {
        Headers.headers.put("x-pid", APIJourneySteps.adminApiHeader());
        ValidatableResponse response = RestUtils.get(url, Headers.getHeaders());
        try {
            return response.extract().response().jsonPath().getString(jsonPath);
        } catch (NullPointerException ne) {
            return defaultReturn;
        }
    }

    protected String fetchApplicationInformation(String applicationNumber, String jsonPath, String defaultReturn) {
        String url = URL.build(env, String.format("application/%s/overview/", applicationNumber)).toString();
        return retrieveData(url, jsonPath, defaultReturn);
    }

    protected String fetchTMApplicationInformation(String applicationNumber, String jsonPath, String defaultReturn) {
        String url = URL.build(env, String.format("transport-manager-application/%s", applicationNumber)).toString();
        return retrieveData(url, jsonPath, defaultReturn);
    }
}
