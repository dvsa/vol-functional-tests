package org.dvsa.testing.framework.parallel;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("org/dvsa/testing/framework")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.dvsa.testing.framework")
public class RunCucumberTests {
    public void something(){
    }
}