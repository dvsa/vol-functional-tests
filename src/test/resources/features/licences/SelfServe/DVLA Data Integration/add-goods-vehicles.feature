@DVLA @VOL-147
@ss_regression


Feature: Search and add a vehicle

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

  Scenario: Error validation
    Given I have a "goods" "standard_national" licence
    When I navigate to manage vehicle page on a licence
    And I search without entering a registration number
    Then An error message should be displayed

