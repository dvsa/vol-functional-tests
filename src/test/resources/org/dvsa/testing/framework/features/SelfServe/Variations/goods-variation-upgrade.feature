Feature: Goods variation upgrade of licence type

  Scenario: Goods variation to upgrade from Restricted to Standard National
    Given i have a valid "goods" "restricted" licence
    When i upgrade my licence type to Standard National
    Then a status of update required should be shown next to financial evidence