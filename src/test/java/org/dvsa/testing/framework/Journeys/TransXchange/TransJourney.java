package org.dvsa.testing.framework.Journeys.TransXchange;

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


public class TransJourney extends BasePage {

    private World world;
    private URL url;
    private HttpURLConnection connection;

    private final String VALID_XML_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/ValidPdfRequest.xml";
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

        File xmlFile = new File(VALID_XML_PATH);
        String token = world.TransXchangeJourney.getAuthToken();
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Authorization",token);
        connection.setRequestMethod("POST");
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

//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            // Request successful
//            System.out.println("XML file sent successfully.");
//        } else {
//            // Request failed
//            System.out.println("Failed to send XML file. Response Code: " + responseCode);
//        }

    }

    public int sendInvalidXmlRequest() throws Exception {
        File xmlFile = new File(INVALID_XML_PATH);
        String token = world.TransXchangeJourney.getAuthToken();
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization",token);
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

        // Check the response code
        //  int responseCode= connection.getResponseCode();

//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            // Request successful
//            System.out.println("XML file sent successfully.");
//        } else {
//            // Request failed
//            System.out.println("Failed to send XML file. Response Code: " + responseCode);
//        }

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

    public void sendUnsecuredRequest() throws Exception {
        File xmlFile = new File(INVALID_XML_PATH);

        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        configureConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);

        FileInputStream fileInputStream = new FileInputStream(xmlFile);

        // Get the output stream of the connection
        OutputStream outputStream = connection.getOutputStream();

        // Write the XML file contents to the output stream
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        int responseCode = connection.getResponseCode();
    }

    /**
     * Adds common connection configuration
     */
    private void configureConnection() {
        connection.setRequestProperty("X-Correlation-Id", "abc123");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("Accept","*/*");
        connection.setRequestProperty("Cache-Control","no-cache");
        connection.setRequestProperty("Host","12r2b5w66k.execute-api.eu-west-1.amazonaws.com");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
        connection.setRequestProperty("Connection","keep-alive");
        connection.setRequestProperty("Content-Length","1844");
    }
}