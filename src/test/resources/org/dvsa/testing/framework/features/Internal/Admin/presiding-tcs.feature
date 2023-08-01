@int_regression

Feature: Admin wishes to add and delete a Presiding tc

  Background:
    Given i have an internal admin user
    When I am on the Presiding TC page

  Scenario: Admin adds a Presiding TC
    Given an admin adds a Presiding TC
    And the Presiding TC should be displayed







