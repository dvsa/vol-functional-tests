@SS-Verify-On
@OLCS-20343
@tm-application
Feature: Countersigning declaration page for the operator (Verify switched on)

  Background:
    When i have a "goods" "NI" partial application
    And i add an operator as a transport manager

  Scenario: Check declaration text for NI
    Then the correct information is displayed on the declaration page

  Scenario: Check that verify button is not displayed when Print and sign is selected as an option
    And the user chooses to print and sign
    Then the declaration text and verify button are not displayed