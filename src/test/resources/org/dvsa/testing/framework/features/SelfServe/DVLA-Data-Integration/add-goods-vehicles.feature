@DVLA @VOL-147 @ss_regression @FullRegression @printAndSign
Feature: Search and add a vehicle

  @ec2_smoke @dvla-add-vehicle @localsmoke @smoke
  Scenario Outline: Add a vehicle to a licence
    Given as a "<user_type>" I have "1" "<Operator>" "standard_national" licences with "2" vehicles and a vehicleAuthority of "13"
    When I navigate to manage vehicle page on a licence
    And choose to add a "<VRM>" vehicle
    Then the "<VRM>" should be displayed on the page
    Examples:
      | user_type  | Operator | VRM     |
      | admin      | goods    | S679ASX |
      | consultant | goods    | S679ASX |


  Scenario Outline: Add a vehicle registration mark on a licence
    Given as a "<user_type>" I have "1" "goods" "standard_national" licences with "2" vehicles and a vehicleAuthority of "5"
    When I navigate to manage vehicle page on a licence
    When I search for a valid "F95 JGE" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

    Examples:
      | user_type  |
      | admin      |
      | consultant |

  @dvla-add-vehicle-another-licence
  Scenario Outline: Add a vehicle belonging to another licence
    Given as a "<user_type>" I have "1" "goods" "standard_national" licences with "2" vehicles and a vehicleAuthority of "5"
    When I navigate to manage vehicle page on a licence
    And I add a vehicle belonging to another licence
    Then I should be prompted that vehicle belongs to another licence

    Examples:
      | user_type  |
      | admin      |
      | consultant |

  @dvla-remove-vehicle @localsmoke @smoke
  Scenario Outline: Remove vehicle on licence
    Given as a "<user_type>" I have "1" "goods" "standard_national" licences with "2" vehicles and a vehicleAuthority of "5"
    And I navigate to manage vehicle page on a licence
    And i remove a vehicle
    Then the "1 vehicle has been removed" confirmation banner should appear
    And the vehicle should no longer be present

    Examples:
      | user_type  |
      | admin      |
      | consultant |

  @dvla-reprint @localsmoke @smoke
  Scenario Outline: Reprint vehicle disc on licence
    Given as a "<user_type>" I have "1" "goods" "standard_national" licences with "2" vehicles and a vehicleAuthority of "5"
    And discs have been added to my licence
    And I navigate to manage vehicle page on a licence
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated

    Examples:
      | user_type  |
      | admin      |
      | consultant |

  Scenario Outline: Error validation
    Given as a "<user_type>" I have a valid "goods" "standard_national" licence
    When I navigate to manage vehicle page on a licence
    And I search without entering a registration number
    Then An error message should be displayed

    Examples:
      | user_type  |
      | admin      |
      | consultant |