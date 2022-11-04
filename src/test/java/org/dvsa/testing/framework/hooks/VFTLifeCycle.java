package org.dvsa.testing.framework.hooks;


import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
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