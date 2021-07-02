package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import com.typesafe.config.Config;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class FeeAndPaymentJourneySteps extends BasePage {

    private World world;

    public FeeAndPaymentJourneySteps(World world){ this.world = world; }

    public void createAdminFee(String amount, String feeType)  {
        waitAndClick("//button[@id='new']", SelectorType.XPATH);
        waitForTextToBePresent("Create new fee");
        selectValueFromDropDown("fee-details[feeType]", SelectorType.NAME, feeType);
        waitAndEnterText("amount", SelectorType.ID, amount);
        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void payFee(String amount, @NotNull String paymentMethod)  {
        String payment = paymentMethod.toLowerCase().trim();
        waitForElementToBePresent("//label[contains(text(),'Fee amount')]");
        if (payment.equals("cash") || payment.equals("cheque") || payment.equals("postal")) {
            waitAndEnterText("details[received]",SelectorType.NAME,amount);
            waitAndEnterText("details[payer]",SelectorType.NAME,"Automation payer");
            waitAndEnterText("details[slipNo]",SelectorType.NAME,"1234567");
        }
        switch (payment) {
            case "cash":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cash");
                if (isTextPresent("Customer reference")) {
                    world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                    waitAndEnterText("details[customerName]",SelectorType.NAME, "Jane Doe");
                    waitAndEnterText("details[customerReference]",SelectorType.NAME, "AutomationCashCustomerRef");
                    clickPayAndConfirm(paymentMethod);
                } else {
                    clickByName("form-actions[pay]");
                }
                break;
            case "cheque":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cheque");
                if (isTextPresent("Customer reference")) {
                    waitAndEnterText("details[customerReference]",SelectorType.NAME, "AutomationChequeCustomerRef");
                }
                world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                waitAndEnterText("details[chequeNo]", SelectorType.NAME, "12345");
                waitAndEnterText("details[customerName]",SelectorType.NAME, "Jane Doe");

                HashMap<String, String> dates;
                dates = world.globalMethods.date.getDateHashMap(0, 0, 0);

                waitAndEnterText("details[chequeDate][day]",SelectorType.NAME, dates.get("day").toString());
                waitAndEnterText("details[chequeDate][month]",SelectorType.NAME, dates.get("month").toString());
                waitAndEnterText("details[chequeDate][year]",SelectorType.NAME, dates.get("year").toString());
                clickPayAndConfirm(paymentMethod);
                break;
            case "postal":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Postal Order");
                if (isTextPresent("Payer name")) {
                    waitAndEnterText("details[payer]",SelectorType.NAME, "Jane Doe");
                }
                world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                waitAndEnterText("details[customerReference]",SelectorType.NAME, "AutomationPostalOrderCustomerRef");
                waitAndEnterText("details[customerName]",SelectorType.NAME, "Jane Doe");
                waitAndEnterText("details[poNo]",SelectorType.NAME, "123456");
                clickPayAndConfirm(paymentMethod);
                break;
            case "card":
                if (isTextPresent("Pay fee")) {
                    selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Card Payment");
                    if (isTextPresent("Customer reference")) {
                        waitAndEnterText("details[customerName]",SelectorType.NAME, "Veena Skish");
                        waitAndEnterText("details[customerReference]", SelectorType.NAME, "AutomationCardCustomerRef");
                        world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                        clickPayAndConfirm(paymentMethod);
                    }
                }
                customerPaymentModule();
                break;
        }
    }


    public void selectFeeById(String feeNumber)  {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        waitForElementToBeClickable("status", SelectorType.ID);
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        waitForTextToBePresent("Outstanding");
        if (isTextPresent("50")){
            clickByLinkText("50");
        }
        waitAndClick("//*[@value='" + feeNumber + "']", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Payment method");
    }

    public void selectFee()  {
        long kickOut = System.currentTimeMillis() + 60000;
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH) && System.currentTimeMillis() < kickOut);
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        assertTrue(isElementEnabled("//tbody", SelectorType.XPATH));
        waitAndClick("//tbody/tr/td[7]/input", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Pay fee");
    }

    public void customerPaymentModule()  {
        Config config = world.configuration.config;
        waitForTextToBePresent("Card Number*");
        enterText("//*[@id='scp_cardPage_cardNumber_input']", SelectorType.XPATH, config.getString("cardNumber"));
        enterText("//*[@id='scp_cardPage_expiryDate_input']", SelectorType.XPATH, config.getString("cardExpiryMonth"));
        enterText("//*[@id='scp_cardPage_expiryDate_input2']", SelectorType.XPATH, config.getString("cardExpiryYear"));
        enterText("//*[@id='scp_cardPage_csc_input']", SelectorType.XPATH, "123");
        if (isElementPresent("scp_cardPage_storedCard_payment_input", SelectorType.ID)) {
            click("scp_cardPage_storedCard_payment_input", SelectorType.ID);
        }
        click("//*[@id='scp_cardPage_buttonsNoBack_continue_button']", SelectorType.XPATH);
        enterText("//*[@id='scp_additionalInformationPage_cardholderName_input']", SelectorType.XPATH, "Mr Regression Test");
        click("//*[@id='scp_additionalInformationPage_buttons_continue_button']", SelectorType.XPATH);
        waitForTextToBePresent("Payment Confirmation Page");
        click("//*[@id='scp_confirmationPage_buttons_payment_button']", SelectorType.XPATH);
        if (isElementPresent("//*[@id='scp_storeCardConfirmationPage_buttons_back_button']", SelectorType.XPATH)) {
            waitForTextToBePresent("Online Payments");
            click("//*[@value='Save']", SelectorType.XPATH);
        }
    }

    public void clickPayAndConfirm(String paymentMethod)  {
        waitForElementToBeClickable("//*[@id='address[searchPostcode][search]']", SelectorType.XPATH);
        waitForElementToBePresent("//*[@id='postcode']");
        waitAndClick("//*[@id='form-actions[pay]']", SelectorType.XPATH);
        if (!paymentMethod.toLowerCase().trim().equals("card"))
            waitForTextToBePresent("The payment was made successfully");
    }
}
