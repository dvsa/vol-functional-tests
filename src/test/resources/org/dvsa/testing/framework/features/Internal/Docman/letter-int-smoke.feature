Feature: Generate letter in INT

  Background:
    Given i have an internal admin user
    And i have logged in to internal

    Scenario: Generate letter as part of INT smoke test
      When I am on a licence Overview page
      And i generate a letter
      And i save the letter
      Then the document is listed on the page