@SS
@Verify-Off
Feature: Countersigning declaration page for the operator (Verify switched off)

  Background:
    When i have a "goods" "GB" partial application
    And i select a transport manager to add

  @OLCS-21374
  Scenario: Verify switched off operator same as TM
    And the transport manager is the owner
    And i choose to sign with print and sign
    And transport manager details approved banner appears
    And transport manager status is "green" and "Not yet received"

  @OLCS-21678
  Scenario: Verify switched off operator not TM
    And the transport manager is not the owner
    And i choose to sign with print and sign
    And submit to operator button is displayed
    And transport manager status is "orange" and "With operator"