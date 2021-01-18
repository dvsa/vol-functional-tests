@VOL-133
@VOL-134

Feature: Reprint a vehicle more than 10 vehicles

  Scenario: Reprint vehicle disc on application
    Given I have a "goods" "standard_national" licence with "5" vehicles
    And discs have been added to my licence
    And I navigate to manage vehicle page
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated.

  Scenario: Reprint vehicle disc on licence
    Given I have a "goods" "standard_national" licence with "5" vehicles
    And discs have been added to my licence
    And I navigate to manage vehicle page
    When I reprint a vehicle disc
    Then the "Disc for this vehicle will be reprinted and sent to you in the post" confirmation banner should appear
    And the licence discs number should be updated.

  Scenario: Reprint vehicle disc on variation (Do we need?)
    Given I have a "goods" "standard_national" licence with "5" vehicles

  Scenario: Search and reprint a vehicle disc with more than 10 vehicles
    Given I have a "goods" "standard_national" licence with "13" vehicles

  Scenario: Reprint all my vehicles discs
    Given I have a "goods" "standard_national" licence with "5" vehicles

  Scenario: Error validation
    Given I have a "goods" "standard_national" licence with "5" vehicles