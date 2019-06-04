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
            waitAndClick("//*[text()='50']", SelectorType.XPATH);
            List<WebElement> radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
            Object[] radioButtonsArray = radioButtons.toArray();
            for (int i=0; i< Integer.parseInt(cap); i++) {
                waitAndClick("//*[text()='50']", SelectorType.XPATH);
                click(String.valueOf(radioButtons.get(i)),SelectorType.XPATH);
                waitAndClick("//*[@id='generate']",SelectorType.XPATH);
                
            }
        });
    }
}
