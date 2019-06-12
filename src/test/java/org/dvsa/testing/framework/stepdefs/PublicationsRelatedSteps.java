package org.dvsa.testing.framework.stepdefs;

import activesupport.driver.Browser;
import activesupport.number.Int;
import cucumber.api.java.eo.Se;
import cucumber.api.java8.En;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublicationsRelatedSteps extends BasePage implements En {
    public PublicationsRelatedSteps() {
        And("^i navigate to the admin publications page$", () -> {
            click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
            click("//*[@id='menu-admin-dashboard/admin-publication']", SelectorType.XPATH);
        });
        And("^i generate and publish all \"([^\"]*)\" publications$", (Integer noOfDifferentLicences) -> {
            String currentPubNo;
            String linkedPubNo;
            String publishedDate;
            for (int i = 0; i < noOfDifferentLicences; i++) {
                // Pending Page
                waitAndClick("//*[text()='50']", SelectorType.XPATH);


                List<WebElement> radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                List<WebElement> publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr[*]/td[2]"));
                currentPubNo = publicationNumbers.get(i).getText();

                if (Browser.getDriver().findElements(By.linkText(currentPubNo)).size() == 0) {
                    radioButtons.get(i).click();
                    waitAndClick("//*[@id='generate']", SelectorType.XPATH);
                    waitForTextToBePresent("Publication was generated, a new publication was also created");
                    waitAndClick("//*[text()='50']", SelectorType.XPATH);

                    radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                    publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));
                    List<WebElement> publicationDates = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[5]"));

                    assertTrue(publicationNumbers.get(i + 1).isEnabled());
                    linkedPubNo = publicationNumbers.get(i + 1).getText();
                    publishedDate = publicationDates.get(i + 1).getText();
                    radioButtons.get(i + 1).click();

                    waitAndClick("//*[@id='publish']", SelectorType.XPATH);
                    waitForTextToBePresent("Update successful");

                    // Published Page
                    waitAndClick("//*[@id='menu-admin-dashboard/admin-publication/published']", SelectorType.XPATH);

                    String[] dateArray = publishedDate.split("/");
                    String month = returnMonth(dateArray[1]);
                    String year = dateArray[2];

                    selectValueFromDropDown("//*[@name='pubDate[month]']",SelectorType.XPATH,month);
                    selectValueFromDropDown("//*[@name='pubDate[year]']",SelectorType.XPATH,year);
                    click("//*[@id='filter']",SelectorType.XPATH);

                    // Increasing table if possible
                    if (Browser.getDriver().findElements(By.linkText("25")).size() != 0) {
                        clickByLinkText("25");
                    }
                    if (Browser.getDriver().findElements(By.linkText("50")).size() != 0) {
                        clickByLinkText("50");
                    }
                    int pageNumber = 1;
                    boolean kickout = true;
                    //Start looping over pages here
                    do {
                        // Storing numbers
                        publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));

                        // Changing into an array with the element's text
                        List<String> textList = new ArrayList<>(publicationNumbers.size());
                        for (WebElement elements : publicationNumbers) {
                            textList.add(elements.getText());
                        }

                        if (textList.contains(linkedPubNo)) {
                            assertTrue(Browser.getDriver().findElements(By.linkText(linkedPubNo)).size() != 0);
                            kickout = false;
                        } else {
                            pageNumber++;
                            clickByLinkText(Integer.toString(pageNumber));
                        }
                    } while(kickout);


                    click("//*[@id='menu-admin-dashboard/admin-publication/pending']", SelectorType.XPATH);
                    waitForTextToBePresent("Publications");
                } else {
                    i++;
                    noOfDifferentLicences++;
                }
            }
        });
    }

    public static String returnMonth(String monthNumber) {
        String monthName;
        switch (monthNumber) {
        case "01":
            monthName = "January";
        case "02":
            monthName = "February";
        case "03":
            monthName = "March";
        case "04":
            monthName = "April";
        case "05":
            monthName = "May";
        case "06":
            monthName = "June";
        case "07":
            monthName = "July";
        case "08":
            monthName = "August";
        case "09":
            monthName = "September";
        case "10":
            monthName = "October";
        case "11":
            monthName = "November";
        case "12":
            monthName = "December";
        default:
            monthName = "Date Not Found";
            return monthName;
        }
    }
}
