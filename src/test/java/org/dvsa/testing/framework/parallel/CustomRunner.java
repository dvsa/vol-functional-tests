package org.dvsa.testing.framework.parallel;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;
public class CustomRunner implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy  {
<<<<<<< HEAD
    private static final int FIXED_PARALLELISM = 4;
=======
    private static final int FIXED_PARALLELISM = 3;
>>>>>>> 09a061cd0 (refactor: updating runners)

    static {
        System.out.println("THREADS: " + FIXED_PARALLELISM);
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        return this;
    }

    @Override
    public Predicate<? super ForkJoinPool> getSaturatePredicate() {
        return (ForkJoinPool p) -> true;
    }

    @Override
    public int getParallelism() {
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
