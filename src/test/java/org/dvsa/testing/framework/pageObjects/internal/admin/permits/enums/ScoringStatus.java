package org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums;

public enum ScoringStatus {
    AcceptanceSuccessful("Acceptance successful"),

    ScoringPending("Scoring pending"),
    ScoringFailure("Scoring unexpected failure"),
    ScoringSuccessful("Scoring successful");

    private String status;

    ScoringStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return status;
    }

}
