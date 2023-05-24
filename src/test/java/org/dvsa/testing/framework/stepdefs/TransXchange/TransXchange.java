package org.dvsa.testing.framework.stepdefs.TransXchange;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.stepdefs.vol.Initialisation;

import static org.junit.Assert.assertEquals;


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

    @When("I send a POST request to the API gateway with valid XML")
    public void iSendAPOSTRequestToTheApiGatewayWithValidXml() throws Exception {
        this.responseCode = world.TransXchangeJourney.sendValidXmlRequest();
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) throws Exception {
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
}