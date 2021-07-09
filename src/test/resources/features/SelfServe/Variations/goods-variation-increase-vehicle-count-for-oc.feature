@SS @ss_regression
@SS-GOODS-VAR-OC-INCREASE-VEHICLE
@OLCS-21133

Feature: Goods Variation increase vehicle count for an OC

  Scenario Outline: Create a variation and increase vehicle count for non standard international licences
    Given i have a valid "goods" "<LicenceType>" licence
    When i increase my vehicle authority count
    Then a status of update required should be shown next to financial evidence

    Examples:
      | LicenceType            |
      | standard_national      |
      | restricted             |

  Scenario: Increasing the vehicle count to an invalid character for required vehicles
    Given i have a valid "goods" "standard_national" licence
    When A selfserve user increases the vehicle required count by invalid characters
    Then An error message should appear

  Scenario: Increasing the vehicle count to an invalid character for authorised vehicles
    Given i have a valid "goods" "standard_national" licence
    When A selfserve user increases the vehicle authority by invalid charecters
    Then An error should appear

  @CPMS_tests
  Scenario: Create a variation and increase authorisation count
    Given i have a valid "goods" "standard_national" licence
    And a selfserve user creates a variation and increases the vehicle authority count
    And i pay for my application
    And i create an admin and url search for my variation
    Then the "Variation Fee for application" fee should be paid

  @CPMS_tests
  Scenario Outline: Create a variation and add operating centre for standard non-international licences
    Given i have a valid "goods" "<LicenceType>" licence
    And a selfserve user creates a variation and adds an operating centre
    And i pay for my application
    And i create an admin and url search for my variation
    Then the "Variation Fee for application" fee should be paid

    Examples:
      | LicenceType            |
      | standard_national      |
      | restricted             |
