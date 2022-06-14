package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

import static junit.framework.TestCase.assertTrue;

public class Trailers extends BasePage {
    public World world;
    public Trailers(World world) {
        this.world = world;
    }
    private static final Logger LOGGER = LogManager.getLogger(BasePage.class);
    String submitButton = "//*[@id='form-actions[submit]']";
    String addTrailerButton = "//*[@id='add']";

    @Given("on self serve I add a valid trailer number {string} and longer semi trailer is set to {string} on the licence")
    public void addATrailerToSelfServe(String trailerNumber, String isLongerSemiTrailer) {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence(trailerNumber);
        world.trailersJourney.isLongerSemiTrailer(isLongerSemiTrailer);
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    @Given("on internal I add a valid trailer number {string} and longer semi trailer is set to {string} on the licence")
    public void addATrailerToInternal(String trailerNumber, String isLongerSemiTrailer) {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence(trailerNumber);
        world.trailersJourney.isLongerSemiTrailer(isLongerSemiTrailer);
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    @When("the trailer {string} and type {string} is successfully added to the trailer table")
    public void trailerAddedCheck(String trailerNumber, String isLongerSemiTrailer) {
        assertTrue(isElementPresent("//tbody/tr/td/input[@value='" + trailerNumber + "']", SelectorType.XPATH));
        Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Longer semi-trailer']",SelectorType.XPATH), isLongerSemiTrailer);
        UIJourney.clickSaveAndReturn();
    }

    @When("trailer number {string} is changed to longer semi trailer {string}")
    public void changeTrailerType(String trailerNumber, String isLongerSemiTrailer) {
        clickByXPath("//tbody/tr/td/input[@value='" + trailerNumber + "']");
        world.trailersJourney.isLongerSemiTrailer(isLongerSemiTrailer);
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    @When("on self serve I add a trailer with no trailer number")
    public void trailerWithNoNumberOnSelfServe() {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.isLongerSemiTrailer("Yes");
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    @When("on internal I add a trailer with no trailer number")
    public void trailerWithNoNumberOnInteral() {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.isLongerSemiTrailer("Yes");
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    @When("on self serve I add a trailer with the longer semi trailer option unanswered")
    public void trailerWithNoLongerSemiTrailerOnSelfServe() {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence("GHTU775");
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    @When("on internal I add a trailer with the longer semi trailer option unanswered")
    public void trailerWithNoLongerSemiTrailerOnInternal() {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.TRAILERS);
        click(addTrailerButton, SelectorType.XPATH);
        world.trailersJourney.addTrailerToLicence("GHTU775");
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    @Then("the trailer number mandatory error message appears")
    public void trailerNumberMandatoryErrorAppears() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        waitForTextToBePresent("There is a problem");
        Assert.assertTrue(isElementPresent("//a[contains(text(),'Enter a value for the field: \"Trailer number\"')]", SelectorType.XPATH));
    }

    @Then("the is longer semi trailer mandatory error message appears")
    public void longerSemiTrailerMandatoryErrorAppears() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        waitForTextToBePresent("There is a problem");
        Assert.assertTrue(isElementPresent("//a[contains(text(),'Select yes if this trailer is a longer semi-trailer')]", SelectorType.XPATH));
    }

}
