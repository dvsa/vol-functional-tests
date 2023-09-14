@ss_regression
@SS-GOODS-VAR-UPGRADE

Feature: Goods variation upgrade of licence type

  Scenario: Goods variation to upgrade from Restricted to Standard National
    Given i have a valid "goods" "restricted" licence
    When i upgrade my licence type to Standard National
    Then correct statuses are shown by the correct seven sections