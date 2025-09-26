package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodVarUpgrade extends BasePage {

    private final World world;
    String upgradeInterimSubmittedText1 = "You must follow the terms and conditions of your current licence until we've granted either:";
    String upgradeInterimSubmittedText2 = "your variation application";
    String upgradeInterimSubmittedText3 = "an interim authorisation";

    public GoodVarUpgrade(World world) {
        this.world = world;
    }

    @When("i upgrade my licence type to Standard National")
    public void iUpgradeMyLicenceTypeToStandardNational() {
        world.generalVariationJourney.beginUpgradeVariation();
        waitAndClick(world.typeOfLicenceJourney.standardNational, SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
    }

    @Then("correct statuses are shown by the correct seven sections")
    public void correctStatusesAreShownByTheCorrectSevenSections() {
        String typeOfLicenceStatus = getText("//a[@id='overview-item__type_of_licence']//strong[1]", SelectorType.XPATH);
        assertTrue(typeOfLicenceStatus.equalsIgnoreCase("UPDATED"));
        String addressesStatus = getText("//a[@id='overview-item__addresses']//strong[1]", SelectorType.XPATH);
        assertTrue(addressesStatus.equalsIgnoreCase("REQUIRES ATTENTION"));
        String financialEvidenceStatus = getText("//a[@id='overview-item__financial_evidence']//strong[1]", SelectorType.XPATH);
        assertTrue(financialEvidenceStatus.equalsIgnoreCase("REQUIRES ATTENTION"));
        String transportManagersStatus = getText("//a[@id='overview-item__transport_managers']//strong[1]", SelectorType.XPATH);
        assertTrue(transportManagersStatus.equalsIgnoreCase("REQUIRES ATTENTION"));
        String financialHistoryStatus = getText("//a[@id='overview-item__financial_history']//strong[1]", SelectorType.XPATH);
        assertTrue(financialHistoryStatus.equalsIgnoreCase("REQUIRES ATTENTION"));
        String convictionsPenaltiesStatus = getText("//a[@id='overview-item__convictions_penalties']//strong[1]", SelectorType.XPATH);
        assertTrue(convictionsPenaltiesStatus.equalsIgnoreCase("REQUIRES ATTENTION"));
        String reviewDeclarationsStatus = getText("//div[@class='overview__item']//strong[1]", SelectorType.XPATH);
        assertTrue(reviewDeclarationsStatus.equalsIgnoreCase("CAN'T START YET"));
    }

    @And("i complete the required five sections")
    public void iCompleteTheRequiredFiveSections() {
        UniversalActions.clickHome();
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.ADDRESSES);
        UniversalActions.clickSaveAndReturn();
        UniversalActions.clickHome();
        world.selfServeUIJourney.completeFinancialEvidencePage();
        refreshPage();
        UniversalActions.clickHome();
        world.TMJourney.addNewPersonAsTransportManager("variation");
        UniversalActions.clickHome();
        scrollToBottom();
        refreshPage();
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.FINANCIAL_HISTORY);
        world.financialHistoryJourney.answerNoToAllQuestionsAndSubmit("variation");
        UniversalActions.clickHome();
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.CONVICTIONS_AND_PENALTIES);
        world.convictionsAndPenaltiesJourney.answerNoToAllQuestionsAndSubmit("variation");
        UniversalActions.clickHome();
    }

    @Then("the upgrade variation and interim are submitted")
    public void theUpgradeVariationAndInterimAreSubmitted() {
        assertTrue(isTitlePresent("Application overview", 30));
        String warningText = getText("//span[@class='govuk-warning-text__icon']/following-sibling::strong[1]", SelectorType.XPATH);
        assertTrue(warningText.contains(upgradeInterimSubmittedText1));
        assertTrue(warningText.contains(upgradeInterimSubmittedText2));
        assertTrue(warningText.contains(upgradeInterimSubmittedText3));
    }
}
