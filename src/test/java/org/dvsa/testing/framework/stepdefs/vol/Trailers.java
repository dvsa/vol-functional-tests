package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.VehicleType;
import cucumber.api.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

public class Trailers extends BasePage {
    public World world;
    public Trailers(World world) {
        this.world = world;
    }
    private static final Logger LOGGER = LogManager.getLogger(BasePage.class);

    @Given("I add a valid trailer number {string} and longer semi trailer is set to {string} on the licence")
    public void addATrailer(String trailerNumber, String isLongerSemiTrailer) {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);
        world.trailersJourney.addTrailerToLicence(trailerNumber, isLongerSemiTrailer);
        //String Selector = "//input[@name='data[longerSemiTrailer][isLongerSemiTrailer]']";
        //untilElementIsPresent(Selector, SelectorType.XPATH, Duration.MEDIUM, TimeUnit.SECONDS);
        //clickByXPath(Selector + "[@value='Y']");
        //scrollAndClick(Selector,SelectorType.XPATH);
    }

    @When("the trailer {string} is successfully added to the trailer table")
    public void trailerAddedCheck(String trailerNumber) {
        assertTrue(isElementPresent("//tbody/tr/td/input[@value='" + trailerNumber + "']", SelectorType.XPATH));

        //List<WebElement> trs = findElements("//tbody/tr/td", SelectorType.XPATH);
        //for (int i = 0; i < trs.size(); i++) {
        //    LOGGER.info("Each open tab ID : " + trs.get(i).getText());
        //}
        //List<WebElement> trs1 = findElements("//tbody/tr/td[@data-heading='Trailer number']", SelectorType.XPATH);
        //String text = getText("//tbody/tr/td[@data-heading='Trailer number']",SelectorType.XPATH);
        //String text1 = getText("//tbody/tr/td[1][@data-heading='Trailer number']",SelectorType.XPATH);
        //String text2 = getText("//tbody/tr/td[1]/input",SelectorType.XPATH);
        //String text3 = getText("//tbody/tr/td[0][@data-heading='Trailer number']",SelectorType.XPATH);
        //String text4 = getText("/input[@type='submit']",SelectorType.XPATH);

        //Assert.assertEquals(getElementValueByText("//tbody/tr/td[@data-heading='Country']",SelectorType.XPATH), AnnualBilateralJourney.getCountry());
        //Assert.assertEquals(trailerNumber, getElementValueByText("//tbody/tr/td[@data-heading='Trailer number']", SelectorType.XPATH));
        UIJourney.clickSaveAndReturn();
    }

}
