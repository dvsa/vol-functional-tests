package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

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
    String submitButton = "//*[@id='form-actions[submit]']";

    public String addOperatingCentre = "//*[@id='add']";
    String totalAuthorisationField = "//input[@id='totAuthVehicles']";
    String totalHGVAuthorisationField = "//input[@id='totAuthHgvVehicles']";
    String totalLGVAuthorisationField = "//input[@id='totAuthLgvVehicles']";
    String totalTrailersAuthorisationField = "//input[@id='totAuthTrailers']";
    public String saveButton = "//*[@id='form-actions[save]']";

    String confirmDeclaration = "//input[@id='declarationsAndUndertakings[declarationConfirmation]']";
    String submitApplication = "//button[@id='submit']";
    String submitAndPayForApplication = "//button[@id='submitAndPay']";

    String payNow = "//button[@id='form-actions[pay]']";

    public OperatingCentreJourney(World world) {
        this.world = world;
    }

    public void loginAndSubmitOperatingCentreVehicleAuthorisationVariationApplication(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(newHGVTotalAuthority, newLGVTotalAuthority, String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
        world.UIJourney.completeFinancialEvidencePage();
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        if (hasHGVAuthorityIncreased(newHGVTotalAuthority) || hasLGVAuthorityIncreased(newLGVTotalAuthority))
            click(submitApplication, SelectorType.XPATH);
        else {
            click(submitAndPayForApplication, SelectorType.XPATH);
            click(payNow, SelectorType.XPATH);
            world.feeAndPaymentJourney.customerPaymentModule();
            world.UIJourney.signDeclarationForVariation();
        }
        waitForTextToBePresent("Thank you, your application has been submitted.");
    }

    public void loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(String newHGVTotalAuthority, String newLGVTotalAuthority, String newTrailerTotalAuthority) {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        if (!newHGVTotalAuthority.equals(String.valueOf(world.createApplication.getNoOfOperatingCentreVehicleAuthorised()))) {
            updateOperatingCentreAuthorisation(newHGVTotalAuthority);
        }
        updateOperatingCentreTotalVehicleAuthority(newHGVTotalAuthority, newLGVTotalAuthority, newTrailerTotalAuthority);
    }

    public void updateOperatingCentreAuthorisation(String newHGVTotalAuthority) {
        String operatingCentreEditLink = String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentreAddressLine1());
        click(operatingCentreEditLink, SelectorType.XPATH);
        replaceText(operatingCentreVehicleField, SelectorType.XPATH, newHGVTotalAuthority);
        if (Integer.parseInt(newHGVTotalAuthority) > world.createApplication.getNoOfOperatingCentreVehicleAuthorised() && world.licenceCreation.isGoodsLicence()) {
            waitAndClick(editOperatingCentreTitle, SelectorType.XPATH);
            waitForElementToBePresent(advertTitle);
        }
        waitAndClick(submitButton, SelectorType.XPATH);
    }

    public void updateOperatingCentreTotalVehicleAuthority(String newHGVTotalAuthority, String newLGVTotalAuthority, String trailers) {
        if (world.licenceCreation.isAGoodsInternationalLicence() && newLGVTotalAuthority != null) {
            replaceText(totalLGVAuthorisationField, SelectorType.XPATH, newLGVTotalAuthority);
        }
        replaceText(totalTrailersAuthorisationField, SelectorType.XPATH, trailers);
        if(isElementPresent("totAuthVehicles",SelectorType.ID)) {
            replaceText(totalAuthorisationField, SelectorType.XPATH, newHGVTotalAuthority);
        }else{
            replaceText(totalHGVAuthorisationField, SelectorType.XPATH, newHGVTotalAuthority);
        }
        click(saveButton, SelectorType.XPATH);
    }

    public void addNewOperatingCentre(String vehicles, String trailers) {
        click(addOperatingCentre, SelectorType.XPATH);
        HashMap<String, String> newOperatingCentreAddress = faker.generateAddress();
        clickByLinkText(enterAddressManually);
        world.UIJourney.addNewAddressDetails(newOperatingCentreAddress, world.createApplication.getPostCodeByTrafficArea(), "address");
        enterText(operatingCentreVehicleField, SelectorType.XPATH, vehicles);
        enterText(operatingCentreTrailerField, SelectorType.XPATH, trailers);
        click(confirmOffStreetParkingCheckbox, SelectorType.XPATH);
        click(uploadAdvertLater, SelectorType.XPATH);
        click(submitButton, SelectorType.XPATH);
    }

    private boolean hasHGVAuthorityIncreased(String newHGVTotalAuthority) {
        return world.createApplication.getTotalOperatingCentreHgvAuthority() >= Integer.parseInt(newHGVTotalAuthority);
    }

    private boolean hasLGVAuthorityIncreased(String newLGVTotalAuthority) {
        return world.createApplication.getTotalOperatingCentreLgvAuthority() >= Integer.parseInt(newLGVTotalAuthority);
    }
}