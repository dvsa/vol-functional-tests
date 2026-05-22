@INT-SMOKE
Feature: Search and add a vehicle

  Scenario: Add a vehicle to a licence on INT
    Given I have an existing licence "OC1057274"
    When I navigate to manage vehicle page on a licence
    And the vehicle "M858PSS" does not exist on the licence
    And choose to add a "M858PSS" vehicle
    Then the "M858PSS" should be displayed on the page