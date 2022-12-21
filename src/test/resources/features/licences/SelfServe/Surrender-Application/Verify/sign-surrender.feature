@Surrender
@sign-surrender
@gov-verify

Feature: Sign surrender

  Background:
    Given i have a valid "goods" "standard_international" licence
    And i choose to surrender my licence

  Scenario: Sign with verify - check that surrender has been created
    When i sign with verify
    Then the post verify success page is displayed
    And the surrender status is "Surrender under consideration"

  @gov-sign-in
    Scenario: Sign with gov sign in - check that surrender has been created
      When I can navigate to gov sign in
      And I sign in to gov sign in to complete the process
      Then the post gov sign in page is displayed
      And the surrender status is "Surrender under consideration"