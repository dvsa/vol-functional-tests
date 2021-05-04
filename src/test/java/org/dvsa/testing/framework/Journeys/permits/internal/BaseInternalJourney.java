package org.dvsa.testing.framework.Journeys.permits.internal;

import activesupport.IllegalBrowserException;
import activesupport.config.Configuration;
import activesupport.system.Properties;
import com.typesafe.config.Config;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import java.net.MalformedURLException;

public class BaseInternalJourney extends BaseJourney {

    private static volatile BaseInternalJourney instance = null;

    protected BaseInternalJourney(){
    }

    public static BaseInternalJourney getInstance(){
        if (instance == null) {
            synchronized (BaseInternalJourney.class){
                instance = new BaseInternalJourney();
            }
        }

        return instance;
    }

    EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    Config config = new Configuration(env.toString()).getConfig();

    public BaseInternalJourney signin() {
        return signin(User.Admin.getUsername(), config.getString("internalNewPassword"));
    }

    public BaseInternalJourney signin(User user) {
        return signin(user.getUsername(), config.getString("internalNewPassword"));
    }

    public BaseInternalJourney signin(String username, String password) {
        LoginPage.signIn(username, password);
        return this;
    }

    public BaseInternalJourney openLicence(Integer licenceId) {
        return openLicence(String.valueOf(licenceId));
    }

    public BaseInternalJourney openLicence(String licenceId) {
        get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true), "licence/".concat(licenceId)).toString());
        return this;
    }

    public BaseInternalJourney openIrhpPermits(int licenceId) {
        return openIrhpPermits(String.valueOf(licenceId));
    }

    public BaseInternalJourney openIrhpPermits(String licenceId) {
        readyBrowser();
        return go(ApplicationType.INTERNAL, "licence/" + licenceId + "/permits/");
    }

    @Override
    public BaseInternalJourney go(ApplicationType applicationType, String endpoint) {
        return (BaseInternalJourney) super.go(applicationType, endpoint);
    }

    @Override
    public BaseInternalJourney go(ApplicationType applicationType) {
        return (BaseInternalJourney) super.go(applicationType);
    }

    public enum User {
        Admin("usr291"),
        Normal("usr59");

        private String username;

        User(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        }
    }


