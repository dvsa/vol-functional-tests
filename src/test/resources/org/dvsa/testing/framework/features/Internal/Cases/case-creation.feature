@INT
@CASE-CREATION
@APIsmoke

Feature: Create a new case

  Background:
    Given i have a valid "goods" "standard_national" licence

  Scenario: Creating a case with case notes
    When I create a new case
    Then I should be able to view the case details