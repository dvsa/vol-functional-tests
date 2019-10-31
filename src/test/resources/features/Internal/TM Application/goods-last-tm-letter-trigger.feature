@INT
@INT-GOODS-LAST-TM-TRIGGER
@-OLCS-19479
@int_regression
Feature: Set and check criteria for triggering automatic letter

  Background:
    Given i have a valid "goods" "sn" licence

  Scenario: Generate letter for valid licence
    And the transport manager has been removed by an internal user
    When the user confirms they want to send letter
    Then a flag should be set in the DB

  Scenario: Generate letter for suspended licence
    And the licence status is "suspend"
    When the transport manager has been removed by an internal user
    And the user confirms they want to send letter
    Then a flag should be set in the DB

  Scenario: Generate letter for curtailed licence
    And the licence status is "curtail"
    When the transport manager has been removed by an internal user
    And the user confirms they want to send letter
    Then a flag should be set in the DB