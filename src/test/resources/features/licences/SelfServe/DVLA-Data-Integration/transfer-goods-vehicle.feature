@VOL-90 @VOL-91 @DVLA

Feature: Transfer a vehicle

  Scenario: Transfer vehicles for dual licence holder
    Given I have "2" "goods" "standard_international" licences with "3" vehicles and a vehicleAuthority of "5"
    And I navigate to manage vehicle page on a licence
    When i transfer a vehicle to an assumed licence
    Then the "1 vehicle has been transferred to licence" confirmation banner should appear

  Scenario: Transfer vehicles for multiple licence holder
    Given I have "3" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "5"
    And I navigate to manage vehicle page on a licence
    When i transfer a vehicle to a specified licence
    Then the "1 vehicle has been transferred to licence" confirmation banner should appear


  Scenario: Transfer all vehicles from licence
    Given I have "2" "goods" "standard_national" licences with "2" vehicles and a vehicleAuthority of "5"
    And I navigate to manage vehicle page on a licence
    When i transfer all the vehicles from my licence
    Then the "2 vehicles have been transferred to licence" confirmation banner should appear
    Then the "You have transferred the last vehicle from your licence" confirmation body should appear
    Then the switchboard only views add vehicle and view vehicle radio buttons

  Scenario: I transfer vehicles to a full licence and an error displays
    Given I have "2" "goods" "standard_national" licences with "4" vehicles and a vehicleAuthority of "5"
    And I navigate to manage vehicle page on a licence
    When i transfer all the vehicles from my licence
    Then a "Transferring these vehicles would exceed the vehicle authority on licence" error banner should appear

  Scenario: Error validation
    Given I have "3" "goods" "standard_national" licences with "3" vehicles and a vehicleAuthority of "5"
    And I navigate to manage vehicle page on a licence
    When I clicks submit on "transfer" without checking a checkbox
    Then the standard "Select the vehicles that you want to transfer" errors appear
    Then the standard "Select the licence that you wish to transfer your vehicles to" errors appear

# Need application, variation and search and transfer.
# Need test to check transfer doesn't appear on SLH.