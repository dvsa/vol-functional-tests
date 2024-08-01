package org.dvsa.testing.framework.enums;

public enum BatchCommands {
    CONTINUATIONS_REMINDER("batch:digital-continuation-reminders"),
    LAST_TM_LETTER("batch:last-tm-letter"),
    DUPLICATE_VEHICLE_WARNING("batch:duplicate-vehicle-warning"),
    EXPIRE_BUS_REGISTRATION("batch:expire-bus-registration"),
    PROCESS_QUEUE("batch:process-queue"),
    BATCH_CLI("Batch_Run_Cli_New"),
    BATCH_PROCESS_QUEUE("Batch_Process_Queue_New"),
    EBSR_QUEUE("que_typ_ebsr_pack");


    private final String batchJobName;

    BatchCommands(String batchJobName) {
        this.batchJobName = batchJobName;
    }

    @Override
    public String toString() {
        return batchJobName;
    }
}