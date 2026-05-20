@INT-SMOKE
Feature: Search and add a vehicle

  Scenario: Add a vehicle to a licence on INT
    Given I have an existing licence "OC1057274"
    When I navigate to manage vehicle page on a licence
    And the vehicle "X981FUJ" does not exist on the licence
    And choose to add a "X981FUJ" vehicle
    Then the "X981FUJ" should be displayed on the page