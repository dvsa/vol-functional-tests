Feature: Batch process

   #For this scenario to work, we need the test pipeline to have access to the read replica DB, due to the fact
  #that we need to have the warning_letter_seed_date to be 28 days in the past
  Scenario: Duplicate Vehicle Warning
    Given I have a "goods" "standard_national" application with "2" vehicles and a vehicleAuthority of "5"
    And i am on the vehicle details page
    When i add a vehicle "S679ASX" belonging to another application
    And the duplicate letter job is run
    Then i should receive a duplicate vehicle email

  @batch-expired-bus @int_regression
  Scenario: Expire Bus Registration
    Given I have a psv application with traffic area "north_east" and enforcement area "north_east" which has been granted
    And i have logged in to internal as "admin"
    When i add a new bus registration with a past date
    And i trigger the expire-bus-registration batch job
    Then the registration should be marked as expired