@ss_regression
Feature: Vehicle management on a licence

  Background: Generate a licence and navigate to vehicle page
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "5"
    And i am on the vehicle details page

  Scenario: I can add a vehicle on my licence
    When i add a vehicle to my licence
    Then the "The vehicle has been added" alert should appear
    And the vehicle should be appear

  Scenario: I can remove a vehicle on my licence
    When i remove a vehicle from my licence
    Then the vehicle no longer appears

  Scenario: I can change a vehicle on my licence
    When i change a vehicle on my licence
    Then the "The vehicle has been updated" alert should appear
    Then the vehicle should have changed

  Scenario: I transfer a vehicle to  another licence
    When i transfer a vehicle to another licence
    Then the "The selected vehicle(s) have been transferred" alert should appear
    And the vehicle no longer appears
    And the other licence contains that vehicle