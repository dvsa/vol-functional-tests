package org.dvsa.testing.framework.Utils.Generic;

import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.dvsa.testing.framework.pageObjects.BasePage;
import scanner.AXEScanner;
import scanner.ReportGenerator;

import java.io.IOException;

public class AxeScanner extends BasePage {


    public void runAxeScannerOnPage() throws IOException {
        AXEScanner scanner = new AXEScanner();
        ReportGenerator reportGenerator = new ReportGenerator();


    }


}
