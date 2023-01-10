@int_regression
@system_messages
Feature: Input system messages as an internal user and check they are correctly displayed

  Background:
    Given I am on the System Messages page

    Scenario: Admin adds an external message to display now
      When I add an external message
      And I select a current time period
      Then The message should be displayed on the external screen