package org.dvsa.testing.framework.Journeys.permits;

import activesupport.IllegalBrowserException;
import activesupport.system.Properties;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.net.MalformedURLException;


public class BaseJourney  extends BasePage {
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

    public BaseJourney go(ApplicationType applicationType, String endpoint) throws MalformedURLException, IllegalBrowserException {
        getDriver().manage().deleteAllCookies();
        getDriver().navigate().refresh();
        get(URL.build(applicationType, Properties.get("env", true), endpoint).toString());

        return this;
    }

    public BaseJourney go(ApplicationType applicationType) throws MalformedURLException, IllegalBrowserException {
        return go(applicationType, "");
    }

    protected void readyBrowser() throws MalformedURLException, IllegalBrowserException {
        if (getDriver() != null && !getDriver().toString().contains("null")) {
            getDriver();
        } else {
            getDriver().manage().deleteAllCookies();
        }
    }
}
