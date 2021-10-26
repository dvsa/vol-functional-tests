@lgv
Feature: LGV only tests

  Scenario: LGV error check
    Given I want to apply for a "goods" "standard_international" licence
    When I click save and continue
    Then A LGV only error message should be displayed
