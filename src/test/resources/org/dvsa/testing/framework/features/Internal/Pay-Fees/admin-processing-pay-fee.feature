@Admin-Payment-Processing
@int_regression
@CPMS_tests
@smoketest
Feature: Admin paying fees

  Background:
    Given i have an admin account to add users

  Scenario: Process GB payment by cash and card
    And i have logged in to internal
    When i am on the payment processing page
    And i add a new "SCOT Bus Fine" fee
    And when i pay for the fee by "cash"
    Then the fee should be paid and no longer visible in the fees table

#    Need NI Payment for internal