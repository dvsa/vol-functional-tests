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
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;


public class TransJourney extends BasePage {

    private World world;
    private URL url;
    private HttpURLConnection connection;
    private int responseCode;
    private String responseMessage;
    private StringBuilder response;

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
        String token = client.accessToken(request,
                OAuth.HttpMethod.POST,
                OAuthJSONAccessTokenResponse.class).getAccessToken();
        return token;

    }

    public void sendValidXmlRequest() throws Exception {

        File xmlFile = new File("./src/test/resources/org/dvsa/testing/framework/TransXchange/ValidPdfRequest.xml");
        String gettoken = world.TransXchangeJourney.getAuthToken();
        int responseCode = connection.getResponseCode();
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Authorization",gettoken);
        connection.setRequestMethod("POST");
        configureConnection();

        connection.setDoOutput(true);
        //connection.setDoInput(false);

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



    public void sendInvalidXmlRequest() throws Exception {

        File xmlFile = new File("./src/test/resources/org/dvsa/testing/framework/TransXchange/InvalidPdfRequest.xml");
        String gettoken = world.TransXchangeJourney.getAuthToken();
        int responseCode = connection.getResponseCode();
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("X-Correlation-Id", "abc123");
        connection.setRequestProperty("Authorization",gettoken);
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("Accept","*/*");
        connection.setRequestProperty("Cache-Control","no-cache");
        connection.setRequestProperty("Host","12r2b5w66k.execute-api.eu-west-1.amazonaws.com");
        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
        connection.setRequestProperty("Connection","keep-alive");
        connection.setRequestProperty("Content-Length","1844");

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

    public void sendUnauthorisedRequest() throws Exception {

        File xmlFile = new File("./src/test/resources/org/dvsa/testing/framework/TransXchange/InvalidPdfRequest.xml");
        int responseCode = connection.getResponseCode();
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        configureConnection();

        connection.setDoOutput(true);
        //connection.setDoInput(false);

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

        // Check the response code
        //  int responseCode= connection.getResponseCode();



    }

    public void sendUnsecuredRequest(String apiUrl, String xml) throws Exception {

        File xmlFile = new File("./src/test/resources/org/dvsa/testing/framework/TransXchange/InvalidPdfRequest.xml");
        int responseCode = connection.getResponseCode();
        url = new URL(world.configuration.config.getString("apiUrl"));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        configureConnection();

        connection.setDoOutput(true);
        connection.setDoInput(false);

        FileInputStream fileInputStream = new FileInputStream(xmlFile);
        //System.out.println("fileInputStream:"+fileInputStream);

        // Get the output stream of the connection
        OutputStream outputStream = connection.getOutputStream();

        // Write the XML file contents to the output stream
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

//
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            // Request successful
//            System.out.println("XML file sent successfully.");
//        } else {
//            // Request failed
//            System.out.println("Failed to send XML file. Response Code: " + responseCode);
//        }

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