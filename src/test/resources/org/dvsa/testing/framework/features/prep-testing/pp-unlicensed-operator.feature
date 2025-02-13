@PP-SMOKE
Feature: Unlicensed operator

  @pp_unlicensed_operator_add
  Scenario: Create unlicensed operator
    Given I log into prep "internal" account with user "intPrepUser"
    When i create an unlicensed operator
    Then the operator should be created