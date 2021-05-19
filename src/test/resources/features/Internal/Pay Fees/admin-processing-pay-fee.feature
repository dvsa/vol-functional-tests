@INT
@Admin-Payment-Processing
@int_regression
@CPMS_tests
@smoketest
Feature: Admin paying fees

  Background:
    Given i have logged in to internal

  Scenario Outline: Process GB payment by cash and card
    Given i am on the payment processing page
    And i add a new "SCOT Bus Fine" fee
    When when i pay for the fee by "<payment-method>"
    Then the fee should be paid and no longer visible in the fees table

    Examples:
      | payment-method |
      | card           |
      | cash           |
      | cheque         |
      | postal         |

#    Need NI Payment for internal