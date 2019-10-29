@SS
@SS-GOODS-VAR-OC-DECREASE-VEHICLE
@OLCS-20946
Feature: Goods Variation decrease vehicle count for an OC

  Background:
    Given i have a valid "goods" "sn" licence

  Scenario: Create a variation and decrease vehicle count
    When A selfserve user decreases the vehicle authority count
    And removes a vehicle because of new vehicle cap
    Then a status of update required should be shown next to Review and declarations

  Scenario: Decreasing the vehicle count to an invalid character for required vehicles
    When A selfserve user decreases the vehicle required count by invalid characters
    Then An error message should appear

  Scenario: Decreasing the vehicle count to an invalid character for authorised vehicles
    When A selfserve user decreases the vehicle authority by invalid charecters
    Then An error should appear