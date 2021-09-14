@Accessibility
Feature: Check that manage vehicle journey is accessible

  Background:
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "6"
    When I navigate to manage vehicle page on a licence

  @Accessibility
  Scenario: Manage Vehicle page is accessible
    And i scan for accessibility violations
    Then no issues should be present on the page

  @Accessibility
  Scenario Outline: Check Add Vehicle page is accessible
    And choose to add a "<VRM>" vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

    Examples:
      | VRM     |
      | PX57DXA |

  @Accessibility
  Scenario: Remove vehicle journey
    And I choose to remove a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

  @Accessibility
  Scenario: Confirm vehicle removal
    And I want to confirm a vehicle removal
    And i scan for accessibility violations
    Then no issues should be present on the page

  @Accessibility
  Scenario: Reprint vehicle discs
    And I choose to reprint a vehicle disc
    And i scan for accessibility violations
    Then no issues should be present on the page

  @Accessibility
  Scenario: Confirm reprint vehicle discs
    And I want to confirm a vehicle disc reprint
    And i scan for accessibility violations
    Then no issues should be present on the page

  @Accessibility
  Scenario: Transfer vehicle journey
    And I choose to transfer a vehicle
    And i scan for accessibility violations
    Then no issues should be present on the page

  @Accessibility
  Scenario: Confirm transfer vehicle
    And I want to confirm a vehicle transfer
    And i scan for accessibility violations
    Then no issues should be present on the page
