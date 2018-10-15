package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TmVerifySameOperator extends BasePage implements En {

    public TmVerifySameOperator(World world) {
        Given("^the operator is on check your answers page$", () -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
        Then("^the correct headings, data and links should be displayed for Your details section$", () -> {
            //TODO: This is written for the prototype please amend
            assertTrue(isTextPresent("Your details", 30));
            assertTrue(isTextPresent("Phil Jowett", 30));
            assertTrue(isTextPresent("11/11/2000", 30));
            assertTrue(isTextPresent("Nottingham", 30));
            assertTrue(isTextPresent("Nottingham", 30));
            assertTrue(isTextPresent("certificate attached", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
        Then("^the correct headings, data and links should be displayed for Responsibilities section$", () -> {
            assertTrue(isTextPresent("Responsibilities", 30));
            assertTrue(isTextPresent("test", 30));
            assertTrue(isTextPresent("test", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
        Then("^the correct headings, data and links should be displayed for Hours per week section$", () -> {
            assertTrue(isTextPresent("Hours per week", 30));
            assertTrue(isTextPresent("8", 30));
            assertTrue(isTextPresent("8", 30));
            assertTrue(isTextPresent("8", 30));
            assertTrue(isTextPresent("8", 30));
            assertTrue(isTextPresent("8", 30));
            assertTrue(isTextPresent("8", 30));
            assertTrue(isTextPresent("8", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
        Then("^the correct headings, data and links should be displayed for Other licences section$", () -> {
            assertTrue(isTextPresent("Other licences", 30));
            assertTrue(isTextPresent("non added", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
        Then("^the correct headings, data and links should be displayed for Additional information section$", () -> {
            assertTrue(isTextPresent("Other licences", 30));
            assertTrue(isTextPresent("non added", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
        Then("^the correct headings, data and links should be displayed for Other employment section$", () -> {
            assertTrue(isTextPresent("Other Employment", 30));
            assertTrue(isTextPresent("non added", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
        Then("^the correct headings, data and links should be displayed for Convictions & Penalties section$", () -> {
            assertTrue(isTextPresent("Convictions & Penalties", 30));
            assertTrue(isTextPresent("information added", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
        Then("^the correct headings, data and links should be displayed for Revoked, curtailed or suspended licences section$", () -> {
            assertTrue(isTextPresent("Revoked Curtailed or Suspended Licences", 30));
            assertTrue(isTextPresent("information added", 30));
            //TODO: Add link
            assertTrue(isElementPresent(""));
        });
    }
}