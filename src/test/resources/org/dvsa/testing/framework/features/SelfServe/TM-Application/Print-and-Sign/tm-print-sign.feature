@printAndSign @localsmoke @smoketest
Feature: Countersigning declaration page for the operator Print and Sign

  Background:
    Given i have a "goods" "GB" partial application
    And i select a transport manager to add

  @tm-print-sign-operator-cosigns-manually
  Scenario: TM prints and signs and Operator co-signs manually
    When i add an existing person as a transport manager who is not the operator on "application"
    And i sign the declaration
    And the operator countersigns by print and sign
    And the print and sign page is displayed