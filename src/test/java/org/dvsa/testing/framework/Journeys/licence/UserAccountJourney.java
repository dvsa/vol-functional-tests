package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
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
        String teamName = selectRandomValueFromDropDown("team", SelectorType.ID);
        setTeamName(teamName);
        selectValueFromDropDown("title", SelectorType.ID, "Mr");
        UniversalActions.clickSubmit();
    }

    public void ChangeUserDetails() {
        //selectRandomValueFromDropDown("team");
        selectValueFromDropDown("title", SelectorType.ID, "Mr");
        replaceText("person[forename]", SelectorType.ID, world.DataGenerator.getOperatorForeName());
        replaceText("person[familyName]", SelectorType.ID, world.DataGenerator.getOperatorFamilyName());
        HashMap<String, String> currentDate = date.getDateHashMap(0, 0, -18);
        enterDateFieldsByPartialId("birthDate", currentDate);
        replaceText("userContact[emailAddress]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        replaceText("userContact[emailConfirm]", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        replaceText("addressLine1", SelectorType.ID, world.DataGenerator.getOperatorAddressLine1());
        replaceText("addressTown", SelectorType.ID, world.DataGenerator.getOperatorTown());
        replaceText("postcode", SelectorType.ID, world.DataGenerator.getOperatorPostCode());
        selectRandomValueFromDropDown("officeAddress[countryCode]", SelectorType.ID);
        UniversalActions.clickSubmit();
        waitForElementToBeClickable("team", SelectorType.ID);
    }
}