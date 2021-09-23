package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import apiCalls.enums.OperatorType;
import apiCalls.enums.TrafficArea;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublicationsRelatedSteps extends BasePage implements En {

    public World world;

    public PublicationsRelatedSteps(World world) {
        this.world = world;
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

                publicationNumbers = show50ResultsAndUpdateWebElementsList("//table/tbody/tr[*]/td[2]");
                    currentPubNo = publicationNumbers.get(i).getText();

                if (Browser.navigate().findElements(By.linkText(currentPubNo)).size() == 0 || Browser.navigate().findElements(By.xpath(String.format("//*[contains(text(),%s)]",currentPubNo))).size()>1) {

                    radioButtons = Browser.navigate().findElements(By.xpath("//*[@type='radio']"));
                    radioButtons.get(i).click();

                    waitAndClick("//*[@id='generate']", SelectorType.XPATH);
                    waitForTextToBePresent("Publication was generated, a new publication was also created");

                    radioButtons = show50ResultsAndUpdateWebElementsList("//*[@type='radio']");
                    List<WebElement> publicationDates = Browser.navigate().findElements(By.xpath("//table/tbody/tr/td[5]"));

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
                    publicationNumbers = show50ResultsAndUpdateWebElementsList("//table/tbody/tr/td[2]");
                    int pageNumber = 1;
                    boolean kickOut = true;
                    //Start looping over pages here
                    while (kickOut) {
                        // Storing numbers
                        publicationNumbers = Browser.navigate().findElements(By.xpath("//table/tbody/tr/td[2]"));

                        // Changing into an array with the element's text
                        List<String> textList = new ArrayList<>(publicationNumbers.size());
                        for (WebElement elements : publicationNumbers) {
                            textList.add(elements.getText());
                        }

                        if (textList.contains(currentPubNo)) {
                            assertTrue(Browser.navigate().findElements(By.linkText(currentPubNo)).size() != 0);
                            kickOut = false;
                        } else {
                            pageNumber++;
                            clickByLinkText(Integer.toString(pageNumber));
                        }
                    }
                    click("//*[@id='menu-admin-dashboard/admin-publication/pending']", SelectorType.XPATH);
                    waitForTextToBePresent("Generate");
                } else {
                    noOfDifferentLicences++;
                    refreshPageWithJavascript();
                }
            }
        });
        When("^i generate \"([^\"]*)\" publications and check their docman link$", (Integer noOfDifferentLicences) -> {
            String currentPubNo;
            String linkedPubNo;
            String publishedDate;
            int missingLinks = 0;
            for (int i = 0; i < noOfDifferentLicences; i++) {

                List<WebElement> radioButtons;
                List<WebElement> publicationNumbers;

                publicationNumbers = show50ResultsAndUpdateWebElementsList("//table/tbody/tr[*]/td[2]");
                currentPubNo = publicationNumbers.get(i).getText();

                if (Browser.navigate().findElements(By.linkText(currentPubNo)).size() == 0 || Browser.navigate().findElements(By.xpath(String.format("//*[contains(text(),%s)]",currentPubNo))).size()>1) {

                    radioButtons = Browser.navigate().findElements(By.xpath("//*[@type='radio']"));
                    radioButtons.get(i).click();

                    waitAndClick("//*[@id='generate']", SelectorType.XPATH);
                    waitForTextToBePresent("Publication was generated, a new publication was also created");

                    radioButtons = show50ResultsAndUpdateWebElementsList("//*[@type='radio']");

                    clickByLinkText(currentPubNo);
                    waitForTextToBePresent("Open document");

                    if (getText("//*[@id='letter-link']",SelectorType.XPATH).isEmpty()){
                        missingLinks++;
                    }

                    click("//*[contains(text(),'Close')]",SelectorType.XPATH);

                    waitForElementToBeClickable("//*[@type='radio']",SelectorType.XPATH);
                    radioButtons.get(i + 1).click();
                    waitAndClick("//*[@id='publish']", SelectorType.XPATH);
                    waitForTextToBePresent("Update successful");

                } else {
                    noOfDifferentLicences++;
                    refreshPageWithJavascript();
                }
            }
            Assert.assertEquals(0, missingLinks);
        });
        Then("^the corresponding publication is generated and published$", () -> {
            world.internalNavigation.logInAndNavigateToAdminProcessing();
            String trafficArea = getTrafficArea(world.createApplication.getTrafficArea());
            String documentType = world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) ? "A&D" : "N&P";
            String selector = String.format("//tr//td[contains(text(),'%s')]/../td[contains(text(),'%s')]/../td/label/input", trafficArea, documentType);
            String publicationValue = getAttribute(selector, SelectorType.XPATH, "value");
            click(selector, SelectorType.XPATH);
            click("generate", SelectorType.ID);
            waitForTextToBePresent("Publication was generated, a new publication was also created");
            click(String.format("//tr/td/label/input[@value='%s']", publicationValue), SelectorType.XPATH);
            click("publish", SelectorType.ID);
            waitForTextToBePresent("Update successful");
        });

        Then("^the publication is visible via self serve search$", () -> {
            world.selfServeNavigation.navigateToVehicleOperatorDecisionsAndApplications();
            enterText("search", SelectorType.ID, world.applicationDetails.getLicenceNumber());
            click("submit", SelectorType.ID);
            WebElement publicationResult = findElement(String.format("//li[div/h4/a[contains(text(),'%s')] and div[3]/p[contains(text(),'New Variation')]]", world.applicationDetails.getLicenceNumber()), SelectorType.XPATH);
            String expectedText = String.format("%s, %s, %s, %s, %s, %s\n",
                    world.createApplication.getCorrespondenceAddressLine1(),
                    world.createApplication.getCorrespondenceAddressLine2(),
                    world.createApplication.getCorrespondenceAddressLine3(),
                    world.createApplication.getCorrespondenceAddressLine4(),
                    world.createApplication.getCorrespondenceTown(),
                    world.createApplication.getCorrespondencePostCode())
                    .concat("Light goods vehicles authorised on the licence. New authorisation will be 5 vehicle(s)");
            assertTrue(publicationResult.findElement(By.xpath("/div[2]/p[3]")).getText().contains(expectedText));
            clickByLinkText(world.applicationDetails.getLicenceNumber());

        });
    }


    public List<WebElement> show50ResultsAndUpdateWebElementsList(String webElementsXpath)  {
        List<WebElement> webElements = Browser.navigate().findElements(By.xpath(webElementsXpath));
        if (Browser.navigate().findElements(By.xpath("//li//a[contains(text(),'50')]")).size() != 0) {
            click("//li//a[contains(text(),'50')]", SelectorType.XPATH);
            webElements = Browser.navigate().findElements(By.xpath(webElementsXpath));
            while (webElements.size()<=10) {
                webElements = Browser.navigate().findElements(By.xpath(webElementsXpath));
            }
        }
        return webElements;
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

    public static String getTrafficArea(TrafficArea trafficArea) {
        String trafficAreaString;
        switch (trafficArea) {
            case NORTH_EAST:
                trafficAreaString = "North East of England";
                break;
            case NORTH_WEST:
            trafficAreaString = "North West of England";
                break;
            case MIDLANDS:
            trafficAreaString = "West Midlands";
                break;
            case EAST:
            trafficAreaString = "East of England";
                break;
            case WALES:
            trafficAreaString = "Wales";
                break;
            case WEST:
            trafficAreaString = "West of England";
                break;
            case LONDON:
            trafficAreaString = "London and the South East of England";
                break;
            case SCOTLAND:
            trafficAreaString = "Scotland";
                break;
            case NORTHERN_IRELAND:
            trafficAreaString = "Northern Ireland";
                break;
            default:
            throw new IllegalStateException("Unexpected value: " + trafficArea);
        }
        return trafficAreaString;
    }
}
