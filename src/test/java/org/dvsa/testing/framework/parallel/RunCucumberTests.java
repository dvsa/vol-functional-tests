package org.dvsa.testing.framework.parallel;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("org/dvsa/testing/framework")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME,value = "src/test/resources/org/dvsa/testing/framework/features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "org.dvsa.testing.framework")
@ConfigurationParameter(key = Constants.EXECUTION_DRY_RUN_PROPERTY_NAME, value = "false")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm, pretty, junit:target/results-summary/summary.xml, rerun:target/rerun.txt")

public class RunCucumberTests {
}