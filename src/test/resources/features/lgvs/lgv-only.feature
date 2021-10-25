@lgv
Feature: LGV only tests

  Scenario: LGV error check
    Given I want to apply for a "goods" "standard_international" licence
    When I click save and continue
    Then An error message should be displayed
