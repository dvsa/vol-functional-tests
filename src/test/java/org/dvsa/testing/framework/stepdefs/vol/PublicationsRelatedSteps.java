package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.driver.Browser;
import apiCalls.enums.OperatorType;
import apiCalls.enums.VehicleType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.Generic.ParseUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.apache.maven.shared.utils.StringUtils.capitalise;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PublicationsRelatedSteps extends BasePage implements En {
    private final World world;

    public PublicationsRelatedSteps(World world) {
        this.world = world;
    }

    @And("i navigate to the admin publications page")
    public void iNavigateToTheAdminPublicationsPage() {
        click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
        click("//*[@id='menu-admin-dashboard/admin-publication']", SelectorType.XPATH);
    }

    @When("i generate {string} publications and check their docman link")
    public void iGeneratePublicationsAndCheckTheirDocmanLink(Integer noOfDifferentLicences) {
        String currentPubNo;
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
        assertEquals(0,missingLinks);
    }

    @And("i generate and publish all {int} publications")
    public void iGenerateAndPublishAllPublications(Integer noOfDifferentLicences) {
        String currentPubNo;
        String publishedDate;
        for (int i = 0; i < noOfDifferentLicences; i++) {
            // Pending Page
            List<WebElement> radioButtons;
            List<WebElement> publicationNumbers;

            publicationNumbers = show50ResultsAndUpdateWebElementsList("//table/tbody/tr[*]/td[2]");
            currentPubNo = publicationNumbers.get(i).getText();

            if (Browser.navigate().findElements(By.linkText(currentPubNo)).size() == 0 || Browser.navigate().findElements(By.xpath(String.format("//*[contains(text(),%s)]", currentPubNo))).size() > 1) {

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
                String month = ParseUtils.parseMonth(dateArray[1]);
                String year = dateArray[2];

                selectValueFromDropDown("//*[@name='pubDate[month]']", SelectorType.XPATH, month);
                selectValueFromDropDown("//*[@name='pubDate[year]']", SelectorType.XPATH, year);
                click("//*[@id='filter']", SelectorType.XPATH);

                // Increasing table if possible
                show50ResultsAndUpdateWebElementsList("//table/tbody/tr/td[2]");
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
    }

    @Then("the corresponding publication is generated and published")
    public void theCorrespondingPublicationIsGeneratedAndPublished() {
        world.internalNavigation.navigateToAdminPublication();
        click("//li/a[contains(text(),'25')]", SelectorType.XPATH);
        String trafficArea = ParseUtils.parseTrafficArea(world.createApplication.getTrafficArea());
        String documentType = world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) ? "A&D" : "N&P";
        String radioButton = String.format("//tr//td[contains(text(),'%s')]/../td[contains(text(),'%s')]/../td/label/input", trafficArea, documentType);
        String radioButtonValue = getAttribute(radioButton, SelectorType.XPATH, "value");
        click(radioButton, SelectorType.XPATH);
        click("generate", SelectorType.ID);
        waitForTextToBePresent("Publication was generated, a new publication was also created");
        click("//li/a[contains(text(),'25')]", SelectorType.XPATH);
        String matchingRadioButton = String.format("//tr/td/label/input[@value='%s']", radioButtonValue);
        click(matchingRadioButton, SelectorType.XPATH);
        click("publish", SelectorType.ID);
        waitForTextToBePresent("Update successful");
    };

    @Then("^the publication is visible via self serve search$")
    public void thePublicationIsVisibleViaSelfServeSearch() {
        String licenceNumber = world.applicationDetails.getLicenceNumber();
        world.selfServeNavigation.navigateToVehicleOperatorDecisionsAndApplications();
        enterText("search", SelectorType.ID, licenceNumber);
        world.selfServeNavigation.clickSearchWhileCheckingTextPresent(licenceNumber, 120, "New publication wasn't present. Possibly the backend didn't process in time. Please check your search value.");
        waitForElementToBeClickable(String.format("//a[contains(text(),%s)]", licenceNumber), SelectorType.XPATH);
    };


    @And("the {string} {string} publication text is correct with {string} hgvs and {string} lgvs")
    public void thePublicationTextIsCorrectWithHGVsAndLGVs(String publicationType, String variationType, String hgvs, String lgvs) {
        WebElement publicationResult = findElement(String.format("//li[div/h4/a[contains(text(),'%s')] and div[3]/p[contains(text(),'%s')]]/div[2]/p[3]", world.applicationDetails.getLicenceNumber(), publicationType), SelectorType.XPATH);
        String adaptiveVehicleTypeText = world.licenceCreation.isAGoodsInternationalLicence() ? "Heavy Goods Vehicle" : "vehicle";
        String correspondenceAddress = String.format("%s, %s, %s, %s, %s, %s ",
                world.createApplication.getCorrespondenceAddressLine1(),
                world.createApplication.getCorrespondenceAddressLine2(),
                world.createApplication.getCorrespondenceAddressLine3(),
                world.createApplication.getCorrespondenceAddressLine4(),
                world.createApplication.getCorrespondenceTown(),
                world.createApplication.getCorrespondencePostCode());

        StringBuilder expectedText = new StringBuilder("");

        if (variationType.contains("HGV")) {
            String operatingCentreAddress = String.format("%s, %s, %s, %s, %s, %s",
                    world.createApplication.getOperatingCentreAddressLine1(),
                    world.createApplication.getOperatingCentreAddressLine2(),
                    world.createApplication.getOperatingCentreAddressLine3(),
                    world.createApplication.getOperatingCentreAddressLine4(),
                    world.createApplication.getOperatingCentreTown(),
                    world.createApplication.getOperatingCentrePostCode());
            String hgvIncreaseText = String.format("Increase at existing operating centre: %s New authorisation at this operating centre will be: %s %s, %s trailer(s) ",
                    operatingCentreAddress,
                    hgvs,
                    adaptiveVehicleTypeText.concat("(s)"),
                    world.createApplication.getTotalOperatingCentreTrailerAuthority());
            expectedText.append(correspondenceAddress);
            expectedText.append(hgvIncreaseText);
        }
        if (variationType.contains("LGV")) {
            expectedText.append(correspondenceAddress);
            String lgvIncreaseText = String.format("Light goods vehicles authorised on the licence. New authorisation will be %s vehicle(s)", lgvs);
            expectedText.append(lgvIncreaseText);
        }
        Assertions.assertEquals(expectedText.toString().trim(), publicationResult.getText());

        clickByLinkText(world.applicationDetails.getLicenceNumber());

        boolean licenceHasUpdated = publicationType.equals("Variation Granted");
        String hgvValue = licenceHasUpdated ? hgvs : String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority());
        String lgvValue = licenceHasUpdated ? lgvs : String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority());

        assertTrue(isElementPresent(String.format("//th[contains(text(),'Operating centre')]/../th[contains(text(),'%s')]", capitalise(adaptiveVehicleTypeText).concat("s")), SelectorType.XPATH));

        assertEquals(getText(String.format("//li/dt[contains(text(),'Total Number of %s')]/../dd", adaptiveVehicleTypeText), SelectorType.XPATH), hgvValue);
        if (world.licenceCreation.isAGoodsInternationalLicence())
            assertEquals(getText("//li/dt[contains(text(),'Total Number of Light Goods Vehicles')]/../dd", SelectorType.XPATH), lgvValue);
        assertEquals(getText("//li/dt[contains(text(),'Total Number of trailers')]/../dd", SelectorType.XPATH), String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
    }


    @And("i navigate to the application publications page")
    public void iNavigateToTheApplicationPublicationsPage() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getApplication();
        clickByLinkText("Processing");
        waitForTextToBePresent("Category");
        clickByLinkText("Publications");
    }

    @Then("the new application publication for LGV Only should be correct on internal")
    public void theNewApplicationPublicationForLGVOnlyShouldBeCorrectOnInternal() {
        Dates date = new Dates(org.joda.time.LocalDate::new);
        String todayDate = date.getFormattedDate(0, 0, 0, "dd/MM/yyyy");
        clickByLinkText(todayDate);
        waitForTextToBePresent("Edit publication");
        checkLGVOnlyTextBeforePublicationOnInternal();
    }

    @And("the new application publication text for LGV Only should be correct on self serve")
    public void theNewApplicationPublicationTextForLGVOnlyShouldBeCorrectOnSelfServe() {
        checkGoodsLicencePublicationTextOnSelfServe("New Application");
    }

    @And("the new application publication text for Non SI Goods should be correct on self serve")
    public void theNewApplicationPublicationTextForNonGoodsSIShouldBeCorrectOnSelfServe() {
        checkGoodsLicencePublicationTextOnSelfServe("New Application");
    }

    @And("the application granted publication text for LGV Only should be correct on self serve")
    public void theApplicationGrantedPublicationTextForLGVOnlyShouldBeCorrectOnSelfServe() {
        checkGoodsLicencePublicationTextOnSelfServe("Application Granted");
    }

    @And("the application refused publication text for LGV Only should be correct on self serve")
    public void theApplicationRefusedPublicationTextForLGVOnlyShouldBeCorrectOnSelfServe() {
        checkGoodsLicencePublicationTextOnSelfServe("Application Refused");
    }

    @And("the application withdrawn publication text for LGV Only should be correct on self serve")
    public void theApplicationWithdrawnPublicationTextForLGVOnlyShouldBeCorrectOnSelfServe() {
        checkGoodsLicencePublicationTextOnSelfServe("Application Withdrawn");
    }

    @And("the licence view of the publication for LGV Only is correct on self serve")
    public void theLicenceViewOfThePublicationForLGVOnlyIsCorrectOnSelfServe() {
        clickByLinkText(world.applicationDetails.getLicenceNumber());

        String operatingCentreSection = getText("//h4[contains(text(),'Operating centres')]/../..", SelectorType.XPATH);
        assertTrue(operatingCentreSection.contains("The table is empty"));

        String authorisationSection = getText("//h4[contains(text(),'Authorisation')]/../..", SelectorType.XPATH);
        assertTrue(authorisationSection.contains("Total number of Light Goods Vehicles"));
        assertTrue(authorisationSection.contains(String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority())));
        assertFalse(authorisationSection.contains("vehicles"));
        assertFalse(authorisationSection.contains("trailers"));
    }


    @And("the licence view of the publication for PSV SI is correct on self serve")
    public void theLicenceViewOfThePublicationForPSVSIIsCorrectOnSelfServe() {
        clickByLinkText(world.applicationDetails.getLicenceNumber());

        String operatingCentreSection = getText("//h4[contains(text(),'Operating centres')]/../..", SelectorType.XPATH);
        assertTrue(operatingCentreSection.contains(world.formattedStrings.getFullCommaOperatingAddress()));
        assertTrue(operatingCentreSection.contains("Vehicles"));

        String authorisationSection = getText("//h4[contains(text(),'Authorisation')]/../..", SelectorType.XPATH);
        assertTrue(authorisationSection.contains("Total number of vehicles"));
        assertTrue(authorisationSection.contains(String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority())));
        assertFalse(authorisationSection.contains("trailers"));
    }

    @And("the licence view of the publication for Goods SN is correct on self serve")
    public void theLicenceViewOfThePublicationForGoodsSNIsCorrectOnSelfServe() {
        clickByLinkText(world.applicationDetails.getLicenceNumber());

        String operatingCentreSection = getText("//h4[contains(text(),'Operating centres')]/../..", SelectorType.XPATH);
        assertTrue(operatingCentreSection.contains(world.formattedStrings.getFullCommaOperatingAddress()));
        assertTrue(operatingCentreSection.contains("Vehicles"));
        assertTrue(operatingCentreSection.contains("Trailers"));

        String authorisationSection = getText("//h4[contains(text(),'Authorisation')]/../..", SelectorType.XPATH);
        assertTrue(authorisationSection.contains("Total number of vehicles"));
        assertTrue(authorisationSection.contains("Total number of trailers"));
        assertTrue(authorisationSection.contains(String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority())));
        assertTrue(authorisationSection.contains(String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority())));
    }

    @Then("^the out of objection date is present on the application (\\d+) days after the publication date$")
    public void theOutOfObjectionDateIsPresentOnTheApplication(Integer arg0) {
        world.internalNavigation.logInAndNavigateToApplicationProcessingPage(true);
        clickByLinkText("Publications");
        String publicationDate = getText("//td[@data-heading='Publication date']", SelectorType.XPATH);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate formattedDate = LocalDate.parse(publicationDate, formatter);
        String expectedDate = formattedDate.plusDays(22).format(formatter);
        clickByLinkText("Application details");
        String actualDate = getText("//dt[text()='Out of objection']/../dd", SelectorType.XPATH);
        assertEquals(expectedDate, actualDate);
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

    public void checkLGVOnlyTextBeforePublicationOnInternal() {
        String publicationText = getText("//*[@name='fields[text3]']", SelectorType.XPATH);
        String correspondenceAddress = world.formattedStrings.getFullCommaCorrespondenceAddress();
        String expectedAuthorisationText = String.format("Authorisation: %s Light goods vehicle(s).", world.createApplication.getTotalOperatingCentreLgvAuthority());
        String transportManagerName = "Transport Manager(s): ".concat(world.formattedStrings.getFullTransportManagerName());
        assertTrue(publicationText.contains(correspondenceAddress));
        assertTrue(publicationText.contains(expectedAuthorisationText));
        assertTrue(publicationText.contains(transportManagerName));
    }

    public void checkGoodsLicencePublicationTextOnSelfServe(String applicationStatus) {
        String publicationText = getText(
                String.format("//li[*//a[contains(text(),'%s')] and *//p[contains(text(),'%s')]]",
                        world.applicationDetails.getLicenceNumber(),
                        applicationStatus), SelectorType.XPATH);

        String correspondenceAddress = world.formattedStrings.getFullCommaCorrespondenceAddress();
        String operatingAddress = world.formattedStrings.getFullCommaOperatingAddress();
        String transportManagerName = "Transport Manager(s): ".concat(world.formattedStrings.getFullTransportManagerName());

        String expectedAuthorisationText;
        if (world.licenceCreation.isLGVOnlyLicence()) {
            expectedAuthorisationText = String.format("Authorisation: %s Light goods vehicle(s).", world.createApplication.getTotalOperatingCentreLgvAuthority());
        } else {
            expectedAuthorisationText = String.format("Authorisation: %s vehicle(s)", world.createApplication.getTotalOperatingCentreHgvAuthority());
            if (world.createApplication.getNoOfOperatingCentreTrailerAuthorised() != 0) {
                expectedAuthorisationText.concat(String.format(", %s trailer(s)", world.createApplication.getTotalOperatingCentreTrailerAuthority()));
            }
        }

        if (applicationStatus.contains("Refused") || applicationStatus.contains("Withdrawn")) {
            assertFalse(publicationText.contains(correspondenceAddress));
            if (!world.licenceCreation.isLGVOnlyLicence()) assertFalse(publicationText.contains(operatingAddress));
            assertFalse(publicationText.contains(expectedAuthorisationText));
            assertFalse(publicationText.contains(transportManagerName));
        } else {
            assertTrue(publicationText.contains(correspondenceAddress));
            if (!world.licenceCreation.isLGVOnlyLicence()) assertTrue(publicationText.contains(operatingAddress));
            assertTrue(publicationText.contains(expectedAuthorisationText));
            assertTrue(publicationText.contains(transportManagerName));
        }
    }
}