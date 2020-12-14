package org.dvsa.testing.framework.Utils.Generic;

import enums.TrafficArea;
import org.jetbrains.annotations.NotNull;

public class EnforcementArea {

    private String area;

    public String getArea() {
        return area;
    }

    EnforcementArea(String area) {
        this.area = area;
    }

    public static String getEnforcementArea(@NotNull TrafficArea trafficArea) {
        String enforcementArea;
        switch (trafficArea) {
            case B:
                enforcementArea = "EA-B";
                break;
            case C:
                enforcementArea = "EA-C";
                break;
            case D:
                enforcementArea = "EA-D";
                break;
            case F:
                enforcementArea = "EA-F";
                break;
            case G:
                enforcementArea = "EA-E";
                break;
            case H:
                enforcementArea = "EA-J";
                break;
            case K:
                enforcementArea = "EA-H";
                break;
            case M:
                enforcementArea = "EA-A";
                break;
            case N:
                enforcementArea = "EA-N";
                break;
            default:
                enforcementArea = "EA-D";
        }
        return enforcementArea;
    }
}