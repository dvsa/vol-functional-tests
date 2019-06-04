package org.dvsa.testing.framework.stepdefs;

import activesupport.driver.Browser;
import cucumber.api.java.eo.Se;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PublicationsRelatedSteps extends BasePage implements En {
    public PublicationsRelatedSteps() {
        And("^i navigate to the admin publications page$", () -> {
            click("//*[contains(text(),'Admin')]",SelectorType.XPATH);
            click("//*[@id='menu-admin-dashboard/admin-publication']",SelectorType.XPATH);
        });
        And("^i generate and publish all \"([^\"]*)\" publications$", (String cap) -> {
            List<WebElement> radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
            List<WebElement> publicationNumbers;
            for (int i=0; i< Integer.parseInt(cap); i++) {
]               waitAndClick("//*[text()='50']", SelectorType.XPATH);
                waitForElementToBeClickable(String.valueOf(radioButtons.get(i)),SelectorType.XPATH);

                click(String.valueOf(radioButtons.get(i)),SelectorType.XPATH);
                waitAndClick("//*[@id='generate']",SelectorType.XPATH);

                waitAndClick("//*[text()='50']", SelectorType.XPATH);
                waitForElementToBeClickable(String.valueOf(radioButtons.get(i)),SelectorType.XPATH);

                radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));

                assertTrue(isElementEnabled(String.valueOf(publicationNumbers.get(i+1)),SelectorType.XPATH));
                click(String.valueOf(radioButtons.get(i+1)),SelectorType.XPATH);


                waitAndClick(String.valueOf(radioButtons.get(i)),SelectorType.XPATH);

            }
        });
    }
}
