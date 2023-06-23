package org.dvsa.testing.framework.Journeys.TransXchange;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.sqs.model.Message;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class TransJourney extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(TransJourney.class);
    private final String BASE_PATH = "./src/test/resources/org/dvsa/testing/framework/TransXchange/";

    // Valid and Invalid PDF request
    private final String VALID_XML_RESPONSE_PATH = BASE_PATH + "valid/ValidPdfResponse.xml";
    private final String INVALID_XML_PATH = BASE_PATH + "invalid/InvalidPdfRequest.xml";
    private final String INVALID_XML_RESPONSE_PATH = BASE_PATH + "invalid/InvalidPdfResponse.xml";
    // Valid timetable request
    private final String VALID_TIMETABLE_PDF_REQUEST_XML = BASE_PATH + "valid/ValidTimetableOperatorXmlPdfRequest.xml";
    private final String VALID_TIMETABLE_OPERATOR_XML_PATH = BASE_PATH + "valid/ValidTimetableOperatorXml.xml";
    private final String VALID_TIMETABLE_OPERATOR_XML_KEY = "ValidTimetableOperatorXml.xml";
    // Valid Dvsa Record request
    private final String VALID_DVSA_RECORD_PDF_REQUEST_XML = BASE_PATH + "valid/ValidDvsaRecordPdfRequest.xml";
    private final String VALID_DVSA_RECORD_OPERATOR_XML_PATH = BASE_PATH + "valid/ValidTimetableOperatorXml.xml";
    private final String VALID_DVSA_RECORD_OPERATOR_XML_KEY = "ValidDvsaRecordOperatorXml.xml";
    // Valid large file test
    private final String VALID_LARGE_FILE_SIZE_TIMETABLE_PDF_REQUEST_XML = BASE_PATH + "valid/ValidLargeFileSizeTimetablePdfRequest.xml";
    private final String VALID_LARGE_FILE_SIZE_DVSA_RECORD_PDF_REQUEST_XML = BASE_PATH + "valid/ValidLargeFileSizeDvsaRecordPdfRequest.xml";
    private final String VALID_LARGE_FILE_SIZE_OPERATOR_XML_KEY = "ValidLargeFileSizeOperatorXml.xml";
    private final String VALID_LARGE_FILE_SIZE_SOURCE_KEY = "example-test-files/PD0001111-2.4-10.9Mb-variation-normalstopping-fullnotice/1_NXB_PD_1_20230416.xml";
    // Valid fileNotFound request
    private final String VALID_FILE_NOT_FOUND_PDF_REQUEST_XML = BASE_PATH + "valid/ValidFileNotFoundPdfRequest.xml";
    // Invalid missing DocumentName
    private final String INVALID_MISSING_DOCUMENT_NAME_PDF_REQUEST_XML = BASE_PATH + "invalid/InvalidMissingDocumentNamePdfRequest.xml";
    private final String INVALID_MISSING_DOCUMENT_NAME_PDF_RESPONSE_XML = BASE_PATH + "invalid/InvalidMissingDocumentNamePdfResponse.xml";
    // Invalid missingOperator
    private final String INVALID_MISSING_OPERATORS_PDF_REQUEST_XML = BASE_PATH + "invalid/InvalidOperatorXmlMissingOperatorsPdfRequest.xml";
    private final String INVALID_MISSING_OPERATORS_OPERATOR_XML_PATH = BASE_PATH + "invalid/InvalidOperatorXmlMissingOperators.xml";
    private final String INVALID_MISSING_OPERATORS_OPERATOR_XML_KEY = "InvalidOperatorXmlMissingOperators.xml";

    private final World world;
    private String responseBodyText;
    private String pdfFilename;

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

    public int sendValidPdfRequest(String type) throws Exception {
        String requestXmlPath;
        switch (type) {
            case "timetable":
                requestXmlPath = VALID_TIMETABLE_PDF_REQUEST_XML;
                break;
            case "dvsaRecord":
                requestXmlPath = VALID_DVSA_RECORD_PDF_REQUEST_XML;
                break;
            case "largeFileSizeTimetable":
                requestXmlPath = VALID_LARGE_FILE_SIZE_TIMETABLE_PDF_REQUEST_XML;
                break;
            case "largeFileSizeDvsaRecord":
                requestXmlPath = VALID_LARGE_FILE_SIZE_DVSA_RECORD_PDF_REQUEST_XML;
                break;
            case "fileNotFound":
                requestXmlPath = VALID_FILE_NOT_FOUND_PDF_REQUEST_XML;
                break;
            case "missingOperators":
                requestXmlPath = INVALID_MISSING_OPERATORS_PDF_REQUEST_XML;
                break;
            default:
                throw new IllegalArgumentException("[" + type + "] is an invalid pdf request type");
        }

        ClassicHttpResponse response = sendPdfRequest(requestXmlPath);

        // Test the response is what we expect
        String xmlResponse = getFileStringUsingFilePath(VALID_XML_RESPONSE_PATH);
        assertEquals(xmlResponse, responseBodyText);
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

    public int sendInvalidXmlRequest(String problem) throws Exception {
        String xml;
        String xmlResponse;
        switch (problem) {
            case "missingDocumentName":
                xml = getFileStringUsingFilePath(INVALID_MISSING_DOCUMENT_NAME_PDF_REQUEST_XML);
                xmlResponse = getFileStringUsingFilePath(INVALID_MISSING_DOCUMENT_NAME_PDF_RESPONSE_XML);
                break;
            case "rootElementOnly":
                xml = getFileStringUsingFilePath(INVALID_XML_PATH);
                xmlResponse = getFileStringUsingFilePath(INVALID_XML_RESPONSE_PATH);
                break;
            default:
                throw new IllegalArgumentException("[" + problem + "] type is not valid");
        }
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
        assertEquals(xmlResponse, responseBodyText);
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
        String sourceBucket = world.configuration.config.getString("exampleFileBucket");
        switch (type) {
            case "timetable":
                world.awsHelper.addFileToBucket(bucketName, VALID_TIMETABLE_OPERATOR_XML_KEY, VALID_TIMETABLE_OPERATOR_XML_PATH);
                break;
            case "dvsaRecord":
                world.awsHelper.addFileToBucket(bucketName, VALID_DVSA_RECORD_OPERATOR_XML_KEY, VALID_DVSA_RECORD_OPERATOR_XML_PATH);
                break;
            case "largeFileSizeOperatorXml":
                world.awsHelper.copyFileFromOneBucketToAnother(sourceBucket, VALID_LARGE_FILE_SIZE_SOURCE_KEY, bucketName, VALID_LARGE_FILE_SIZE_OPERATOR_XML_KEY);
                break;
            default:
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

    /**
     * Asserts a file exists in the bucket defined by the "pdfOutputBucket" config item by failing the test if it's
     * missing.
     * Requires the pdfFilename variable is set in this class
     */
    public void verifyFileExistsInOutputBucket(){
        if (pdfFilename == null) {
            throw new IllegalArgumentException("pdfFilename hasn't been set");
        }
        String bucketName = world.configuration.config.getString("pdfOutputBucket");
        try {
            world.awsHelper.getObjectFromBucket(bucketName, pdfFilename);
        } catch (AmazonS3Exception e){
            fail("File [" + pdfFilename + "] doesn't exist in the bucket");
        }
    }


    public void getMessagesFromSqs(String problem){
        String expectedElement;
        if (problem.equals("missingOperators")){
            expectedElement = "<BadRequest>";
        }
        else if (problem.equals("fileNotFound")){
            expectedElement = "<Failed>";
        }
        else {
            throw new IllegalArgumentException("[" + problem + "] problem is not valid");
        }
        String queueUrl = world.configuration.config.getString("fileProcessedOutputQueueUrl");
        List<Message> sqsMessages = world.awsHelper.getMessageFromSqs(queueUrl);
        String messageBody = sqsMessages.get(0).getBody();

        assertTrue(messageBody.contains(expectedElement));
    }

    public void getFilenameFromSuccessfulPdfGenerationMessage() {
        String queueUrl = world.configuration.config.getString("fileProcessedOutputQueueUrl");
        List<Message> sqsMessages = world.awsHelper.getMessageFromSqs(queueUrl);
        String messageBody = sqsMessages.get(0).getBody();
        Document doc = Jsoup.parse(messageBody, "", Parser.xmlParser());
        pdfFilename = doc.getElementsByTag("OutputFile").get(0).text();
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
        LOGGER.info("Id for this request is [" + id + "]");
        request.setHeader("X-Correlation-Id", id);
        request.setHeader("Content-Type", "application/xml");
        request.setHeader("Cache-Control", "no-cache");
        return request;
    }

    private String getFileStringUsingFilePath(String path) throws IOException {
        File xmlResponseFile = new File(path);
        return new String(Files.readAllBytes(xmlResponseFile.toPath()));
    }

    public void deleteGeneratedFileFromOutputBucket() {
        if (pdfFilename == null) {
            throw new IllegalArgumentException("pdfFilename hasn't been set");
        }
        String bucketName = world.configuration.config.getString("pdfOutputBucket");
        world.awsHelper.deleteObjectFromBucket(bucketName, pdfFilename);
        LOGGER.info("Successfully deleted [" + pdfFilename + "]");
    }
}
