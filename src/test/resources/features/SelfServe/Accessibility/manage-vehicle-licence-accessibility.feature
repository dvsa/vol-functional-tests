Feature: Check that manage vehicle journey is accessible

  Background:
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted

  Scenario: Check that Manage Vehicle page is accessible
    When I navigate to manage vehicle page
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Check Add Vehicle page is accessible
    When I navigate to manage vehicle page
    And choose to add a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page