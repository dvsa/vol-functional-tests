@create-operator
@INT-SMOKE

Feature: Create operator from search results

  Background:
    Given i have an internal admin user
    And i have logged in to internal

    Scenario: Create registered company operator
      When i search for a company and click Create operator
      And i enter and search for a Company number
      Then the operator details should be populated