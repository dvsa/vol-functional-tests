package org.dvsa.testing.framework.pageObjects.enums.dataretention;

import activesupport.system.out.Output;
import org.jetbrains.annotations.NotNull;

public enum ActionType {

    REVIEW,
    AUTOMATE;

    public static org.dvsa.testing.framework.pageObjects.enums.dataretention.ActionType getEnum(@NotNull String actionType) {
        org.dvsa.testing.framework.pageObjects.enums.dataretention.ActionType action;

        switch (actionType.toLowerCase()) {
            case "automate":
                action = org.dvsa.testing.framework.pageObjects.enums.dataretention.ActionType.AUTOMATE;
                break;
            case "review":
                action = org.dvsa.testing.framework.pageObjects.enums.dataretention.ActionType.REVIEW;
                break;
            default:
                throw new IllegalArgumentException(Output.printColoredLog(String.format(
                        "[ERROR] Unsupported NAME: %S passed as an argument.",
                        actionType
                )));
        }

        return action;
    }

}
