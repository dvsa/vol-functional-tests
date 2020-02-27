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

import java.util.LinkedHashMap;

public class Continuations extends BasePage implements En {

    private Dates dates = new Dates(new LocalDateCalendar());
    private LinkedHashMap<String, Integer> continuationDates;

    public Continuations(World world) {

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
        When("^i change my continuation date$", () -> {
            world.UIJourneySteps.urlSearchAndViewLicence();
            continuationDates = dates.getDate(10, 0, 0);
            waitForTextToBePresent("Continuation date");
            replaceDateById("details[continuationDate]", continuationDates);
            replaceDateById("details[reviewDate]", continuationDates);
            click("form-actions[submit]", SelectorType.ID);
            waitForElementToBeClickable("form-actions[submit]", SelectorType.ID);
        });
        And("^i generate a continuation$", () -> {
            world.UIJourneySteps.continueALicenceOnInternal(world.createLicence.getLicenceNumber(), world.updateLicence.getLicenceTrafficArea(), continuationDates.get("month"));
        });
        And("^fill in my continuation details on self serve$", () -> {
            world.UIJourneySteps.continueLicenceWithVerifyAndPay();
        });
        Then("^the continuation should be approved and a snapshot generated on Internal$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewLicence();
            clickByLinkText("Docs & attachments");
            Assert.assertTrue(isTextPresent("Digital continuation snapshot", 10));
        });
    }
}
