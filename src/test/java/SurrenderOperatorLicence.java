import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class SurrenderOperatorLicence extends BasePage implements En {
    public SurrenderOperatorLicence(World world) {
        When("^the user selects In your possession option$", () -> {
        click("//*[contains(text(),'possession')]", SelectorType.XPATH);
        });

        Then("^they should be presented with the text You must destroy your operator licence$", () -> {
            assertTrue(isTextPresent("You must destroy your operator licence",5));
        });

        When("^the user select the Lost option$", () -> {
            click("//*[contains(text(),'Lost')]", SelectorType.XPATH);

        });

        Then("^they are presented with the lost additional information text box", () -> {
            assertTrue(isElementPresent("//*[@class='govuk-radios__conditional']//*[@class='govuk-form-group']",SelectorType.XPATH));
        });

        When("^the user select the Stolen option$", () -> {
            click("//*[contains(text(),'Stolen')]", SelectorType.XPATH);

        });

        Then("^they are presented with the stolen additional information text box$", () -> {
            assertTrue(isElementPresent("//*[@class='govuk-radios__conditional']//*[@class='govuk-form-group']",SelectorType.XPATH));

        });

        When("^I click on Save and continue$", () -> {
            //TODO add step to click on save button

        });

        Then("^I should be taken to the Review your discs and documentations page$", () -> {
            assertTrue(Browser.navigate().getCurrentUrl().contains("review"));

        });
        And("^the correct discs details should be displayed$", () -> {
            String licenceNumber = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][1]/div[@class='app-check-your-answers__contents'][1]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            assertEquals(world.updateLicence.getLicenceStatusDetails(),licenceNumber);
            String licenceHolder = getText("//*[@class='app-check-your-answers app-check-your-answers--long'][1]/div[@class='app-check-your-answers__contents'][2]/dd[@class='app-check-your-answers__answer']", SelectorType.XPATH);
            assertEquals(world.createLicence.getForeName()+" "+world.createLicence.getFamilyName(),licenceHolder);

        });
        And("^the correct documentation details should be displayed$", () -> {


        });
        And("^i navigate to the operator licence page$", () -> {
           world.UIJourneySteps.navigateToOperatorLicencePage();




        });
    }
}
