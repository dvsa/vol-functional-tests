@SS
@tm-application
@Verify-Off
Feature: Countersigning declaration page for the operator Print and Sign

  Background:
    Given i have a "goods" "GB" partial application
    And i select a transport manager to add

  @OLCS-21374
  Scenario: Add TM as the Operator Print and Sign
    When the transport manager is the owner
    And i choose to print and sign
    Then transport manager details approved banner appears
    And transport manager status is "green" and "Not yet received"

  @OLCS-21678
  Scenario: Add TM who is not the Operator Print and Sign
    When the transport manager is not the owner
    And i choose to print and sign
    Then submit to operator button is displayed
    And transport manager status is "orange" and "With operator"