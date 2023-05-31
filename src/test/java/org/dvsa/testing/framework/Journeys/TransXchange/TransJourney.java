package org.dvsa.testing.framework.Journeys.TransXchange;

import com.amazonaws.services.sqs.model.Message;
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
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransJourney extends BasePage {

    private final String BASE_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/";

    // Valid and Invalid PDF request
    private final String VALID_XML_RESPONSE_PATH = BASE_PATH + "valid/ValidPdfResponse.xml";
    private final String INVALID_XML_PATH = BASE_PATH + "invalid/InvalidPdfRequest.xml";
    private final String INVALID_XML_RESPONSE_PATH = BASE_PATH + "invalid/InvalidPdfResponse.xml";
    // Valid timetable request
    private final String VALID_TIMETABLE_PDF_REQUEST_XML = BASE_PATH + "valid/ValidTimetableOperatorXmlPdfRequest.xml";
    private final String VALID_TIMETABLE_OPERATOR_XML_PATH = BASE_PATH + "valid/ValidTimetableOperatorXml.xml";
    private final String VALID_TIMETABLE_OPERATOR_XML_KEY = "ValidTimetableOperatorXml.xml";
    // Valid fileNotFound request
    private final String VALID_FILE_NOT_FOUND_PDF_REQUEST_XML = BASE_PATH + "valid/ValidFileNotFoundPdfRequest.xml";
    private final String FILE_NOT_FOUND_RESPONSE = BASE_PATH + "invalid/fileNotFoundResponse.xml";
    // Invalid missingOperator
    private final String INVALID_MISSING_OPERATORS_PDF_REQUEST_XML = BASE_PATH + "invalid/InvalidOperatorXmlMissingOperatorsPdfRequest.xml";
    private final String INVALID_MISSING_OPERATORS_OPERATOR_XML_PATH = BASE_PATH + "invalid/InvalidOperatorXmlMissingOperators.xml";
    private final String INVALID_MISSING_OPERATORS_RESPONSE = BASE_PATH + "invalid/InvalidOperatorXmlMissingOperatorsResponse.xml";
    private final String INVALID_MISSING_OPERATORS_OPERATOR_XML_KEY = "InvalidOperatorXmlMissingOperators.xml";

    private final World world;
    private String responseBodyText;

    public TransJourney(World world) {
        this.world = world;
    }


    /**
     * Requests an Oauth2 token for authentication with the api gateway.
     *
     * @return A string with the token
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    public String getAuthToken() throws OAuthSystemException, OAuthProblemException {
        OAuthClient client = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest request =
                OAuthClientRequest.tokenLocation(world.configuration.config.getString("tokenUrl"))
                        .setGrantType(GrantType.CLIENT_CREDENTIALS)
                        .setClientId(world.configuration.config.getString("clientId"))
                        .setClientSecret(world.configuration.config.getString("clientSecret"))
                        .setScope(world.configuration.config.getString("scope"))
                        .buildBodyMessage();
        return client.accessToken(request,
                OAuth.HttpMethod.POST,
                OAuthJSONAccessTokenResponse.class).getAccessToken();
    }

    public int sendMissingOperatorsValidXmlRequest() throws Exception {
        ClassicHttpResponse response = sendPdfRequest(INVALID_MISSING_OPERATORS_PDF_REQUEST_XML);

        // Test the response is what we expect
        String xmlResponse = getFileStringUsingFilePath(VALID_XML_RESPONSE_PATH);
        assertEquals(responseBodyText, xmlResponse);
        return response.getCode();
    }

    public int sendValidPdfRequest(String type) throws Exception {
        String requestXmlPath;
        if (type.equals("timetable")){
            requestXmlPath = VALID_TIMETABLE_PDF_REQUEST_XML;
        }
        else if (type.equals("fileNotFound")){
            requestXmlPath = VALID_FILE_NOT_FOUND_PDF_REQUEST_XML;
        }
        else {
            throw new IllegalArgumentException("[" + type + "] is an invalid pdf request type");
        }

        ClassicHttpResponse response = sendPdfRequest(requestXmlPath);

        // Test the response is what we expect
        String xmlResponse = getFileStringUsingFilePath(VALID_XML_RESPONSE_PATH);
        assertEquals(responseBodyText, xmlResponse);
        return response.getCode();
    }

    public ClassicHttpResponse sendPdfRequest(String path) throws Exception {
        String xml = getFileStringUsingFilePath(path);
        HttpPost request = createRequest();
        String token = getAuthToken();
        request.setHeader("Authorization", "Bearer " + token);
        request.setEntity(new StringEntity(xml));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        return client.execute(request, responseHandler -> {
            responseBodyText = EntityUtils.toString(responseHandler.getEntity());
            return responseHandler;
        });
    }

    public int sendInvalidXmlRequest() throws Exception {
        String xml = getFileStringUsingFilePath(INVALID_XML_PATH);
        HttpPost request = createRequest();
        String token = getAuthToken();
        request.setHeader("Authorization", "Bearer " + token);
        request.setEntity(new StringEntity(xml));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ClassicHttpResponse response = client.execute(request, responseHandler -> {
            responseBodyText = EntityUtils.toString(responseHandler.getEntity());
            return responseHandler;
        });

        // Test the response is what we expect
        String xmlResponse = getFileStringUsingFilePath(INVALID_XML_RESPONSE_PATH);
        assertEquals(responseBodyText, xmlResponse);
        return response.getCode();
    }

    public int sendUnauthorisedRequest() throws Exception {
        String xml = getFileStringUsingFilePath(VALID_TIMETABLE_PDF_REQUEST_XML);
        HttpPost request = createRequest();
        request.setEntity(new StringEntity(xml));
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ClassicHttpResponse response = client.execute(request, responseHandler -> {
            responseBodyText = EntityUtils.toString(responseHandler.getEntity());
            return responseHandler;
        });

        // Test the response is what we expect
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "Unauthorized");
        assertEquals(jsonObject.toString(), responseBodyText);
        return response.getCode();
    }

    public void inputValidOperatorXml(String type) {
        String bucketName = world.configuration.config.getString("operatorXmlInputBucket");
        if (type.equals("timetable")) {
            world.awsHelper.addFileToBucket(bucketName, VALID_TIMETABLE_OPERATOR_XML_KEY, VALID_TIMETABLE_OPERATOR_XML_PATH);
        }
        else {
            throw new IllegalArgumentException("[" + type + "] type is not valid");
        }
    }

    public void inputInvalidOperatorXml(String type) {
        String bucketName = world.configuration.config.getString("operatorXmlInputBucket");
        if (type.equals("missingOperators")) {
            world.awsHelper.addFileToBucket(bucketName, INVALID_MISSING_OPERATORS_OPERATOR_XML_KEY, INVALID_MISSING_OPERATORS_OPERATOR_XML_PATH);
        }
        else {
            throw new IllegalArgumentException("[" + type + "] type is not valid");
        }
    }


    public void getMessagesFromSqs(String problem) throws IOException {
        String xml;
        if (problem.equals("missingOperators")){
            xml = getFileStringUsingFilePath(INVALID_MISSING_OPERATORS_RESPONSE);
        }
        else if (problem.equals("fileNotFound")){
            xml = getFileStringUsingFilePath(FILE_NOT_FOUND_RESPONSE);
        }
        else {
            throw new IllegalArgumentException("[" + problem + "] problem is not valid");
        }
        String queueUrl = world.configuration.config.getString("fileProcessedOutputQueueUrl");
        List<Message> sqsMessages = world.awsHelper.getMessagesFromSqs(queueUrl);
        String messageBody = sqsMessages.get(0).getBody();
        assertEquals(xml, messageBody);
    }

    /**
     * Creates request object and adds common headers
     */
    private HttpPost createRequest() {
        HttpPost request = new HttpPost(world.configuration.config.getString("apiUrl"));
        Random random = new Random();

        // generate a random integer from 0 to 899, then add 100
        int x = random.nextInt(900) + 100;
        String id = "abc" + x;
        System.out.println("Id for this request is [" + id + "]");
        request.setHeader("X-Correlation-Id", id);
        request.setHeader("Content-Type", "application/xml");
        request.setHeader("Cache-Control", "no-cache");
        return request;
    }

    private String getFileStringUsingFilePath(String path) throws IOException {
        File xmlResponseFile = new File(path);
        return new String(Files.readAllBytes(xmlResponseFile.toPath()));
    }
}
