package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import com.typesafe.config.Config;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.HashMap;

public class FeeAndPaymentJourneySteps extends BasePage {

    private World world;

    public FeeAndPaymentJourneySteps(World world){ this.world = world; }

    public void createAdminFee(String amount, String feeType) throws IllegalBrowserException, MalformedURLException {
        waitAndClick("//button[@id='new']", SelectorType.XPATH);
        waitForTextToBePresent("Create new fee");
        selectValueFromDropDown("fee-details[feeType]", SelectorType.NAME, feeType);
        waitAndEnterText("amount", SelectorType.ID, amount);
        waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
    }

    public void payFee(String amount, @NotNull String paymentMethod) throws IllegalBrowserException, MalformedURLException {
        String payment = paymentMethod.toLowerCase().trim();
        waitForTextToBePresent("Pay fee");
        if (payment.equals("cash") || payment.equals("cheque") || payment.equals("postal")) {
            enterText("details[received]", amount, SelectorType.NAME);
            enterText("details[payer]", "Automation payer", SelectorType.NAME);
            enterText("details[slipNo]", "1234567", SelectorType.NAME);
        }
        switch (payment) {
            case "cash":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cash");
                if (isTextPresent("Customer reference", 10)) {
                    enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
                    enterText("details[customerReference]", "AutomationCashCustomerRef", SelectorType.NAME);
                    world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                    clickPayAndConfirm(paymentMethod);
                } else {
                    clickByName("form-actions[pay]");
                }
                break;
            case "cheque":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Cheque");
                if (isTextPresent("Customer reference", 10)) {
                    enterText("details[customerReference]", "AutomationChequeCustomerRef", SelectorType.NAME);
                }
                enterText("details[chequeNo]", "12345", SelectorType.NAME);
                enterText("details[customerName]", "Jane Doe", SelectorType.NAME);

                HashMap<String, Integer> dates;
                dates = world.globalMethods.date.getDate(0, 0, 0);

                enterText("details[chequeDate][day]", dates.get("day").toString(), SelectorType.NAME);
                enterText("details[chequeDate][month]", dates.get("month").toString(), SelectorType.NAME);
                enterText("details[chequeDate][year]", dates.get("year").toString(), SelectorType.NAME);
                world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                clickPayAndConfirm(paymentMethod);
                break;
            case "postal":
                selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Postal Order");
                if (isTextPresent("Payer name", 10)) {
                    enterText("details[payer]", "Jane Doe", SelectorType.NAME);
                }
                enterText("details[customerReference]", "AutomationPostalOrderCustomerRef", SelectorType.NAME);
                enterText("details[customerName]", "Jane Doe", SelectorType.NAME);
                enterText("details[poNo]", "123456", SelectorType.NAME);
                world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                clickPayAndConfirm(paymentMethod);
                break;
            case "card":
                if (isTextPresent("Pay fee", 10)) {
                    selectValueFromDropDown("details[paymentType]", SelectorType.NAME, "Card Payment");
                    if (isTextPresent("Customer reference", 10)) {
                        enterText("details[customerName]", "Veena Skish", SelectorType.NAME);
                        enterText("details[customerReference]", "AutomationCardCustomerRef", SelectorType.NAME);
                        world.UIJourneySteps.searchAndSelectAddress("postcodeInput1", "NG1 5FW", 1);
                        clickPayAndConfirm(paymentMethod);
                    }
                }
                customerPaymentModule();
                break;
        }
    }


    public void selectFeeById(String feeNumber) throws IllegalBrowserException, MalformedURLException {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        waitForElementToBeClickable("status", SelectorType.ID);
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        waitForTextToBePresent("Outstanding");
        if (isTextPresent("50", 10)){
            clickByLinkText("50");
        }
        waitAndClick("//*[@value='" + feeNumber + "']", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Payment method");
    }


    public void selectFee() throws IllegalBrowserException, MalformedURLException {
        do {
            //nothing
        } while (isElementPresent("//button[@id='form-actions[submit]']", SelectorType.XPATH));
        selectValueFromDropDown("status", SelectorType.ID, "Current");
        isElementEnabled("//tbody", SelectorType.XPATH);
        waitAndClick("//tbody/tr/td[7]/input", SelectorType.XPATH);
        waitAndClick("//*[@value='Pay']", SelectorType.XPATH);
        waitForTextToBePresent("Pay fee");
    }

    public void customerPaymentModule() throws IllegalBrowserException, MalformedURLException {
        Config config = world.configuration.config;
        waitForTextToBePresent("Card Number*");
        enterText("//*[@id='scp_cardPage_cardNumber_input']", config.getString("cardNumber"), SelectorType.XPATH);
        enterText("//*[@id='scp_cardPage_expiryDate_input']", config.getString("cardExpiryMonth"), SelectorType.XPATH);
        enterText("//*[@id='scp_cardPage_expiryDate_input2']", config.getString("cardExpiryYear"), SelectorType.XPATH);
        enterText("//*[@id='scp_cardPage_csc_input']", "123", SelectorType.XPATH);
        click("//*[@id='scp_cardPage_buttonsNoBack_continue_button']", SelectorType.XPATH);
        enterText("//*[@id='scp_additionalInformationPage_cardholderName_input']", "Mr Regression Test", SelectorType.XPATH);
        click("//*[@id='scp_additionalInformationPage_buttons_continue_button']", SelectorType.XPATH);
        waitForTextToBePresent("Payment Confirmation Page");
        click("//*[@id='scp_confirmationPage_buttons_payment_button']", SelectorType.XPATH);
        if (isElementPresent("//*[@id='scp_storeCardConfirmationPage_buttons_back_button']", SelectorType.XPATH)) {
            waitForTextToBePresent("Online Payments");
            click("//*[@value='Save']", SelectorType.XPATH);
        }
    }

    public void clickPayAndConfirm(String paymentMethod) throws IllegalBrowserException, MalformedURLException {
        waitForElementToBeClickable("//*[@id='address[searchPostcode][search]']", SelectorType.XPATH);
        waitAndClick("//*[@id='form-actions[pay]']", SelectorType.XPATH);
        if (!paymentMethod.toLowerCase().trim().equals("card"))
            waitForTextToBePresent("The payment was made successfully");
    }
}
