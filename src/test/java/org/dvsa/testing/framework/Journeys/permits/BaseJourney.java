package org.dvsa.testing.framework.Journeys.permits;

import activesupport.system.Properties;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;


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
        get(URL.build(applicationType, Properties.get("env", true), endpoint).toString());

        return this;
    }

    public BaseJourney go(ApplicationType applicationType) {
        return go(applicationType, "");
    }
}
