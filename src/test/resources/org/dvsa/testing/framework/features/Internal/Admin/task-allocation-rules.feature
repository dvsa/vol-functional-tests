@int_regression
@task_allocation
@FullRegression
@printAndSign
Feature: User is able to Add, edit and delete a Task allocation rule

  Background:
    Given I am on the task allocation rules page

  @reads-and-writes-system-properties
  Scenario: User wants to delete a task allocation rule
    Given I delete an allocation rule
    Then that rule should have been deleted

  @reads-system-properties
  Scenario: User wants to edit a task allocation rule
    Given I edit an allocated rule
    Then that rule should have been edited

  @reads-system-properties
  Scenario: User wants to add a task allocation rule
    Given I add an allocated rule
    Then the rule should have been added

