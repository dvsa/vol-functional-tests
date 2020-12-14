Feature: Check that manage vehicle journey is accessible

  Background:
    Given I have applied for "2" "standard_national" "goods" licences

  Scenario: Check that Manage Vehicle page is accessible
    When I navigate to manage vehicle page
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario Outline: Check Add Vehicle page is accessible
    When I navigate to manage vehicle page
    And choose to add a "<VRM>" vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

    Examples:
      | VRM     |
      | PX57DXA |

  Scenario: Remove vehicle journey
    When I navigate to manage vehicle page
    And I choose to remove a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Confirm vehicle removal
    When I navigate to manage vehicle page
    And I want to confirm a vehicle removal
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Transfer vehicle journey
    When I navigate to manage vehicle page
    And i choose to transfer a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page