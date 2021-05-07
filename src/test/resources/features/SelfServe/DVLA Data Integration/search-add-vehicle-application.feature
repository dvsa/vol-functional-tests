@VOL-1319
Feature: Search and add a vehicle

  Scenario Outline: Add a vehicle
    Given I have a "<Operator>" application with 0 vehicles
    When I navigate to manage vehicle page on an application
    And choose to add a "<VRM>" vehicle
    Then the add vehicle page should display licence number
    And "Add a vehicle" heading
    Examples:
      | Operator | VRM     |
      | goods    | H495BND |