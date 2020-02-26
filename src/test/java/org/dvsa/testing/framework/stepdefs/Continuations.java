package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.junit.Assert;

import java.util.HashMap;

public class Continuations extends BasePage implements En {

    private Dates dates = new Dates(new LocalDateCalendar());
    private HashMap<String, Integer> continuationDates;

    public Continuations(World world) {

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
        When("^i change my continuation date and generate a continuation on internal$", () -> {
            world.UIJourneySteps.urlSearchAndViewLicence();
            continuationDates = dates.getDate(10, 0, 0);
            waitForTextToBePresent("Continuation date");
            replaceText("//*[@id='details[continuationDate]_day']", String.valueOf(continuationDates.get("day")));
            replaceText("//*[@id='details[continuationDate]_month']", String.valueOf(continuationDates.get("month")));
            replaceText("//*[@id='details[continuationDate]_year']", String.valueOf(continuationDates.get("year")));
            replaceText("//*[@id='details[reviewDate]_day']", String.valueOf(continuationDates.get("day")));
            replaceText("//*[@id='details[reviewDate]_month']", String.valueOf(continuationDates.get("month")));
            replaceText("//*[@id='details[reviewDate]_year']", String.valueOf(continuationDates.get("year")));
            click("form-actions[submit]", SelectorType.ID);
            waitForElementToBeClickable("form-actions[submit]", SelectorType.ID);
            world.UIJourneySteps.continueALicenceOnInternal(world.createLicence.getLicenceNumber(), world.updateLicence.getLicenceTrafficArea());
        });
        And("^fill in my continuation details on self serve$", () -> {
            world.UIJourneySteps.continueLicenceWithVerifyAndPay();
        });
        Then("^the continuation should be approved and a snapshot generated on Internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewLicence();
            clickByLinkText("Docs & attachments");
            try {
                Assert.assertTrue(isTextPresent("Digital continuation snapshot",10));
            } catch (Exception e) {
                throw new Exception("Digital continuation snapshot not visible in Internal Docs and attachments.");
            }
        });
    }
}
