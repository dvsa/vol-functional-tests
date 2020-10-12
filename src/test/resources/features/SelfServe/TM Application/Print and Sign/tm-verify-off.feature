@Verify-Off
Feature: Countersigning declaration page for the operator (Verify switched off)

  Background:
    When i have a "goods" "GB" partial application
    And i select a transport manager to add

  @OLCS-21374
  Scenario: Verify switched off operator same as TM
    And the transport manager is the owner
    Then Signing options are not displayed on the page
    And submit to operator button is not displayed

  @OLCS-21678
  Scenario: Verify switched off operator not TM
    And the transport manager is not the owner
    Then Signing options are not displayed on the page
    And submit to operator button is displayed

  @OLCS-21374
  Scenario: New countersigning declaration page for the operator (Verify switched off)
    And the transport manager is the owner
    When i submit the application
    Then the print and sign page is displayed
    And the application status is "Not yet received"

  @OLCS-21678
  Scenario: New countersigning declaration page for the operator (Verify switched off)
    And the transport manager is not the owner
    When i submit the application
    Then the 'Awaiting operator review' verify off page is displayed
    And the application status is "With operator"