package org.dvsa.testing.framework.hooks;

import cucumber.api.Scenario;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import cucumber.api.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class VFTLifeCycle implements EventListener {
    String scenarioName;
    private static final Logger LOGGER = LogManager.getLogger(VFTLifeCycle.class);
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class,
                event -> LOGGER.info("Test is starting " + LocalDateTime.now()));

        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            LOGGER.info("Test is completing "  + LocalDateTime.now());
        });
    }
}