package org.dvsa.testing.framework.Journeys.permits.external;

import activesupport.IllegalBrowserException;
import activesupport.string.Str;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.external.ChangeYourPasswordPage;

import java.net.MalformedURLException;

public class VolAccountJourney extends BaseJourney {

    private static VolAccountJourney instance = null;

    protected VolAccountJourney(){
        // The code below assures that someone can't new up instances using reflections
        if (instance != null)
            throw new RuntimeException("Use #getInstance to obtain an instance of this class");
    }

    public static VolAccountJourney getInstance(){
        if (instance == null) {
            synchronized (VolAccountJourney.class){
                instance = new VolAccountJourney();
            }
        }

        return instance;
    }

    public VolAccountJourney signin(World world) {
        return signin(new OperatorStore(), world);
    }

    public VolAccountJourney signin(OperatorStore operator, World world) {
        LoginPage.signIn(operator.getUsername(), operator.getPassword());

        if (ChangeYourPasswordPage.onPage()) {
            operator.setPassword(Str.randomWord(7).concat("1Pp"));
            ChangeYourPasswordPage.update(operator.getPreviousPassword(), operator.getPassword());
            world.put("password", operator.getPassword());
        }

        return this;
    }

}
