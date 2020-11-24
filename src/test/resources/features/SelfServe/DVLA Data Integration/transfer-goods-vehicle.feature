#https://jira.dvsacloud.uk/browse/VOL-90 and https://jira.dvsacloud.uk/browse/VOL-91

@VOL-90
@VOL-91
Feature: Transfer a vehicle

  Scenario: Transfer vehicles for dual licence holder
    Given I have applied for "2" "standard_national" "goods" licences
    And I navigate to manage vehicle page
    When i transfer vehicles for an assumed licence

  Scenario: Transfer vehicles for multiple licence holder
    Given I have applied for "3" "standard_national" "goods" licences
    And I navigate to manage vehicle page
    When i transfer vehicles to a specified licence

  Scenario: Transfer all vehicles from licence
    Given I have applied for "2" "standard_national" "goods" licences
    And I navigate to manage vehicle page
    When i transfer all the vehicles from my licence
    Then the confirmation banner should

  Scenario: I transfer vehicles to a full licence
    Given I have applied for "2" "standard_national" "goods" licences
    And I navigate to manage vehicle page
    When i transfer vehicles for an assumed licence
    Then an transfer vehicles error banner should appear
    Then a "value" error banner should appear