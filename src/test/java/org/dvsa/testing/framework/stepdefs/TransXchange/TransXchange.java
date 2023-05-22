package org.dvsa.testing.framework.stepdefs.TransXchange;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.jsonwebtoken.lang.Assert;
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
import org.dvsa.testing.framework.stepdefs.vol.Initialisation;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.Assert.*;


public class TransXchange  extends BasePage {
    private final World world;
    private String token;
    private URL url;
    private HttpURLConnection connection;
    private int responseCode;
    private String responseMessage;
    private StringBuilder response;

    Initialisation initialisation;

    public TransXchange(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("I generate an OAuth token")
    public void iGenerateAnOAuthToken() throws OAuthProblemException, OAuthSystemException {
        world.TransXchangeJourney.getAuthToken();
    }

    @When("I send a POST request to end point {string} with the valid XML{string}")
    public void iSendAPOSTRequestToEndPointURLWithTheValidXMLXml(String URL, String XML) throws Exception {
        world.TransXchangeJourney.sendValidXmlRequest(URL, XML);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) throws Exception {

        assertEquals(200, responseCode);
    }

    @When("I send a POST Invalid request to end point {string} with the valid XML{string}")
    public void iSendAPOSTInvalidRequestToEndPointURLWithTheValidXMLXml(String URL, String XML) throws Exception {
        world.TransXchangeJourney.sendInValidXmlRequest(URL, XML);
    }

    @Then("the bad request response status code should be {int}")
    public void theBadRequestResponseStatusCodeShouldBe(int statusCode) throws Exception {

        assertEquals(400, responseCode);
    }

    @Given("I do not generate an OAuth token")
    public void iDoNotGenerateAnOAuthToken() {
        //No implementation
    }


    @When("I send a unauthorised POST request to end point {string} with the any XML{string}")
    public void iSendAUnauthorisedPOSTRequestToEndPointURLWithTheAnyXMLXml(String URL, String XML) throws Exception {
        world.TransXchangeJourney.sendUnauthorisedRequest(URL, XML);
    }

    @Then("the unauthorised response status code should be {int}")
    public void theUnauthorisedResponseStatusCodeShouldBe(int statusCode) throws Exception {

        assertEquals(401, responseCode);
    }

    @When("I send a unsecured POST request to end point {string} with the any XML{string}")
    public void iSendAUnsecuredPOSTRequestToEndPointURLWithTheAnyXMLXml(String URL, String XML) throws Exception {
        world.TransXchangeJourney.sendUnsecuredRequest(URL, XML);
    }

    @Then("the connection is REFUSED.")
    public void theConnectionIsREFUSED() {
        assertEquals("REFUSED", responseCode);
    }
}