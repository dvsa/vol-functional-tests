@VOL-1319
Feature: Search and add a vehicle

  Scenario Outline: Add a vehicle to application
    Given I have a "<Operator>" application with 0 vehicles and a vehicleAuthority of 5
    When I navigate to manage vehicle page on an application
    And choose to add a "<VRM>" vehicle
    Then the "<VRM>" should be displayed on the page
    Examples:
      | Operator | VRM     |
      | goods    | S679ASX |