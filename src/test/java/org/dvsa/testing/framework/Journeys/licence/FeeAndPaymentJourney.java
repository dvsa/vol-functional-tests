package org.dvsa.testing.framework.Journeys.licence;

import activesupport.aws.s3.SecretsManager;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FeeAndPaymentJourney extends BasePage {

    private World world;

    public FeeAndPaymentJourney(World world) {
        this.world = world;
    }

    public void createAdminFee(String amount, String feeType) {
        waitAndClick("//button[@id='new']", SelectorType.XPATH);
        waitForTextToBePresent("Create new fee");
        selectValueFromDropDown("fee-details[feeType]", SelectorType.NAME, feeType);
        waitAndEnterText("amount", SelectorType.ID, amount);
        UniversalActions.clickSubmit();
    }

    public void payFee(String amount, @NotNull String paymentMethod) {
        waitForTextToBePresent("Pay fee");
        String payment = paymentMethod.toLowerCase().trim();
        waitForElementToBePresent("//label[contains(text(),'Fee amount')]");
        if (payment.equals("cash") || payment.equals("cheque") || payment.equals("postal")) {
            waitAndEnterText("details[received]", SelectorType.NAME, amount);
            waitAndEnterText("details[payer]", SelectorType.NAME, "Automation payer");
            waitAndEnterText("details[slipNo]", SelectorType.NAME, "1234567");
        }
        switch (payment) {
            case "cash":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cash");
                if (isTextPresent("Customer reference")) {
                    world.internalUIJourney.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                    waitAndEnterText("details[customerName]", SelectorType.NAME, "Jane Doe");
                    waitAndEnterText("details[customerReference]", SelectorType.NAME, "AutomationCashCustomerRef");
                    clickPayAndConfirm(paymentMethod);
                } else {
                    waitAndClick("//*[@id='form-actions[pay]']", SelectorType.XPATH);
                }
                break;
            case "cheque":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cheque");
                if (isTextPresent("Customer reference")) {
                    waitAndEnterText("details[customerReference]", SelectorType.NAME, "AutomationChequeCustomerRef");
                }
                world.internalUIJourney.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                waitAndEnterText("details[chequeNo]", SelectorType.NAME, "12345");
                waitAndEnterText("details[customerName]", SelectorType.NAME, "Jane Doe");

                HashMap<String, String> dates;
                dates = world.globalMethods.date.getDateHashMap(0, 0, 0);

                waitAndEnterText("details[chequeDate][day]", SelectorType.NAME, dates.get("day").toString());
                waitAndEnterText("details[chequeDate][month]", SelectorType.NAME, dates.get("month").toString());
                waitAndEnterText("details[chequeDate][year]", SelectorType.NAME, dates.get("year").toString());
                clickPayAndConfirm(paymentMethod);
                break;
            case "postal":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Postal Order");
                if (isTextPresent("Payer name")) {
                    waitAndEnterText("details[payer]", SelectorType.NAME, "Jane Doe");
                }
                world.internalUIJourney.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                waitAndEnterText("details[customerReference]", SelectorType.NAME, "AutomationPostalOrderCustomerRef");
                waitAndEnterText("details[customerName]", SelectorType.NAME, "Jane Doe");
                waitAndEnterText("details[poNo]", SelectorType.NAME, "123456");
                clickPayAndConfirm(paymentMethod);
                break;
            case "card":
                if (isTextPresent("Pay fee")) {
                    selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Card Payment");
                    if (isTextPresent("Customer reference")) {
                        waitAndEnterText("details[customerName]", SelectorType.NAME, "Veena Skish");
                        waitAndEnterText("details[customerReference]", SelectorType.NAME, "AutomationCardCustomerRef"); // 15 Chars max due to CPSM API value length cap.
                        world.internalUIJourney.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                        clickPayAndConfirm(paymentMethod);
                    }
                    if (isElementPresent("form-actions[pay]", SelectorType.ID)) {
                        waitAndClick("form-actions[pay]", SelectorType.ID);
                    }
                }
                customerPaymentModule();
                break;
        }
    }


    public void selectFeeById(String feeNumber) {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        waitForElementToBeClickable("status", SelectorType.ID);
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        waitForTextToBePresent("Outstanding");
        if (isTextPresent("50")) {
            clickByLinkText("50");
        }
        waitAndClick("//*[@value='" + feeNumber + "']", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Payment method");
    }

    public void selectFee() {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
           refreshPage();
        } while (!isTextPresent("1 Fee") && System.currentTimeMillis() < kickOut);
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        assertTrue(isElementEnabled("//tbody", SelectorType.XPATH));
        waitAndClick("//tbody/tr/td[7]/input", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Pay fee");
    }

    public void customerPaymentModule() {
        if (isTitlePresent("Enter payment details" , 4) || isTitlePresent("Enter card details", 4)) {
            waitAndEnterText("//*[@id='card-no']", SelectorType.XPATH,
                    SecretsManager.getSecretValue("govPayCardNo"));
            waitAndEnterText("//*[@id='expiry-month']", SelectorType.XPATH,
                    SecretsManager.getSecretValue("cardExpiryMonth"));
            waitAndEnterText("//*[@id='expiry-year']", SelectorType.XPATH,
                    SecretsManager.getSecretValue("cardExpiryYear"));
            waitAndEnterText("cardholder-name", SelectorType.ID,
                    world.DataGenerator.getOperatorForeName() + " " + world.DataGenerator.getOperatorFamilyName());
            waitAndEnterText("//*[@id='cvc']", SelectorType.XPATH, "123");
            enterCardHolderDetails();
        } else {
            throw new IllegalStateException("Expected title not found: 'Enter payment details' or 'Enter card details'");
        }
    }

    public void enterCardHolderDetails() {
        if (!isElementPresent("//*[@id='address-line-1']", SelectorType.XPATH)) {
            waitAndEnterText("email", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
            waitAndClick("submit-card-details", SelectorType.ID);
        } else {
            waitAndEnterText("address-line-1", SelectorType.ID, world.DataGenerator.getOperatorAddressLine1());
            waitAndEnterText("address-city", SelectorType.ID, world.DataGenerator.getOperatorTown());
            waitAndEnterText("address-postcode", SelectorType.ID, "NG2 1AW");
            waitAndEnterText("email", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
            waitAndClick("submit-card-details", SelectorType.ID);
        }
        assertFalse(isElementPresent("h1.govuk-heading-l.system-error", SelectorType.CSS),
                "Technical problems error message is displayed.");
    }

    public void clickPayAndConfirm(String paymentMethod) {
        waitForElementToBeClickable("//*[@id='address[searchPostcode][search]']", SelectorType.XPATH);
        waitForElementToBePresent("//*[@id='postcode']");
        UniversalActions.clickPay();
        waitForTextToBePresent("The payment was made successfully");
        if (!paymentMethod.toLowerCase().trim().equals("card"))
            waitForTextToBePresent("The payment was made successfully");
    }
}
