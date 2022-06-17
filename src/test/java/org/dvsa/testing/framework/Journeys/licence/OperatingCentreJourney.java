package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.faker.FakerUtils;
import apiCalls.actions.CreateApplication;
import apiCalls.enums.OperatorType;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.io.IOException;
import java.util.HashMap;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication.accessibilityScanner;

public class OperatingCentreJourney extends BasePage {

    World world;
    private FakerUtils faker = new FakerUtils();

    String editOperatingCentreTitle = "//h1[contains(text(),'Edit operating centre')]";
    String enterAddressManually = "Enter the address yourself";
    String operatingCentreVehicleField = "//*[@id='noOfVehiclesRequired']";
    String operatingCentreTrailerField = "//*[@id='noOfTrailersRequired']";
    String confirmOffStreetParkingCheckbox = "//*[@id='permission']";
    String advertTitle = "//h3[text()='Newspaper advert']";
    String uploadAdvertLater = "//*[@value='adPlacedLater']";

    public String addOperatingCentre = "//*[@id='add']";
    public String totalAuthorisationField = "//input[@id='totAuthVehicles']";
    public String totalHGVAuthorisationField = "//input[@id='totAuthHgvVehicles']";
    public String totalLGVAuthorisationField = "//input[@id='totAuthLgvVehicles']";
    public String totalCommunityAuthorisationField = "//input[@id='totCommunityLicences']";
    public String totalTrailersAuthorisationField = "//input[@id='totAuthTrailers']";
    public String vehicleAuthorisationHelpLink = "//span[contains(text(),'Help with vehicle authorisation')]";

    String confirmDeclaration = "//input[@id='declarationsAndUndertakings[declarationConfirmation]']";
    String submitApplication = "//button[@id='submit']";
    String submitAndPayForApplication = "//button[@id='submitAndPay']";

    String payNow = "//button[@id='form-actions[pay]']";

    public OperatingCentreJourney(World world) {
        this.world = world;
    }

    public void submitOperatingCentreVehicleAuthorisationVariationApplication(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        saveOperatingCentreVehicleAuthorisationVariationChange(newHGVTotalAuthority, newLGVTotalAuthority);
        completeApplicationAfterUpdatingAuthorities(newHGVTotalAuthority, newLGVTotalAuthority);
    }

