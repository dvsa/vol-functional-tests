@PP-SMOKE
@PP-TASK-ALLOCATION

Feature: Operator re-assigns a task

  Scenario: User wishes to re-assign a task
    Given I have a prep "internal" account
    When I re-assign a task
    Then the User has re-assigned a task

