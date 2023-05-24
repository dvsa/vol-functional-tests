package org.dvsa.testing.framework.Journeys.TransXchange;

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
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;


public class TransJourney extends BasePage {

    private World world;
    private URL url;
    private HttpURLConnection connection;
    private String text;

    private final String VALID_XML_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/ValidPdfRequest.xml";
    private final String VALID_XML_RESPONSE_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/ValidPdfResponse.xml";
    private final String INVALID_XML_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/InvalidPdfRequest.xml";

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

        final HttpPost request = new HttpPost(world.configuration.config.getString("apiUrl"));
        File xmlFile = new File(VALID_XML_PATH);
        String xml = new String(Files.readAllBytes(xmlFile.toPath()));

        String token = world.TransXchangeJourney.getAuthToken();
        request.setHeader("Authorization","Bearer " + token);
        request.setHeader("X-Correlation-Id", "abc123");
        request.setHeader("Content-Type", "application/xml");
        request.setHeader("Cache-Control","no-cache");
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
        String token = world.TransXchangeJourney.getAuthToken();
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization","Bearer " + token);
        configureConnection();

        connection.setDoOutput(true);

        FileInputStream fileInputStream = new FileInputStream(xmlFile);
        // Get the output stream of the connection
        OutputStream outputStream = connection.getOutputStream();

        // Write the XML file contents to the output stream
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        outputStream.close();

        fileInputStream.close();
        return connection.getResponseCode();

    }

    public int sendUnauthorisedRequest() throws Exception {

        File xmlFile = new File(INVALID_XML_PATH);
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        configureConnection();

        connection.setDoOutput(true);

        FileInputStream fileInputStream = new FileInputStream(xmlFile);
        System.out.println("fileInputStream:"+fileInputStream);

        // Get the output stream of the connection
        OutputStream outputStream = connection.getOutputStream();

        // Write the XML file contents to the output stream
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        outputStream.close();

        fileInputStream.close();
        return connection.getResponseCode();
    }

    /**
     * Adds common connection configuration
     */
    private void configureConnection() {
        connection.setRequestProperty("X-Correlation-Id", "abc123");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.setRequestProperty("Cache-Control","no-cache");
    }
}
