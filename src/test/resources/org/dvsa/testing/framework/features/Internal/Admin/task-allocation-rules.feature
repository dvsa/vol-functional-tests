@int_regression
@task_allocation
@FullRegression
@printAndSign
Feature: User is able to Add, edit and delete a Task allocation rule

  Background:
    Given I am on the task allocation rules page

  @delete_allocation
  Scenario: User wants to delete a task allocation rule
    And I delete an allocation rule
    Then that rule should have been deleted

  @edit_allocation
  Scenario: User wants to edit a task allocation rule
    And I edit an allocated rule
    Then that rule should have been edited

  @add_allocation @ec2-smoke
  Scenario: User wants to add a task allocation rule
    And I add an allocated rule
    Then the rule should have been added