package org.dvsa.testing.framework.stepdefs;

import activesupport.driver.Browser;
import activesupport.number.Int;
import cucumber.api.java.eo.Se;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublicationsRelatedSteps extends BasePage implements En {
    public PublicationsRelatedSteps() {
        And("^i navigate to the admin publications page$", () -> {
            click("//*[contains(text(),'Admin')]",SelectorType.XPATH);
            click("//*[@id='menu-admin-dashboard/admin-publication']",SelectorType.XPATH);
        });
        And("^i generate and publish all \"([^\"]*)\" publications$", (Integer noOfDifferentLicences) -> {
            String currentPubNo;
            String linkedPubNo;
            for (int i=0; i< noOfDifferentLicences; i++) {
                waitAndClick("//*[text()='50']", SelectorType.XPATH);


                List<WebElement> radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                List<WebElement> publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr[*]/td[2]"));
                currentPubNo = publicationNumbers.get(i).getText();

                if (Browser.getDriver().findElements(By.linkText(currentPubNo)).size()==0) {
                    radioButtons.get(i).click();
                    waitAndClick("//*[@id='generate']", SelectorType.XPATH);
                    waitForTextToBePresent("Publication was generated, a new publication was also created");
                    waitAndClick("//*[text()='50']", SelectorType.XPATH);

                    radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                    publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));

                    assertTrue(publicationNumbers.get(i + 1).isEnabled());
                    linkedPubNo = publicationNumbers.get(i + 1).getText();
                    radioButtons.get(i + 1).click();

                    waitAndClick("//*[@id='publish']",SelectorType.XPATH);
                    waitForTextToBePresent("Update successful");
                    waitAndClick("//*[@id='menu-admin-dashboard/admin-publication/published']", SelectorType.XPATH);
                    publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));

                    List<String> textList;
                    for (WebElement elements:publicationNumbers) {
                        textList = elements.getText();
                    }


                    assertTrue(textList.contains(linkedPubNo));
                    for (WebElement x : publicationNumbers) {
                        if (publicationNumbers.toString().contains(linkedPubNo)) {
                            assertTrue(isElementEnabled(String.valueOf(x), SelectorType.XPATH));
                        }
                    }

                    click("//*[@id='menu-admin-dashboard/admin-publication/pending']", SelectorType.XPATH);
                    waitForTextToBePresent("Publications");
                }
                else {
                    i++;
                    noOfDifferentLicences++;
                }
            }
        });
    }
}