    public void completeLGVOnlyApplicationAfterUpdatingLGVAuthority(String newLGVTotalAuthority) {
        String currentHGVAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority());
        completeApplicationAfterUpdatingAuthorities(currentHGVAuthority, newLGVTotalAuthority);
    }

    private void completeApplicationAfterUpdatingAuthorities(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        world.UIJourney.completeFinancialEvidencePage();
        if (world.licenceCreation.isPSVLicence()) {
            world.psvJourney.completeVehicleDeclarationsPage();
        }
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        if (hasTotalHGVAuthorityIncreased(newHGVTotalAuthority) || hasTotalLGVAuthorityIncreased(newLGVTotalAuthority)) {
            click(submitApplication, SelectorType.XPATH);
        } else {
            click(submitAndPayForApplication, SelectorType.XPATH);
            click(payNow, SelectorType.XPATH);
            world.feeAndPaymentJourney.customerPaymentModule();
        }
        waitForTextToBePresent("Thank you, your application has been submitted.");
    }

    public void saveOperatingCentreVehicleAuthorisationVariationChange(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        String trailerCount = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        saveOCVehicleAuthVarChange(newHGVTotalAuthority, newLGVTotalAuthority, trailerCount);
    }

    public void saveOCVehicleAuthVarChange(String newHGVTotalAuthority, String newLGVTotalAuthority, String newTrailerTotalAuthority) {
        world.generalVariationJourney.beginOperatingCentreVariation();
        if (hasNumberOfHGVChanged(newHGVTotalAuthority) || hasNumberOfTrailersChanged(newTrailerTotalAuthority)) {
            updateOperatingCentreAuthorisation(newHGVTotalAuthority, newTrailerTotalAuthority);
        }
        updateOperatingCentreTotalVehicleAuthority(newHGVTotalAuthority, newLGVTotalAuthority, newTrailerTotalAuthority);
    }

    public void updateOperatingCentreAuthorisation(String newHGVTotalAuthority, String newTrailerTotalAuthority) {
        String operatingCentreEditLink = String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentreAddressLine1());
        waitAndClick(operatingCentreEditLink, SelectorType.XPATH);
        replaceText(operatingCentreVehicleField, SelectorType.XPATH, newHGVTotalAuthority);
        if (world.createApplication.getTotalOperatingCentreTrailerAuthority() != Integer.parseInt(newTrailerTotalAuthority)) {
            replaceText(operatingCentreTrailerField, SelectorType.XPATH, newTrailerTotalAuthority);
        }
        if ((hasHGVAuthorityOnOCIncreased(newHGVTotalAuthority) || hasTrailerAuthorityOnOCIncreased(newTrailerTotalAuthority)) && world.licenceCreation.isGoodsLicence()) {
            waitAndClick(editOperatingCentreTitle, SelectorType.XPATH);
            waitForElementToBePresent(advertTitle);
        }
        world.UIJourney.clickSubmit();
    }

    public void updateOperatingCentreTotalVehicleAuthority(String newHGVTotalAuthority, String newLGVTotalAuthority, String trailers) {
        if (world.licenceCreation.isAGoodsInternationalLicence() && newLGVTotalAuthority != null) {
            waitAndClick(totalLGVAuthorisationField, SelectorType.XPATH);
            replaceText(totalLGVAuthorisationField, SelectorType.XPATH, newLGVTotalAuthority);
        }
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            waitAndClick(totalHGVAuthorisationField, SelectorType.XPATH);
            replaceText(totalTrailersAuthorisationField, SelectorType.XPATH, trailers);
        }
        if (isElementPresent("totAuthVehicles",SelectorType.ID)) {
            replaceText(totalAuthorisationField, SelectorType.XPATH, newHGVTotalAuthority);
        } else {
            replaceText(totalHGVAuthorisationField, SelectorType.XPATH, newHGVTotalAuthority);
        }
        UIJourney.clickSaveAndReturn();
    }

    public void updateLGVOnlyAuthorityAndSave(String newAuthority) {
        waitAndClick(totalLGVAuthorisationField, SelectorType.XPATH);
        replaceText(totalLGVAuthorisationField, SelectorType.XPATH, newAuthority);
        UIJourney.clickSaveAndReturn();
    }

    public void addNewOperatingCentre(String vehicles, String trailers) {
        waitForElementToBePresent(addOperatingCentre);
        click(addOperatingCentre, SelectorType.XPATH);
        try {
            accessibilityScanner();
        } catch (IllegalBrowserException | IOException e) {
            e.printStackTrace();
        }
        HashMap<String, String> newOperatingCentreAddress = faker.generateAddress();
        clickByLinkText(enterAddressManually);
        world.UIJourney.addNewAddressDetails(newOperatingCentreAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
        enterText(operatingCentreVehicleField, SelectorType.XPATH, vehicles);
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            enterText(operatingCentreTrailerField, SelectorType.XPATH, trailers);
            click(uploadAdvertLater, SelectorType.XPATH);
        }
        click(confirmOffStreetParkingCheckbox, SelectorType.XPATH);
        world.UIJourney.clickSubmit();
    }

    private boolean hasTotalHGVAuthorityIncreased(String newHGVTotalAuthority) {
        return world.createApplication.getTotalOperatingCentreHgvAuthority() > Integer.parseInt(newHGVTotalAuthority);
    }

    private boolean hasTotalLGVAuthorityIncreased(String newLGVTotalAuthority) {
        return world.createApplication.getTotalOperatingCentreLgvAuthority() > Integer.parseInt(newLGVTotalAuthority);
    }

    private boolean hasHGVAuthorityOnOCIncreased(String newHGVOCAuthority) {
        return Integer.parseInt(newHGVOCAuthority) > world.createApplication.getNoOfOperatingCentreVehicleAuthorised();
    }

    private boolean hasTrailerAuthorityOnOCIncreased(String newTrailerOCAuthority) {
        return Integer.parseInt(newTrailerOCAuthority) > world.createApplication.getNoOfOperatingCentreVehicleAuthorised();
    }

    private boolean hasNumberOfHGVChanged(String newHGVTotalAuthority) {
        return !newHGVTotalAuthority.equals(String.valueOf(world.createApplication.getNoOfOperatingCentreVehicleAuthorised()));
    }

    private boolean hasNumberOfTrailersChanged(String newTrailerTotalAuthority) {
        return !newTrailerTotalAuthority.equals(String.valueOf(world.createApplication.getNoOfOperatingCentreVehicleAuthorised()));
    }
}