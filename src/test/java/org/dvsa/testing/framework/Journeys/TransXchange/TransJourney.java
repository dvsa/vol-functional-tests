package org.dvsa.testing.framework.Journeys.TransXchange;


import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.model.Message;
import com.google.gson.JsonObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TransJourney extends BasePage {

    private final String BASE_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/";
    // TODO - put each variation into a more easily digestible format
    private final String VALID_XML_PATH = BASE_PATH + "valid/ValidPdfRequest.xml";
    private final String VALID_XML_RESPONSE_PATH = BASE_PATH + "valid/ValidPdfResponse.xml";
    private final String INVALID_XML_PATH = BASE_PATH + "invalid/InvalidPdfRequest.xml";
    private final String INVALID_XML_RESPONSE_PATH = BASE_PATH + "invalid/InvalidPdfResponse.xml";

    // Valid timetable request
    private final String VALID_TIMETABLE_PDF_REQUEST_XML = BASE_PATH + "valid/ValidTimetableOperatorXmlPdfRequest.xml";
    private final String VALID_TIMETABLE_OPERATOR_XML_PATH = BASE_PATH + "valid/ValidTimetableOperatorXml.xml";
    private final String VALID_TIMETABLE_OPERATOR_XML_KEY = "ValidTimetableOperatorXml.xml";
    // Invalid missingOperator
    private final String INVALID_MISSING_OPERATORS_PDF_REQUEST_XML = BASE_PATH + "invalid/ValidPdfRequestMissingOperators.xml";
    private final String INVALID_MISSING_OPERATORS_OPERATOR_XML_PATH = BASE_PATH + "invalid/InvalidOperatorXmlMissingOperators.xml";
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

    public int sendValidXmlRequest() throws Exception {
        ClassicHttpResponse response = sendPdfRequest(VALID_XML_PATH);

        // Test the response is what we expect
        File xmlResponseFile = new File(VALID_XML_RESPONSE_PATH);
        String xmlResponse = new String(Files.readAllBytes(xmlResponseFile.toPath()));
        assertEquals(responseBodyText, xmlResponse);
        return response.getCode();
    }

    public int sendMissingOperatorsValidXmlRequest() throws Exception {
        ClassicHttpResponse response = sendPdfRequest(INVALID_MISSING_OPERATORS_PDF_REQUEST_XML);

        // Test the response is what we expect
        File xmlResponseFile = new File(VALID_XML_RESPONSE_PATH);
        String xmlResponse = new String(Files.readAllBytes(xmlResponseFile.toPath()));
        assertEquals(responseBodyText, xmlResponse);
        return response.getCode();
    }

    public int sendValidPdfRequest(String type) throws Exception {
        String requestXmlPath = "";
        if (type.equals("timetable")){
            requestXmlPath = VALID_TIMETABLE_PDF_REQUEST_XML;
        }
        else {
            throw new IllegalArgumentException("[" + type + "] is an invalid pdf request type");
        }

        ClassicHttpResponse response = sendPdfRequest(requestXmlPath);

        // Test the response is what we expect
        File xmlResponseFile = new File(VALID_XML_RESPONSE_PATH);
        String xmlResponse = new String(Files.readAllBytes(xmlResponseFile.toPath()));
        assertEquals(responseBodyText, xmlResponse);
        return response.getCode();
    }

    public ClassicHttpResponse sendPdfRequest(String path) throws Exception {
        File xmlFile = new File(path);
        String xml = new String(Files.readAllBytes(xmlFile.toPath()));

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
        File xmlFile = new File(INVALID_XML_PATH);
        String xml = new String(Files.readAllBytes(xmlFile.toPath()));
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
        File xmlResponseFile = new File(INVALID_XML_RESPONSE_PATH);
        String xmlResponse = new String(Files.readAllBytes(xmlResponseFile.toPath()));
        assertEquals(responseBodyText, xmlResponse);
        return response.getCode();
    }

    public int sendUnauthorisedRequest() throws Exception {
        File xmlFile = new File(VALID_XML_PATH);
        String xml = new String(Files.readAllBytes(xmlFile.toPath()));

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
            System.out.println("[" + type + "] type is not valid");

        }
    }

    public void inputInvalidOperatorXml(String type) {
        String bucketName = world.configuration.config.getString("operatorXmlInputBucket");
        if (type.equals("missingOperators")) {
            world.awsHelper.addFileToBucket(bucketName, INVALID_MISSING_OPERATORS_OPERATOR_XML_KEY, INVALID_MISSING_OPERATORS_OPERATOR_XML_PATH);
        }
        else {
            System.out.println("[" + type + "] is not a valid type");
        }
    }

    public void readFileFromBucket() throws IOException {
        String bucketName = world.configuration.config.getString("operatorXmlInputBucket");
        String key = "test-timetable.pdf";
        S3Object s3Object = world.awsHelper.getObjectFromBucket(bucketName, key);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        PdfReader reader = new PdfReader(inputStream);
        int pages = reader.getNumberOfPages();
        StringBuilder text = new StringBuilder();
        for (int i = 1; i <= pages; i++) {
            String line = PdfTextExtractor.getTextFromPage(reader, i);
            text.append(line);
            System.out.println("------");
            System.out.println(line);
        }
        reader.close();
        assertTrue(text.toString().contains("New Farm Loch, Kilmarnock - Bellfield, Kilmarnock"));
    }


    public void getMessagesFromSqs(String problem){
        String queueUrl = world.configuration.config.getString("fileProcessedOutputQueueUrl");
        List<Message> sqsMessages = world.awsHelper.getMessagesFromSqs(queueUrl);
        System.out.println(sqsMessages.get(0).getBody());
        // TODO get correct expected output based on the problem passed in
    }

    /**
     * Creates request object and adds common headers
     */
    private HttpPost createRequest() {
        HttpPost request = new HttpPost(world.configuration.config.getString("apiUrl"));
        request.setHeader("X-Correlation-Id", "abc123");
        request.setHeader("Content-Type", "application/xml");
        request.setHeader("Cache-Control", "no-cache");
        return request;
    }

    public void cleanInputBucket() {
        System.out.println("Cleaning data from input bucket");
        String bucketName = world.configuration.config.getString("operatorXmlInputBucket");
        world.awsHelper.deleteObjectFromBucket(bucketName, VALID_TIMETABLE_OPERATOR_XML_KEY);

        System.out.println("Successfully cleaned input bucket data");
    }
}
