@VOL-84
@VOL-85

Feature: Remove a vehicle more than 10 vehicles

  Scenario: Remove vehicle on application
    Given I have applied for a "goods" "standard_national" licence with "5" vehicles

  Scenario: Remove vehicle on licence
    Given I have a "goods" "standard_national" licence with "5" vehicles

  Scenario: Remove vehicle on variation (Do we need?)
    Given I have a "goods" "standard_national" licence with "5" vehicles

  Scenario: Search and remove a vehicle with more than 10 vehicles
    Given I have a "goods" "standard_national" licence with "13" vehicles

  Scenario: Remove all my vehicles
    Given I have a "goods" "standard_national" licence with "5" vehicles

  Scenario: Error validation
    Given I have a "goods" "standard_national" licence with "5" vehicles
