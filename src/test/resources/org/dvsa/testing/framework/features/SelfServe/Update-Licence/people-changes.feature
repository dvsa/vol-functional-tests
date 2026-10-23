@people-changes @ss_regression
Feature: People changes (partners, LLP members)

  Background:
    Given i have a valid "goods" "standard_national" LLP licence

  @add-partner
  Scenario: Add a non-director partner to an LLP licence
    When i navigate to the "licence" people page
    Then the people page should display
