package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Trailers extends BasePage {
    public World world;
    public Trailers(World world) {
        this.world = world;
    }
    private static final Logger LOGGER = LogManager.getLogger(BasePage.class);
    String addTrailerButton = "//*[@id='add']";

    @Given("on self serve I add a valid trailer number {string} and longer semi trailer is set to {string} on the licence")
    public void addATrailerToSelfServe(String trailerNumber, String isLongerSemiTrailer) {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence(trailerNumber);
        world.trailersJourney.isLongerSemiTrailer(isLongerSemiTrailer);
        UniversalActions.clickSubmit();
    }

    @Given("on internal I add a valid trailer number {string} and longer semi trailer is set to {string} on the licence")
    public void addATrailerToInternal(String trailerNumber, String isLongerSemiTrailer) throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence(trailerNumber);
        world.trailersJourney.isLongerSemiTrailer(isLongerSemiTrailer);
        UniversalActions.clickSubmit();
    }

    @When("the trailer {string} and type {string} is successfully added to the trailer table")
    public void trailerAddedCheck(String trailerNumber, String isLongerSemiTrailer) {
        assertTrue(isElementPresent("//tbody/tr/td/input[@value='" + trailerNumber + "']", SelectorType.XPATH));
        assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Longer semi-trailer']",SelectorType.XPATH), isLongerSemiTrailer);
        UniversalActions.clickSaveAndReturn();
    }

    @When("trailer number {string} is changed to longer semi trailer {string}")
    public void changeTrailerType(String trailerNumber, String isLongerSemiTrailer) {
        clickByXPath("//tbody/tr/td/input[@value='" + trailerNumber + "']");
        world.trailersJourney.isLongerSemiTrailer(isLongerSemiTrailer);
        UniversalActions.clickSubmit();
    }

    @When("on self serve I add a trailer with no trailer number")
    public void trailerWithNoNumberOnSelfServe() {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.isLongerSemiTrailer("Yes");
        UniversalActions.clickSubmit();
    }

    @When("on internal I add a trailer with no trailer number")
    public void trailerWithNoNumberOnInteral() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.isLongerSemiTrailer("Yes");
        UniversalActions.clickSubmit();
    }

    @When("on self serve I add a trailer with the longer semi trailer option unanswered")
    public void trailerWithNoLongerSemiTrailerOnSelfServe() {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence("GHTU775");
        UniversalActions.clickSubmit();
    }

    @When("on internal I add a trailer with the longer semi trailer option unanswered")
    public void trailerWithNoLongerSemiTrailerOnInternal() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence("GHTU775");
        UniversalActions.clickSubmit();
    }

    @Then("the trailer number mandatory error message appears")
    public void trailerNumberMandatoryErrorAppears() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        waitForTextToBePresent("There is a problem");
        assertTrue(isElementPresent("//a[contains(text(),'Enter a value for the field: \"Trailer number\"')]", SelectorType.XPATH));
    }

    @Then("the is longer semi trailer mandatory error message appears")
    public void longerSemiTrailerMandatoryErrorAppears() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        waitForTextToBePresent("There is a problem");
        assertTrue(isElementPresent("//a[contains(text(),'Select yes if this trailer is a longer semi-trailer')]", SelectorType.XPATH));
    }
}
