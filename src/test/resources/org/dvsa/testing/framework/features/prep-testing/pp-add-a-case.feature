@PP-SMOKE
<<<<<<< HEAD
@PP-ADD-USER
=======
@PP-ADD-CASE

>>>>>>> 2daa626c1 (chore: tags and envs)
Feature: Add a user account

  Scenario: Internal user adds a case to a licence
    Given I have a prep "internal" account
    When I navigate to a case
    And I add a case
    Then that case has been created