package org.dvsa.testing.framework.enums;

public enum BatchCommands {
    CONTINUATIONS_REMINDER("batch:digital-continuation-reminders"),
    LAST_TM_LETTER("batch:last-tm-letter"),
    DUPLICATE_VEHICLE_WARNING("batch:duplicate-vehicle-warning"),
    PROCESS_QUEUE("batch:process-queue");

    private final String batchJobName;

    BatchCommands(String batchJobName) {
        this.batchJobName = batchJobName;
    }

    @Override
    public String toString() {
        return batchJobName;
    }
}