@INT-SMOKE
Feature: Search and add a vehicle

  Scenario: Add a vehicle to a licence on INT
    Given I have an existing licence "OC1057274"
    When I navigate to manage vehicle page on a licence
    And the vehicle "S679ASX" does not exist on the licence
    And choose to add a "S679ASX" vehicle
    Then the "S679ASX" should be displayed on the page