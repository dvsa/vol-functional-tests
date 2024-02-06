package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class MessagingSelfServe extends BasePage {

    @Then("i click on Start a new conversation link")
        public void iClickOnStartANewConversationLink(){
        clickByLinkText("Start a new conversation");
    }
}
