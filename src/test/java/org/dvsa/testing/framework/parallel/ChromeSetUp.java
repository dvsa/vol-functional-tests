package org.dvsa.testing.framework.parallel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.dvsa.testing.framework.packages.Browser.*;


public class ChromeSetUp {

    private ChromeOptions chromeOptions = new ChromeOptions();

    public ChromeOptions getChromeOptions() {
        return chromeOptions;
    }

    public void setChromeOptions(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public static WebDriver driver;

    public WebDriver driver() throws MalformedURLException {
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.setCapability("webSocketUrl", true);
        if (getBrowserVersion() == null) {
            driver = new ChromeDriver(getChromeOptions());
        } else {
            chromeOptions.setPlatformName(getPlatform());
            driver = new RemoteWebDriver(new URL(hubURL()), getChromeOptions());
        }
        return driver;
    }
}