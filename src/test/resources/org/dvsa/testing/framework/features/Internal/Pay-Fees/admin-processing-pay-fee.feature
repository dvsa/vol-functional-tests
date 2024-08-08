@Admin-Payment-Processing
@int_regression
@CPMS_tests
@smoketest
@printAndSign
@FullRegression
@ec2-smoke

Feature: Admin paying fees

  Background:
    Given i have an internal admin user

  Scenario: Process GB payment by cash and card
    And i have logged in to internal as "admin"
    When i am on the payment processing page
    And i add a new "SCOT Bus Fine" fee
    And when i pay for the fee by "cash"
    Then the fee should be paid and no longer visible in the fees table

#    Need NI Payment for internal