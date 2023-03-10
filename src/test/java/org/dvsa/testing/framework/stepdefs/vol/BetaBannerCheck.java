package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.support.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BetaBannerCheck extends BasePage {
private final World world;

    public BetaBannerCheck(World world) {this.world = world;}

        @Given("^I am on Selfserve homepage$")
         public void iAmOnSelfServeHomepage() {
            String env = System.getProperty("env");
            String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();
            Browser.navigate().get(myURL);
        }

        @Then("^banner colour is blue")
        public void bannerColourIsBlue() {
        String bannerColour = findElement("//*[@class='phase__tag']", SelectorType.XPATH).getCssValue("background-color");
        String hex = Color.fromString(bannerColour).asHex();
        assertEquals("#005ea5", hex);
        }
    }
