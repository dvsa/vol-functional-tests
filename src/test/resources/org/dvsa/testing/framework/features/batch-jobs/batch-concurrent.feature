@batch-smoke
Feature: AWS Batch - Concurrent Execution

  Scenario: Run all AWS batch jobs concurrently
    Given I submit the "LAST_TM_LETTER" batch job
    And I submit the "CONTINUATIONS_REMINDER" batch job
    And I submit the "DUPLICATE_VEHICLE_WARNING" batch job
    And I submit the "EXPIRE_BUS_REGISTRATION" batch job
    And I submit the "PROCESS_QUEUE" batch job
    Then all submitted batch jobs should succeed