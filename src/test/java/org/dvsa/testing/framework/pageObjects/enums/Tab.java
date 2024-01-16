package org.dvsa.testing.framework.pageObjects.enums;

import org.jetbrains.annotations.NotNull;

public enum Tab {
    LICENCES("Licences"),
    FEES("Fees"),
    DOCUMENTS("Documents"),
    PERMITS("Permits");

    private String title;

    Tab(@NotNull String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return this.title;
    }

}
