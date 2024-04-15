Feature: Batch process

  Background:
    Given I have a "goods" "standard_national" application with "2" vehicles and a vehicleAuthority of "5"
    And i am on the vehicle details page

   #For this scenario to work, we need the test pipeline to have access to the read replica DB, due to the fact
  #that we need to have the warning_letter_seed_date to be 28 days in the past
  Scenario: Duplicate Vehicle Warning
    When i add a vehicle "S679ASX" belonging to another application
    And the duplicate letter job is run
    Then i should receive a duplicate vehicle email
