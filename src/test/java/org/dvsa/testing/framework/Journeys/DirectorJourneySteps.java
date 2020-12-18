package org.dvsa.testing.framework.Journeys;

import Injectors.World;
import activesupport.IllegalBrowserException;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import java.net.MalformedURLException;
import java.util.HashMap;

public class DirectorJourneySteps extends BasePage {

    private World world;

    public DirectorJourneySteps(World world){
        this.world = world;
    }


    public void addDirectorWithoutConvictions(String firstName, String lastName) throws IllegalBrowserException, MalformedURLException {
        world.selfServeNavigation.navigateToPage("licence", "Directors");
        addPerson(firstName, lastName);
        findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
        findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
    }


    public void addDirector(String forename, String familyName) throws IllegalBrowserException, MalformedURLException {
        addPerson(forename, familyName);
        world.genericUtils.findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
        world.genericUtils.findSelectAllRadioButtonsByValue("N");
        clickByName("form-actions[saveAndContinue]");
    }

    public void removeDirector() throws IllegalBrowserException, MalformedURLException {
        int sizeOfTable = size("//*/td[4]/input[@type='submit']", SelectorType.XPATH);
        click("//*/tr[" + sizeOfTable + "]/td[4]/input[@type='submit']", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure");
        click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
    }


    public void addPerson(String firstName, String lastName) throws IllegalBrowserException, MalformedURLException {
        clickByName("add");
        waitForTitleToBePresent("Add a director");
        selectValueFromDropDown("//select[@id='title']", SelectorType.XPATH, "Dr");
        enterText("forename", firstName, SelectorType.ID);
        enterText("familyname", lastName, SelectorType.ID);

        HashMap<String, Integer> dates;
        dates = world.globalMethods.date.getDateHashMap(-5, 0, -20);

        enterText("dob_day", dates.get("day"), SelectorType.ID);
        enterText("dob_month", dates.get("month"), SelectorType.ID);
        enterText("dob_year", dates.get("year"), SelectorType.ID);
        clickByName("form-actions[saveAndContinue]");
    }
}
