@PP-SMOKE
Feature: Unlicensed operator

  @pp_unlicensed_operator_add
  Scenario: Create unlicensed operator
    Given I have a prep "internal" account
    When i create an unlicensed operator
    Then the operator should be created