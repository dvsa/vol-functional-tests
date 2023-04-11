package org.dvsa.testing.framework.Utils.common;

import activesupport.system.Properties;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public static void triggerProcessQueue() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Jenkins-Crumb", "14532652b847d469552151997a208306");

        ValidatableResponse response = RestAssured
                .given()
                    .log().all()
                    .config(RestAssuredConfig.config().sslConfig(new SSLConfig().relaxedHTTPSValidation().allowAllHostnames()))
                    .config(RestAssured.config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs("x-www-form-urlencoded", ContentType.TEXT)))
                .auth()
                .preemptive()
                .basic(
                        Properties.get("LDAP_NONPROD_USER", true),
                        Properties.get("LDAP_NONPROD_PASSWORD", true)
                )
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                    .formParam("platformEnv", Properties.get("env", true))
                .headers(headers)
                .post(
                        "http" +
                                "://olcsci.shd.ci.nonprod.dvsa.aws" +
                                ":8080/job/Miscellaneous/job/Process_Queue/buildWithParameters"
                )
                .then();

        response.statusCode(HttpStatus.SC_CREATED);
    }
}