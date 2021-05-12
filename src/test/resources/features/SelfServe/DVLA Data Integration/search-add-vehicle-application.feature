@VOL-1319
Feature: Search and add a vehicle

  Scenario Outline: Add a vehicle
    Given I have a "<Operator>" application with 5 vehicle authorisation with no vehicles
    When I navigate to manage vehicle page on an application
    And choose to add a "<VRM>" vehicle
    Then the "<VRM>" should be displayed on the page
    Examples:
      | Operator | VRM     |
      | goods    | S679ASX |