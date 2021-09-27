package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.LicenceType;
import apiCalls.enums.OperatorType;
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

    public OperatingCentreVariation(World world) {
        this.world = world;
    }

    @And("i create an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAnOperatingCentreVariationWithHgvAndLgvs(String unformattedNumberOfHGVs, String unformattedNumberOfLGVs) {
        loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(unformattedNumberOfHGVs, unformattedNumberOfLGVs);
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

    public void loginAndSubmitOperatingCentreVehicleAuthorisationVariationApplication(String numberOfHGVs, String numberOfLGVs) {
        loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(numberOfHGVs, numberOfLGVs);
        clickByLinkText("Financial evidence");
        click("uploadLaterRadio", SelectorType.ID);
        click(saveButton, SelectorType.XPATH);
        clickByLinkText("Review and declarations");
        click("declarationsAndUndertakings[declarationConfirmation]", SelectorType.ID);
        if (String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority()).equals(numberOfHGVs))
            click("submit", SelectorType.ID);
        else {
            click("submitAndPay", SelectorType.ID);
            click("form-actions[pay]", SelectorType.ID);
            world.feeAndPaymentJourney.customerPaymentModule();
        }
        waitForTextToBePresent("Thank you, your application has been submitted.");
    }

    private void loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(String unformattedNumberOfHGVs, String unformattedNumberOfLGVs) {
        String hgvs = unformattedNumberOfHGVs.replaceAll(" ", "");
        String lgvs = unformattedNumberOfLGVs.replaceAll(" ", "");
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        changeAndSaveOperatingCentreVehicleAuthorisation(hgvs, lgvs);
    }

    private void changeAndSaveOperatingCentreVehicleAuthorisation(String hgvs, String lgvs) {
        changeOperatingCentreVehicleAuthorisation(hgvs);
        replaceText(totalHGVAuthorisationField, SelectorType.XPATH, hgvs);
        if (world.licenceCreation.isAGoodsInternationalLicence()) {
            replaceText(totalLGVAuthorisationField, SelectorType.XPATH, lgvs);
        }

        if (FinancialEvidence.licences.get(world.createApplication.getLicenceId()) != null) {
            FinancialEvidence.licences.get(world.createApplication.getLicenceId())[3] = hgvs;
            if (world.licenceCreation.isAGoodsInternationalLicence()) {
                FinancialEvidence.licences.get(world.createApplication.getLicenceId())[4] = lgvs;
            }
        }

        click(saveButton, SelectorType.XPATH);
    }

    private void changeOperatingCentreVehicleAuthorisation(String hgvs) {
        String totalNumberOfHgvsOnOperatingCentres = findElement(totalHgvInTable, SelectorType.XPATH).getText();
        if (!hgvs.equals(totalNumberOfHgvsOnOperatingCentres)) {
            String operatingCentreLink = String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentrePostCode());
            click(operatingCentreLink, SelectorType.XPATH);
            replaceText(operatingCentreVehicleField, SelectorType.XPATH, hgvs);
            if (Integer.parseInt(hgvs) > Integer.parseInt(totalNumberOfHgvsOnOperatingCentres) && world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString()) && !world.createApplication.getOperatorType().equals(LicenceType.RESTRICTED.asString())) {
                waitAndClick(operatingCentreTitle, SelectorType.XPATH);
                waitForElementToBePresent(advertTitle);
            }
            click(submitButton, SelectorType.XPATH);
            waitForTextToBePresent(operatingCentreConfirmationText);
        }
    }
}
