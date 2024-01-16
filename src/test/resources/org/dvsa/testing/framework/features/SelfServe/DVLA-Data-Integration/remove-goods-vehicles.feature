@VOL-84 @VOL-85 @DVLA

Feature: Remove a vehicle

  Scenario: Remove vehicle on application
    Given I have a "goods" "standard_national" application
    And I navigate to manage vehicle page on an application
    And i remove a vehicle
    Then the "1 vehicle has been removed" confirmation banner should appear
    And the vehicle should no longer be present

  Scenario: Remove vehicle on variation
    Given I have a "goods" "standard_national" licence
    When i add an existing person as a transport manager who is not the operator on "variation"
    And I navigate to manage vehicle page on a variation
    And i remove a vehicle
    Then the "1 vehicle has been removed" confirmation banner should appear
    And the vehicle should no longer be present

  Scenario: Search and remove a vehicle with more than 10 vehicles
    Given I have a "goods" "standard_national" licence with "13" vehicle authorisation
    And I navigate to manage vehicle page on a licence
    And i search and remove a vehicle
    Then the "1 vehicle has been removed" confirmation banner should appear
    And i search and the vehicle should no longer be present

  Scenario: Remove all my vehicles
    Given I have a "goods" "standard_national" licence
    And I navigate to manage vehicle page on a licence
    And i remove all my vehicles
    Then the "5 vehicles have been removed" confirmation banner should appear
    Then the "You have removed the last vehicle from your licence" confirmation body should appear
    Then the switchboard only views add vehicle and view vehicle radio buttons

  Scenario: Error validation
    Given I have a "goods" "standard_national" licence
    And I navigate to manage vehicle page on a licence
    When I clicks submit on "remove" without checking a checkbox
    Then the standard "Select one or more vehicles to remove" errors appear
