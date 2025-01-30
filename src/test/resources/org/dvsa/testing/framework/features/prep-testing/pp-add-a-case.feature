@PP-SMOKE
@PP-ADD-USER

Feature: Add a user account

  Scenario: Internal user adds a case to a licence
    Given I have a prep "internal" account
    When I navigate to a case
    And I add a case
    Then that case has been created

