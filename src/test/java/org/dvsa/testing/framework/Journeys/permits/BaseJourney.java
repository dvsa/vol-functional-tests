package org.dvsa.testing.framework.Journeys.permits;

import activesupport.system.Properties;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import static org.dvsa.testing.framework.pageObjects.Driver.DriverUtils.getDriver;


public class BaseJourney  extends BasePermitPage {
    private static volatile BaseJourney instance = null;

    protected BaseJourney(){}

    public static BaseJourney getInstance(){
        if (instance == null) {
            synchronized (BaseJourney.class){
                instance = new BaseJourney();
            }
        }

        return instance;
    }

    public BaseJourney go(ApplicationType applicationType, String endpoint) {
        getDriver().manage().deleteAllCookies();
        getDriver().navigate().refresh();
        DriverUtils.get(URL.build(applicationType, Properties.get("env", true), endpoint).toString());

        return this;
    }

    public BaseJourney go(ApplicationType applicationType) {
        return go(applicationType, "");
    }
}
