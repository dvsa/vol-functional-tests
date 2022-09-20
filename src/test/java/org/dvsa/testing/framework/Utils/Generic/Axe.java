package org.dvsa.testing.framework.Utils.Generic;

import scanner.AXEScanner;
import scanner.ReportGenerator;

public class Axe {

    static AXEScanner axeScanner;
    static ReportGenerator reportGenerator;
    public static AXEScanner axeScanner(){
         axeScanner = new AXEScanner();
         return axeScanner;
    }

    public static ReportGenerator reportGenerator(){
        reportGenerator = new ReportGenerator();
        return reportGenerator;
    }
}
