@Surrender
@sign-surrender
@gov-verify
Feature: Sign surrender

  Background:
    Given i have a valid "goods" "standard_international" licence
    And i choose to surrender my licence

  @ss_regression
  Scenario: Sign with verify - check that surrender has been created
    When i sign with verify
    Then the post verify success page is displayed
    And the surrender status is "Surrender under consideration"
    Then no issues should be present on the page