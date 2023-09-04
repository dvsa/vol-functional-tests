package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class SubmissionsJourney extends BasePage {

    private final World world;

    public SubmissionsJourney(World world) {
        this.world = world;
    }

    public void addPresidingTC() {
        waitAndClick("add", SelectorType.ID);
        waitAndClick("user", SelectorType.ID);
        selectRandomValueFromDropDown("user");
     //   selectValueFromDropDown("user", SelectorType.ID, "Unassigned");
        waitAndEnterText("presidingTcDetails[name]",SelectorType.ID, world.DataGenerator.getOperatorForeName());
        world.UIJourney.clickSubmit();
    }

    public void createAndSubmitSubmission() {
        click("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        clickByLinkText(Integer.toString(world.updateLicence.getCaseId()));
        clickByLinkText("Submissions");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex("fields[submissionSections][submissionType]", SelectorType.NAME, 13);
        world.UIJourney.clickSubmit();
    }

    public void setInfoCompleteAndAssignSubmission() {
        clickByLinkText("Set info complete");
        world.UIJourney.clickSubmit();
        waitAndClick("Assign submission", SelectorType.LINKTEXT);
        waitForTextToBePresent("Assign to:");
        clickByXPath("//*[@id='tcOrOther']");
        clickByXPath("//*[@id='fields[urgent]']");
        selectRandomValueFromDropDown("user");
        world.UIJourney.clickSubmit();
    }



}
