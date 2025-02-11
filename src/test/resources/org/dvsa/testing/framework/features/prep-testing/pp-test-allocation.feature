@PP-SMOKE
@PP-TASK-ALLOCATION

Feature: Operator re-assigns a task

  Scenario: User wishes to re-assign a task
    Given I log into prep "internal" account with user "intPrepUser"
    When I re-assign a task
    Then the User has re-assigned a task