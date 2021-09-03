package org.dvsa.testing.framework.enums;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum YearSelection {

    Year_2019("2019"),
    Year_2020("2020"),
    Year_2021("2021");

    private String type;

    YearSelection(String type) {
        this.type = type;
    }

    public static org.dvsa.testing.framework.enums.YearSelection getEnum(@NotNull String name) {
        return Arrays.stream(org.dvsa.testing.framework.enums.YearSelection.values()).filter(yearSelection -> yearSelection.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return type;
    }

}


