package org.dvsa.testing.framework.hooks;

import activesupport.driver.Browser;
import activesupport.driver.BrowserStack;
import cucumber.api.Scenario;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

public class VFTLifeCycle implements EventListener {
    private static final Logger LOGGER = LogManager.getLogger(VFTLifeCycle.class);
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, event -> {
           LOGGER.info("Test is starting");
        });
        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            LOGGER.info("Test is shutting down");
//            System.out.println("THIS IS THE TITLE:" + Browser.navigate().getTitle());
//            if(Browser.isBrowserOpen()){
//            try {
//                Browser.closeBrowser();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        });
    }
}