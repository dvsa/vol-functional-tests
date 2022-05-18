package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.dates.Dates;
import apiCalls.enums.OperatorType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.apache.commons.lang.WordUtils;
import org.dvsa.testing.framework.Utils.Generic.ParseUtils;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PublicationsRelatedSteps extends BasePage implements En {
    private static final Logger LOGGER = LogManager.getLogger(PublicationsRelatedSteps.class);
    private final World world;

    private final String fiftyResultsPerPageLink = "//li/a[text()='50']";
    private final String publicationDatesColumn = "//table/tbody/tr/td[5]";
    private final String publicationNumberColumn = "//table/tbody/tr[*]/td[2]";
    private final String radioButtonsColumn = "//*[@type='radio']";

    public PublicationsRelatedSteps(World world) {
        this.world = world;
    }

    @And("i navigate to the admin publications page")
    public void iNavigateToTheAdminPublicationsPage() {
        click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
        click("//*[@id='menu-admin-dashboard/admin-publication']", SelectorType.XPATH);
    }

    @When("i generate {int} publications and check their docman link")
    public void iGeneratePublicationsAndCheckTheirDocmanLink(Integer noOfDifferentLicences) {
        String currentPubNo;
        int missingLinks = 0;
        for (int i = 0; i < noOfDifferentLicences; i++) {

            List<WebElement> radioButtons;
            List<WebElement> publicationNumbers;

            publicationNumbers = show50ResultsAndUpdateWebElementsList(publicationNumberColumn);
            currentPubNo = publicationNumbers.get(i).getText();

            if (size(currentPubNo, SelectorType.LINKTEXT) == 0 || size(String.format("//*[contains(text(),%s)]", currentPubNo), SelectorType.XPATH) > 1) {

                radioButtons = findElements(radioButtonsColumn, SelectorType.XPATH);
                radioButtons.get(i).click();

                waitAndClick("//*[@id='generate']", SelectorType.XPATH);
                waitForTextToBePresent("Publication was generated, a new publication was also created");

                radioButtons = show50ResultsAndUpdateWebElementsList(radioButtonsColumn);

                clickByLinkText(currentPubNo);
                waitForTextToBePresent("Open document");

                if (getText("//*[@id='letter-link']",SelectorType.XPATH).isEmpty()){
                    missingLinks++;
                }

                click("//*[contains(text(),'Close')]",SelectorType.XPATH);

                waitForElementToBeClickable(radioButtonsColumn, SelectorType.XPATH);
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

            publicationNumbers = show50ResultsAndUpdateWebElementsList(publicationNumberColumn);
            currentPubNo = publicationNumbers.get(i).getText();

            if (size(currentPubNo, SelectorType.LINKTEXT) == 0 || size(String.format("//*[contains(text(),%s)]", currentPubNo), SelectorType.XPATH) > 1) {

                radioButtons = findElements(radioButtonsColumn, SelectorType.XPATH);
                radioButtons.get(i).click();

                waitAndClick("//*[@id='generate']", SelectorType.XPATH);
                waitForTextToBePresent("Publication was generated, a new publication was also created");

                radioButtons = show50ResultsAndUpdateWebElementsList(radioButtonsColumn);
                List<WebElement> publicationDates = findElements(publicationDatesColumn, SelectorType.XPATH);

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
                show50ResultsAndUpdateWebElementsList(publicationNumberColumn);
                int pageNumber = 1;
                boolean kickOut = true;
                //Start looping over pages here
                while (kickOut) {
                    // Storing numbers
                    publicationNumbers = findElements(publicationNumberColumn, SelectorType.XPATH);

                    // Changing into an array with the element's text
                    List<String> textList = new ArrayList<>(publicationNumbers.size());
                    for (WebElement elements : publicationNumbers) {
                        textList.add(elements.getText());
                    }

                    if (textList.contains(currentPubNo)) {
                        assertTrue(findElements(currentPubNo, SelectorType.LINKTEXT).size() != 0);
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
        if (world.updateLicence.getInternalUserId() == null) {world.APIJourney.createAdminUser();}

        if (isElementNotPresent(world.internalNavigation.adminDropdown,SelectorType.XPATH)) {world.internalNavigation.logInAsAdmin();}

        world.internalNavigation.adminNavigation(AdminOption.PUBLICATIONS);
        click(fiftyResultsPerPageLink, SelectorType.XPATH);
        String trafficArea = ParseUtils.parseTrafficArea(world.createApplication.getTrafficArea());
        String documentType = world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) ? "A&D" : "N&P";
        String radioButton = String.format("//tr//td[contains(text(),'%s')]/../td[contains(text(),'%s')]/../td/label/input", trafficArea, documentType);
        String radioButtonValue = getAttribute(radioButton, SelectorType.XPATH, "value");
        click(radioButton, SelectorType.XPATH);
        click("generate", SelectorType.ID);
        waitForTextToBePresent("Publication was generated, a new publication was also created");
        click(fiftyResultsPerPageLink, SelectorType.XPATH);
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
        String licenceNumber = world.applicationDetails.getLicenceNumber();
        if (!isElementPresent(licenceNumber, SelectorType.XPATH))
            waitForElementToBeClickable(String.format("//a[contains(text(),%s)]", licenceNumber), SelectorType.XPATH);

        WebElement publicationResult = findElement(String.format("//li[div/h4/a[contains(text(),'%s')] and div[3]/p[contains(text(),'%s')]]/div[2]/p[3]", licenceNumber, publicationType), SelectorType.XPATH);
        String adaptiveVehicleTypeText = world.licenceCreation.isAGoodsInternationalLicence() ? "Heavy goods vehicle" : "vehicle";
        String operatingCentreAddress = world.formattedStrings.getFullCommaOperatingAddress();
        String correspondenceAddress = world.formattedStrings.getFullCommaCorrespondenceAddress();
        String hgvIncreaseText = String.format(" New licence authorisation will be %s Heavy goods vehicle(s)", hgvs);
        String lgvIncreaseText = String.format(" New licence authorisation will be %s Light goods vehicle(s)", lgvs);

        switch(variationType){
            case "HGV":
                String hgvOCIncreaseText = String.format(" Increase at existing operating centre: %s New authorisation at this operating centre will be: %s %s, %s trailer(s) %s New licence authorisation will be %s %s",operatingCentreAddress,hgvs,adaptiveVehicleTypeText.concat("(s)"),world.createApplication.getTotalOperatingCentreTrailerAuthority(),correspondenceAddress,hgvs,adaptiveVehicleTypeText.concat("(s)"));
                String hgvExpectedText = correspondenceAddress.concat(hgvOCIncreaseText);
                LOGGER.info("AP HGV Exp text:" + hgvExpectedText);
                LOGGER.info("AP HGV Act text:" + publicationResult.getText());
                Assertions.assertTrue(publicationResult.getText().contains(hgvExpectedText));
                break;

            case "LGV":
                String lgvExpectedText = correspondenceAddress.concat(lgvIncreaseText);
                LOGGER.info("AP LGV Exp text:" + lgvExpectedText);
                LOGGER.info("AP LGV Act text:" + publicationResult.getText());
                Assertions.assertTrue(publicationResult.getText().toLowerCase().contains(lgvExpectedText.toLowerCase()));
                break;

            case "HGV and LGV":
                String hgvAndLgv = String.format(" Increase at existing operating centre: %s New authorisation at this operating centre will be: %s %s, %s trailer(s) %s%s%s",operatingCentreAddress,hgvs,adaptiveVehicleTypeText.concat("(s)"),world.createApplication.getTotalOperatingCentreTrailerAuthority(),correspondenceAddress,hgvIncreaseText,lgvIncreaseText);
                String hgvAndLgvExpectedText = correspondenceAddress.concat(hgvAndLgv);
                LOGGER.info("AP HGVAndLGV Exp text:" + hgvAndLgvExpectedText);
                LOGGER.info("AP HGVAndLGV Act text:" + publicationResult.getText());
                Assertions.assertTrue(publicationResult.getText().contains(hgvAndLgvExpectedText));
                break;

            case "HGV auth":
                String hgvAuthExpectedText = correspondenceAddress.concat(" New licence authorisation will be " + hgvs + " " + adaptiveVehicleTypeText.concat("(s)"));
                LOGGER.info("AP HGV auth Exp text:" + hgvAuthExpectedText);
                LOGGER.info("AP HGV auth Act text:" + publicationResult.getText());
                Assertions.assertTrue(publicationResult.getText().contains(hgvAuthExpectedText));
                break;

            case "HGV and LGV auth":
                String hgvAndLgvAuthExpectedText = correspondenceAddress.concat(hgvIncreaseText + lgvIncreaseText);
                LOGGER.info("AP HGVAndLGV Exp text:" + hgvAndLgvAuthExpectedText);
                LOGGER.info("AP HGVAndLGV Act text:" + publicationResult.getText());
                Assertions.assertTrue(publicationResult.getText().contains(hgvAndLgvAuthExpectedText));
                break;
        }

        waitForPageLoad();
        clickByLinkText(world.applicationDetails.getLicenceNumber());

        boolean licenceHasUpdated = publicationType.equals("Variation Granted");
        String hgvValue = licenceHasUpdated ? hgvs : String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority());
        String lgvValue = licenceHasUpdated ? lgvs : String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority());

        assertTrue(isElementPresent(String.format("//th[contains(text(),'Operating centre')]/../th[contains(text(),'%s')]", WordUtils.capitalizeFully(adaptiveVehicleTypeText).concat("s")), SelectorType.XPATH));

        if (world.licenceCreation.isAGoodsInternationalLicence()) {
            assertEquals(getText(String.format("//li/dt[contains(text(),'Total Number of %s')]/../dd", WordUtils.capitalizeFully(adaptiveVehicleTypeText).concat("s")), SelectorType.XPATH), hgvValue);
            assertEquals(getText("//li/dt[contains(text(),'Total Number of Light Goods Vehicles')]/../dd", SelectorType.XPATH), lgvValue);
        } else {
            assertEquals(getText(String.format("//li/dt[contains(text(),'Total Number of %s')]/../dd", adaptiveVehicleTypeText.concat("s")), SelectorType.XPATH), hgvValue);
        }

        //if (world.licenceCreation.isAGoodsInternationalLicence()){
        //    assertEquals(getText("//li/dt[contains(text(),'Total Number of Light Goods Vehicles')]/../dd", SelectorType.XPATH), lgvValue);
        //}

        assertEquals(getText("//li/dt[contains(text(),'Total Number of trailers')]/../dd", SelectorType.XPATH), String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
    }


    @And("i navigate to the application publications page")
    public void iNavigateToTheApplicationPublicationsPage() {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
        clickByLinkText("Processing");
        waitForTextToBePresent("Processing");
        clickByLinkText("Publications");
    }

    @And("I view the application publications page")
    public void iViewTheApplicationPublicationsPage() {
        clickByLinkText("Processing");
        waitForTextToBePresent("Processing");
        clickByLinkText("Publications");
    }

    @And("I publish the application on internal")
    public void iPublishTheApplicationOnInternal() {
        waitForTextToBePresent("Publish application");
        clickByLinkText("Publish application");
        waitForTextToBePresent("publish this application");
        world.UIJourney.clickOk();
        waitForTextToBePresent("Republish application");
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
        assertTrue(authorisationSection.contains("Total Number of Light Goods Vehicles"));
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
        assertTrue(authorisationSection.contains("Total Number of vehicles"));
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
        assertTrue(authorisationSection.contains("Total Number of vehicles"));
        assertTrue(authorisationSection.contains("Total Number of trailers"));
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
        List<WebElement> webElements = findElements(webElementsXpath, SelectorType.XPATH);
        if (size(fiftyResultsPerPageLink, SelectorType.XPATH) != 0) {
            click(fiftyResultsPerPageLink, SelectorType.XPATH);
            webElements = findElements(webElementsXpath, SelectorType.XPATH);
            while (webElements.size() <= 10) {
                webElements = findElements(webElementsXpath, SelectorType.XPATH);
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

    @Then("the variation publication for LGV Only should be correct on internal with {int} more lgvs")
    public void theVariationPublicationForLGVOnlyShouldBeCorrectOnInternal(int additionalAuthority) {
        String publicationLinkForVariation = "//td[contains(text(),'New Variation')]/../td/a";
        click(publicationLinkForVariation, SelectorType.XPATH);
        waitForTextToBePresent("Edit publication");

        String publicationTexts = getText("//*[@name='fields[text1]']", SelectorType.XPATH)
                .concat(getText("//*[@name='fields[text2]']", SelectorType.XPATH))
                .concat(getText("//*[@name='fields[text3]']", SelectorType.XPATH));
        String newAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority() + additionalAuthority);

        assertExpectedLGVVariationText(publicationTexts, newAuthority);
    }

    @And("the variation publication for LGV Only should be correct on self serve with {int} more lgvs")
    public void theVariationPublicationForLGVOnlyShouldBeCorrectOnSelfServeWithMoreLgvs(int additionalAuthority) {
        String publicationResult = getText(String.format("//li[div/h4/a[contains(text(),'%s')] and div[3]/p[contains(text(),'%s')]]/div[2]", world.applicationDetails.getLicenceNumber(), "New Variation"), SelectorType.XPATH);
        String newAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority() + additionalAuthority);
        assertExpectedLGVVariationText(publicationResult, newAuthority);
    }

    public void assertExpectedLGVVariationText(String actualPublicationText, String newAuthority) {
        String licenceType = world.applicationDetails.getLicenceNumber().concat(" SI");
        String organisationName = world.createApplication.getOrganisationName();
        String directorText = "Director(s): "
                .concat(world.createApplication.getDirectorForeName())
                .concat(" ").concat(world.createApplication.getDirectorFamilyName());
        String correspondenceAddress = world.formattedStrings.getFullCommaCorrespondenceAddress();
        String authorityText = String.format("Light goods vehicles authorised on the licence. New authorisation will be %s vehicle(s)", newAuthority);

        assertTrue(actualPublicationText.contains(licenceType));
        assertTrue(actualPublicationText.contains(organisationName));
        assertTrue(actualPublicationText.contains(directorText));
        assertTrue(actualPublicationText.contains(correspondenceAddress));
        assertTrue(actualPublicationText.contains(authorityText));
    }
}