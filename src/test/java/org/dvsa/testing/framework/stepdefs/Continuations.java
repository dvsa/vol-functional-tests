package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.dates.Dates;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

public class Continuations extends BasePage implements En {
    public Continuations(World world) {
        When("^i change my continuation date and generate a continuation on internal$", () -> {
            world.UIJourneySteps.urlSearchAndViewLicence();
            int[] date = new Dates().getRelativeDate(0,0,0);
            waitForTextToBePresent("Continuation date");
            replaceText("//*[@id='details[continuationDate]_day']", String.valueOf(date[0]));
            replaceText("//*[@id='details[continuationDate]_month']", String.valueOf(date[1]));
            replaceText("//*[@id='details[continuationDate]_year']", String.valueOf(date[2]));
            replaceText("//*[@id='details[reviewDate]_day']", String.valueOf(date[0]));
            replaceText("//*[@id='details[reviewDate]_month']", String.valueOf(date[1]));
            replaceText("//*[@id='details[reviewDate]_year']", String.valueOf(date[2]));
            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForElementToBeClickable("//*[@id='form-actions[submit]']", SelectorType.XPATH);
            click("//*[contains(text(),'Admin')]", SelectorType.XPATH);
            click("//*[@id='menu-admin-dashboard/continuations']", SelectorType.XPATH);
            waitForTextToBePresent("Generate continuations");
            selectValueFromDropDown("//*[@id='generate-continuation-trafficArea']", SelectorType.XPATH, world.updateLicence.getLicenceTrafficArea());
            click("//*[@id='form-actions[generate]']", SelectorType.XPATH);
            enterText("//*[@id='filters[licenceNo]']", world.createLicence.getLicenceNumber(), SelectorType.XPATH);
            click("//*[@id='main']", SelectorType.XPATH);
            waitForElementToBeClickable("//*[@id='generate']", SelectorType.XPATH);
            click("//input[@name='id[]']", SelectorType.XPATH);
            click("//*[@id='generate']", SelectorType.XPATH);
            wait();
        });
        And("^fill in my continuation details on self serve$", () -> {
        });
        Then("^the continuation should be approved$", () -> {
        });
    }
}
