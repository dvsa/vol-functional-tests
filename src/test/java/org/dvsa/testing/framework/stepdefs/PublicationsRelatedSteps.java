package org.dvsa.testing.framework.stepdefs;

import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import activesupport.number.Int;
import cucumber.api.java.eo.Se;
import cucumber.api.java8.En;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
                List<WebElement> radioButtons;
                List<WebElement> publicationNumbers;

                if (Browser.getDriver().findElements(By.xpath("//*/a[text()=50]")).size() != 0) {
                    click("//*/a[text()=50]",SelectorType.XPATH);
                    radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                    while (radioButtons.size()<=10) {
                        radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                    }
                }
                    publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr[*]/td[2]"));
                    currentPubNo = publicationNumbers.get(i).getText();

                if (Browser.getDriver().findElements(By.linkText(currentPubNo)).size() == 0) {

                    radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                    radioButtons.get(i).click();

                    waitAndClick("//*[@id='generate']", SelectorType.XPATH);
                    waitForTextToBePresent("Publication was generated, a new publication was also created");

                    if (Browser.getDriver().findElements(By.xpath("//*/a[text()=50]")).size() != 0) {
                        click("//*/a[text()=50]",SelectorType.XPATH);
                        radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                        while (radioButtons.size()<=10) {
                            radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                        }
                    }
                    radioButtons = Browser.getDriver().findElements(By.xpath("//*[@type='radio']"));
                    publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));
                    List<WebElement> publicationDates = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[5]"));

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
                    if (Browser.getDriver().findElements(By.xpath("//*/a[text()=50]")).size() != 0) {
                        click("//*/a[text()=50]",SelectorType.XPATH);
                        publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));
                        while (publicationNumbers.size()<=10) {
                            publicationNumbers = Browser.getDriver().findElements(By.xpath("//table/tbody/tr/td[2]"));
                        }
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
                            try {
                                clickByLinkText(Integer.toString(pageNumber));
                            } catch (IllegalBrowserException var4) {
                                var4.printStackTrace();
                                System.out.println("Publication not found. Something has gone wrong.");
                            }
                        }
                    } while(kickout);

                    click("//*[@id='menu-admin-dashboard/admin-publication/pending']", SelectorType.XPATH);
                    waitForTextToBePresent("Generate");
                } else {
                    noOfDifferentLicences++;
                    javaScriptExecutor("location.reload(true)");
                }
            }
        });
    }

    public static String returnMonth(String monthNumber) {
        String monthName;
        switch (monthNumber) {
        case "01":
            monthName = "January";
            break;
        case "02":
            monthName = "February";
            break;
        case "03":
            monthName = "March";
            break;
        case "04":
            monthName = "April";
            break;
        case "05":
            monthName = "May";
            break;
        case "06":
            monthName = "June";
            break;
        case "07":
            monthName = "July";
            break;
        case "08":
            monthName = "August";
            break;
        case "09":
            monthName = "September";
            break;
        case "10":
            monthName = "October";
            break;
        case "11":
            monthName = "November";
            break;
        case "12":
            monthName = "December";
            break;
        default:
            monthName = "Date Not Found";
        }
        return monthName;
    }
}
