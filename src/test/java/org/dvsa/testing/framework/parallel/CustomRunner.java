package org.dvsa.testing.framework.parallel;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import java.util.function.Predicate;

public class CustomRunner implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {
    private static final Logger LOGGER = Logger.getLogger(CustomRunner.class.getName());
    private static final int FIXED_PARALLELISM = 3;

    static {
        System.out.println("THREADS: " + FIXED_PARALLELISM);
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        LOGGER.info("Creating ParallelExecutionConfiguration with fixed parallelism: " + FIXED_PARALLELISM);
        return this;
    }

    @Override
    public Predicate<? super ForkJoinPool> getSaturatePredicate() {
        return (ForkJoinPool p) -> true;
    }

    @Override
    public int getParallelism() {
        LOGGER.info("Returning parallelism level: " + FIXED_PARALLELISM);
        return FIXED_PARALLELISM;
    }

    @Override
    public int getMinimumRunnable() {
        return FIXED_PARALLELISM;
    }

    @Override
    public int getMaxPoolSize() {
        return FIXED_PARALLELISM;
    }

    @Override
    public int getCorePoolSize() {
        return FIXED_PARALLELISM;
    }

    @Override
    public int getKeepAliveSeconds() {
        return 100;
    }
}