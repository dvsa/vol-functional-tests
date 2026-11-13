# @ss_regression
@vol763
Feature: PSV vehicle management

  @psv-add-vehicle
  Scenario: Add a PSV vehicle and retain it on the licence
    Given I have a "public" "standard_national" licence with "3" vehicle authorisation
    And i have logged in to self serve as "admin"
    When I add a PSV vehicle with make/model "Regression coach"
    Then the added PSV vehicle should persist after reloading the licence

  @psv-remove-vehicle
  Scenario: Remove one PSV vehicle while retaining another
    Given I have a "public" "standard_national" licence with "3" vehicle authorisation
    And i have logged in to self serve as "admin"
    When I add two PSV vehicles with make/models "Vehicle to remove" and "Vehicle to retain"
    And I remove the first PSV vehicle I added
    Then the removed PSV vehicle should be absent and the other added vehicle should remain

  @psv-export-vehicles
  Scenario: Export PSV vehicle data after adding a vehicle
    Given I have a "public" "standard_national" licence with "3" vehicle authorisation
    And i have logged in to self serve as "admin"
    When I add a PSV vehicle with make/model "Export regression coach"
    And I export the PSV vehicle list
    Then the PSV export should contain the added vehicle's registration and make/model
