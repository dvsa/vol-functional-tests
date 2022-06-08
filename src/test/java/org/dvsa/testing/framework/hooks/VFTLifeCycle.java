package org.dvsa.testing.framework.hooks;

import activesupport.driver.Browser;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import cucumber.api.java.After;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VFTLifeCycle implements EventListener {
    private static final Logger LOGGER = LogManager.getLogger(VFTLifeCycle.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, event -> LOGGER.info("Test is starting"));

        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            LOGGER.info("Test is shutting down");
            Browser.navigate().quit();
        });
    }
}