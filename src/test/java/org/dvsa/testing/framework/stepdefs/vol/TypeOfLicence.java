package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import apiCalls.enums.*;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.WebElement;
import java.util.List;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.returnNthNumberSequenceInString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeOfLicence extends BasePage {
    World world;

    public TypeOfLicence(World world){
        this.world = world;
    }

    @Given("I apply for a {string} {string} {string} {string} {string} licence")
    public void iApplyForALicence(String licenceWhere, String operatorType, String licenceType, String vehicleType, String lgvUndertaking) {
        clickByLinkText("Apply for a new licence");
        world.typeOfLicenceJourney.chooseGBOrNI(licenceWhere);
        if (licenceWhere.equals("GB")) {
            clickByXPath("//input[@value='" + OperatorType.valueOf(operatorType.toUpperCase()).asString() + "']");
        }
        world.selfServeUIJourney.inputLicenceAndVehicleType(licenceType, vehicleType, lgvUndertaking);
    }

    @Given("I {string} the LGV undertaking declaration checkbox")
    public void iCheckTheLGVUndertakingDeclaration(String checkBoxAction) {
        if (checkBoxAction.equals("select")){
            clickByXPath(world.typeOfLicenceJourney.lgvDeclarationCheckbox);
        }
    }

    @Given("I go to update the vehicle type on the licence to {string} {string} {string}")
    public void iGoToUpdateVehicleTypeOnLicence(String newLicenceType, String newVehicleType, String newLgvUndertaking) {
        clickByLinkText("Type of licence");
        world.selfServeUIJourney.inputLicenceAndVehicleType(newLicenceType, newVehicleType, newLgvUndertaking);
    }

    @Then("A LGV only error message should be displayed")
    public void lgvOnlyErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        waitForTextToBePresent("There is a problem");
        assertTrue(isElementPresent("//a[contains(text(),'Will you only be operating Light goods vehicles on this licence?')]", SelectorType.XPATH));
    }

    @Then("A LGV undertakings error message should be displayed")
    public void lgvUndertakingsErrorMessage() {
        isElementPresent("//div[@class=\"validation-summary\"]", SelectorType.XPATH);
        waitForTextToBePresent("There is a problem");
        assertTrue(isTextPresent("You must confirm you have read and agree to the undertaking to apply for this licence type."));
    }

    @Then("A change licence type warning message is displayed")
    public void changeLicenceWarningMessage() {
        assertTrue(isTextPresent("Are you sure you want to make this change?"));
    }

    @When("I confirm the warning message")
    public void iConfirmWarningMessage() {
        UniversalActions.clickSubmit();
    }

    @When("I cancel the warning message and click cancel on the type of licence page")
    public void iCancelWarningMessageAndClickCancelOnTheTypeOfLicencePage() {
        UniversalActions.clickCancel();
        UniversalActions.clickCancel();
    }

    @When("each section on the application overview page has the correct status for the {string} licence")
    public void changeLicenceTypeOverviewSectionsStatus(String newType) {
        assertTrue(isTextPresent("Apply for a new licence"));
        List<WebElement> applicationOverviewStatusElements = findElements("//ol[@class='overview__list']/li", SelectorType.XPATH);

        if (newType.equals("lgv_only_fleet")) {
            for (int i = 0; i < applicationOverviewStatusElements.size(); i++) {
                assertEquals(world.typeOfLicenceJourney.expectedLgvOnlyStatusArray[i], applicationOverviewStatusElements.get(i).getText());
            }
        } else {
            for (int i = 0; i < applicationOverviewStatusElements.size(); i++) {
                assertEquals(world.typeOfLicenceJourney.expectedStandardNationalOrMixedFleetStatusArray[i], applicationOverviewStatusElements.get(i).getText());
            }
        }
    }

    @When("i go to apply for a {string} goods standard international licence")
    public void iManuallyApplyForAGoodsStandardInternationalLicence(String licenceWhere) {
        clickByLinkText("Apply for a new licence");
        waitForTitleToBePresent("Type of licence");
        world.typeOfLicenceJourney.chooseGBOrNI(licenceWhere);
        if (licenceWhere.equals("GB"))
            click(world.typeOfLicenceJourney.goodsLicence, SelectorType.XPATH);
        click(world.typeOfLicenceJourney.standardInternational, SelectorType.XPATH);
    }

    @Then("i am prompted with the choice of LGV Mixed and LGV Only applications")
    public void iAmPromptedWithTheChoiceOfLGVMixedAndLGVOnlyApplications() {
        world.typeOfLicenceJourney.isLGVChoiceTextAndRadioButtonsPresent();
    }

    @And("i choose to have light goods vehicles only and click save and continue")
    public void iChooseToHaveLightGoodsVehiclesOnlyAndClickSaveAndContinue() {
        click(world.typeOfLicenceJourney.lgvOnly, SelectorType.XPATH);
        click(world.typeOfLicenceJourney.lgvDeclarationCheckbox, SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();
        String url = navigate().getCurrentUrl();
        world.createApplication.setApplicationId(returnNthNumberSequenceInString(url, 1));
    }

    @And("i choose to have mixed vehicles and click save and continue")
    public void iChooseToHaveMixedVehiclesAndClickSaveAndContinue() {
        click(world.typeOfLicenceJourney.mixedFleet, SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();
        String url = navigate().getCurrentUrl();
        world.createApplication.setApplicationId(returnNthNumberSequenceInString(url, 1));
    }

    @Then("the caseworker can review the {string} LGV Only choice on internal")
    public void theCaseworkerCanReviewTheLGVOnlyChoiceOnInternal(String choice) throws HttpException {
        world.internalNavigation.navigateToPage("application", SelfServeSection.TYPE_OF_LICENCE);
        world.typeOfLicenceJourney.isLGVChoiceTextAndRadioButtonsPresent();

        if (choice.equals("yes")) {
            assertTrue(findElement(world.typeOfLicenceJourney.lgvOnly, SelectorType.XPATH).isSelected());
            assertTrue(findElement(world.typeOfLicenceJourney.lgvDeclarationCheckbox,SelectorType.XPATH).isSelected());
        } else {
            assertTrue(findElement(world.typeOfLicenceJourney.mixedFleet, SelectorType.XPATH).isSelected());
        }
    }

    @When("each section on the application overview page should have the complete status with no data deleted")
    public void eachSectionOnTheApplicationOverviewPageShouldHaveTheCompleteStatusWithNoDataDeleted() {
        assertTrue(isTextPresent("Apply for a new licence"));
        List<WebElement> applicationOverviewStatusElements = findElements("//ol[@class='overview__list']/li", SelectorType.XPATH);
        for (int i = 0; i < applicationOverviewStatusElements.size(); i++) {
            assertTrue(applicationOverviewStatusElements.get(i).getText().contains("COMPLETE"));
        }
    }

    @When("a caseworker goes to apply for a goods standard_international licence")
    public void aCaseworkerGoesToApplyForAGoodsStandard_internationalLicence() throws HttpException {
        String organisationId = world.userDetails.getOrganisationId().substring(1, world.userDetails.getOrganisationId().length() - 1);
        String internalOrganisationUrl = String.format("%soperator/%s/licences/", URL.build(ApplicationType.INTERNAL, world.configuration.env).toString(), organisationId);
        world.internalNavigation.loginIntoInternal("");
        get(internalOrganisationUrl);
        waitForTitleToBePresent(world.registerUser.getOrganisationName());
        clickByLinkText("New application");
        waitForTextToBePresent("Application received");
        click(world.typeOfLicenceJourney.goodsLicence, SelectorType.XPATH);
        click(world.typeOfLicenceJourney.standardInternational, SelectorType.XPATH);
        if (isElementPresent("//input[@id='appliedVia']", SelectorType.XPATH)) {
            click("//input[@id='appliedVia']", SelectorType.XPATH);
        }
    }

    @When("i choose to have light goods vehicles only and click create")
    public void iChooseToHaveLightGoodsVehiclesOnlyAndClickCreate() {
        click(world.typeOfLicenceJourney.lgvOnly, SelectorType.XPATH);
        click(world.typeOfLicenceJourney.lgvDeclarationCheckbox, SelectorType.XPATH);
        UniversalActions.clickSubmit();
    }

    @When("i choose to have mixed vehicles and create")
    public void iChooseToHaveMixedVehiclesAndCreate() {
        click(world.typeOfLicenceJourney.mixedFleet, SelectorType.XPATH);
        UniversalActions.clickSubmit();
    }

    @Then("the caseworker is navigated to the lgv only application overview")
    public void theCaseworkerIsNavigatedToTheLgvOnlyApplicationOverview() {
        waitForTextToBePresent("Application details");
        assertTrue(isLinkPresent("Licence authorisation", 10));
    }

    @Then("the caseworker is navigated to the lgv mixed application overview")
    public void theCaseworkerIsNavigatedToTheLgvMixedApplicationOverview() {
        waitForTextToBePresent("Application details");
        assertTrue(isLinkPresent("Operating centres and authorisation", 10));
    }

    @And("the type of licence section is marked as complete")
    public void theTypeOfLicenceSectionIsMarkedAsComplete() {
        assertTrue(getAttribute("//a[contains(text(),'Type of licence')]/..", SelectorType.XPATH, "class").contains("complete"));
        assertTrue(getAttribute("//a[contains(text(),'Business type')]/..", SelectorType.XPATH, "class").contains("complete"));
    }

    @And("i choose to have light goods vehicles only and click create without confirming the declaration")
    public void iChooseToHaveLightGoodsVehiclesOnlyAndClickCreateWithoutConfirmingTheDeclaration() {
        click(world.typeOfLicenceJourney.lgvOnly, SelectorType.XPATH);
        UniversalActions.clickSubmit();
    }
}