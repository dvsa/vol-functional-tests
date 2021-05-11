@VOL-133 @VOL-134 @DVLA

Feature: Reprint a vehicle more than 10 vehicles

  Scenario: Reprint vehicle disc on application
    Given I have a "goods" "standard_national" application
    And discs have been added to my licence
    And I navigate to manage vehicle page on an application
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated

  Scenario: Reprint vehicle disc on licence
    Given I have a "goods" "standard_national" licence
    And discs have been added to my licence
    And I navigate to manage vehicle page on a licence
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated

  Scenario: Reprint vehicle disc on variation
    Given I have a "goods" "standard_national" licence
    When i add an existing person as a transport manager who is not the operator on "variation"
    And I navigate to manage vehicle page on a variation
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated

  Scenario: Reprint vehicle disc on a MLH


  Scenario: Search and reprint a vehicle disc with more than 10 vehicles
    Given I have a "goods" "standard_national" licence with "13" vehicles
    And discs have been added to my licence
    And I navigate to manage vehicle page on a licence
    When i search for and reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And i search and the licence discs number should be updated

  Scenario: Clear VRM search?
  Scenario: View a vehicle via reprint disc page?


  Scenario: Reprint all my vehicles discs
    Given I have a "goods" "standard_national" licence
    And discs have been added to my licence
    And I navigate to manage vehicle page on a licence
    When I reprint all my discs
    Then the "Discs for these vehicles will be reprinted and sent to you in the post" confirmation banner should appear
    And all the licence discs number should be updated

  Scenario: Error validation
    Given I have a "goods" "standard_national" licence
    And discs have been added to my licence
    And I navigate to manage vehicle page on a licence
    When I clicks submit on "reprint" without checking a checkbox
    Then the standard "Select the discs you want to reprint" errors appear
