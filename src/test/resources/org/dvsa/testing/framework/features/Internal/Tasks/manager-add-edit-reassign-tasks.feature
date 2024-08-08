@int_regression
@task_management
@FullRegression
@printAndSign

Feature:Add, Edit and reassign tasks on the internal application

  Background:
    Given i have a valid "goods" "standard_national" licence
    When I have logged into the internal application

  @re-assign-task @localsmoke @ec2-smoke
  Scenario: User wishes to re-assign a task
    Then I re-assign a task
    Then the User has re-assigned a task

  @edit_task
  Scenario: User wishes to edit a task
    Then I edit a task
    Then the User has edited a task

  @dd_task
  Scenario: User wishes to add a task
    Then I add a new task
    Then the User has added a task
