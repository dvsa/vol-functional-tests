package org.dvsa.testing.framework.Journeys.licence;

import activesupport.driver.Browser;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.ArrayList;

import static activesupport.driver.Browser.navigate;

public class TOPSJourney extends BasePage {

    private World world;

    public TOPSJourney(World world) {
        this.world = world;
    }

    public void navigateAndLoginToTopsReport() {
        clickByLinkText("Your DVSA Reports");
        String userName = world.configuration.config.getString("topsUsername");
        String passWord = world.configuration.config.getString("topsPassword");
        getCurrentUrl();
        navigate().get(String.format("https://%s:%s@operator-reports.integration.edh.dvsacloud.uk/", userName, passWord));

    }

    public void viewComplianceRiskScore() {
        clickById("what-do-you-want");
        world.UIJourney.clickSubmit();
    }



}
