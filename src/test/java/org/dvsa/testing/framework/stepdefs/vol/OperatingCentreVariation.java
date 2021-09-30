package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class OperatingCentreVariation extends BasePage {
    World world;

    String operatingCentreTitle = "//h1[contains(text(),'Edit operating centre')]";
    String operatingCentreVehicleField = "//*[@id='noOfVehiclesRequired']";
    String advertTitle = "//h3[text()='Newspaper advert']";
    String submitButton = "//*[@id='form-actions[submit]']";

    String operatingCentreConfirmationText = "Operating centre updated";
    String totalHGVAuthorisationField = "//input[@id='totAuthHgvVehicles']";
    String totalLGVAuthorisationField = "//input[@id='totAuthLgvVehicles']";
    String saveButton = "//*[@id='form-actions[save]']";
    String totalHgvInTable = "//td[@colspan][1]";

    String uploadLaterRadioButton =  "//input[@id='uploadLaterRadio']";

    String confirmDeclaration =  "//input[@id='declarationsAndUndertakings[declarationConfirmation]']";
    String submitApplication = "//button[@id='submit']";
    String submitAndPayForApplication = "//button[@id='submitAndPay']";

    String payNow = "//button[@id='form-actions[pay]']";

    public OperatingCentreVariation(World world) {
        this.world = world;
    }

    @And("i create an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAnOperatingCentreVariationWithHgvAndLgvs(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(newHGVTotalAuthority, newLGVTotalAuthority);
    }

    @And("i create and submit an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAndSubmitAnOperatingCentreVariationWithHgvsAndLgvs(String numberOfHGVs, String numberOfLGVs) {
        loginAndSubmitOperatingCentreVehicleAuthorisationVariationApplication(numberOfHGVs, numberOfLGVs);
    }

    @And("i create and submit and grant an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAndSubmitAndGrantAnOperatingCentreVariationWithHgvsAndLgvs(String numberOfHGVs, String numberOfLGVs) {
        loginAndSubmitOperatingCentreVehicleAuthorisationVariationApplication(numberOfHGVs, numberOfLGVs);
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getVariationApplication();
        world.UIJourney.caseWorkerCompleteOverview();
        waitForTextToBePresent("The overview page has been saved");
        world.UIJourney.grantApplicationUnderDelegatedAuthority();
    }

    public void loginAndSubmitOperatingCentreVehicleAuthorisationVariationApplication(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(newHGVTotalAuthority, newLGVTotalAuthority);
        clickByLinkText("Financial evidence");
        click(uploadLaterRadioButton, SelectorType.XPATH);
        click(saveButton, SelectorType.XPATH);
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        if (world.createApplication.getTotalOperatingCentreHgvAuthority() >= Integer.parseInt(newHGVTotalAuthority) && world.createApplication.getTotalOperatingCentreLgvAuthority() >= Integer.parseInt(newLGVTotalAuthority))
            click(submitApplication, SelectorType.XPATH);
        else {
            click(submitAndPayForApplication, SelectorType.XPATH);
            click(payNow, SelectorType.XPATH);
            world.feeAndPaymentJourney.customerPaymentModule();
        }
        waitForTextToBePresent("Thank you, your application has been submitted.");
    }

    private void loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        updateOperatingCentreTotalVehicleAuthorisations(newHGVTotalAuthority, newLGVTotalAuthority);
    }

    private void updateOperatingCentreTotalVehicleAuthorisations(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        updateOperatingCentreWithNewVehicleAuthorisation(newHGVTotalAuthority);
        replaceText(totalHGVAuthorisationField, SelectorType.XPATH, newHGVTotalAuthority);
        if (world.licenceCreation.isAGoodsInternationalLicence()) {
            replaceText(totalLGVAuthorisationField, SelectorType.XPATH, newLGVTotalAuthority);
        }
        click(saveButton, SelectorType.XPATH);
    }

    private void updateOperatingCentreWithNewVehicleAuthorisation(String newHGVTotalAuthority) {
        String totalCountOfHGVAuthorisationsOnOperatingCentres = findElement(totalHgvInTable, SelectorType.XPATH).getText();
        if (!newHGVTotalAuthority.equals(totalCountOfHGVAuthorisationsOnOperatingCentres)) {
            String operatingCentreEditLink = String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentrePostCode());
            click(operatingCentreEditLink, SelectorType.XPATH);
            replaceText(operatingCentreVehicleField, SelectorType.XPATH, newHGVTotalAuthority);
            if (Integer.parseInt(newHGVTotalAuthority) > Integer.parseInt(totalCountOfHGVAuthorisationsOnOperatingCentres) && world.licenceCreation.isGoodsLicence()) {
                waitAndClick(operatingCentreTitle, SelectorType.XPATH);
                waitForElementToBePresent(advertTitle);
            }
            waitAndClick(submitButton, SelectorType.XPATH);
            waitForTextToBePresent(operatingCentreConfirmationText);
        }
    }
}
