package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

public class UserAccountJourney extends BasePage {
    private World world;
    public String teamName;

    Dates date = new Dates(org.joda.time.LocalDate::new);

    public void setTeamName(String teamName) {this.teamName = teamName;}

    public UserAccountJourney(World world) {
        this.world = world;
    }

    public void ChangeTeam() {
        String teamName = selectRandomValueFromDropDown("team");
        setTeamName(teamName);
        selectValueFromDropDown("title", SelectorType.ID, "Mr");
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }

    public void ChangeUserDetails() {
        selectRandomValueFromDropDown("team");
        replaceText("userDetails[loginId]", SelectorType.ID, world.DataGenerator.getOperatorUser());
        selectValueFromDropDown("title", SelectorType.ID, "Mr");
        replaceText("person[forename]", SelectorType.ID, world.DataGenerator.getOperatorForeName());
        replaceText("person[familyName]", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
        HashMap<String, String> currentDate = date.getDateHashMap(0, 0, -18);
        replaceDateFieldsByPartialId("birthDate", currentDate);
        replaceText("userContact[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        replaceText("userContact[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        replaceText("addressLine1", SelectorType.ID, world.DataGenerator.getOperatorAddressLine1());
        replaceText("addressTown", SelectorType.ID, world.DataGenerator.getOperatorTown());
        replaceText("postcode", SelectorType.ID, world.DataGenerator.getOperatorPostCode());
        selectRandomValueFromDropDown("officeAddress[countryCode]");
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForElementToBeClickable("team", SelectorType.ID);
    }
}