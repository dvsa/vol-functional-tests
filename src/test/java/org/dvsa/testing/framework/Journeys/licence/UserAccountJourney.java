package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class UserAccountJourney extends BasePage {
    private World world;

    public String teamName;

    public void setTeamName(String teamName) {this.teamName = teamName;}

    public String getTeamName() {return teamName;}

    public UserAccountJourney(World world) {this.world = world;}

    public void ChangeTeam() {
        String teamName = selectRandomValueFromDropDown("team");
        setTeamName(teamName);
        selectValueFromDropDown("title", SelectorType.ID, "Mr");
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }



}
