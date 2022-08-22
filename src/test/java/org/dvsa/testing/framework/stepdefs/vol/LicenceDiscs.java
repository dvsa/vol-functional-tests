package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import cucumber.api.java.en.And;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

import java.sql.SQLException;

public class LicenceDiscs extends BasePage implements En {
    private final World world;

    public LicenceDiscs (World world) {this.world = world;}

    @And("the licence discs should be present")
    public void theLicenceDiscsShouldBPresent() throws SQLException, UnsupportedDatabaseDriverException {
        int psvDiscNumber = Integer.parseInt(world.updateLicence.getStartNumber());
        for (int i = 0; i < 5; i++){
            Assert.assertTrue(isTextPresent(String.valueOf(psvDiscNumber + i)));
        }
    }
}