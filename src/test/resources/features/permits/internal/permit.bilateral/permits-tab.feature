Feature: Internal permits tab

  Background:
    Given I am on the VOL self-serve site
    Given I have a "goods" "standard_international" licence

  @INTERNAL
  Scenario: Table is formatted for annual bilateral
    And I am on the permits dashboard on external
    And have valid Annual Bilateral Permits
    And i create an admin and url search for my licence
    When I'm viewing the permits tab
    Then the annual bilateral permit table has the expected format

  @CLOSES-WINDOW @INTERNAL
  Scenario: Table is formatted correctly for annual ECMT
    And have valid permits
    And i create an admin and url search for my licence
    When I'm viewing the permits tab
    Then the annual ECMT permit table has the expected format