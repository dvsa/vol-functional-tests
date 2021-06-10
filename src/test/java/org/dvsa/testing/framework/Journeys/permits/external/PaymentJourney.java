package org.dvsa.testing.framework.Journeys.permits.external;

import activesupport.string.Str;
import org.dvsa.testing.framework.Utils.common.RandomUtils;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.permit.FeeCardDetailsPage;
import org.dvsa.testing.lib.pages.external.permit.FeeCardHolderDetailsPage;
import org.dvsa.testing.lib.pages.external.permit.FeePaymentConfirmationPage;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.dvsa.testing.lib.newPages.Driver.DriverUtils.getDriver;

public interface PaymentJourney {

    default PaymentJourney cardDetailsPage() {
        Date date = new Date();
        SimpleDateFormat monthDF = new SimpleDateFormat("M");
        SimpleDateFormat yearDF = new SimpleDateFormat("YY");
        int currentMonth = Integer.parseInt(monthDF.format(date));
        int currentYear = Integer.parseInt(yearDF.format(date));
        FeeCardDetailsPage.cardNumber("4543059790016721");
        FeeCardDetailsPage.expiryDate(currentMonth, currentYear);
        FeeCardDetailsPage.securityCode(587);
        FeeCardDetailsPage.continueButton();

        return this;
    }

    default PaymentJourney cardHolderDetailsPage() {
        String email = RandomUtils.email();
        FeeCardHolderDetailsPage.cardHoldersName(Str.randomWord(3, 8) + " " + Str.randomWord(3, 8));
        FeeCardHolderDetailsPage.email(email);
        FeeCardHolderDetailsPage.emailConfirmation(email);
        FeeCardHolderDetailsPage.continueButton();

        return this;
    }

    default PaymentJourney confirmAndPay() {
        FeePaymentConfirmationPage.makeMayment();
        return this;
    }

    default PaymentJourney passwordAuthorisation() {
        WebDriver driver = getDriver();
        driver.switchTo().frame("scp_threeDSecure_iframe");
        FeePaymentConfirmationPage.passwordAuthorisation("Test_6721");
        BasePage.waitAndClick("//input[@id='Continue']", SelectorType.XPATH);
        return this;
    }
}
