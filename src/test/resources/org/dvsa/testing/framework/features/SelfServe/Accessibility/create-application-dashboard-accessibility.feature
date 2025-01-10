@ss_regression
@accessibility
@FullRegression
Feature: Create application page should comply to the WCAG 2.1 AA accessibility standards

  Background:
    Given I create a new external user

  Scenario: Check 'Not started' section colour
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    Then the colour of the 'Business type' section should be blue

    @directors-section
  Scenario: Check 'Can't start yet' section colour
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    Then the colour of the 'Directors' section should be grey

  Scenario: Scan for accessibility violations
    And I submit and pay for a "Goods" licence application
    Then no issues should be present on the page