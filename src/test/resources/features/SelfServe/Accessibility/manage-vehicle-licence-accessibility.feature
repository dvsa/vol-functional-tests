Feature: Check that manage vehicle journey is accessible

  Background:
    Given I have "2" "goods" "standard_national" licences with "2" vehicles and a cap of "6"

  Scenario: Check that Manage Vehicle page is accessible
    When I navigate to manage vehicle page on a licence
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario Outline: Check Add Vehicle page is accessible
    When I navigate to manage vehicle page on a licence
    And choose to add a "<VRM>" vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

    Examples:
      | VRM     |
      | PX57DXA |

  Scenario: Remove vehicle journey
    When I navigate to manage vehicle page on a licence
    And I choose to remove a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Confirm vehicle removal
    When I navigate to manage vehicle page on a licence
    And I want to confirm a vehicle removal
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Reprint vehicle discs
    When I navigate to manage vehicle page on a licence
    And I choose to reprint a vehicle disc
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Confirm reprint vehicle discs
    When I navigate to manage vehicle page on a licence
    And I want to confirm a vehicle disc reprint
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Transfer vehicle journey
    When I navigate to manage vehicle page on a licence
    And I choose to transfer a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

  Scenario: Confirm transfer vehicle
    When I navigate to manage vehicle page on a licence
    And I want to confirm a vehicle transfer
    And i scan for accessibility violations
    Then no issues should be present on the page
