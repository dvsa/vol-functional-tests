@application-fee-ordering
@CPMS_tests
@FullRegression
Feature: Application interim fees are ordered by invoice date

  Scenario: Outstanding interim fees are selected oldest invoice first, not in creation order
    Given I have a "goods" "standard_national" application
    And the application has outstanding interim fees created in this order
      | days ago | amount |
      | 10       | 33.00  |
      | 30       | 11.00  |
      | 20       | 22.00  |
    Then the application interim fees should be selected in ascending invoice date order
