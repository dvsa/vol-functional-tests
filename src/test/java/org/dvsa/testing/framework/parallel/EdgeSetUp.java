package org.dvsa.testing.framework.parallel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.dvsa.testing.framework.packages.Browser.*;


public class EdgeSetUp {
    private static final Logger LOGGER = LogManager.getLogger(EdgeSetUp.class);
    private EdgeOptions edgeOptions = new EdgeOptions();

    public EdgeOptions getEdgeOptions() {
        return edgeOptions;
    }

    public void setEdgeOptions(EdgeOptions edgeOptions) {
        this.edgeOptions = edgeOptions;
    }

    public static WebDriver driver;

    public WebDriver driver() throws MalformedURLException {
      //  edgeOptions.setCapability("proxy",ProxyConfig.dvsaProxy());
        edgeOptions.setAcceptInsecureCerts(true);
        edgeOptions.addArguments("--disable-gpu");
        edgeOptions.addArguments("--disable-dev-shm-usage");
        edgeOptions.setCapability("webSocketUrl", true);
        if (getBrowserVersion() == null) {
            driver = new EdgeDriver(edgeOptions);
        } else {
            edgeOptions.setPlatformName(getPlatform());
            driver = new RemoteWebDriver(new URL(hubURL()), edgeOptions);
        }
        return driver;
    }
}