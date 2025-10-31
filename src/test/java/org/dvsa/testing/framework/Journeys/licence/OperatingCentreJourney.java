package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.faker.FakerUtils;
import apiCalls.enums.OperatorType;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;


public class OperatingCentreJourney extends BasePage {

    World world;
    private final FakerUtils faker = new FakerUtils();

    String editOperatingCentreTitle = "//h1[contains(text(),'Edit operating centre')]";
    String enterAddressManually = "Enter the address yourself";
    String operatingCentreVehicleField = "//*[@id='noOfVehiclesRequired']";
    String operatingCentreTrailerField = "//*[@id='noOfTrailersRequired']";
    String confirmOffStreetParkingCheckbox = "permission";
    String advertTitle = "//h3[text()='Newspaper advert']";
    String uploadAdvertLater = "//*[contains(text(),'Upload documents later')]";

    public String addOperatingCentre = "//*[@id='add']";
    public String totalAuthorisationField = "//input[@id='totAuthVehicles']";
    public String totalHGVAuthorisationField = "//input[@id='totAuthHgvVehicles']";
    public String totalLGVAuthorisationField = "//input[@id='totAuthLgvVehicles']";
    public String totalCommunityAuthorisationField = "//input[@id='totCommunityLicences']";
    public String totalTrailersAuthorisationField = "//input[@id='totAuthTrailers']";
    public String vehicleAuthorisationHelpLink = "//span[contains(text(),'Help with vehicle authorisation')]";

    String confirmDeclaration = "//input[@id='declarationsAndUndertakings[declarationConfirmation]']";
    String submitApplication = "//button[@id='submit']";
    String submitAndPayForApplication = "//*[@id='submitAndPay']";

    String payNow = "//*[@id='form-actions[pay]']";
    public String psvCertifiedDocsField = "//*[@id=\"totCommunityLicences\"]";
    public String trafficAreaDropdown = "//*[@id='trafficArea']";

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
        world.selfServeUIJourney.completeFinancialEvidencePage();
        if (world.licenceCreation.isPSVLicence()) {
            world.psvJourney.completeVehicleDeclarationsPage();
        }
        waitAndClickByLinkText("Review and declarations");
        clickByXPath("//*[@id='declarationsAndUndertakings[declarationConfirmation]']");
        waitAndClick("//*[@id='submitAndPay']", SelectorType.XPATH);
        if (hasTotalHGVAuthorityIncreased(newHGVTotalAuthority) || hasTotalLGVAuthorityIncreased(newLGVTotalAuthority)) {
            waitAndClick(submitApplication, SelectorType.XPATH);
        } else {
            UniversalActions.clickPay();
            world.feeAndPaymentJourney.customerPaymentModule();
        }
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
        String operatingCentreEditLink = String.format("//*[contains(text(),'%s')]", world.createApplication.getOperatingCentrePostCode());
        waitForElementToBePresent("//*[@id='add']");
        waitAndClick(operatingCentreEditLink, SelectorType.XPATH);
        waitForElementToBePresent(operatingCentreVehicleField);
        replaceText(operatingCentreVehicleField, SelectorType.XPATH, newHGVTotalAuthority);
        if (world.createApplication.getTotalOperatingCentreTrailerAuthority() != Integer.parseInt(newTrailerTotalAuthority)) {
            replaceText(operatingCentreTrailerField, SelectorType.XPATH, newTrailerTotalAuthority);
        }
        if ((hasHGVAuthorityOnOCIncreased(newHGVTotalAuthority) || hasTrailerAuthorityOnOCIncreased(newTrailerTotalAuthority)) && world.licenceCreation.isGoodsLicence()) {
            waitAndClick(editOperatingCentreTitle, SelectorType.XPATH);
            waitForElementToBePresent(advertTitle);
            clickByXPath("//input[@value='adPlacedLater']");
        }
        UniversalActions.clickSubmit();
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
        UniversalActions.clickSaveAndReturn();
    }

    public void updateLGVOnlyAuthorityAndSave(String newAuthority) {
        waitAndClick(totalLGVAuthorisationField, SelectorType.XPATH);
        replaceText(totalLGVAuthorisationField, SelectorType.XPATH, newAuthority);
        UniversalActions.clickSaveAndReturn();
    }

    public void addNewOperatingCentre(String vehicles, String trailers) {
        waitForElementToBePresent(addOperatingCentre);
        waitAndClick(addOperatingCentre, SelectorType.XPATH);
        HashMap<String, String> newOperatingCentreAddress = faker.generateAddress();
        waitAndClick(enterAddressManually,  SelectorType.LINKTEXT);
        world.selfServeUIJourney.addNewAddressDetails(newOperatingCentreAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
        waitAndEnterText(operatingCentreVehicleField, SelectorType.XPATH, vehicles);
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            waitAndEnterText(operatingCentreTrailerField, SelectorType.XPATH, trailers);
            waitAndClick(uploadAdvertLater, SelectorType.XPATH);
        }
        clickById(confirmOffStreetParkingCheckbox);
        UniversalActions.clickSubmit();
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

    public void addPsvOperatingCentre(String numberOfVehicles) {
        waitForElementToBePresent(addOperatingCentre);
        waitAndClick(addOperatingCentre, SelectorType.XPATH);
        HashMap<String, String> newOperatingCentreAddress = faker.generateAddress();
        waitAndClick(enterAddressManually, SelectorType.LINKTEXT);
        world.selfServeUIJourney.addNewAddressDetails(newOperatingCentreAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
        waitAndEnterText(operatingCentreVehicleField, SelectorType.XPATH, numberOfVehicles);
        clickById(confirmOffStreetParkingCheckbox);
        UniversalActions.clickSubmit();
    }

    public void selectOperatingCentreAndAuthorisationSection() {
        waitAndClick("//*[@id=\"overview-item__operating_centres\"]/span", SelectorType.XPATH);
    }

    public void savePsvAuthorisation(String numberOfVehicles) {
        if (isElementPresent(trafficAreaDropdown, SelectorType.XPATH)) {
            waitAndClick(trafficAreaDropdown, SelectorType.XPATH);
            selectValueFromDropDownByIndex(trafficAreaDropdown, SelectorType.XPATH, 1);
        }
        replaceText(totalHGVAuthorisationField, SelectorType.XPATH, numberOfVehicles);
        replaceText(psvCertifiedDocsField, SelectorType.XPATH, "0");
        UniversalActions.clickSaveAndReturn();
    }
}
