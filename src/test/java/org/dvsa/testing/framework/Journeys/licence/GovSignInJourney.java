package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.driver.Browser;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import java.util.Random;

import static activesupport.driver.Browser.configuration;
import static activesupport.driver.Browser.navigate;
import static activesupport.qrReader.QRReader.getTOTPCode;



public class GovSignInJourney extends BasePage {

    private World world;

    public GovSignInJourney(World world) {
        this.world = world;
    }

    Random random = new Random();

    String registrationEmail = "DVSA.Tester+" + random.nextInt(900) + "@dev-dvsacloud.uk";

    public void navigateToGovUkSignIn() {
        if(isTextPresent("Declaration information")) {
            clickById("sign");
        }
        String userName = world.configuration.config.getString("basicAuthUserName");
        String passWord = world.configuration.config.getString("basicAuthPassword");
        navigate().get(String.format("https://%s:%s@signin.integration.account.gov.uk/",userName,passWord));
    }

    public void signInGovAccount() {
        String signInUsername = world.configuration.config.getString("signInUsername");
        String signInPassword = world.configuration.config.getString("signInPassword");
        String AUTH_KEY = world.configuration.config.getString("AUTH_KEY");

        if(isTitlePresent("Prove your identity with a GOV.UK account", 1) &&
                (isTextPresent("Choose a way to prove your identity"))) {
            clickById("chooseWayPyi");
            waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        } else {
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }
        if(isTitlePresent("You’ve signed in to your GOV.UK account", 1)) {
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }
        if(isTitlePresent("You must have a photo ID to prove your identity with a GOV.UK account", 1)) {
            photoIDQuestion();
        }
        if(isTitlePresent("Create a GOV.UK account or sign in",1)) {
            waitAndClick("sign-in-link", SelectorType.ID);
            waitAndEnterText("email", SelectorType.ID, signInUsername);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
            waitAndEnterText("password", SelectorType.ID, signInPassword);
            waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
            String authCode = getTOTPCode(AUTH_KEY);
            waitAndEnterText("code", SelectorType.ID, authCode);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
            waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        }
        if (isTitlePresent("You have already proved your identity", 2)) {
          waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        } else if (isTitlePresent("Do you have a smartphone you can use?", 2))
         goThroughVerificationSteps();
    }

    public void registerGovAccount() {
        if(isTitlePresent("Prove your identity with a GOV.UK account", 2)) {
            clickByXPath("//*[@id='form-tracking']/button");
        } else {
            clickById("chooseWayPyi");
        }
        waitAndClick("//*[@id='form-tracking']/button", SelectorType.XPATH);
        photoIDQuestion();
        clickById("create-account-link");
        waitAndEnterText("email", SelectorType.ID, registrationEmail);
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndEnterText("code", SelectorType.ID, world.configuration.getGovCode());
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='havePhotoId']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        waitAndClick("sign-in-link", SelectorType.ID);
        waitAndEnterText("email", SelectorType.ID, registrationEmail);
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        clickByLinkText("Sign in to a service");
    }

    public void alreadySignedIn() {
    waitForTitleToBePresent("You’ve signed in to your GOV.UK account");
    waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
    waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
    signInGovAccount();
    }

    public void goThroughVerificationSteps() {
        clickByXPath("//*[@id='smartphone-choice-3']");
        waitAndClick("//*[contains(text(),'Continue')]", SelectorType.XPATH);
        clickByXPath("//*[@id='journey']");
        waitAndClick("submitButton", SelectorType.ID);
        enterPassportDetails();
        cycletThroughSignInJourney();
        answerPersonalQuestions();
    }

    public void photoIDQuestion() {
        waitForTitleToBePresent("You must have a photo ID to prove your identity with a GOV.UK account");
        clickByXPath("//*[@id='havePhotoId']");
        clickByXPath("//*[@id='form-tracking']/button");
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
        int i;
        for (i = 0; i < 5; i++) {
            if (isTitlePresent("How much of your loan do you pay back every month?", 2)) {
                answerPersonalLoan();
            } else if (isTitlePresent("How much do you have left to pay on your mortgage?", 2)) {
                answerMortgageQuestion();
            } else if (isTitlePresent("How much is your monthly mortgage payment?", 2)) {
                answerMonthlyPaymentQuestion();
            } else if (isTitlePresent("How much do you have left to pay on your mortgage?", 2)) {
                answerLeftToPayOnYourMortgageQuestion();
            } else if (isTitlePresent("When was the other person on your mortgage born??", 2)) {
                answerOtherPersonQuestion();
            } else if (isTitlePresent("What is the name of your loan provider?", 2))
                answerBankingQuestion();
            if (isElementNotPresent("//input[@type='radio']", SelectorType.XPATH)){
                break;
            }
        }
    }

    public void answerPersonalLoan() {
        if (isTextPresent("UP TO £ 600")) {
            clickById("Q00042");
        } else if (isTextPresent("OVER £550 UP TO £600")) {
            clickById("Q00042-OVER550UPTO600");
        } else {
            clickById("Q00042-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }

    public void answerMortgageQuestion() {
        if (isTextPresent("UP TO £ 60,000")) {
            clickById("Q00015");
        } else if (isTextPresent("OVER £35,000 UP TO £60,000")) {
            clickById("Q00015-OVER35000UPTO60000");
        } else {
            clickById("Q00015-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }
    public void answerMonthlyPaymentQuestion() {
        if (isTextPresent("OVER £500 UP TO £600")) {
            clickById("Q00018-OVER500UPTO600");
        } else if (isTextPresent("UP TO £ 600")) {
            clickById("Q00018");
        } else {
            clickById("Q00018-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }


    public void answerBankingQuestion() {
        if (isElementPresent("Q00048-LLOYDSTSBBANKPLC", SelectorType.ID)) {
            clickByXPath("Q00048-LLOYDSTSBBANKPLC");
            waitAndClick("continue", SelectorType.ID);
        } else {
            clickByXPath("//input[@value='NONE OF THE ABOVE / DOES NOT APPLY']");
            waitAndClick("continue", SelectorType.ID);
        }
    }

    public void answerOtherPersonQuestion() {
        if (isElementPresent("Q00020-101967", SelectorType.ID)) {
            clickByXPath("//*[@id='Q00020-101967']");
            waitAndClick("continue", SelectorType.ID);
        } else {
            clickByXPath("//input[@value='NONE OF THE ABOVE / DOES NOT APPLY']");
            waitAndClick("continue", SelectorType.ID);
        }
    }

    public void answerLeftToPayOnYourMortgageQuestion() {
        if (isTextPresent("OVER £35,000 UP TO £60,000")) {
            clickById("Q00015-OVER35000UPTO60000");
        } else if (isTextPresent("UP TO £ 60,000")) {
            clickById("Q00015");
        } else {
            clickById("Q00015-NONEOFTHEABOVEDOESNOTAPPLY");
        }
        clickById("continue");
    }
}


