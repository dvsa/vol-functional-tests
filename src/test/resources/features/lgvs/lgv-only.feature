@lgv
Feature: LGV only tests

  Scenario: LGV only no option selected error check
    Given I want to apply for a "goods" "standard_international" licence
    When I click save and continue
    Then A LGV only error message should be displayed

  Scenario: LGV error check
    Given I am on the LGV only undertakings page for a LGV only licence application
    When I click save and continue
    Then A LGV undertakings error message should be displayed