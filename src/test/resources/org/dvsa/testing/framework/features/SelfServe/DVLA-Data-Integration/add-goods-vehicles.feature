@DVLA @VOL-147 @ss_regression
Feature: Search and add a vehicle

  @smoketest @dvla-add-vehicle
  Scenario Outline: Add a vehicle to a licence
    Given I have "1" "<Operator>" "standard_national" licences with "2" vehicles and a vehicleAuthority of "13"
    When I navigate to manage vehicle page on a licence
    And choose to add a "<VRM>" vehicle
    Then the "<VRM>" should be displayed on the page
    Examples:
      | Operator | VRM     |
      | goods    | S679ASX |

  Scenario: Add a vehicle registration mark on a licence
    Given I have "1" "goods" "standard_national" licences with "2" vehicles and a vehicleAuthority of "5"
    When I navigate to manage vehicle page on a licence
    When I search for a valid "F95 JGE" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

  Scenario: Add an a vehicle belonging to another licence
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on a licence
    And I add a vehicle belonging to another licence
    Then I should be prompted that vehicle belongs to another licence

  @dvla-remove-vehicle
  Scenario: Remove vehicle on licence
    Given I have "1" "goods" "standard_national" licences with "2" vehicles and a vehicleAuthority of "5"
    And I navigate to manage vehicle page on a licence
    And i remove a vehicle
    Then the "1 vehicle has been removed" confirmation banner should appear
    And the vehicle should no longer be present

  @dvla-reprint
  Scenario: Reprint vehicle disc on licence
    Given I have a "goods" "standard_national" licence
    And discs have been added to my licence
    And I navigate to manage vehicle page on a licence
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated

    @dvla-variation-reprint
  Scenario: Reprint vehicle disc on variation
    Given I have a "goods" "standard_national" licence
    When i add an existing person as a transport manager who is not the operator on "variation"
    And I navigate to manage vehicle page on a variation
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated

  Scenario: Error validation
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on a licence
    And I search without entering a registration number
    Then An error message should be displayed