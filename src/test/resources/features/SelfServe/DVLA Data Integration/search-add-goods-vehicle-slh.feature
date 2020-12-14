#https://jira.dvsacloud.uk/browse/VOL-147

@VOL-147
Feature: Search and add a vehicle

  Background:
    Given I have applied for a "goods" "standard_national" licence
    When I grant licence
    Then the licence should be granted

  Scenario Outline: Check page contents
    When I navigate to manage vehicle page
    And choose to add a "<VRM>" vehicle
    Then the add vehicle page should display licence number
    And "Add a vehicle" heading

    Examples:
      | VRM    |
      | Y23WSH |

  Scenario: Check error messages
    When I navigate to manage vehicle page
    And I search without entering a registration number
    Then An error message should be displayed

  Scenario Outline: Search for a vehicle registration mark
    When I navigate to manage vehicle page
    When I search for a valid "<VRM>" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

    Examples:
      | VRM     |
      | F95 JGE |