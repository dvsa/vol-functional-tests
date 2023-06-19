@ss_regression
@FullRegression
Feature: Vehicle management on an application

  Background: Generate a licence and navigate to vehicle page
    Given I have a "goods" "standard_national" application with "2" vehicles and a vehicleAuthority of "5"
    And i am on the vehicle details page

  Scenario: I can add a vehicle on my application
    When i add a vehicle to my application
    Then the "The vehicle has been added" alert should appear
    And the vehicle should be appear

  Scenario: I can remove a vehicle on my application
    When i remove a vehicle from my application
    Then the vehicle no longer appears
