package org.dvsa.testing.framework.stepdefs.TransXchange;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.stepdefs.vol.Initialisation;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransXchange extends BasePage {
    private final World world;
    Initialisation initialisation;
    private int responseCode;

    public TransXchange(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("I generate an OAuth token")
    public void iGenerateAnOAuthToken() throws OAuthProblemException, OAuthSystemException {
        world.TransXchangeJourney.getAuthToken();
    }


    @When("I upload valid {string} operator xml into the bucket")
    public void iUploadValidOperatorXmlIntoTheBucket(String type) {
        world.TransXchangeJourney.inputValidOperatorXml(type);
    }

    @When("I upload invalid {string} operator xml into the bucket")
    public void iUploadInvalidOperatorXmlIntoTheBucket(String type) {
        world.TransXchangeJourney.inputInvalidOperatorXml(type);
    }

    @Then("I read the file from the bucket")
    public void iReadTheFileFromTheBucket() throws IOException {
        world.TransXchangeJourney.readFileFromBucket();
    }

    @Then("I read a message off the queue and verify it looks right for the {string}")
    public void iReadAMessageFromOffTheQueue(String problem){
        world.TransXchangeJourney.getMessagesFromSqs(problem);
    }

    @When("I send a POST request to the API gateway with valid XML")
    public void iSendAPOSTRequestToTheApiGatewayWithValidXml() throws Exception {
        this.responseCode = world.TransXchangeJourney.sendValidXmlRequest();
    }

    @When("I send a POST request to the API gateway with valid XML for missing operators")
    public void iSendAPOSTRequestToTheApiGatewayWithValidXmlForMissingOperators() throws Exception {
        this.responseCode = world.TransXchangeJourney.sendMissingOperatorsValidXmlRequest();
    }

    @When("I send a POST request to the API gateway with valid {string} XML")
    public void iSendAPostRequestToTheApiGatewayWithValidTimetableXml(String type) throws Exception {
        this.responseCode = world.TransXchangeJourney.sendValidPdfRequest(type);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode){
        assertEquals(statusCode, responseCode);
    }

    @When("I send a POST request to the API gateway with invalid XML")
    public void iSendAPOSTRequestToTheApiGatewayWithTheInvalidXml() throws Exception {
        this.responseCode = world.TransXchangeJourney.sendInvalidXmlRequest();
    }

    @Given("I do not generate an OAuth token")
    public void iDoNotGenerateAnOAuthToken() {
        //No implementation
    }

    @When("I send an unauthorised POST request to the API gateway with any XML")
    public void iSendAnUnauthorisedPOSTRequestToTheApiGatewayWithAnyXml() throws Exception {
        this.responseCode = world.TransXchangeJourney.sendUnauthorisedRequest();
    }

    @Then("I clean up all the data in the input bucket")
    public void iCleanUpAllTheDataInTheInputBucket() {
        world.TransXchangeJourney.cleanInputBucket();
    }
}