@ss_regression
@SS-GOODS-VAR-UPGRADE

Feature: Goods variation upgrade of licence type

  Scenario: Goods variation to upgrade from Restricted to Standard National
    Given i have a valid "goods" "restricted" licence
    When i upgrade my licence type to Standard National
    Then correct statuses are shown by the correct seven sections

  Scenario: Goods upgrade to Standard National with interim request
    Given i have a valid "goods" "restricted" licence
    When i upgrade my licence type to Standard National
    And i complete the required five section
    And the variation fee is not required on internal