Feature: Add a Schedule 4/1

  Background:
    Given i have a valid "goods" "standard_national" licence

  Scenario: Adding a Schedule 4/1
    When a caseworker adds a new operating centre out of the traffic area
    And i create a variation in internal
    And i am on the internal variation overview page
    And i open the operating centres and authorisation tab
    Then i can transfer an operating centre with schedule 41
    Then i can approve the schedule 41