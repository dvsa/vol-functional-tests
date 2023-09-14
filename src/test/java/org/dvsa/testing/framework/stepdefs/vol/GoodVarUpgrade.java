package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodVarUpgrade extends BasePage {
    private final World world;

    public GoodVarUpgrade (World world) {this.world = world;}

    @When("i upgrade my licence type to Standard National")
    public void iUpgradeMyLicenceTypeToStandardNational() {
        world.generalVariationJourney.beginUpgradeVariation();
        click(world.typeOfLicenceJourney.standardNational, SelectorType.XPATH);
        world.UIJourney.clickSaveAndReturn();
    }

    @Then("correct statuses are shown by the correct seven sections")
    public void correctStatusesAreShownByTheCorrectSevenSections() {
        String typeOfLicenceStatus = getText("//a[@id='overview-item__type_of_licence']//strong[1]", SelectorType.XPATH);
        assertTrue(typeOfLicenceStatus.contains("UPDATED"));
        String addressesStatus = getText("//a[@id='overview-item__addresses']//strong[1]", SelectorType.XPATH);
        assertTrue(addressesStatus.contains("REQUIRES ATTENTION"));
        String financialEvidenceStatus = getText("//a[@id='overview-item__financial_evidence']//strong[1]", SelectorType.XPATH);
        assertTrue(financialEvidenceStatus.contains("REQUIRES ATTENTION"));
        String transportManagersStatus = getText("//a[@id='overview-item__transport_managers']//strong[1]", SelectorType.XPATH);
        assertTrue(transportManagersStatus.contains("REQUIRES ATTENTION"));
        String financialHistoryStatus = getText("//a[@id='overview-item__financial_history']//strong[1]",SelectorType.XPATH);
        assertTrue(financialHistoryStatus.contains("REQUIRES ATTENTION"));
        String convictionsPenaltiesStatus = getText("//a[@id='overview-item__convictions_penalties']//strong[1]", SelectorType.XPATH);
        assertTrue(convictionsPenaltiesStatus.contains("REQUIRES ATTENTION"));
        String reviewDeclarationsStatus = getText("//div[@class='overview__item']//strong[1]", SelectorType.XPATH);
        assertTrue(reviewDeclarationsStatus.contains("CAN'T START YET"));
    }

    @And("i complete the required five sections")
    public void iCompleteTheRequiredFiveSections() {
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.ADDRESSES);
        UIJourney.clickSaveAndReturn();
        world.UIJourney.completeFinancialEvidencePage();
        world.TMJourney.addNewPersonAsTransportManager("variation");
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.FINANCIAL_HISTORY);
        world.financialHistoryJourney.answerNoToAllQuestionsAndSubmit("variation");
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.CONVICTIONS_AND_PENALTIES);
        world.convictionsAndPenaltiesJourney.answerNoToAllQuestionsAndSubmit("variation");









    }
}
