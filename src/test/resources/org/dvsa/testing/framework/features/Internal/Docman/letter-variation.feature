@INT-SMOKE
@letter-variation

Feature: Generate letter on Variation

  Background:
    Given i have an internal admin user
    And i have logged in to internal

    Scenario: Generate letter and filter by variation
      When I am on a licence Overview page
      And i create a variation in internal
      And i generate a letter
      And i save the letter
      And i filter by This application only
      Then the document is listed on the page
