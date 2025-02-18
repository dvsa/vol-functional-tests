package org.dvsa.testing.framework.packages;

import activesupport.IllegalBrowserException;
import activesupport.config.Configuration;
import activesupport.proxy.ProxyConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.parallel.ChromeSetUp;
import org.dvsa.testing.framework.parallel.EdgeSetUp;
import org.dvsa.testing.framework.parallel.FirefoxSetUp;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.util.Optional;

public class Browser {

    private static WebDriver driver;

    private static String gridURL;
    private static String ipAddress;
    private static String portNumber;
    private static String platform;
    private static String browserVersion;
    protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private static final Logger LOGGER = LogManager.getLogger(Browser.class);

    public static Configuration configuration = new Configuration();

    public static void setIpAddress(String ipAddress) {
        Browser.ipAddress = ipAddress;
    }

    public static void setPortNumber(String portNumber) {
        Browser.portNumber = portNumber;
    }

    public static String getIpAddress() {
        return ipAddress;
    }

    public static String getPortNumber() {
        return portNumber;
    }

    public static String getPlatform() {
        return platform;
    }

    public static void setPlatform(String platform) {
        Browser.platform = platform;
    }

    public static String getBrowserVersion() {
        return browserVersion;
    }

    public static void setBrowserVersion(String browserVersion) {
        Browser.browserVersion = browserVersion;
    }

    public static String getGridURL() {
        return gridURL;
    }

    public static void setGridURL(String gridURL) {
        Browser.gridURL = gridURL;
    }

    public static WebDriver navigate() {
        if (getDriver() == null) {
            setGridURL(System.getProperty("gridURL"));
            setPlatform(System.getProperty("platform"));
            setBrowserVersion(System.getProperty("browserVersion"));
            try {
                whichBrowser(System.getProperty("browser"));
            } catch (IllegalBrowserException | MalformedURLException e) {
                LOGGER.info("STACK TRACE: ".concat(e.toString()));
            }
        }
        return getDriver();
    }

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public static String hubURL() {
        return Optional.ofNullable(gridURL).orElse("http://localhost:4444/wd/hub");
    }

    private static void whichBrowser(String browserName) throws IllegalBrowserException, MalformedURLException {
        var chrome = new ChromeSetUp();
        var firefox = new FirefoxSetUp();
        var edge = new EdgeSetUp();

        driver = switch (browserName.toLowerCase().trim()) {
            case "chrome" -> chrome.driver();
            case "edge" -> edge.driver();
            case "firefox" -> firefox.driver();
            case "safari" -> null;
            case "headless" -> {
                chrome.getChromeOptions().addArguments("--headless");
                yield chrome.driver();
            }
            case "chrome-proxy" -> {
                chrome.getChromeOptions().setProxy(ProxyConfig.dvsaProxy().setSslProxy(getIpAddress().concat(":").concat(getPortNumber())));
                yield chrome.driver();
            }
            case "firefox-proxy" -> {
                firefox.getOptions().setProxy(ProxyConfig.dvsaProxy().setSslProxy(getIpAddress().concat(":").concat(getPortNumber())));
                yield firefox.driver();
            }
            default -> throw new IllegalBrowserException();
        };

        threadLocalDriver.set(driver);
        getDriver();
    }

    public static void closeBrowser() throws Exception {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }

    public static boolean isBrowserOpen() {
        return getDriver() != null;
    }

    public static void removeLocalDriverThread() {
        threadLocalDriver.remove();
    }
}