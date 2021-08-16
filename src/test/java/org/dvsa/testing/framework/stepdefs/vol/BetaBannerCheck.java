package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.driver.Browser;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;
import org.openqa.selenium.support.Color;

public class BetaBannerCheck extends BasePage implements En {

    public BetaBannerCheck() {

        Given("^I am on Selfserve homepage$", () -> {
            String env = System.getProperty("env");
            String myURL = URL.build(ApplicationType.EXTERNAL, env).toString();
            Browser.navigate().get(myURL);
        });

        Then("^banner colour is blue$", () -> {
            String bannerColour = findElement("//*[@class='phase__tag']", SelectorType.XPATH).getCssValue("background-color");
            String hex = Color.fromString(bannerColour).asHex();
            Assert.assertEquals("#005ea5", hex);
        });
    }
}