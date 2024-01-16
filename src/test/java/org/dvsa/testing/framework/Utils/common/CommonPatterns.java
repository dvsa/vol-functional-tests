package org.dvsa.testing.framework.Utils.common;

public interface CommonPatterns {
    String LICENCE = "\\w{2}\\d{7}";
    String PERMIT_NUMBER = "\\d+";
    String REFERENCE_NUMBER = LICENCE + " / " + PERMIT_NUMBER;
}
