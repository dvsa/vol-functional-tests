#https://jira.dvsacloud.uk/browse/VOL-90 and https://jira.dvsacloud.uk/browse/VOL-91

@VOL-90
@VOL-91
Feature: Transfer a vehicle

  Scenario: Transfer vehicles for dual licence holder
    Given I have "2" "goods" "standard_national" licences with "3" vehicles and a cap of "5"
    And I navigate to manage vehicle page
    When i transfer a vehicle to an assumed licence
    Then the "1 vehicle has been transferred to licence" confirmation banner should appear

  Scenario: Transfer vehicles for multiple licence holder
    Given I have "3" "goods" "standard_national" licences with "3" vehicles and a cap of "5"
    And I navigate to manage vehicle page
    When i transfer a vehicle to a specified licence
    Then the "1 vehicle has been transferred to licence" confirmation banner should appear

  Scenario: Transfer all vehicles from licence
    Given I have "2" "goods" "standard_national" licences with "2" vehicles and a cap of "5"
    And I navigate to manage vehicle page
    When i transfer all the vehicles from my licence
    Then the "2 vehicles have been transferred to licence" confirmation banner should appear
    Then the "You have transferred the last vehicle from your licence" confirmation body should appear

  Scenario: I transfer vehicles to a full licence and an error displays
    Given I have "2" "goods" "standard_national" licences with "4" vehicles and a cap of "5"
    And I navigate to manage vehicle page
    When i transfer all the vehicles from my licence
    Then a "Transferring these vehicles would exceed the vehicle authority on licence" error banner should appear