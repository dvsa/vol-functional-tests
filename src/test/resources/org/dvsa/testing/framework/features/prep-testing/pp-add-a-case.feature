@PP-SMOKE
@PP-ADD-CASE

Feature: Add a user account

  Scenario: Internal user adds a case to a licence
    Given I log into prep "internal" account with user "intPrepUser"
    When I navigate to a case
    And I add a case
    Then that case has been created