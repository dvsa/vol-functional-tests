package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentProcessing extends BasePage {
    private final World world;
    private String currentFeeCount;
    private String feeNumber;

    private void setCurrentFeeCount(String currentFeeCount) {
        this.currentFeeCount = currentFeeCount;
    }

    public String getFeeNumber() {
        return feeNumber;
    }

    public void setFeeNumber(String feeNumber) {
        this.feeNumber = feeNumber;
    }

    public PaymentProcessing(World world) {
        this.world = world;
    }

    @When("when i pay for the fee by {string}")
    public void whenIPayForTheFeeBy(String arg0) {
        waitForTextToBePresent("Fee No.");
        String feeAmount = String.valueOf(findElement("//*/tbody/tr[1]/td[5]", SelectorType.XPATH, 10).getText()).substring(1);
        setFeeNumber(world.genericUtils.stripAlphaCharacters(String.valueOf(findElement("//*/tbody/tr[1]/td[1]", SelectorType.XPATH, 10).getText())));
        world.feeAndPaymentJourney.selectFeeById(feeNumber);
        if (arg0.equals("card")) {
            world.feeAndPaymentJourney.payFee(null, arg0);
        } else {
            world.feeAndPaymentJourney.payFee(feeAmount, arg0);
        }
    }

    @Given("i am on the payment processing page")
    public void iAmOnThePaymentProcessingPage() {
        waitAndClick("//li[@class='admin__title']", SelectorType.XPATH);
        clickByLinkText("Payment processing");
        waitForTextToBePresent("Payment Processing");
    }

    @And("i add a new {string} fee")
    public void iAddANewFee(String arg0) {
        String amount = "100";
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        String feeCountBeforeAddingNewFee = getElementValueByText("//*[@class='govuk-table__caption govuk-table__caption--m']", SelectorType.XPATH);
        setCurrentFeeCount(world.genericUtils.stripAlphaCharacters(feeCountBeforeAddingNewFee));
        assertEquals("current", findElement("status", SelectorType.ID, 30).getAttribute("value"));
        world.feeAndPaymentJourney.createAdminFee(amount, arg0);
    }

    @Then("the fee should be paid and no longer visible in the fees table")
    public void theFeeShouldBePaidAndNoLongerVisibleInTheFeesTable() {
        world.internalNavigation.getAdminEditFee(getFeeNumber());
        waitForTextToBePresent("Payments and adjustments");
        refreshPageWithJavascript();
        assertEquals(getText("//*[contains(@class,'govuk-tag govuk-tag--green')]", SelectorType.XPATH), "PAID");
    }
}