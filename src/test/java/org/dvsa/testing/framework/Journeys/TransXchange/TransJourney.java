package org.dvsa.testing.framework.Journeys.TransXchange;

import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;


public class TransJourney extends BasePage {

    private World world;
    private String text;

    private final String VALID_XML_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/ValidPdfRequest.xml";
    private final String VALID_XML_RESPONSE_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/ValidPdfResponse.xml";
    private final String INVALID_XML_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/InvalidPdfRequest.xml";
    private final String INVALID_XML_RESPONSE_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/InvalidPdfResponse.xml";

    public TransJourney(World world) {
        this.world = world;
    }


    public String getAuthToken() throws OAuthSystemException, OAuthProblemException {
        OAuthClient client = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest request =
                OAuthClientRequest.tokenLocation(world.configuration.config.getString("tokenUrl"))
                        .setGrantType(GrantType.CLIENT_CREDENTIALS)
                        .setClientId(world.configuration.config.getString("setClientId"))
                        .setClientSecret(world.configuration.config.getString("setClientSecret"))
                        .setScope(world.configuration.config.getString("setScope"))
                        .buildBodyMessage();
        return client.accessToken(request,
                OAuth.HttpMethod.POST,
                OAuthJSONAccessTokenResponse.class).getAccessToken();
    }

    public int sendValidXmlRequest() throws Exception {


        File xmlFile = new File(VALID_XML_PATH);
        String xml = new String(Files.readAllBytes(xmlFile.toPath()));

        HttpPost request = createRequest();
        String token = getAuthToken();
        request.setHeader("Authorization","Bearer " + token);
        request.setEntity(new StringEntity(xml));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ClassicHttpResponse response = client.execute(request, responseHandler -> {
            text = EntityUtils.toString(responseHandler.getEntity());
          return responseHandler;
        });

        // Test the response is what we expect
        File xmlResponseFile = new File(VALID_XML_RESPONSE_PATH);
        String xmlResponse = new String(Files.readAllBytes(xmlResponseFile.toPath()));
        assertEquals(text, xmlResponse);
        return response.getCode();
    }

    public int sendInvalidXmlRequest() throws Exception {

        File xmlFile = new File(INVALID_XML_PATH);
        String xml = new String(Files.readAllBytes(xmlFile.toPath()));
        HttpPost request = createRequest();
        String token = getAuthToken();
        request.setHeader("Authorization","Bearer " + token);
        request.setEntity(new StringEntity(xml));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ClassicHttpResponse response = client.execute(request, responseHandler -> {
            text = EntityUtils.toString(responseHandler.getEntity());
            System.out.println("here1");
            System.out.println(text);
            return responseHandler;
        });

        // Test the response is what we expect
        File xmlResponseFile = new File(INVALID_XML_RESPONSE_PATH);
        String xmlResponse = new String(Files.readAllBytes(xmlResponseFile.toPath()));
        assertEquals(text, xmlResponse);
        return response.getCode();

    }

    public int sendUnauthorisedRequest() throws Exception {
        File xmlFile = new File(VALID_XML_PATH);
        String xml = new String(Files.readAllBytes(xmlFile.toPath()));

        HttpPost request = createRequest();
        request.setEntity(new StringEntity(xml));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ClassicHttpResponse response = client.execute(request, responseHandler -> {
            text = EntityUtils.toString(responseHandler.getEntity());
            return responseHandler;
        });

        // Test the response is what we expect
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "Unauthorized");
        assertEquals(jsonObject.toString(), text);
        return response.getCode();
    }

    /**
     * Creates request object and adds common headers
     */
    private HttpPost createRequest() {
        HttpPost request = new HttpPost(world.configuration.config.getString("apiUrl"));
        request.setHeader("X-Correlation-Id", "abc123");
        request.setHeader("Content-Type", "application/xml");
        request.setHeader("Cache-Control","no-cache");
        return request;
    }
}
