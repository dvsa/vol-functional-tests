package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.mail.MailSlurp;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.stringtemplate.v4.ST;

import java.util.UUID;

import static activesupport.driver.Browser.navigate;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GovSignInJourney extends BasePage {

    private World world;

    MailSlurp mailSlurp = new MailSlurp();

    public GovSignInJourney(World world) {this.world = world;}

    public void navigateToGovUkSignIn()
    {
        world.selfServeNavigation.navigateToGovSignIn();
        clickByLinkText("Login");
        waitAndClick("Login with GOV.UK Sign In", SelectorType.LINKTEXT);
        navigate().manage().deleteAllCookies();
        navigate().get("https://integration-user:winter2021@signin.integration.account.gov.uk/");
    }

    public void createGovAccount() throws Exception {
        waitAndClick("create-account-link", SelectorType.ID);
        waitAndEnterText("email", SelectorType.ID, mailSlurp.inbox().getEmailAddress());
        waitAndClick("//button[@type='Submit']", SelectorType.XPATH);
        String emailID = String.valueOf(mailSlurp.inbox().getId());
        assertTrue(mailSlurp.hasEmailBeenReceived(UUID.fromString(emailID), "Your security code for your GOV.UK account | Eich cod diogelwch ar gyfer eich cyfrif GOV.UK"));
        assertNotNull(mailSlurp.RetrieveVerificationCode(UUID.fromString(emailID)));
        String verificationCode = mailSlurp.RetrieveVerificationCode(UUID.fromString(emailID));
        waitAndEnterText("code", SelectorType.ID, verificationCode);

    }


}
