@DVLA @VOL-147

Feature: Search and add a vehicle

  Scenario: Check page contents
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on an application
    And choose to add a "Y23WSH" vehicle
    Then the add vehicle page should display licence number
    And "Add a vehicle" heading

  @VOL-1319 @WIP
  Feature: Search and add a vehicle

  Scenario Outline: Add a vehicle to application (This or the one below?)
    Given I have a "<Operator>" application with 0 vehicles and a vehicleAuthority of 5
    When I navigate to manage vehicle page on an application
    And choose to add a "<VRM>" vehicle
    Then the "<VRM>" should be displayed on the page
    Examples:
      | Operator | VRM     |
      | goods    | S679ASX |

  Scenario: Add a vehicle registration mark on an application
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on an application
    When I search for a valid "F95 JGE" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

  Scenario: Add a vehicle registration mark on a licence
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on an application
    When I search for a valid "F95 JGE" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

  Scenario: Add a vehicle registration mark on a variation
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on an application
    When I search for a valid "F95 JGE" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

  @WIP
  Scenario: Clear active search for a VRM

  @WIP
  Scenario: Add a vehicle registration mark for MLH
    Given I have "2" "standard_national" "goods" licences

  Scenario: Add an a vehicle belonging to another licence
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on a licence
    And I add a vehicle belonging to another licence
    Then I should be prompted that vehicle belongs to another licence

  Scenario: Error validation
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on an application
    And I search without entering a registration number
    Then An error message should be displayed

