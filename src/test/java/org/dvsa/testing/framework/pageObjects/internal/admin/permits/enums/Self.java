package org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums;

import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.ECMTType;

import java.time.LocalDateTime;

public class Self {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ECMTType type;

    public Self(LocalDateTime startDate, LocalDateTime endDate, ECMTType type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public ECMTType getType() {
        return type;
    }
}