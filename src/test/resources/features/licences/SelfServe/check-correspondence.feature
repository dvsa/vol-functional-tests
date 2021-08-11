@SS
@SS-CHECK-DOCUMENTS
@ss_regression
Feature: Check correspondence

  Background:
    Given i have a valid "public" "standard_international" licence
    And i have logged in to self serve

  Scenario: Verify that documents are produced in Self-serve for a new licence
    When i open the documents tab
    Then all correspondence for the application should be displayed