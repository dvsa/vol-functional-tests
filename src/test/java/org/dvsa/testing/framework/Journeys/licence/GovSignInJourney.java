package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.mail.MailSlurp;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

import static activesupport.driver.Browser.navigate;
import static activesupport.qrReader.QRReader.getTOTPCode;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GovSignInJourney extends BasePage {

    private World world;

    public GovSignInJourney(World world) {this.world = world;}

    public void navigateToGovUkSignIn()
    {
        navigate().get("https://integration-user:winter2021@signin.integration.account.gov.uk/");
    }

    public void signInGovAccount() throws Exception {
        String signInUsername = world.configuration.config.getString("signInUsername");
        String signInPassword = world.configuration.config.getString("signInPassword");
        String AUTH_KEY = world.configuration.config.getString("AUTH_KEY");

        clickByXPath("//*[@value='sign-in']");
        waitAndClick("//*[@id='form-tracking']/button", SelectorType.XPATH);
        clickByXPath("//*[@id='havePhotoId']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndClick("sign-in-link", SelectorType.ID);
        waitAndEnterText("email", SelectorType.ID, signInUsername);
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        waitAndEnterText("password", SelectorType.ID, signInPassword);
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        String authCode = getTOTPCode(AUTH_KEY);
        waitAndEnterText("code", SelectorType.ID,authCode);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='select-device-choice']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='smartphone-choice-3']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='journey']");
        waitAndClick("submitButton", SelectorType.ID);
        enterPassportDetails();
        cycletThroughSignInJourney();
        answerPersonalQuestions();
    }
    public void enterPassportDetails() {
        waitAndEnterText("passportNumber", SelectorType.ID, "321654987");
        waitAndEnterText("surname", SelectorType.ID, "Decerqueira");
        waitAndEnterText("firstName", SelectorType.ID, "Kenneth");
        enterDOB();
        enterExpiryDate();
    }

    public void enterDOB() {
        enterText("dateOfBirth-day", SelectorType.NAME, "23");
        enterText("dateOfBirth-month", SelectorType.NAME, "08");
        enterText("dateOfBirth-year", SelectorType.NAME, "1959");
    }

    public void enterExpiryDate() {
      enterText("expiryDate-day", SelectorType.ID, "01");
      enterText("expiryDate-month", SelectorType.ID, "01");
      enterText("expiryDate-year", SelectorType.ID, "2030");
    }

    public void cycletThroughSignInJourney() {
        waitAndClick("submitButton", SelectorType.ID);
        waitAndEnterText("addressSearch", SelectorType.ID, "BA25AA");
        waitAndClick("continue", SelectorType.ID);
        selectValueFromDropDown("addressResults", SelectorType.ID, "8 HADLEY ROAD, BATH, BA2 5AA");
        waitAndClick("continue", SelectorType.ID);
        waitAndEnterText("addressYearFrom", SelectorType.ID, "2000");
        waitAndClick("continue", SelectorType.ID);
        clickByXPath("//*[@id='main-content']/div/div/form/button");
        waitAndClick("continue", SelectorType.ID);
        waitAndClick("submitButton", SelectorType.ID);
    }

    public void answerPersonalQuestions() {
        if(isTitlePresent("How much of your loan do you pay back every month?", 60)) {
            answerPersonalLoan();
        } else if (isTitlePresent("How much do you have left to pay on your mortgage?", 60)) {
            answerMortgageQuestion();
        }
    }

    public void answerPersonalLoan() {
        if(isElementPresent("Q00042-OVER550UPTO600", SelectorType.ID)) {
            clickByXPath("//*[@id='Q00042-OVER550UPTO600']");
        } else if (isElementPresent("Q00042-OVER550UPTO600", SelectorType.ID));
        clickByXPath("//*[@id='Q00042-NONEOFTHEABOVEDOESNOTAPPLY']");
    }

    public void answerMortgageQuestion() {
        if (isElementPresent("Q00015-OVER35000UPTO60000", SelectorType.ID)) {
            clickByXPath("//*[@id='Q00015-OVER35000UPTO60000']");
        }

    }

}
