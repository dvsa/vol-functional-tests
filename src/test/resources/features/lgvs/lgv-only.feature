@lgv
Feature: LGV only tests

  Scenario: Type of Licence page LGV only no option selected error check
    Given I am applying for a "great_britain" "goods" "standard_international" "no_selection" licence
    When I click save and continue
    Then A LGV only error message should be displayed

  Scenario: Type of Licence LGV Undertakings page no option selected error check
    Given I am applying for a "great_britain" "goods" "standard_international" "lgv_only_fleet" licence
    When I click save and continue
    Then A LGV undertakings error message should be displayed