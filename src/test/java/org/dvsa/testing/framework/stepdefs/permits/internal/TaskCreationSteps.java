package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.TaskCreationPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;


public class TaskCreationSteps extends BasePage implements En {

    private World world;

    public TaskCreationSteps(World world, OperatorStore operatorStore) {

        And("^I am on the internal home page$", () -> {
            BaseInternalJourney.getInstance().signin(BaseInternalJourney.User.Admin.getUsername(), world.configuration.config.getString("internalNewPassword"));
            Assert.assertEquals(BasePage.getElementValueByText("//h1[contains(text(),'Home')]", SelectorType.XPATH), "Home");
        });
        And("^I navigate to the corresponding task created against the licence$", () -> {
            TaskCreationPage.navigateToTaskCreated();
            get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true), "?i=e&assignedToTeam=1004&assignedToUser=&category=4&taskSubCategory=&date=tdt_today&status=tst_open&urgent=0&sort=urgent%2CactionDate&order=DESC&limit=1").toString());
        });
        Then ("^I can view the created task$", () -> {
            TaskCreationPage.viewTaskCreated();
            isPath("/licence/\\d+/irhp-application/edit/\\d+");
            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            Assert.assertTrue(String.valueOf(BasePage.getElementValueByText("//div[@class='page-header']", SelectorType.XPATH).contains((CharSequence) licence)),true);
            IrhpPermitsApplyPage.saveIRHP();
        });

    }
}

