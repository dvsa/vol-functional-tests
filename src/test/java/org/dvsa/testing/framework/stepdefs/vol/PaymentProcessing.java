package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.junit.Assert.*;

public class PaymentProcessing extends BasePage implements En {
    private World world;
    private String currentFeeCount;
    private String feeNumber;

    public String getCurrentFeeCount() {
        return currentFeeCount;
    }

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
        Given("^i am on the payment processing page$", () -> {
            waitAndClick("//li[@class='admin__title']", SelectorType.XPATH);
            clickByLinkText("Payment processing");
            waitForTextToBePresent("Payment Processing");
        });
        When("^i add a new \"([^\"]*)\" fee$", (String arg0) -> {
            String amount = "100";
            selectValueFromDropDown("status", SelectorType.ID, "Current");
            String feeCountBeforeAddingNewFee = getElementValueByText("//div[@class='table__header']/h2", SelectorType.XPATH);
            setCurrentFeeCount(world.genericUtils.stripAlphaCharacters(feeCountBeforeAddingNewFee));
            assertEquals("current", findElement("status", SelectorType.ID, 30).getAttribute("value"));
            world.feeAndPaymentJourneySteps.createAdminFee(amount, arg0);
        });
        Then("^the fee should be created$", () -> {
            // Refresh page
            javaScriptExecutor("location.reload(true)");
            String newFeeCount = world.genericUtils.stripAlphaCharacters(getElementValueByText("//div[@class='table__header']/h3", SelectorType.XPATH));
            assertEquals(getText("//*[contains(text(),'" + getFeeNumber() + "')]//*[contains(@class,'status')]", SelectorType.XPATH), "OUTSTANDING");
            assertNotEquals(currentFeeCount, newFeeCount);
        });
        Then("^the fee should be paid and no longer visible in the fees table$", () -> {
            world.internalNavigation.urlSearchAndViewEditFee(getFeeNumber());
            waitForTextToBePresent("Payments and adjustments");
            javaScriptExecutor("location.reload(true)");
            assertEquals(getText("//*[contains(@class,'status')]", SelectorType.XPATH), "PAID");
        });
        And("^when i pay for the fee by \"([^\"]*)\"$", (String arg0) -> {
            waitForTextToBePresent("Fee No.");
            String feeAmount = String.valueOf(findElement("//*/tbody/tr[1]/td[5]", SelectorType.XPATH, 10).getText()).substring(1);
            setFeeNumber(world.genericUtils.stripAlphaCharacters(String.valueOf(findElement("//*/tbody/tr[1]/td[1]", SelectorType.XPATH, 10).getText())));
            world.feeAndPaymentJourneySteps.selectFeeById(feeNumber);
            if (arg0.equals("card")) {
                world.feeAndPaymentJourneySteps.payFee(null, arg0);
            } else {
                world.feeAndPaymentJourneySteps.payFee(feeAmount, arg0);
            }
        });
    }
}